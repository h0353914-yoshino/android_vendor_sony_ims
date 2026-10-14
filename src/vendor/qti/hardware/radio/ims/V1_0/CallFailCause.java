package vendor.qti.hardware.radio.ims.V1_0;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class CallFailCause {
    public static final int CALL_FAIL_ACCESS_CLASS_BLOCKED = 513;
    public static final int CALL_FAIL_ANSWERED_ELSEWHERE = 510;
    public static final int CALL_FAIL_BLACKLISTED_CALL_ID = 504;
    public static final int CALL_FAIL_BUSY = 2;
    public static final int CALL_FAIL_CALL_BARRED = 6;
    public static final int CALL_FAIL_CAUSE_CALL_PULLED = 512;
    public static final int CALL_FAIL_CONGESTION = 4;
    public static final int CALL_FAIL_CS_RETRY_REQUIRED = 505;
    public static final int CALL_FAIL_DIAL_MODIFIED_TO_DIAL = 11;
    public static final int CALL_FAIL_DIAL_MODIFIED_TO_DIAL_VIDEO = 543;
    public static final int CALL_FAIL_DIAL_MODIFIED_TO_SS = 10;
    public static final int CALL_FAIL_DIAL_MODIFIED_TO_USSD = 9;
    public static final int CALL_FAIL_DIAL_VIDEO_MODIFIED_TO_DIAL = 544;
    public static final int CALL_FAIL_DIAL_VIDEO_MODIFIED_TO_DIAL_VIDEO = 545;
    public static final int CALL_FAIL_DIAL_VIDEO_MODIFIED_TO_SS = 546;
    public static final int CALL_FAIL_DIAL_VIDEO_MODIFIED_TO_USSD = 547;
    public static final int CALL_FAIL_EMERGENCY_PERM_FAILURE = 13;
    public static final int CALL_FAIL_EMERGENCY_TEMP_FAILURE = 12;
    public static final int CALL_FAIL_ERROR_UNSPECIFIED = 548;
    public static final int CALL_FAIL_FDN_BLOCKED = 7;
    public static final int CALL_FAIL_FEATURE_UNAVAILABLE = 507;
    public static final int CALL_FAIL_HOLD_REINVITE_COLLISION = 542;
    public static final int CALL_FAIL_HOLD_RESUME_CANCELED = 541;
    public static final int CALL_FAIL_HOLD_RESUME_FAILED = 540;
    public static final int CALL_FAIL_HO_NOT_FEASIBLE = 14;
    public static final int CALL_FAIL_IMEI_NOT_ACCEPTED = 8;
    public static final int CALL_FAIL_INCOMPATIBILITY_DESTINATION = 5;
    public static final int CALL_FAIL_INVALID = 549;
    public static final int CALL_FAIL_LOW_BATTERY = 503;
    public static final int CALL_FAIL_MEDIA_INIT_FAILED = 536;
    public static final int CALL_FAIL_MEDIA_NOT_ACCEPTABLE = 538;
    public static final int CALL_FAIL_MEDIA_NO_DATA = 537;
    public static final int CALL_FAIL_MEDIA_UNSPECIFIED_ERROR = 539;
    public static final int CALL_FAIL_MISC = 509;
    public static final int CALL_FAIL_NETWORK_UNAVAILABLE = 506;
    public static final int CALL_FAIL_NORMAL = 1;
    public static final int CALL_FAIL_NORMAL_UNSPECIFIED = 3;
    public static final int CALL_FAIL_PULL_OUT_OF_SYNC = 511;
    public static final int CALL_FAIL_SIP_BAD_ADDRESS = 521;
    public static final int CALL_FAIL_SIP_BAD_REQUEST = 515;
    public static final int CALL_FAIL_SIP_BUSY = 522;
    public static final int CALL_FAIL_SIP_ERROR = 508;
    public static final int CALL_FAIL_SIP_FORBIDDEN = 516;
    public static final int CALL_FAIL_SIP_GLOBAL_ERROR = 535;
    public static final int CALL_FAIL_SIP_NOT_ACCEPTABLE = 524;
    public static final int CALL_FAIL_SIP_NOT_FOUND = 517;
    public static final int CALL_FAIL_SIP_NOT_REACHABLE = 525;
    public static final int CALL_FAIL_SIP_NOT_SUPPORTED = 518;
    public static final int CALL_FAIL_SIP_REDIRECTED = 514;
    public static final int CALL_FAIL_SIP_REQUEST_CANCELLED = 523;
    public static final int CALL_FAIL_SIP_REQUEST_TIMEOUT = 519;
    public static final int CALL_FAIL_SIP_SERVER_BAD_GATEWAY = 528;
    public static final int CALL_FAIL_SIP_SERVER_INTERNAL_ERROR = 526;
    public static final int CALL_FAIL_SIP_SERVER_MESSAGE_TOOLARGE = 532;
    public static final int CALL_FAIL_SIP_SERVER_NOT_IMPLEMENTED = 527;
    public static final int CALL_FAIL_SIP_SERVER_PRECONDITION_FAILURE = 533;
    public static final int CALL_FAIL_SIP_SERVER_TIMEOUT = 530;
    public static final int CALL_FAIL_SIP_SERVER_VERSION_UNSUPPORTED = 531;
    public static final int CALL_FAIL_SIP_SERVICE_UNAVAILABLE = 529;
    public static final int CALL_FAIL_SIP_TEMPORARILY_UNAVAILABLE = 520;
    public static final int CALL_FAIL_SIP_USER_REJECTED = 534;
    public static final int CALL_FAIL_UNOBTAINABLE_NUMBER = 0;
    public static final int CALL_FAIL_USER_BUSY = 501;
    public static final int CALL_FAIL_USER_REJECT = 502;

    public static final String toString(int o) {
        if (o == 0) {
            return "CALL_FAIL_UNOBTAINABLE_NUMBER";
        }
        if (o == 1) {
            return "CALL_FAIL_NORMAL";
        }
        if (o == 2) {
            return "CALL_FAIL_BUSY";
        }
        if (o == 3) {
            return "CALL_FAIL_NORMAL_UNSPECIFIED";
        }
        if (o == 4) {
            return "CALL_FAIL_CONGESTION";
        }
        if (o == 5) {
            return "CALL_FAIL_INCOMPATIBILITY_DESTINATION";
        }
        if (o == 6) {
            return "CALL_FAIL_CALL_BARRED";
        }
        if (o == 7) {
            return "CALL_FAIL_FDN_BLOCKED";
        }
        if (o == 8) {
            return "CALL_FAIL_IMEI_NOT_ACCEPTED";
        }
        if (o == 9) {
            return "CALL_FAIL_DIAL_MODIFIED_TO_USSD";
        }
        if (o == 10) {
            return "CALL_FAIL_DIAL_MODIFIED_TO_SS";
        }
        if (o == 11) {
            return "CALL_FAIL_DIAL_MODIFIED_TO_DIAL";
        }
        if (o == 12) {
            return "CALL_FAIL_EMERGENCY_TEMP_FAILURE";
        }
        if (o == 13) {
            return "CALL_FAIL_EMERGENCY_PERM_FAILURE";
        }
        if (o == 14) {
            return "CALL_FAIL_HO_NOT_FEASIBLE";
        }
        if (o == 501) {
            return "CALL_FAIL_USER_BUSY";
        }
        if (o == 502) {
            return "CALL_FAIL_USER_REJECT";
        }
        if (o == 503) {
            return "CALL_FAIL_LOW_BATTERY";
        }
        if (o == 504) {
            return "CALL_FAIL_BLACKLISTED_CALL_ID";
        }
        if (o == 505) {
            return "CALL_FAIL_CS_RETRY_REQUIRED";
        }
        if (o == 506) {
            return "CALL_FAIL_NETWORK_UNAVAILABLE";
        }
        if (o == 507) {
            return "CALL_FAIL_FEATURE_UNAVAILABLE";
        }
        if (o == 508) {
            return "CALL_FAIL_SIP_ERROR";
        }
        if (o == 509) {
            return "CALL_FAIL_MISC";
        }
        if (o == 510) {
            return "CALL_FAIL_ANSWERED_ELSEWHERE";
        }
        if (o == 511) {
            return "CALL_FAIL_PULL_OUT_OF_SYNC";
        }
        if (o == 512) {
            return "CALL_FAIL_CAUSE_CALL_PULLED";
        }
        if (o == 513) {
            return "CALL_FAIL_ACCESS_CLASS_BLOCKED";
        }
        if (o == 514) {
            return "CALL_FAIL_SIP_REDIRECTED";
        }
        if (o == 515) {
            return "CALL_FAIL_SIP_BAD_REQUEST";
        }
        if (o == 516) {
            return "CALL_FAIL_SIP_FORBIDDEN";
        }
        if (o == 517) {
            return "CALL_FAIL_SIP_NOT_FOUND";
        }
        if (o == 518) {
            return "CALL_FAIL_SIP_NOT_SUPPORTED";
        }
        if (o == 519) {
            return "CALL_FAIL_SIP_REQUEST_TIMEOUT";
        }
        if (o == 520) {
            return "CALL_FAIL_SIP_TEMPORARILY_UNAVAILABLE";
        }
        if (o == 521) {
            return "CALL_FAIL_SIP_BAD_ADDRESS";
        }
        if (o == 522) {
            return "CALL_FAIL_SIP_BUSY";
        }
        if (o == 523) {
            return "CALL_FAIL_SIP_REQUEST_CANCELLED";
        }
        if (o == 524) {
            return "CALL_FAIL_SIP_NOT_ACCEPTABLE";
        }
        if (o == 525) {
            return "CALL_FAIL_SIP_NOT_REACHABLE";
        }
        if (o == 526) {
            return "CALL_FAIL_SIP_SERVER_INTERNAL_ERROR";
        }
        if (o == 527) {
            return "CALL_FAIL_SIP_SERVER_NOT_IMPLEMENTED";
        }
        if (o == 528) {
            return "CALL_FAIL_SIP_SERVER_BAD_GATEWAY";
        }
        if (o == 529) {
            return "CALL_FAIL_SIP_SERVICE_UNAVAILABLE";
        }
        if (o == 530) {
            return "CALL_FAIL_SIP_SERVER_TIMEOUT";
        }
        if (o == 531) {
            return "CALL_FAIL_SIP_SERVER_VERSION_UNSUPPORTED";
        }
        if (o == 532) {
            return "CALL_FAIL_SIP_SERVER_MESSAGE_TOOLARGE";
        }
        if (o == 533) {
            return "CALL_FAIL_SIP_SERVER_PRECONDITION_FAILURE";
        }
        if (o == 534) {
            return "CALL_FAIL_SIP_USER_REJECTED";
        }
        if (o == 535) {
            return "CALL_FAIL_SIP_GLOBAL_ERROR";
        }
        if (o == 536) {
            return "CALL_FAIL_MEDIA_INIT_FAILED";
        }
        if (o == 537) {
            return "CALL_FAIL_MEDIA_NO_DATA";
        }
        if (o == 538) {
            return "CALL_FAIL_MEDIA_NOT_ACCEPTABLE";
        }
        if (o == 539) {
            return "CALL_FAIL_MEDIA_UNSPECIFIED_ERROR";
        }
        if (o == 540) {
            return "CALL_FAIL_HOLD_RESUME_FAILED";
        }
        if (o == 541) {
            return "CALL_FAIL_HOLD_RESUME_CANCELED";
        }
        if (o == 542) {
            return "CALL_FAIL_HOLD_REINVITE_COLLISION";
        }
        if (o == 543) {
            return "CALL_FAIL_DIAL_MODIFIED_TO_DIAL_VIDEO";
        }
        if (o == 544) {
            return "CALL_FAIL_DIAL_VIDEO_MODIFIED_TO_DIAL";
        }
        if (o == 545) {
            return "CALL_FAIL_DIAL_VIDEO_MODIFIED_TO_DIAL_VIDEO";
        }
        if (o == 546) {
            return "CALL_FAIL_DIAL_VIDEO_MODIFIED_TO_SS";
        }
        if (o == 547) {
            return "CALL_FAIL_DIAL_VIDEO_MODIFIED_TO_USSD";
        }
        if (o == 548) {
            return "CALL_FAIL_ERROR_UNSPECIFIED";
        }
        if (o == 549) {
            return "CALL_FAIL_INVALID";
        }
        return "0x" + Integer.toHexString(o);
    }

    public static final String dumpBitfield(int o) {
        ArrayList<String> list = new ArrayList<>();
        int flipped = 0;
        list.add("CALL_FAIL_UNOBTAINABLE_NUMBER");
        if ((o & 1) == 1) {
            list.add("CALL_FAIL_NORMAL");
            flipped = 0 | 1;
        }
        if ((o & 2) == 2) {
            list.add("CALL_FAIL_BUSY");
            flipped |= 2;
        }
        if ((o & 3) == 3) {
            list.add("CALL_FAIL_NORMAL_UNSPECIFIED");
            flipped |= 3;
        }
        if ((o & 4) == 4) {
            list.add("CALL_FAIL_CONGESTION");
            flipped |= 4;
        }
        if ((o & 5) == 5) {
            list.add("CALL_FAIL_INCOMPATIBILITY_DESTINATION");
            flipped |= 5;
        }
        if ((o & 6) == 6) {
            list.add("CALL_FAIL_CALL_BARRED");
            flipped |= 6;
        }
        if ((o & 7) == 7) {
            list.add("CALL_FAIL_FDN_BLOCKED");
            flipped |= 7;
        }
        if ((o & 8) == 8) {
            list.add("CALL_FAIL_IMEI_NOT_ACCEPTED");
            flipped |= 8;
        }
        if ((o & 9) == 9) {
            list.add("CALL_FAIL_DIAL_MODIFIED_TO_USSD");
            flipped |= 9;
        }
        if ((o & 10) == 10) {
            list.add("CALL_FAIL_DIAL_MODIFIED_TO_SS");
            flipped |= 10;
        }
        if ((o & 11) == 11) {
            list.add("CALL_FAIL_DIAL_MODIFIED_TO_DIAL");
            flipped |= 11;
        }
        if ((o & 12) == 12) {
            list.add("CALL_FAIL_EMERGENCY_TEMP_FAILURE");
            flipped |= 12;
        }
        if ((o & 13) == 13) {
            list.add("CALL_FAIL_EMERGENCY_PERM_FAILURE");
            flipped |= 13;
        }
        if ((o & 14) == 14) {
            list.add("CALL_FAIL_HO_NOT_FEASIBLE");
            flipped |= 14;
        }
        if ((o & 501) == 501) {
            list.add("CALL_FAIL_USER_BUSY");
            flipped |= 501;
        }
        if ((o & 502) == 502) {
            list.add("CALL_FAIL_USER_REJECT");
            flipped |= 502;
        }
        if ((o & 503) == 503) {
            list.add("CALL_FAIL_LOW_BATTERY");
            flipped |= 503;
        }
        if ((o & 504) == 504) {
            list.add("CALL_FAIL_BLACKLISTED_CALL_ID");
            flipped |= 504;
        }
        if ((o & 505) == 505) {
            list.add("CALL_FAIL_CS_RETRY_REQUIRED");
            flipped |= 505;
        }
        if ((o & 506) == 506) {
            list.add("CALL_FAIL_NETWORK_UNAVAILABLE");
            flipped |= 506;
        }
        if ((o & 507) == 507) {
            list.add("CALL_FAIL_FEATURE_UNAVAILABLE");
            flipped |= 507;
        }
        if ((o & 508) == 508) {
            list.add("CALL_FAIL_SIP_ERROR");
            flipped |= 508;
        }
        if ((o & 509) == 509) {
            list.add("CALL_FAIL_MISC");
            flipped |= 509;
        }
        if ((o & 510) == 510) {
            list.add("CALL_FAIL_ANSWERED_ELSEWHERE");
            flipped |= 510;
        }
        if ((o & 511) == 511) {
            list.add("CALL_FAIL_PULL_OUT_OF_SYNC");
            flipped |= 511;
        }
        if ((o & 512) == 512) {
            list.add("CALL_FAIL_CAUSE_CALL_PULLED");
            flipped |= 512;
        }
        if ((o & 513) == 513) {
            list.add("CALL_FAIL_ACCESS_CLASS_BLOCKED");
            flipped |= 513;
        }
        if ((o & 514) == 514) {
            list.add("CALL_FAIL_SIP_REDIRECTED");
            flipped |= 514;
        }
        if ((o & 515) == 515) {
            list.add("CALL_FAIL_SIP_BAD_REQUEST");
            flipped |= 515;
        }
        if ((o & 516) == 516) {
            list.add("CALL_FAIL_SIP_FORBIDDEN");
            flipped |= 516;
        }
        if ((o & 517) == 517) {
            list.add("CALL_FAIL_SIP_NOT_FOUND");
            flipped |= 517;
        }
        if ((o & 518) == 518) {
            list.add("CALL_FAIL_SIP_NOT_SUPPORTED");
            flipped |= 518;
        }
        if ((o & 519) == 519) {
            list.add("CALL_FAIL_SIP_REQUEST_TIMEOUT");
            flipped |= 519;
        }
        if ((o & 520) == 520) {
            list.add("CALL_FAIL_SIP_TEMPORARILY_UNAVAILABLE");
            flipped |= 520;
        }
        if ((o & 521) == 521) {
            list.add("CALL_FAIL_SIP_BAD_ADDRESS");
            flipped |= 521;
        }
        if ((o & 522) == 522) {
            list.add("CALL_FAIL_SIP_BUSY");
            flipped |= 522;
        }
        if ((o & 523) == 523) {
            list.add("CALL_FAIL_SIP_REQUEST_CANCELLED");
            flipped |= 523;
        }
        if ((o & 524) == 524) {
            list.add("CALL_FAIL_SIP_NOT_ACCEPTABLE");
            flipped |= 524;
        }
        if ((o & 525) == 525) {
            list.add("CALL_FAIL_SIP_NOT_REACHABLE");
            flipped |= 525;
        }
        if ((o & 526) == 526) {
            list.add("CALL_FAIL_SIP_SERVER_INTERNAL_ERROR");
            flipped |= 526;
        }
        if ((o & 527) == 527) {
            list.add("CALL_FAIL_SIP_SERVER_NOT_IMPLEMENTED");
            flipped |= 527;
        }
        if ((o & 528) == 528) {
            list.add("CALL_FAIL_SIP_SERVER_BAD_GATEWAY");
            flipped |= 528;
        }
        if ((o & 529) == 529) {
            list.add("CALL_FAIL_SIP_SERVICE_UNAVAILABLE");
            flipped |= 529;
        }
        if ((o & 530) == 530) {
            list.add("CALL_FAIL_SIP_SERVER_TIMEOUT");
            flipped |= 530;
        }
        if ((o & 531) == 531) {
            list.add("CALL_FAIL_SIP_SERVER_VERSION_UNSUPPORTED");
            flipped |= 531;
        }
        if ((o & 532) == 532) {
            list.add("CALL_FAIL_SIP_SERVER_MESSAGE_TOOLARGE");
            flipped |= 532;
        }
        if ((o & 533) == 533) {
            list.add("CALL_FAIL_SIP_SERVER_PRECONDITION_FAILURE");
            flipped |= 533;
        }
        if ((o & 534) == 534) {
            list.add("CALL_FAIL_SIP_USER_REJECTED");
            flipped |= 534;
        }
        if ((o & 535) == 535) {
            list.add("CALL_FAIL_SIP_GLOBAL_ERROR");
            flipped |= 535;
        }
        if ((o & 536) == 536) {
            list.add("CALL_FAIL_MEDIA_INIT_FAILED");
            flipped |= 536;
        }
        if ((o & 537) == 537) {
            list.add("CALL_FAIL_MEDIA_NO_DATA");
            flipped |= 537;
        }
        if ((o & 538) == 538) {
            list.add("CALL_FAIL_MEDIA_NOT_ACCEPTABLE");
            flipped |= 538;
        }
        if ((o & 539) == 539) {
            list.add("CALL_FAIL_MEDIA_UNSPECIFIED_ERROR");
            flipped |= 539;
        }
        if ((o & 540) == 540) {
            list.add("CALL_FAIL_HOLD_RESUME_FAILED");
            flipped |= 540;
        }
        if ((o & 541) == 541) {
            list.add("CALL_FAIL_HOLD_RESUME_CANCELED");
            flipped |= 541;
        }
        if ((o & 542) == 542) {
            list.add("CALL_FAIL_HOLD_REINVITE_COLLISION");
            flipped |= 542;
        }
        if ((o & 543) == 543) {
            list.add("CALL_FAIL_DIAL_MODIFIED_TO_DIAL_VIDEO");
            flipped |= 543;
        }
        if ((o & 544) == 544) {
            list.add("CALL_FAIL_DIAL_VIDEO_MODIFIED_TO_DIAL");
            flipped |= 544;
        }
        if ((o & 545) == 545) {
            list.add("CALL_FAIL_DIAL_VIDEO_MODIFIED_TO_DIAL_VIDEO");
            flipped |= 545;
        }
        if ((o & 546) == 546) {
            list.add("CALL_FAIL_DIAL_VIDEO_MODIFIED_TO_SS");
            flipped |= 546;
        }
        if ((o & 547) == 547) {
            list.add("CALL_FAIL_DIAL_VIDEO_MODIFIED_TO_USSD");
            flipped |= 547;
        }
        if ((o & 548) == 548) {
            list.add("CALL_FAIL_ERROR_UNSPECIFIED");
            flipped |= 548;
        }
        if ((o & 549) == 549) {
            list.add("CALL_FAIL_INVALID");
            flipped |= 549;
        }
        if (o != flipped) {
            list.add("0x" + Integer.toHexString((~flipped) & o));
        }
        return String.join(" | ", list);
    }
}
