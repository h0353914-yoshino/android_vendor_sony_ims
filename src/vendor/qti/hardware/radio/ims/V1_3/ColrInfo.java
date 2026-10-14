package vendor.qti.hardware.radio.ims.V1_3;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;
import vendor.qti.hardware.radio.ims.V1_0.IpPresentation;
import vendor.qti.hardware.radio.ims.V1_0.ServiceClassStatus;
import vendor.qti.hardware.radio.ims.V1_0.SipErrorInfo;

/* JADX INFO: loaded from: classes.dex */
public final class ColrInfo {
    public int status = 0;
    public int provisionStatus = 0;
    public int presentation = 0;
    public boolean hasErrorDetails = false;
    public SipErrorInfo errorDetails = new SipErrorInfo();

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != ColrInfo.class) {
            return false;
        }
        ColrInfo other = (ColrInfo) otherObject;
        if (this.status == other.status && this.provisionStatus == other.provisionStatus && this.presentation == other.presentation && this.hasErrorDetails == other.hasErrorDetails && HidlSupport.deepEquals(this.errorDetails, other.errorDetails)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.status))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.provisionStatus))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.presentation))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.hasErrorDetails))), Integer.valueOf(HidlSupport.deepHashCode(this.errorDetails)));
    }

    public final String toString() {
        return "{.status = " + ServiceClassStatus.toString(this.status) + ", .provisionStatus = " + ServiceClassProvisionStatus.toString(this.provisionStatus) + ", .presentation = " + IpPresentation.toString(this.presentation) + ", .hasErrorDetails = " + this.hasErrorDetails + ", .errorDetails = " + this.errorDetails + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(40L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<ColrInfo> readVectorFromParcel(HwParcel parcel) {
        ArrayList<ColrInfo> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 40, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            ColrInfo _hidl_vec_element = new ColrInfo();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 40);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.status = _hidl_blob.getInt32(0 + _hidl_offset);
        this.provisionStatus = _hidl_blob.getInt32(4 + _hidl_offset);
        this.presentation = _hidl_blob.getInt32(8 + _hidl_offset);
        this.hasErrorDetails = _hidl_blob.getBool(12 + _hidl_offset);
        this.errorDetails.readEmbeddedFromParcel(parcel, _hidl_blob, 16 + _hidl_offset);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(40);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<ColrInfo> _hidl_vec) {
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
        _hidl_blob.putInt32(0 + _hidl_offset, this.status);
        _hidl_blob.putInt32(4 + _hidl_offset, this.provisionStatus);
        _hidl_blob.putInt32(8 + _hidl_offset, this.presentation);
        _hidl_blob.putBool(12 + _hidl_offset, this.hasErrorDetails);
        this.errorDetails.writeEmbeddedToBlob(_hidl_blob, 16 + _hidl_offset);
    }
}
