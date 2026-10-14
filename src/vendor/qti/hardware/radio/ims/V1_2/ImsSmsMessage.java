package vendor.qti.hardware.radio.ims.V1_2;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class ImsSmsMessage {
    public int messageRef = 0;
    public String format = new String();
    public String smsc = new String();
    public boolean shallRetry = false;
    public ArrayList<Byte> pdu = new ArrayList<>();

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != ImsSmsMessage.class) {
            return false;
        }
        ImsSmsMessage other = (ImsSmsMessage) otherObject;
        if (this.messageRef == other.messageRef && HidlSupport.deepEquals(this.format, other.format) && HidlSupport.deepEquals(this.smsc, other.smsc) && this.shallRetry == other.shallRetry && HidlSupport.deepEquals(this.pdu, other.pdu)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.messageRef))), Integer.valueOf(HidlSupport.deepHashCode(this.format)), Integer.valueOf(HidlSupport.deepHashCode(this.smsc)), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.shallRetry))), Integer.valueOf(HidlSupport.deepHashCode(this.pdu)));
    }

    public final String toString() {
        return "{.messageRef = " + this.messageRef + ", .format = " + this.format + ", .smsc = " + this.smsc + ", .shallRetry = " + this.shallRetry + ", .pdu = " + this.pdu + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(64L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<ImsSmsMessage> readVectorFromParcel(HwParcel parcel) {
        ArrayList<ImsSmsMessage> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 64, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            ImsSmsMessage _hidl_vec_element = new ImsSmsMessage();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 64);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.messageRef = _hidl_blob.getInt32(_hidl_offset + 0);
        String string = _hidl_blob.getString(_hidl_offset + 8);
        this.format = string;
        parcel.readEmbeddedBuffer(string.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 8 + 0, false);
        String string2 = _hidl_blob.getString(_hidl_offset + 24);
        this.smsc = string2;
        parcel.readEmbeddedBuffer(string2.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 24 + 0, false);
        this.shallRetry = _hidl_blob.getBool(_hidl_offset + 40);
        int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 48 + 8);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 1, _hidl_blob.handle(), _hidl_offset + 48 + 0, true);
        this.pdu.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            byte _hidl_vec_element = childBlob.getInt8(_hidl_index_0 * 1);
            this.pdu.add(Byte.valueOf(_hidl_vec_element));
        }
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(64);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<ImsSmsMessage> _hidl_vec) {
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
        _hidl_blob.putInt32(_hidl_offset + 0, this.messageRef);
        _hidl_blob.putString(_hidl_offset + 8, this.format);
        _hidl_blob.putString(24 + _hidl_offset, this.smsc);
        _hidl_blob.putBool(40 + _hidl_offset, this.shallRetry);
        int _hidl_vec_size = this.pdu.size();
        _hidl_blob.putInt32(_hidl_offset + 48 + 8, _hidl_vec_size);
        _hidl_blob.putBool(_hidl_offset + 48 + 12, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 1);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            childBlob.putInt8(_hidl_index_0 * 1, this.pdu.get(_hidl_index_0).byteValue());
        }
        _hidl_blob.putBlob(48 + _hidl_offset + 0, childBlob);
    }
}
