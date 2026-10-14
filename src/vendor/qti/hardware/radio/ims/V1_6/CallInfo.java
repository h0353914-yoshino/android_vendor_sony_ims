package vendor.qti.hardware.radio.ims.V1_6;

import android.os.HidlSupport;
import android.os.HwBlob;
import android.os.HwParcel;
import java.util.ArrayList;
import java.util.Objects;
import vendor.qti.hardware.radio.ims.V1_0.CallState;
import vendor.qti.hardware.radio.ims.V1_3.VerstatInfo;
import vendor.qti.hardware.radio.ims.V1_4.MultiIdentityLineInfoHal;
import vendor.qti.hardware.radio.ims.V1_5.CallFailCauseResponse;
import vendor.qti.hardware.radio.ims.V1_5.TirMode;

/* JADX INFO: loaded from: classes.dex */
public final class CallInfo {
    public int state = 0;
    public int index = 0;
    public int toa = 0;
    public boolean hasIsMpty = false;
    public boolean isMpty = false;
    public boolean hasIsMT = false;
    public boolean isMT = false;
    public MultiIdentityLineInfoHal mtMultiLineInfo = new MultiIdentityLineInfoHal();
    public int als = 0;
    public boolean hasIsVoice = false;
    public boolean isVoice = false;
    public boolean hasIsVoicePrivacy = false;
    public boolean isVoicePrivacy = false;
    public String number = new String();
    public int numberPresentation = 0;
    public String name = new String();
    public int namePresentation = 0;
    public boolean hasCallDetails = false;
    public CallDetails callDetails = new CallDetails();
    public boolean hasFailCause = false;
    public CallFailCauseResponse failCause = new CallFailCauseResponse();
    public boolean hasIsEncrypted = false;
    public boolean isEncrypted = false;
    public boolean hasIsCalledPartyRinging = false;
    public boolean isCalledPartyRinging = false;
    public String historyInfo = new String();
    public boolean hasIsVideoConfSupported = false;
    public boolean isVideoConfSupported = false;
    public VerstatInfo verstatInfo = new VerstatInfo();
    public int tirMode = 0;

    public final boolean equals(Object otherObject) {
        if (this == otherObject) {
            return true;
        }
        if (otherObject == null || otherObject.getClass() != CallInfo.class) {
            return false;
        }
        CallInfo other = (CallInfo) otherObject;
        if (this.state == other.state && this.index == other.index && this.toa == other.toa && this.hasIsMpty == other.hasIsMpty && this.isMpty == other.isMpty && this.hasIsMT == other.hasIsMT && this.isMT == other.isMT && HidlSupport.deepEquals(this.mtMultiLineInfo, other.mtMultiLineInfo) && this.als == other.als && this.hasIsVoice == other.hasIsVoice && this.isVoice == other.isVoice && this.hasIsVoicePrivacy == other.hasIsVoicePrivacy && this.isVoicePrivacy == other.isVoicePrivacy && HidlSupport.deepEquals(this.number, other.number) && this.numberPresentation == other.numberPresentation && HidlSupport.deepEquals(this.name, other.name) && this.namePresentation == other.namePresentation && this.hasCallDetails == other.hasCallDetails && HidlSupport.deepEquals(this.callDetails, other.callDetails) && this.hasFailCause == other.hasFailCause && HidlSupport.deepEquals(this.failCause, other.failCause) && this.hasIsEncrypted == other.hasIsEncrypted && this.isEncrypted == other.isEncrypted && this.hasIsCalledPartyRinging == other.hasIsCalledPartyRinging && this.isCalledPartyRinging == other.isCalledPartyRinging && HidlSupport.deepEquals(this.historyInfo, other.historyInfo) && this.hasIsVideoConfSupported == other.hasIsVideoConfSupported && this.isVideoConfSupported == other.isVideoConfSupported && HidlSupport.deepEquals(this.verstatInfo, other.verstatInfo) && this.tirMode == other.tirMode) {
            return true;
        }
        return false;
    }

