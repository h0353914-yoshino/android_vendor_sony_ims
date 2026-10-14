package vendor.qti.hardware.radio.ims.V1_6;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class CallComposerInfo {
    public int callId = 0;
    public int priority = 0;
    public ArrayList<Short> subject = new ArrayList<>();
    public CallLocation location = new CallLocation();
    public String imageUrl = new String();

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != CallComposerInfo.class) {
            return false;
        }
        CallComposerInfo other = (CallComposerInfo) otherObject;
        if (this.callId == other.callId && this.priority == other.priority && HidlSupport.deepEquals(this.subject, other.subject) && HidlSupport.deepEquals(this.location, other.location) && HidlSupport.deepEquals(this.imageUrl, other.imageUrl)) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.callId))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.priority))), Integer.valueOf(HidlSupport.deepHashCode(this.subject)), Integer.valueOf(HidlSupport.deepHashCode(this.location)), Integer.valueOf(HidlSupport.deepHashCode(this.imageUrl)));
    }

    public final String toString() {
        return "{.callId = " + this.callId + ", .priority = " + CallPriority.toString(this.priority) + ", .subject = " + this.subject + ", .location = " + this.location + ", .imageUrl = " + this.imageUrl + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(64L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<CallComposerInfo> readVectorFromParcel(HwParcel parcel) {
        ArrayList<CallComposerInfo> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 64, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            CallComposerInfo _hidl_vec_element = new CallComposerInfo();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 64);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.callId = _hidl_blob.getInt32(_hidl_offset + 0);
        this.priority = _hidl_blob.getInt32(_hidl_offset + 4);
        int _hidl_vec_size = _hidl_blob.getInt32(_hidl_offset + 8 + 8);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 2, _hidl_blob.handle(), _hidl_offset + 8 + 0, true);
        this.subject.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            short _hidl_vec_element = childBlob.getInt16(_hidl_index_0 * 2);
            this.subject.add(Short.valueOf(_hidl_vec_element));
        }
        this.location.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 24);
        String string = _hidl_blob.getString(_hidl_offset + 48);
        this.imageUrl = string;
        parcel.readEmbeddedBuffer(string.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 48 + 0, false);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(64);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<CallComposerInfo> _hidl_vec) {
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
        _hidl_blob.putInt32(_hidl_offset + 0, this.callId);
        _hidl_blob.putInt32(4 + _hidl_offset, this.priority);
        int _hidl_vec_size = this.subject.size();
        _hidl_blob.putInt32(_hidl_offset + 8 + 8, _hidl_vec_size);
        _hidl_blob.putBool(_hidl_offset + 8 + 12, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 2);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            childBlob.putInt16(_hidl_index_0 * 2, this.subject.get(_hidl_index_0).shortValue());
        }
        _hidl_blob.putBlob(8 + _hidl_offset + 0, childBlob);
        this.location.writeEmbeddedToBlob(_hidl_blob, 24 + _hidl_offset);
        _hidl_blob.putString(48 + _hidl_offset, this.imageUrl);
    }
}
