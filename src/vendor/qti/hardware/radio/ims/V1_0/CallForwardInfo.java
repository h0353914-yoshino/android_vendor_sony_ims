package vendor.qti.hardware.radio.ims.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class CallForwardInfo {
    public int status = 0;
    public int reason = 0;
    public int serviceClass = 0;
    public int toa = 0;
    public String number = new String();
    public int timeSeconds = 0;
    public boolean hasCallFwdTimerStart = false;
    public CallFwdTimerInfo callFwdTimerStart = new CallFwdTimerInfo();
    public boolean hasCallFwdTimerEnd = false;
    public CallFwdTimerInfo callFwdTimerEnd = new CallFwdTimerInfo();

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != CallForwardInfo.class) {
            return false;
        }
        CallForwardInfo other = (CallForwardInfo) otherObject;
        if (this.status == other.status && this.reason == other.reason && this.serviceClass == other.serviceClass && this.toa == other.toa && HidlSupport.deepEquals(this.number, other.number) && this.timeSeconds == other.timeSeconds && this.hasCallFwdTimerStart == other.hasCallFwdTimerStart && HidlSupport.deepEquals(this.callFwdTimerStart, other.callFwdTimerStart) && this.hasCallFwdTimerEnd == other.hasCallFwdTimerEnd && HidlSupport.deepEquals(this.callFwdTimerEnd, other.callFwdTimerEnd)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.status))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.reason))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.serviceClass))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.toa))), Integer.valueOf(HidlSupport.deepHashCode(this.number)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.timeSeconds))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.hasCallFwdTimerStart))), Integer.valueOf(HidlSupport.deepHashCode(this.callFwdTimerStart)), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.hasCallFwdTimerEnd))), Integer.valueOf(HidlSupport.deepHashCode(this.callFwdTimerEnd)));
    }

    public final String toString() {
        return "{.status = " + this.status + ", .reason = " + this.reason + ", .serviceClass = " + this.serviceClass + ", .toa = " + this.toa + ", .number = " + this.number + ", .timeSeconds = " + this.timeSeconds + ", .hasCallFwdTimerStart = " + this.hasCallFwdTimerStart + ", .callFwdTimerStart = " + this.callFwdTimerStart + ", .hasCallFwdTimerEnd = " + this.hasCallFwdTimerEnd + ", .callFwdTimerEnd = " + this.callFwdTimerEnd + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(104L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<CallForwardInfo> readVectorFromParcel(HwParcel parcel) {
        ArrayList<CallForwardInfo> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 104, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            CallForwardInfo _hidl_vec_element = new CallForwardInfo();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 104);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.status = _hidl_blob.getInt32(_hidl_offset + 0);
        this.reason = _hidl_blob.getInt32(_hidl_offset + 4);
        this.serviceClass = _hidl_blob.getInt32(_hidl_offset + 8);
        this.toa = _hidl_blob.getInt32(_hidl_offset + 12);
        String string = _hidl_blob.getString(_hidl_offset + 16);
        this.number = string;
        parcel.readEmbeddedBuffer(string.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 16 + 0, false);
        this.timeSeconds = _hidl_blob.getInt32(_hidl_offset + 32);
        this.hasCallFwdTimerStart = _hidl_blob.getBool(_hidl_offset + 36);
        this.callFwdTimerStart.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 40);
        this.hasCallFwdTimerEnd = _hidl_blob.getBool(_hidl_offset + 68);
        this.callFwdTimerEnd.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 72);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(104);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<CallForwardInfo> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 104);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 104);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(0 + _hidl_offset, this.status);
        _hidl_blob.putInt32(4 + _hidl_offset, this.reason);
        _hidl_blob.putInt32(8 + _hidl_offset, this.serviceClass);
        _hidl_blob.putInt32(12 + _hidl_offset, this.toa);
        _hidl_blob.putString(16 + _hidl_offset, this.number);
        _hidl_blob.putInt32(32 + _hidl_offset, this.timeSeconds);
        _hidl_blob.putBool(36 + _hidl_offset, this.hasCallFwdTimerStart);
        this.callFwdTimerStart.writeEmbeddedToBlob(_hidl_blob, 40 + _hidl_offset);
        _hidl_blob.putBool(68 + _hidl_offset, this.hasCallFwdTimerEnd);
        this.callFwdTimerEnd.writeEmbeddedToBlob(_hidl_blob, 72 + _hidl_offset);
    }
}
