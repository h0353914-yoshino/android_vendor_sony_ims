package vendor.qti.hardware.radio.ims.V1_3;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;
import vendor.qti.hardware.radio.ims.V1_0.CbNumListInfo;
import vendor.qti.hardware.radio.ims.V1_0.FacilityType;
import vendor.qti.hardware.radio.ims.V1_0.ServiceClassStatus;
import vendor.qti.hardware.radio.ims.V1_0.SipErrorInfo;

/* JADX INFO: loaded from: classes.dex */
public final class SuppServiceStatus {
    public int status = 0;
    public int provisionStatus = 0;
    public int facilityType = 0;
    public String failureCause = new String();
    public ArrayList<CbNumListInfo> cbNumListInfo = new ArrayList<>();
    public boolean hasErrorDetails = false;
    public SipErrorInfo errorDetails = new SipErrorInfo();

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != SuppServiceStatus.class) {
            return false;
        }
        SuppServiceStatus other = (SuppServiceStatus) otherObject;
        if (this.status == other.status && this.provisionStatus == other.provisionStatus && this.facilityType == other.facilityType && HidlSupport.deepEquals(this.failureCause, other.failureCause) && HidlSupport.deepEquals(this.cbNumListInfo, other.cbNumListInfo) && this.hasErrorDetails == other.hasErrorDetails && HidlSupport.deepEquals(this.errorDetails, other.errorDetails)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.status))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.provisionStatus))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.facilityType))), Integer.valueOf(HidlSupport.deepHashCode(this.failureCause)), Integer.valueOf(HidlSupport.deepHashCode(this.cbNumListInfo)), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.hasErrorDetails))), Integer.valueOf(HidlSupport.deepHashCode(this.errorDetails)));
    }

    public final String toString() {
        return "{.status = " + ServiceClassStatus.toString(this.status) + ", .provisionStatus = " + ServiceClassProvisionStatus.toString(this.provisionStatus) + ", .facilityType = " + FacilityType.toString(this.facilityType) + ", .failureCause = " + this.failureCause + ", .cbNumListInfo = " + this.cbNumListInfo + ", .hasErrorDetails = " + this.hasErrorDetails + ", .errorDetails = " + this.errorDetails + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(80L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<SuppServiceStatus> readVectorFromParcel(HwParcel parcel) {
        ArrayList<SuppServiceStatus> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 80, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            SuppServiceStatus _hidl_vec_element = new SuppServiceStatus();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 80);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.status = _hidl_blob.getInt32(_hidl_offset + 0);
        this.provisionStatus = _hidl_blob.getInt32(_hidl_offset + 4);
        this.facilityType = _hidl_blob.getInt32(_hidl_offset + 8);
        String string = _hidl_blob.getString(_hidl_offset + 16);
        this.failureCause = string;
        parcel.readEmbeddedBuffer(string.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 16 + 0, false);
        int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 32 + 8);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 24, _hidl_blob.handle(), _hidl_offset + 32 + 0, true);
        this.cbNumListInfo.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            CbNumListInfo _hidl_vec_element = new CbNumListInfo();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 24);
            this.cbNumListInfo.add(_hidl_vec_element);
        }
        this.hasErrorDetails = _hidl_blob.getBool(_hidl_offset + 48);
        this.errorDetails.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 56);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(80);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<SuppServiceStatus> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 80);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 80);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(_hidl_offset + 0, this.status);
        _hidl_blob.putInt32(4 + _hidl_offset, this.provisionStatus);
        _hidl_blob.putInt32(_hidl_offset + 8, this.facilityType);
        _hidl_blob.putString(16 + _hidl_offset, this.failureCause);
        int _hidl_vec_size = this.cbNumListInfo.size();
        _hidl_blob.putInt32(_hidl_offset + 32 + 8, _hidl_vec_size);
        _hidl_blob.putBool(_hidl_offset + 32 + 12, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 24);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            this.cbNumListInfo.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 24);
        }
        _hidl_blob.putBlob(32 + _hidl_offset + 0, childBlob);
        _hidl_blob.putBool(48 + _hidl_offset, this.hasErrorDetails);
        this.errorDetails.writeEmbeddedToBlob(_hidl_blob, 56 + _hidl_offset);
    }
}
