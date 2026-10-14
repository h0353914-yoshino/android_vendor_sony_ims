package vendor.qti.hardware.radio.ims.V1_0;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class MessageDetails {
    public String toAddress = new String();
    public String fromAddress = new String();
    public String subject = new String();
    public String date = new String();
    public int priority = 0;
    public String id = new String();
    public int type = 0;

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != MessageDetails.class) {
            return false;
        }
        MessageDetails other = (MessageDetails) otherObject;
        if (HidlSupport.deepEquals(this.toAddress, other.toAddress) && HidlSupport.deepEquals(this.fromAddress, other.fromAddress) && HidlSupport.deepEquals(this.subject, other.subject) && HidlSupport.deepEquals(this.date, other.date) && this.priority == other.priority && HidlSupport.deepEquals(this.id, other.id) && this.type == other.type) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(this.toAddress)), Integer.valueOf(HidlSupport.deepHashCode(this.fromAddress)), Integer.valueOf(HidlSupport.deepHashCode(this.subject)), Integer.valueOf(HidlSupport.deepHashCode(this.date)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.priority))), Integer.valueOf(HidlSupport.deepHashCode(this.id)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.type))));
    }

    public final String toString() {
        return "{.toAddress = " + this.toAddress + ", .fromAddress = " + this.fromAddress + ", .subject = " + this.subject + ", .date = " + this.date + ", .priority = " + MessagePriority.toString(this.priority) + ", .id = " + this.id + ", .type = " + MessageType.toString(this.type) + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(96L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<MessageDetails> readVectorFromParcel(HwParcel parcel) {
        ArrayList<MessageDetails> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 96, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            MessageDetails _hidl_vec_element = new MessageDetails();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 96);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        String string = _hidl_blob.getString(_hidl_offset + 0);
        this.toAddress = string;
        parcel.readEmbeddedBuffer(string.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 0 + 0, false);
        String string2 = _hidl_blob.getString(_hidl_offset + 16);
        this.fromAddress = string2;
        parcel.readEmbeddedBuffer(string2.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 16 + 0, false);
        String string3 = _hidl_blob.getString(_hidl_offset + 32);
        this.subject = string3;
        parcel.readEmbeddedBuffer(string3.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 32 + 0, false);
        String string4 = _hidl_blob.getString(_hidl_offset + 48);
        this.date = string4;
        parcel.readEmbeddedBuffer(string4.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 48 + 0, false);
        this.priority = _hidl_blob.getInt32(_hidl_offset + 64);
        String string5 = _hidl_blob.getString(_hidl_offset + 72);
        this.id = string5;
        parcel.readEmbeddedBuffer(string5.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 72 + 0, false);
        this.type = _hidl_blob.getInt32(_hidl_offset + 88);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(96);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<MessageDetails> _hidl_vec) {
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
        _hidl_blob.putString(0 + _hidl_offset, this.toAddress);
        _hidl_blob.putString(16 + _hidl_offset, this.fromAddress);
        _hidl_blob.putString(32 + _hidl_offset, this.subject);
        _hidl_blob.putString(48 + _hidl_offset, this.date);
        _hidl_blob.putInt32(64 + _hidl_offset, this.priority);
        _hidl_blob.putString(72 + _hidl_offset, this.id);
        _hidl_blob.putInt32(88 + _hidl_offset, this.type);
    }
}
