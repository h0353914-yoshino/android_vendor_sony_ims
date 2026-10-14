package vendor.qti.hardware.radio.ims.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class CallDetails {
    public int callType = 0;
    public int callDomain = 0;
    public int extrasLength = 0;
    public ArrayList<String> extras = new ArrayList<>();
    public ArrayList<ServiceStatusInfo> localAbility = new ArrayList<>();
    public ArrayList<ServiceStatusInfo> peerAbility = new ArrayList<>();
    public int callSubstate = 0;
    public int mediaId = 0;
    public int causeCode = 0;
    public int rttMode = 0;
    public String sipAlternateUri = new String();

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != CallDetails.class) {
            return false;
        }
        CallDetails other = (CallDetails) otherObject;
        if (this.callType == other.callType && this.callDomain == other.callDomain && this.extrasLength == other.extrasLength && HidlSupport.deepEquals(this.extras, other.extras) && HidlSupport.deepEquals(this.localAbility, other.localAbility) && HidlSupport.deepEquals(this.peerAbility, other.peerAbility) && this.callSubstate == other.callSubstate && this.mediaId == other.mediaId && this.causeCode == other.causeCode && this.rttMode == other.rttMode && HidlSupport.deepEquals(this.sipAlternateUri, other.sipAlternateUri)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.callType))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.callDomain))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.extrasLength))), Integer.valueOf(HidlSupport.deepHashCode(this.extras)), Integer.valueOf(HidlSupport.deepHashCode(this.localAbility)), Integer.valueOf(HidlSupport.deepHashCode(this.peerAbility)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.callSubstate))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.mediaId))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.causeCode))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.rttMode))), Integer.valueOf(HidlSupport.deepHashCode(this.sipAlternateUri)));
    }

    public final String toString() {
        return "{.callType = " + CallType.toString(this.callType) + ", .callDomain = " + CallDomain.toString(this.callDomain) + ", .extrasLength = " + this.extrasLength + ", .extras = " + this.extras + ", .localAbility = " + this.localAbility + ", .peerAbility = " + this.peerAbility + ", .callSubstate = " + this.callSubstate + ", .mediaId = " + this.mediaId + ", .causeCode = " + this.causeCode + ", .rttMode = " + RttMode.toString(this.rttMode) + ", .sipAlternateUri = " + this.sipAlternateUri + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(96L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<CallDetails> readVectorFromParcel(HwParcel parcel) {
        ArrayList<CallDetails> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 96, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            CallDetails _hidl_vec_element = new CallDetails();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 96);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.callType = _hidl_blob.getInt32(_hidl_offset + 0);
        this.callDomain = _hidl_blob.getInt32(_hidl_offset + 4);
        this.extrasLength = _hidl_blob.getInt32(_hidl_offset + 8);
        int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 16 + 8);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 16, _hidl_blob.handle(), _hidl_offset + 16 + 0, true);
        this.extras.clear();
        int _hidl_index_0 = 0;
        while (_hidl_index_0 < _hidl_vec_size) {
            new String();
            String _hidl_vec_element = childBlob.getString(_hidl_index_0 * 16);
            parcel.readEmbeddedBuffer(_hidl_vec_element.getBytes().length + 1, childBlob.handle(), (_hidl_index_0 * 16) + 0, false);
            this.extras.add(_hidl_vec_element);
            _hidl_index_0++;
            childBlob = childBlob;
        }
        int _hidl_vec_size2 = _hidl_blob.getInt32(_hidl_offset + 32 + 8);
        HwBlob childBlob2 = parcel.readEmbeddedBuffer(_hidl_vec_size2 * 64, _hidl_blob.handle(), _hidl_offset + 32 + 0, true);
        this.localAbility.clear();
        for (int _hidl_index_1 = 0; _hidl_index_1 < _hidl_vec_size2; _hidl_index_1++) {
            ServiceStatusInfo _hidl_vec_element2 = new ServiceStatusInfo();
            _hidl_vec_element2.readEmbeddedFromParcel(parcel, childBlob2, _hidl_index_1 * 64);
            this.localAbility.add(_hidl_vec_element2);
        }
        int _hidl_vec_size3 = _hidl_blob.getInt32(_hidl_offset + 48 + 8);
        HwBlob childBlob3 = parcel.readEmbeddedBuffer(_hidl_vec_size3 * 64, _hidl_blob.handle(), _hidl_offset + 48 + 0, true);
        this.peerAbility.clear();
        for (int _hidl_index_2 = 0; _hidl_index_2 < _hidl_vec_size3; _hidl_index_2++) {
            ServiceStatusInfo _hidl_vec_element3 = new ServiceStatusInfo();
            _hidl_vec_element3.readEmbeddedFromParcel(parcel, childBlob3, _hidl_index_2 * 64);
            this.peerAbility.add(_hidl_vec_element3);
        }
        this.callSubstate = _hidl_blob.getInt32(_hidl_offset + 64);
        this.mediaId = _hidl_blob.getInt32(_hidl_offset + 68);
        this.causeCode = _hidl_blob.getInt32(_hidl_offset + 72);
        this.rttMode = _hidl_blob.getInt32(_hidl_offset + 76);
        String string = _hidl_blob.getString(_hidl_offset + 80);
        this.sipAlternateUri = string;
        parcel.readEmbeddedBuffer(string.getBytes().length + 1, _hidl_blob.handle(), 0 + _hidl_offset + 80, false);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(96);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<CallDetails> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 96);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 96);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(_hidl_offset + 0, this.callType);
        _hidl_blob.putInt32(_hidl_offset + 4, this.callDomain);
        _hidl_blob.putInt32(_hidl_offset + 8, this.extrasLength);
        int _hidl_vec_size = this.extras.size();
        _hidl_blob.putInt32(_hidl_offset + 16 + 8, _hidl_vec_size);
        _hidl_blob.putBool(_hidl_offset + 16 + 12, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 16);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            childBlob.putString(_hidl_index_0 * 16, this.extras.get(_hidl_index_0));
        }
        _hidl_blob.putBlob(_hidl_offset + 16 + 0, childBlob);
        int _hidl_vec_size2 = this.localAbility.size();
        _hidl_blob.putInt32(_hidl_offset + 32 + 8, _hidl_vec_size2);
        _hidl_blob.putBool(_hidl_offset + 32 + 12, false);
        HwBlob childBlob2 = new HwBlob(_hidl_vec_size2 * 64);
        for (int _hidl_index_1 = 0; _hidl_index_1 < _hidl_vec_size2; _hidl_index_1++) {
            this.localAbility.get(_hidl_index_1).writeEmbeddedToBlob(childBlob2, _hidl_index_1 * 64);
        }
        _hidl_blob.putBlob(_hidl_offset + 32 + 0, childBlob2);
        int _hidl_vec_size3 = this.peerAbility.size();
        _hidl_blob.putInt32(_hidl_offset + 48 + 8, _hidl_vec_size3);
        _hidl_blob.putBool(_hidl_offset + 48 + 12, false);
        HwBlob childBlob3 = new HwBlob(_hidl_vec_size3 * 64);
        for (int _hidl_index_2 = 0; _hidl_index_2 < _hidl_vec_size3; _hidl_index_2++) {
            this.peerAbility.get(_hidl_index_2).writeEmbeddedToBlob(childBlob3, _hidl_index_2 * 64);
        }
        _hidl_blob.putBlob(_hidl_offset + 48 + 0, childBlob3);
        _hidl_blob.putInt32(_hidl_offset + 64, this.callSubstate);
        _hidl_blob.putInt32(_hidl_offset + 68, this.mediaId);
        _hidl_blob.putInt32(_hidl_offset + 72, this.causeCode);
        _hidl_blob.putInt32(_hidl_offset + 76, this.rttMode);
        _hidl_blob.putString(_hidl_offset + 80, this.sipAlternateUri);
    }
}
