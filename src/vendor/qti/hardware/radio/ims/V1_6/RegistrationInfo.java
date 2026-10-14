package vendor.qti.hardware.radio.ims.V1_6;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;
import vendor.qti.hardware.radio.ims.V1_0.RegState;

/* JADX INFO: loaded from: classes.dex */
public final class RegistrationInfo {
    public int state = 0;
    public int errorCode = 0;
    public String errorMessage = new String();
    public int radioTech = 0;
    public String pAssociatedUris = new String();

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != RegistrationInfo.class) {
            return false;
        }
        RegistrationInfo other = (RegistrationInfo) otherObject;
        if (this.state == other.state && this.errorCode == other.errorCode && HidlSupport.deepEquals(this.errorMessage, other.errorMessage) && this.radioTech == other.radioTech && HidlSupport.deepEquals(this.pAssociatedUris, other.pAssociatedUris)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.state))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.errorCode))), Integer.valueOf(HidlSupport.deepHashCode(this.errorMessage)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.radioTech))), Integer.valueOf(HidlSupport.deepHashCode(this.pAssociatedUris)));
    }

    public final String toString() {
        return "{.state = " + RegState.toString(this.state) + ", .errorCode = " + this.errorCode + ", .errorMessage = " + this.errorMessage + ", .radioTech = " + RadioTechType.toString(this.radioTech) + ", .pAssociatedUris = " + this.pAssociatedUris + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(48L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<RegistrationInfo> readVectorFromParcel(HwParcel parcel) {
        ArrayList<RegistrationInfo> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 48, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            RegistrationInfo _hidl_vec_element = new RegistrationInfo();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 48);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.state = _hidl_blob.getInt32(_hidl_offset + 0);
        this.errorCode = _hidl_blob.getInt32(_hidl_offset + 4);
        String string = _hidl_blob.getString(_hidl_offset + 8);
        this.errorMessage = string;
        parcel.readEmbeddedBuffer(string.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 8 + 0, false);
        this.radioTech = _hidl_blob.getInt32(_hidl_offset + 24);
        String string2 = _hidl_blob.getString(_hidl_offset + 32);
        this.pAssociatedUris = string2;
        parcel.readEmbeddedBuffer(string2.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 32 + 0, false);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(48);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<RegistrationInfo> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 48);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 48);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(0 + _hidl_offset, this.state);
        _hidl_blob.putInt32(4 + _hidl_offset, this.errorCode);
        _hidl_blob.putString(8 + _hidl_offset, this.errorMessage);
        _hidl_blob.putInt32(24 + _hidl_offset, this.radioTech);
        _hidl_blob.putString(32 + _hidl_offset, this.pAssociatedUris);
    }
}
