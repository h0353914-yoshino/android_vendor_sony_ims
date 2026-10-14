package vendor.qti.hardware.radio.ims.V1_6;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;
import vendor.qti.hardware.radio.ims.V1_0.RttMode;
import vendor.qti.hardware.radio.ims.V1_0.ServiceType;
import vendor.qti.hardware.radio.ims.V1_0.StatusType;

/* JADX INFO: loaded from: classes.dex */
public final class ServiceStatusInfo {
    public boolean hasIsValid = false;
    public boolean isValid = false;
    public int type = 0;
    public int callType = 0;
    public int status = 0;
    public ArrayList<Byte> userdata = new ArrayList<>();
    public int restrictCause = 0;
    public ArrayList<StatusForAccessTech> accTechStatus = new ArrayList<>();
    public int rttMode = 0;

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != ServiceStatusInfo.class) {
            return false;
        }
        ServiceStatusInfo other = (ServiceStatusInfo) otherObject;
        if (this.hasIsValid == other.hasIsValid && this.isValid == other.isValid && this.type == other.type && this.callType == other.callType && this.status == other.status && HidlSupport.deepEquals(this.userdata, other.userdata) && this.restrictCause == other.restrictCause && HidlSupport.deepEquals(this.accTechStatus, other.accTechStatus) && this.rttMode == other.rttMode) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.hasIsValid))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.isValid))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.type))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.callType))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.status))), Integer.valueOf(HidlSupport.deepHashCode(this.userdata)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.restrictCause))), Integer.valueOf(HidlSupport.deepHashCode(this.accTechStatus)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.rttMode))));
    }

    public final String toString() {
        return "{.hasIsValid = " + this.hasIsValid + ", .isValid = " + this.isValid + ", .type = " + ServiceType.toString(this.type) + ", .callType = " + CallType.toString(this.callType) + ", .status = " + StatusType.toString(this.status) + ", .userdata = " + this.userdata + ", .restrictCause = " + this.restrictCause + ", .accTechStatus = " + this.accTechStatus + ", .rttMode = " + RttMode.toString(this.rttMode) + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(64L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<ServiceStatusInfo> readVectorFromParcel(HwParcel parcel) {
        ArrayList<ServiceStatusInfo> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 64, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            ServiceStatusInfo _hidl_vec_element = new ServiceStatusInfo();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 64);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.hasIsValid = _hidl_blob.getBool(_hidl_offset + 0);
        this.isValid = _hidl_blob.getBool(_hidl_offset + 1);
        this.type = _hidl_blob.getInt32(_hidl_offset + 4);
        this.callType = _hidl_blob.getInt32(_hidl_offset + 8);
        this.status = _hidl_blob.getInt32(_hidl_offset + 12);
        int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 16 + 8);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 1, _hidl_blob.handle(), _hidl_offset + 16 + 0, true);
        this.userdata.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            this.userdata.add(Byte.valueOf(childBlob.getInt8(_hidl_index_0 * 1)));
        }
        this.restrictCause = _hidl_blob.getInt32(_hidl_offset + 32);
        int _hidl_vec_size2 = _hidl_blob.getInt32(_hidl_offset + 40 + 8);
        HwBlob childBlob2 = parcel.readEmbeddedBuffer(_hidl_vec_size2 * 64, _hidl_blob.handle(), _hidl_offset + 40 + 0, true);
        this.accTechStatus.clear();
        for (int _hidl_index_1 = 0; _hidl_index_1 < _hidl_vec_size2; _hidl_index_1++) {
            StatusForAccessTech _hidl_vec_element = new StatusForAccessTech();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob2, _hidl_index_1 * 64);
            this.accTechStatus.add(_hidl_vec_element);
        }
        this.rttMode = _hidl_blob.getInt32(_hidl_offset + 56);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(64);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<ServiceStatusInfo> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 64);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 64);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putBool(_hidl_offset + 0, this.hasIsValid);
        _hidl_blob.putBool(_hidl_offset + 1, this.isValid);
        _hidl_blob.putInt32(_hidl_offset + 4, this.type);
        _hidl_blob.putInt32(_hidl_offset + 8, this.callType);
        _hidl_blob.putInt32(_hidl_offset + 12, this.status);
        int _hidl_vec_size = this.userdata.size();
        _hidl_blob.putInt32(_hidl_offset + 16 + 8, _hidl_vec_size);
        _hidl_blob.putBool(_hidl_offset + 16 + 12, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 1);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            childBlob.putInt8(_hidl_index_0 * 1, this.userdata.get(_hidl_index_0).byteValue());
        }
        _hidl_blob.putBlob(_hidl_offset + 16 + 0, childBlob);
        _hidl_blob.putInt32(_hidl_offset + 32, this.restrictCause);
        int _hidl_vec_size2 = this.accTechStatus.size();
        _hidl_blob.putInt32(_hidl_offset + 40 + 8, _hidl_vec_size2);
        _hidl_blob.putBool(_hidl_offset + 40 + 12, false);
        HwBlob childBlob2 = new HwBlob(_hidl_vec_size2 * 64);
        for (int _hidl_index_1 = 0; _hidl_index_1 < _hidl_vec_size2; _hidl_index_1++) {
            this.accTechStatus.get(_hidl_index_1).writeEmbeddedToBlob(childBlob2, _hidl_index_1 * 64);
        }
        _hidl_blob.putBlob(_hidl_offset + 40 + 0, childBlob2);
        _hidl_blob.putInt32(_hidl_offset + 56, this.rttMode);
    }
}
