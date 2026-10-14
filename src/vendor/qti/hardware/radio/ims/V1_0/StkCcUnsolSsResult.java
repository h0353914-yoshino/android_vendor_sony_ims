package vendor.qti.hardware.radio.ims.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class StkCcUnsolSsResult {
    public int serviceType = 0;
    public int requestType = 0;
    public int teleserviceType = 0;
    public int serviceClass = 0;
    public int result = 0;
    public ArrayList<SsInfoData> ssInfoData = new ArrayList<>();
    public ArrayList<CfData> cfData = new ArrayList<>();
    public ArrayList<CbNumInfo> cbNumInfo = new ArrayList<>();

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != StkCcUnsolSsResult.class) {
            return false;
        }
        StkCcUnsolSsResult other = (StkCcUnsolSsResult) otherObject;
        if (this.serviceType == other.serviceType && this.requestType == other.requestType && this.teleserviceType == other.teleserviceType && this.serviceClass == other.serviceClass && this.result == other.result && HidlSupport.deepEquals(this.ssInfoData, other.ssInfoData) && HidlSupport.deepEquals(this.cfData, other.cfData) && HidlSupport.deepEquals(this.cbNumInfo, other.cbNumInfo)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.serviceType))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.requestType))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.teleserviceType))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.serviceClass))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.result))), Integer.valueOf(HidlSupport.deepHashCode(this.ssInfoData)), Integer.valueOf(HidlSupport.deepHashCode(this.cfData)), Integer.valueOf(HidlSupport.deepHashCode(this.cbNumInfo)));
    }

    public final String toString() {
        return "{.serviceType = " + SsServiceType.toString(this.serviceType) + ", .requestType = " + SsRequestType.toString(this.requestType) + ", .teleserviceType = " + SsTeleserviceType.toString(this.teleserviceType) + ", .serviceClass = " + this.serviceClass + ", .result = " + this.result + ", .ssInfoData = " + this.ssInfoData + ", .cfData = " + this.cfData + ", .cbNumInfo = " + this.cbNumInfo + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(72L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<StkCcUnsolSsResult> readVectorFromParcel(HwParcel parcel) {
        ArrayList<StkCcUnsolSsResult> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 72, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            StkCcUnsolSsResult _hidl_vec_element = new StkCcUnsolSsResult();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 72);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.serviceType = _hidl_blob.getInt32(_hidl_offset + 0);
        this.requestType = _hidl_blob.getInt32(_hidl_offset + 4);
        this.teleserviceType = _hidl_blob.getInt32(_hidl_offset + 8);
        this.serviceClass = _hidl_blob.getInt32(_hidl_offset + 12);
        this.result = _hidl_blob.getInt32(_hidl_offset + 16);
        int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 24 + 8);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 16, _hidl_blob.handle(), _hidl_offset + 24 + 0, true);
        this.ssInfoData.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            SsInfoData _hidl_vec_element = new SsInfoData();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 16);
            this.ssInfoData.add(_hidl_vec_element);
        }
        int _hidl_vec_size2 = _hidl_blob.getInt32(_hidl_offset + 40 + 8);
        HwBlob childBlob2 = parcel.readEmbeddedBuffer(_hidl_vec_size2 * 16, _hidl_blob.handle(), _hidl_offset + 40 + 0, true);
        this.cfData.clear();
        for (int _hidl_index_1 = 0; _hidl_index_1 < _hidl_vec_size2; _hidl_index_1++) {
            CfData _hidl_vec_element2 = new CfData();
            _hidl_vec_element2.readEmbeddedFromParcel(parcel, childBlob2, _hidl_index_1 * 16);
            this.cfData.add(_hidl_vec_element2);
        }
        int _hidl_vec_size3 = _hidl_blob.getInt32(_hidl_offset + 56 + 8);
        HwBlob childBlob3 = parcel.readEmbeddedBuffer(_hidl_vec_size3 * 24, _hidl_blob.handle(), _hidl_offset + 56 + 0, true);
        this.cbNumInfo.clear();
        for (int _hidl_index_2 = 0; _hidl_index_2 < _hidl_vec_size3; _hidl_index_2++) {
            CbNumInfo _hidl_vec_element3 = new CbNumInfo();
            _hidl_vec_element3.readEmbeddedFromParcel(parcel, childBlob3, _hidl_index_2 * 24);
            this.cbNumInfo.add(_hidl_vec_element3);
        }
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(72);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<StkCcUnsolSsResult> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 72);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 72);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(_hidl_offset + 0, this.serviceType);
        _hidl_blob.putInt32(_hidl_offset + 4, this.requestType);
        _hidl_blob.putInt32(_hidl_offset + 8, this.teleserviceType);
        _hidl_blob.putInt32(_hidl_offset + 12, this.serviceClass);
        _hidl_blob.putInt32(_hidl_offset + 16, this.result);
        int _hidl_vec_size = this.ssInfoData.size();
        _hidl_blob.putInt32(_hidl_offset + 24 + 8, _hidl_vec_size);
        _hidl_blob.putBool(_hidl_offset + 24 + 12, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 16);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            this.ssInfoData.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 16);
        }
        _hidl_blob.putBlob(_hidl_offset + 24 + 0, childBlob);
        int _hidl_vec_size2 = this.cfData.size();
        _hidl_blob.putInt32(_hidl_offset + 40 + 8, _hidl_vec_size2);
        _hidl_blob.putBool(_hidl_offset + 40 + 12, false);
        HwBlob childBlob2 = new HwBlob(_hidl_vec_size2 * 16);
        for (int _hidl_index_1 = 0; _hidl_index_1 < _hidl_vec_size2; _hidl_index_1++) {
            this.cfData.get(_hidl_index_1).writeEmbeddedToBlob(childBlob2, _hidl_index_1 * 16);
        }
        _hidl_blob.putBlob(_hidl_offset + 40 + 0, childBlob2);
        int _hidl_vec_size3 = this.cbNumInfo.size();
        _hidl_blob.putInt32(_hidl_offset + 56 + 8, _hidl_vec_size3);
        _hidl_blob.putBool(_hidl_offset + 56 + 12, false);
        HwBlob childBlob3 = new HwBlob(_hidl_vec_size3 * 24);
        for (int _hidl_index_2 = 0; _hidl_index_2 < _hidl_vec_size3; _hidl_index_2++) {
            this.cbNumInfo.get(_hidl_index_2).writeEmbeddedToBlob(childBlob3, _hidl_index_2 * 24);
        }
        _hidl_blob.putBlob(_hidl_offset + 56 + 0, childBlob3);
    }
}
