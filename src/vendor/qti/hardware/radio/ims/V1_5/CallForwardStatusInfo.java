package vendor.qti.hardware.radio.ims.V1_5;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;
import vendor.qti.hardware.radio.ims.V1_0.SipErrorInfo;

/* JADX INFO: loaded from: classes.dex */
public final class CallForwardStatusInfo {
    public SipErrorInfo errorDetails = new SipErrorInfo();
    public ArrayList<CallForwardStatus> status = new ArrayList<>();

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != CallForwardStatusInfo.class) {
            return false;
        }
        CallForwardStatusInfo other = (CallForwardStatusInfo) otherObject;
        if (HidlSupport.deepEquals(this.errorDetails, other.errorDetails) && HidlSupport.deepEquals(this.status, other.status)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(this.errorDetails)), Integer.valueOf(HidlSupport.deepHashCode(this.status)));
    }

    public final String toString() {
        return "{.errorDetails = " + this.errorDetails + ", .status = " + this.status + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(40L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<CallForwardStatusInfo> readVectorFromParcel(HwParcel parcel) {
        ArrayList<CallForwardStatusInfo> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 40, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            CallForwardStatusInfo _hidl_vec_element = new CallForwardStatusInfo();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 40);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.errorDetails.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 0);
        int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 24 + 8);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 32, _hidl_blob.handle(), _hidl_offset + 24 + 0, true);
        this.status.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            CallForwardStatus _hidl_vec_element = new CallForwardStatus();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 32);
            this.status.add(_hidl_vec_element);
        }
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(40);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<CallForwardStatusInfo> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 40);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 40);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        this.errorDetails.writeEmbeddedToBlob(_hidl_blob, _hidl_offset + 0);
        int _hidl_vec_size = this.status.size();
        _hidl_blob.putInt32(_hidl_offset + 24 + 8, _hidl_vec_size);
        _hidl_blob.putBool(_hidl_offset + 24 + 12, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 32);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            this.status.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 32);
        }
        _hidl_blob.putBlob(24 + _hidl_offset + 0, childBlob);
    }
}
