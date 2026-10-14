package vendor.qti.hardware.radio.ims.V1_1;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class HangupRequestInfo {
    public int connIndex = 0;
    public boolean hasMultiParty = false;
    public boolean multiParty = false;
    public String connUri = new String();
    public int conf_id = 0;
    public boolean hasFailCauseResponse = false;
    public CallFailCauseResponse failCauseResponse = new CallFailCauseResponse();

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != HangupRequestInfo.class) {
            return false;
        }
        HangupRequestInfo other = (HangupRequestInfo) otherObject;
        if (this.connIndex == other.connIndex && this.hasMultiParty == other.hasMultiParty && this.multiParty == other.multiParty && HidlSupport.deepEquals(this.connUri, other.connUri) && this.conf_id == other.conf_id && this.hasFailCauseResponse == other.hasFailCauseResponse && HidlSupport.deepEquals(this.failCauseResponse, other.failCauseResponse)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.connIndex))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.hasMultiParty))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.multiParty))), Integer.valueOf(HidlSupport.deepHashCode(this.connUri)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.conf_id))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.hasFailCauseResponse))), Integer.valueOf(HidlSupport.deepHashCode(this.failCauseResponse)));
    }

    public final String toString() {
        return "{.connIndex = " + this.connIndex + ", .hasMultiParty = " + this.hasMultiParty + ", .multiParty = " + this.multiParty + ", .connUri = " + this.connUri + ", .conf_id = " + this.conf_id + ", .hasFailCauseResponse = " + this.hasFailCauseResponse + ", .failCauseResponse = " + this.failCauseResponse + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(104L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<HangupRequestInfo> readVectorFromParcel(HwParcel parcel) {
        ArrayList<HangupRequestInfo> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 104, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            HangupRequestInfo _hidl_vec_element = new HangupRequestInfo();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 104);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.connIndex = _hidl_blob.getInt32(_hidl_offset + 0);
        this.hasMultiParty = _hidl_blob.getBool(_hidl_offset + 4);
        this.multiParty = _hidl_blob.getBool(_hidl_offset + 5);
        String string = _hidl_blob.getString(_hidl_offset + 8);
        this.connUri = string;
        parcel.readEmbeddedBuffer(string.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 8 + 0, false);
        this.conf_id = _hidl_blob.getInt32(_hidl_offset + 24);
        this.hasFailCauseResponse = _hidl_blob.getBool(_hidl_offset + 28);
        this.failCauseResponse.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 32);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(104);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<HangupRequestInfo> _hidl_vec) {
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
        _hidl_blob.putInt32(0 + _hidl_offset, this.connIndex);
        _hidl_blob.putBool(4 + _hidl_offset, this.hasMultiParty);
        _hidl_blob.putBool(5 + _hidl_offset, this.multiParty);
        _hidl_blob.putString(8 + _hidl_offset, this.connUri);
        _hidl_blob.putInt32(24 + _hidl_offset, this.conf_id);
        _hidl_blob.putBool(28 + _hidl_offset, this.hasFailCauseResponse);
        this.failCauseResponse.writeEmbeddedToBlob(_hidl_blob, 32 + _hidl_offset);
    }
}
