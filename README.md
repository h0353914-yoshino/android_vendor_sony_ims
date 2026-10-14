# android_vendor_sony_ims

Sony Xperia (yoshino：poplar / poplardcm) 使用的 Qualcomm IMS App（`org.codeaurora.ims`）。
內容是把 Fairphone FP3 韌體（6.A.040.4）的 `ims.apk` 以 JADX 反編譯後，修正到可重新編譯並在 LineageOS 22.2（Android 15）上運作的 Java 原始碼。

## 授權聲明（重要）

- 本 repo **沒有授予任何授權**。`src/` 是 Qualcomm 專有 App 的反編譯衍生物，原始著作權屬於 Qualcomm Technologies, Inc. 與其授權方；反編譯輸出不含原始版權標頭，授權狀態不明，請視為「保留所有權利」。
- `compat/` 內的檔案取自 LineageOS/CAF 的 `vendor/codeaurora/telephony/ims/ims-ext-common`（Linux Foundation，BSD 3-clause 風格），該部分沿用其原授權與版權聲明。
- 使用前請自行評估，本 repo 僅供個人研究與自用編譯。

## 內容

| 路徑 | 說明 |
|---|---|
| `Android.bp` | `ims`（`android_app`，平台簽章、`system_ext`、`privileged`）與編譯期專用的 `ims-ext-common-fp3-compat` |
| `AndroidManifest.xml`、`res/` | 取自原 APK（apktool/JADX 解碼），資源 ID 與原版相同 |
| `src/` | JADX 輸出（273 檔 → 去除 `android/hidl/base` 後 271 檔），已依 smali 修正還原錯誤 |
| `compat/` | 對 FP3 版 `ims-ext-common` 的編譯期相容層：無參數建構子、`QtiCallConstants.EXTRA_CALL_COMPOSER_TOKEN/_CALL_ENDED`、`queryVirtualLineInfo`。`installable: false`，不會進入映像 |
| `tools/cmp_cfg.py` | 重編後與原 APK 的控制流比對工具（見下） |

## 來源與版本

- 原 APK：FP3 6.A.040.4 的 `system_ext/priv-app/ims/ims.apk`，SHA-1 `5340ba42c0108de7d7bad44f48492fd06bd1f91f`（見 `device/sony/yoshino-common/proprietary-files.txt`）。
- 反編譯：JADX（jadx-headless-mcp 0.7.1 內建），`JadxDecompiler` 直接輸出。
- 目標平台：LineageOS 22.2 / Android 15，`lineage_poplar-bp1a-userdebug`。

## 編譯

放在 `vendor/sony/ims`，然後（務必先匯出 ccache 變數，避免 Soong 快取失效）：

```bash
cd ~/lineageos
export CCACHE_EXEC=/usr/bin/ccache USE_CCACHE=1
source build/envsetup.sh && lunch lineage_poplar-bp1a-userdebug && mka ims
```

輸出：`out/target/product/poplar/system/system_ext/priv-app/ims/ims.apk`。

注意事項：
- `uses_libs` 順序必須是 `qti-telephony-hidl-wrapper, qti-telephony-utils, ims-ext-common`，否則 manifest_check 失敗。
- 模組名稱為 `ims`，與原版同名同路徑（`/system_ext/priv-app/ims/ims.apk`），所以 `yoshino-common` 的 `PRODUCT_PACKAGES += ims` 與 `priv-app/ims/lib/arm64/` 的 JNI 符號連結都不用改。代價是原本 `vendor/sony/yoshino-common` 的 `android_app_import ims` 必須移除（`proprietary-files.txt` 該行改成 `-` 前綴：只保留 blob、不產生編譯規則）。
- 裝置上試驗新 APK 前先備份原檔，並暫時 `setprop persist.sys.disable_rescue true`；APK 若讓持久性程序當機，RescueParty 會自動重開機。

## 重點：JADX 的還原錯誤

JADX 輸出能通過 javac，但有幾處行為是錯的，已依 smali 逐一修正（原始碼內有「由 JADX 反編譯修正」註解）：

- `ImsServiceClassTracker.handleCalls`：JADX 完全未還原，依 smali 手寫。
- `ImsVideoGlobals.init`：缺 `return`，一律丟 `Default subscription is null`（造成 ImsService 當機）。
- `ImsSubController.updateActiveImsStackForPhoneId`：被還原成無窮迴圈。
- `ImsUtImpl$ImsUtImplHandler`：CF 查詢 `while` 迴圈內無條件 `break`。
- `ImsSenderRxr.dial` / `sendGeolocationInfo` / `suppSvcStatusInternal`：漏掉共用的 `catch (Exception)` 收尾。
- 其他小修：`ImsConfigImpl.sendRequest`、`ImsSenderRxr$ImsRadioHandler.handleMessage`、`CallDetails.addExtra`、`Log.secureHash`、`Camera2`、`CameraController` 等。

## 驗證工具 `tools/cmp_cfg.py`

以 `dexdump -d` 輸出加原始 `classes.dex` 位元組建出每個方法的控制流，比較兩個 dex 中「事件」（呼叫、寫欄位、new、return、throw、monitor）之間的先後邊集合。能抓到缺 `return`、迴圈、`break`、try 範圍錯誤；只比對呼叫次數的方法抓不到這類錯誤。

```bash
dexdump -d orig/classes.dex > orig.dump
dexdump -d new/classes.dex  > new.dump
python3 tools/cmp_cfg.py orig.dump orig/classes.dex new.dump new/classes.dex 60        # 方法
python3 tools/cmp_cfg.py orig.dump orig/classes.dex new.dump new/classes.dex 0 x anon  # 匿名類別
```

已知的良性差異：enum `$values()`、建構子欄位賦值順序、synchronized/finally 的 monitor 展開、`$$Nest$` 存取器、lambda 類別改為匿名類別、常數折疊、HIDL `hashCode` 宿主類別。

## 驗證狀況（G8342，LineageOS 22.2，中華電信 46692）

- 乾淨重開機後 `REGISTRATION_STATE_REGISTERED`、`MmTel Voice: true`、`rmnet_data` 3 條 UP、無當機；VoLTE 通話實測可通；IMS App 通知（狀態列 VOLTE 徽章）有發出。
- 推送後第一次開機曾未註冊（原因未查明），之後連續 4 次開機全部成功。
- 視訊通話（JNI）路徑尚未驗證。