    public final int hashCode() {
        return Objects.hash(Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.state))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.index))), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.toa))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.hasIsMpty))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.isMpty))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.hasIsMT))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.isMT))), Integer.valueOf(HidlSupport.deepHashCode(this.mtMultiLineInfo)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.als))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.hasIsVoice))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.isVoice))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.hasIsVoicePrivacy))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.isVoicePrivacy))), Integer.valueOf(HidlSupport.deepHashCode(this.number)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.numberPresentation))), Integer.valueOf(HidlSupport.deepHashCode(this.name)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.namePresentation))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.hasCallDetails))), Integer.valueOf(HidlSupport.deepHashCode(this.callDetails)), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.hasFailCause))), Integer.valueOf(HidlSupport.deepHashCode(this.failCause)), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.hasIsEncrypted))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.isEncrypted))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.hasIsCalledPartyRinging))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.isCalledPartyRinging))), Integer.valueOf(HidlSupport.deepHashCode(this.historyInfo)), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.hasIsVideoConfSupported))), Integer.valueOf(HidlSupport.deepHashCode(Boolean.valueOf(this.isVideoConfSupported))), Integer.valueOf(HidlSupport.deepHashCode(this.verstatInfo)), Integer.valueOf(HidlSupport.deepHashCode(Integer.valueOf(this.tirMode))));
    }

    public final String toString() {
        return "{.state = " + CallState.toString(this.state) + ", .index = " + this.index + ", .toa = " + this.toa + ", .hasIsMpty = " + this.hasIsMpty + ", .isMpty = " + this.isMpty + ", .hasIsMT = " + this.hasIsMT + ", .isMT = " + this.isMT + ", .mtMultiLineInfo = " + this.mtMultiLineInfo + ", .als = " + this.als + ", .hasIsVoice = " + this.hasIsVoice + ", .isVoice = " + this.isVoice + ", .hasIsVoicePrivacy = " + this.hasIsVoicePrivacy + ", .isVoicePrivacy = " + this.isVoicePrivacy + ", .number = " + this.number + ", .numberPresentation = " + this.numberPresentation + ", .name = " + this.name + ", .namePresentation = " + this.namePresentation + ", .hasCallDetails = " + this.hasCallDetails + ", .callDetails = " + this.callDetails + ", .hasFailCause = " + this.hasFailCause + ", .failCause = " + this.failCause + ", .hasIsEncrypted = " + this.hasIsEncrypted + ", .isEncrypted = " + this.isEncrypted + ", .hasIsCalledPartyRinging = " + this.hasIsCalledPartyRinging + ", .isCalledPartyRinging = " + this.isCalledPartyRinging + ", .historyInfo = " + this.historyInfo + ", .hasIsVideoConfSupported = " + this.hasIsVideoConfSupported + ", .isVideoConfSupported = " + this.isVideoConfSupported + ", .verstatInfo = " + this.verstatInfo + ", .tirMode = " + TirMode.toString(this.tirMode) + "}";
    }

    public final void readFromParcel(HwParcel parcel) {
        HwBlob blob = parcel.readBuffer(312L);
        readEmbeddedFromParcel(parcel, blob, 0L);
    }

    public static final ArrayList<CallInfo> readVectorFromParcel(HwParcel parcel) {
        ArrayList<CallInfo> _hidl_vec = new ArrayList<>();
        HwBlob _hidl_blob = parcel.readBuffer(16L);
        int _hidl_vec_size = _hidl_blob.getInt32(8L);
        HwBlob childBlob = parcel.readEmbeddedBuffer(_hidl_vec_size * 312, _hidl_blob.handle(), 0L, true);
        _hidl_vec.clear();
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            CallInfo _hidl_vec_element = new CallInfo();
            _hidl_vec_element.readEmbeddedFromParcel(parcel, childBlob, _hidl_index_0 * 312);
            _hidl_vec.add(_hidl_vec_element);
        }
        return _hidl_vec;
    }

    public final void readEmbeddedFromParcel(HwParcel parcel, HwBlob _hidl_blob, long _hidl_offset) {
        this.state = _hidl_blob.getInt32(_hidl_offset + 0);
        this.index = _hidl_blob.getInt32(_hidl_offset + 4);
        this.toa = _hidl_blob.getInt32(_hidl_offset + 8);
        this.hasIsMpty = _hidl_blob.getBool(_hidl_offset + 12);
        this.isMpty = _hidl_blob.getBool(_hidl_offset + 13);
        this.hasIsMT = _hidl_blob.getBool(_hidl_offset + 14);
        this.isMT = _hidl_blob.getBool(_hidl_offset + 15);
        this.mtMultiLineInfo.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 16);
        this.als = _hidl_blob.getInt32(_hidl_offset + 40);
        this.hasIsVoice = _hidl_blob.getBool(_hidl_offset + 44);
        this.isVoice = _hidl_blob.getBool(_hidl_offset + 45);
        this.hasIsVoicePrivacy = _hidl_blob.getBool(_hidl_offset + 46);
        this.isVoicePrivacy = _hidl_blob.getBool(_hidl_offset + 47);
        String string = _hidl_blob.getString(_hidl_offset + 48);
        this.number = string;
        parcel.readEmbeddedBuffer(string.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 48 + 0, false);
        this.numberPresentation = _hidl_blob.getInt32(_hidl_offset + 64);
        String string2 = _hidl_blob.getString(_hidl_offset + 72);
        this.name = string2;
        parcel.readEmbeddedBuffer(string2.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 72 + 0, false);
        this.namePresentation = _hidl_blob.getInt32(_hidl_offset + 88);
        this.hasCallDetails = _hidl_blob.getBool(_hidl_offset + 92);
        this.callDetails.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 96);
        this.hasFailCause = _hidl_blob.getBool(_hidl_offset + 192);
        this.failCause.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 200);
        this.hasIsEncrypted = _hidl_blob.getBool(_hidl_offset + 272);
        this.isEncrypted = _hidl_blob.getBool(_hidl_offset + 273);
        this.hasIsCalledPartyRinging = _hidl_blob.getBool(_hidl_offset + 274);
        this.isCalledPartyRinging = _hidl_blob.getBool(_hidl_offset + 275);
        String string3 = _hidl_blob.getString(_hidl_offset + 280);
        this.historyInfo = string3;
        parcel.readEmbeddedBuffer(string3.getBytes().length + 1, _hidl_blob.handle(), _hidl_offset + 280 + 0, false);
        this.hasIsVideoConfSupported = _hidl_blob.getBool(_hidl_offset + 296);
        this.isVideoConfSupported = _hidl_blob.getBool(_hidl_offset + 297);
        this.verstatInfo.readEmbeddedFromParcel(parcel, _hidl_blob, _hidl_offset + 300);
        this.tirMode = _hidl_blob.getInt32(_hidl_offset + 308);
    }

    public final void writeToParcel(HwParcel parcel) {
        HwBlob _hidl_blob = new HwBlob(312);
        writeEmbeddedToBlob(_hidl_blob, 0L);
        parcel.writeBuffer(_hidl_blob);
    }

    public static final void writeVectorToParcel(HwParcel parcel, ArrayList<CallInfo> _hidl_vec) {
        HwBlob _hidl_blob = new HwBlob(16);
        int _hidl_vec_size = _hidl_vec.size();
        _hidl_blob.putInt32(8L, _hidl_vec_size);
        _hidl_blob.putBool(12L, false);
        HwBlob childBlob = new HwBlob(_hidl_vec_size * 312);
        for (int _hidl_index_0 = 0; _hidl_index_0 < _hidl_vec_size; _hidl_index_0++) {
            _hidl_vec.get(_hidl_index_0).writeEmbeddedToBlob(childBlob, _hidl_index_0 * 312);
        }
        _hidl_blob.putBlob(0L, childBlob);
        parcel.writeBuffer(_hidl_blob);
    }

    public final void writeEmbeddedToBlob(HwBlob _hidl_blob, long _hidl_offset) {
        _hidl_blob.putInt32(0 + _hidl_offset, this.state);
        _hidl_blob.putInt32(4 + _hidl_offset, this.index);
        _hidl_blob.putInt32(8 + _hidl_offset, this.toa);
        _hidl_blob.putBool(12 + _hidl_offset, this.hasIsMpty);
        _hidl_blob.putBool(13 + _hidl_offset, this.isMpty);
        _hidl_blob.putBool(14 + _hidl_offset, this.hasIsMT);
        _hidl_blob.putBool(15 + _hidl_offset, this.isMT);
        this.mtMultiLineInfo.writeEmbeddedToBlob(_hidl_blob, 16 + _hidl_offset);
        _hidl_blob.putInt32(40 + _hidl_offset, this.als);
        _hidl_blob.putBool(44 + _hidl_offset, this.hasIsVoice);
        _hidl_blob.putBool(45 + _hidl_offset, this.isVoice);
        _hidl_blob.putBool(46 + _hidl_offset, this.hasIsVoicePrivacy);
        _hidl_blob.putBool(47 + _hidl_offset, this.isVoicePrivacy);
        _hidl_blob.putString(48 + _hidl_offset, this.number);
        _hidl_blob.putInt32(64 + _hidl_offset, this.numberPresentation);
        _hidl_blob.putString(72 + _hidl_offset, this.name);
        _hidl_blob.putInt32(88 + _hidl_offset, this.namePresentation);
        _hidl_blob.putBool(92 + _hidl_offset, this.hasCallDetails);
        this.callDetails.writeEmbeddedToBlob(_hidl_blob, 96 + _hidl_offset);
        _hidl_blob.putBool(192 + _hidl_offset, this.hasFailCause);
        this.failCause.writeEmbeddedToBlob(_hidl_blob, 200 + _hidl_offset);
        _hidl_blob.putBool(272 + _hidl_offset, this.hasIsEncrypted);
        _hidl_blob.putBool(273 + _hidl_offset, this.isEncrypted);
        _hidl_blob.putBool(274 + _hidl_offset, this.hasIsCalledPartyRinging);
        _hidl_blob.putBool(275 + _hidl_offset, this.isCalledPartyRinging);
        _hidl_blob.putString(280 + _hidl_offset, this.historyInfo);
        _hidl_blob.putBool(296 + _hidl_offset, this.hasIsVideoConfSupported);
        _hidl_blob.putBool(297 + _hidl_offset, this.isVideoConfSupported);
        this.verstatInfo.writeEmbeddedToBlob(_hidl_blob, 300 + _hidl_offset);
        _hidl_blob.putInt32(308 + _hidl_offset, this.tirMode);
    }
}
