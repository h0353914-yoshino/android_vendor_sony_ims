package vendor.qti.hardware.radio.ims.V1_6;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;

/* JADX INFO: loaded from: classes.dex */
public final class CallLocation {
    public float radius = 0.0f;
    public double latitude = 0.0d;
    public double longitude = 0.0d;

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != CallLocation.class) {
            return false;
        }
        CallLocation other = (CallLocation) otherObject;
        if (this.radius == other.radius && this.latitude == other.latitude && this.longitude == other.longitude) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Float.valueOf(this.radius))), Integer.valueOf(HidlSupport.deepHashCode(Double.valueOf(this.latitude))), Integer.valueOf(HidlSupport.deepHashCode(Double.valueOf(this.longitude))));
    }

    public final String toString() {
        return "{.radius = " + this.radius + ", .latitude = " + this.latitude + ", .longitude = " + this.longitude + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(24L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<CallLocation> readVectorFromParcel(HwParcel parcel) {
        ArrayList<CallLocation> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 24, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            CallLocation _hidl_vec_element = new CallLocation();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 24);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.radius = _hidl_blob.getFloat(0 + _hidl_offset);
        this.latitude = _hidl_blob.getDouble(8 + _hidl_offset);
        this.longitude = _hidl_blob.getDouble(16 + _hidl_offset);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(24);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<CallLocation> _hidl_vec) {
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
        _hidl_blob.putFloat(0 + _hidl_offset, this.radius);
        _hidl_blob.putDouble(8 + _hidl_offset, this.latitude);
        _hidl_blob.putDouble(16 + _hidl_offset, this.longitude);
    }
}
