package vendor.qti.hardware.radio.ims.V1_4;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class MultiIdentityLineInfoHal {
    public String msisdn = new String();
    public int registrationStatus = 0;
    public int lineType = 0;

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != MultiIdentityLineInfoHal.class) {
            return false;
        }
        MultiIdentityLineInfoHal other = (MultiIdentityLineInfoHal) otherObject;
        if (HidlSupport.deepEquals(this.msisdn, other.msisdn) && this.registrationStatus == other.registrationStatus && this.lineType == other.lineType) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(this.msisdn)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.registrationStatus))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.lineType))));
    }

    public final String toString() {
        return "{.msisdn = " + this.msisdn + ", .registrationStatus = " + MultiIdentityRegistrationStatus.toString(this.registrationStatus) + ", .lineType = " + MultiIdentityLineType.toString(this.lineType) + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(24L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<MultiIdentityLineInfoHal> readVectorFromParcel(HwParcel parcel) {
        ArrayList<MultiIdentityLineInfoHal> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 24, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            MultiIdentityLineInfoHal _hidl_vec_element = new MultiIdentityLineInfoHal();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 24);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        String string = _hidl_blob.getString(_hidl_offset + 0);
        this.msisdn = string;
        parcel.readEmbeddedBuffer(string.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 0 + 0, false);
        this.registrationStatus = _hidl_blob.getInt32(16 + _hidl_offset);
        this.lineType = _hidl_blob.getInt32(20 + _hidl_offset);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(24);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<MultiIdentityLineInfoHal> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 24);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 24);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putString(0 + _hidl_offset, this.msisdn);
        _hidl_blob.putInt32(16 + _hidl_offset, this.registrationStatus);
        _hidl_blob.putInt32(20 + _hidl_offset, this.lineType);
    }
}
