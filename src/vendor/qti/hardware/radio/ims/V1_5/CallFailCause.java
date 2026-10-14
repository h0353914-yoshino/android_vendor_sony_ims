package vendor.qti.hardware.radio.ims.V1_5;

import java.util.ArrayList;

/* JADX INFO: loaded from: classes.dex */
public final class CallFailCause {
    public static final int CALL_FAIL_1X_COLLISION = 599;
    public static final int CALL_FAIL_ACCESS_CLASS_BLOCKED = 513;
    public static final int CALL_FAIL_ANSWERED_ELSEWHERE = 510;
    public static final int CALL_FAIL_BLACKLISTED_CALL_ID = 504;
    public static final int CALL_FAIL_BUSY = 2;
    public static final int CALL_FAIL_CALL_BARRED = 6;
    public static final int CALL_FAIL_CALL_CONFERENCE_ONGOING = 594;
    public static final int CALL_FAIL_CALL_ONGOING_CW_DISABLED = 597;
    public static final int CALL_FAIL_CALL_ON_OTHER_SUB = 598;
    public static final int CALL_FAIL_CALL_SETUP_ONGOING = 584;
    public static final int CALL_FAIL_CALL_TRANSFER_ONGOING = 587;
    public static final int CALL_FAIL_CALL_TYPE_NOT_ALLOWED = 582;
    public static final int CALL_FAIL_CALL_UPGRADE_ONGOING = 592;
    public static final int CALL_FAIL_CAUSE_CALL_PULLED = 512;
    public static final int CALL_FAIL_CONFERENCE_WITH_TTY_NOT_ALLOWED = 593;
    public static final int CALL_FAIL_CONGESTION = 4;
    public static final int CALL_FAIL_CS_CALL_ONGOING = 601;
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
    public static final int CALL_FAIL_EMRG_CALL_ONGOING = 583;
    public static final int CALL_FAIL_ENCRYPTION_CALL_ONGOING = 596;
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
    public static final int CALL_FAIL_MAX_CALL_LIMIT_REACHED = 585;
    public static final int CALL_FAIL_MEDIA_INIT_FAILED = 536;
    public static final int CALL_FAIL_MEDIA_NOT_ACCEPTABLE = 538;
    public static final int CALL_FAIL_MEDIA_NO_DATA = 537;
    public static final int CALL_FAIL_MEDIA_UNSPECIFIED_ERROR = 539;
    public static final int CALL_FAIL_MISC = 509;
    public static final int CALL_FAIL_NETWORK_DETACH = 564;
    public static final int CALL_FAIL_NETWORK_REJECT = 556;
    public static final int CALL_FAIL_NETWORK_RESP_TIMEOUT = 555;
    public static final int CALL_FAIL_NETWORK_UNAVAILABLE = 506;
    public static final int CALL_FAIL_NORMAL = 1;
    public static final int CALL_FAIL_NORMAL_UNSPECIFIED = 3;
    public static final int CALL_FAIL_NO_CSFB_IN_CS_ROAM = 580;
    public static final int CALL_FAIL_NO_VALID_SIM = 553;
    public static final int CALL_FAIL_OEM_CAUSE_1 = 565;
    public static final int CALL_FAIL_OEM_CAUSE_10 = 574;
    public static final int CALL_FAIL_OEM_CAUSE_11 = 575;
    public static final int CALL_FAIL_OEM_CAUSE_12 = 576;
    public static final int CALL_FAIL_OEM_CAUSE_13 = 577;
    public static final int CALL_FAIL_OEM_CAUSE_14 = 578;
    public static final int CALL_FAIL_OEM_CAUSE_15 = 579;
    public static final int CALL_FAIL_OEM_CAUSE_2 = 566;
    public static final int CALL_FAIL_OEM_CAUSE_3 = 567;
    public static final int CALL_FAIL_OEM_CAUSE_4 = 568;
    public static final int CALL_FAIL_OEM_CAUSE_5 = 569;
    public static final int CALL_FAIL_OEM_CAUSE_6 = 570;
    public static final int CALL_FAIL_OEM_CAUSE_7 = 571;
    public static final int CALL_FAIL_OEM_CAUSE_8 = 572;
    public static final int CALL_FAIL_OEM_CAUSE_9 = 573;
    public static final int CALL_FAIL_ONGOING_HANDOVER = 590;
    public static final int CALL_FAIL_OUT_OF_SERVICE = 552;
    public static final int CALL_FAIL_PRACK_TIMEOUT = 588;
    public static final int CALL_FAIL_PULL_OUT_OF_SYNC = 511;
    public static final int CALL_FAIL_QOS_FAILURE = 589;
    public static final int CALL_FAIL_RADIO_ACCESS_FAILURE = 557;
    public static final int CALL_FAIL_RADIO_INTERNAL_ERROR = 554;
    public static final int CALL_FAIL_RADIO_LINK_FAILURE = 558;
    public static final int CALL_FAIL_RADIO_LINK_LOST = 559;
    public static final int CALL_FAIL_RADIO_OFF = 551;
    public static final int CALL_FAIL_RADIO_RELEASE_ABNORMAL = 563;
    public static final int CALL_FAIL_RADIO_RELEASE_NORMAL = 562;
    public static final int CALL_FAIL_RADIO_SETUP_FAILURE = 561;
    public static final int CALL_FAIL_RADIO_UPLINK_FAILURE = 560;
    public static final int CALL_FAIL_REJECTED_ELSEWHERE = 603;
    public static final int CALL_FAIL_RETRY_ON_IMS_WITHOUT_RTT = 619;
    public static final int CALL_FAIL_SESSION_MODIFICATION_FAILED = 606;
    public static final int CALL_FAIL_SIP_ALTERNATE_EMERGENCY_CALL = 550;
    public static final int CALL_FAIL_SIP_AMBIGUOUS = 616;
    public static final int CALL_FAIL_SIP_BAD_ADDRESS = 521;
    public static final int CALL_FAIL_SIP_BAD_REQUEST = 515;
    public static final int CALL_FAIL_SIP_BUSY = 522;
    public static final int CALL_FAIL_SIP_CALL_OR_TRANS_DOES_NOT_EXIST = 613;
    public static final int CALL_FAIL_SIP_ERROR = 508;
    public static final int CALL_FAIL_SIP_EXTENSION_REQUIRED = 611;
    public static final int CALL_FAIL_SIP_FORBIDDEN = 516;
    public static final int CALL_FAIL_SIP_GLOBAL_ERROR = 535;
    public static final int CALL_FAIL_SIP_INTERVAL_TOO_BRIEF = 612;
    public static final int CALL_FAIL_SIP_LOOP_DETECTED = 614;
    public static final int CALL_FAIL_SIP_METHOD_NOT_ALLOWED = 607;
    public static final int CALL_FAIL_SIP_NOT_ACCEPTABLE = 524;
    public static final int CALL_FAIL_SIP_NOT_FOUND = 517;
    public static final int CALL_FAIL_SIP_NOT_REACHABLE = 525;
    public static final int CALL_FAIL_SIP_NOT_SUPPORTED = 518;
    public static final int CALL_FAIL_SIP_PROXY_AUTHENTICATION_REQUIRED = 608;
    public static final int CALL_FAIL_SIP_REDIRECTED = 514;
    public static final int CALL_FAIL_SIP_REQUEST_CANCELLED = 523;
    public static final int CALL_FAIL_SIP_REQUEST_ENTITY_TOO_LARGE = 609;
    public static final int CALL_FAIL_SIP_REQUEST_PENDING = 617;
    public static final int CALL_FAIL_SIP_REQUEST_TIMEOUT = 519;
    public static final int CALL_FAIL_SIP_REQUEST_URI_TOO_LARGE = 610;
    public static final int CALL_FAIL_SIP_SERVER_BAD_GATEWAY = 528;
    public static final int CALL_FAIL_SIP_SERVER_INTERNAL_ERROR = 526;
    public static final int CALL_FAIL_SIP_SERVER_MESSAGE_TOOLARGE = 532;
    public static final int CALL_FAIL_SIP_SERVER_NOT_IMPLEMENTED = 527;
    public static final int CALL_FAIL_SIP_SERVER_PRECONDITION_FAILURE = 533;
    public static final int CALL_FAIL_SIP_SERVER_TIMEOUT = 530;
    public static final int CALL_FAIL_SIP_SERVER_VERSION_UNSUPPORTED = 531;
    public static final int CALL_FAIL_SIP_SERVICE_UNAVAILABLE = 529;
    public static final int CALL_FAIL_SIP_TEMPORARILY_UNAVAILABLE = 520;
    public static final int CALL_FAIL_SIP_TOO_MANY_HOPS = 615;
    public static final int CALL_FAIL_SIP_UNDECIPHERABLE = 618;
    public static final int CALL_FAIL_SIP_USER_MARKED_UNWANTED = 602;
    public static final int CALL_FAIL_SIP_USER_REJECTED = 534;
    public static final int CALL_FAIL_SRV_NOT_REGISTERED = 581;
    public static final int CALL_FAIL_UI_NOT_READY = 600;
    public static final int CALL_FAIL_UNOBTAINABLE_NUMBER = 0;
    public static final int CALL_FAIL_UNSUPPORTED_SIP_HDRS = 586;
    public static final int CALL_FAIL_USER_BUSY = 501;
    public static final int CALL_FAIL_USER_CANCELLED_SESSION_MODIFICATION = 605;
    public static final int CALL_FAIL_USER_REJECT = 502;
    public static final int CALL_FAIL_USER_REJECTED_SESSION_MODIFICATION = 604;
    public static final int CALL_FAIL_VT_WITH_AVPF_NOT_ALLOWED = 595;
    public static final int CALL_FAIL_VT_WITH_TTY_NOT_ALLOWED = 591;

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
        if (o == 550) {
            return "CALL_FAIL_SIP_ALTERNATE_EMERGENCY_CALL";
        }
        if (o == 551) {
            return "CALL_FAIL_RADIO_OFF";
        }
        if (o == 552) {
            return "CALL_FAIL_OUT_OF_SERVICE";
        }
        if (o == 553) {
            return "CALL_FAIL_NO_VALID_SIM";
        }
        if (o == 554) {
            return "CALL_FAIL_RADIO_INTERNAL_ERROR";
        }
        if (o == 555) {
            return "CALL_FAIL_NETWORK_RESP_TIMEOUT";
        }
        if (o == 556) {
            return "CALL_FAIL_NETWORK_REJECT";
        }
        if (o == 557) {
            return "CALL_FAIL_RADIO_ACCESS_FAILURE";
        }
        if (o == 558) {
            return "CALL_FAIL_RADIO_LINK_FAILURE";
        }
        if (o == 559) {
            return "CALL_FAIL_RADIO_LINK_LOST";
        }
        if (o == 560) {
            return "CALL_FAIL_RADIO_UPLINK_FAILURE";
        }
        if (o == 561) {
            return "CALL_FAIL_RADIO_SETUP_FAILURE";
        }
        if (o == 562) {
            return "CALL_FAIL_RADIO_RELEASE_NORMAL";
        }
        if (o == 563) {
            return "CALL_FAIL_RADIO_RELEASE_ABNORMAL";
        }
        if (o == 564) {
            return "CALL_FAIL_NETWORK_DETACH";
        }
        if (o == 565) {
            return "CALL_FAIL_OEM_CAUSE_1";
        }
        if (o == 566) {
            return "CALL_FAIL_OEM_CAUSE_2";
        }
        if (o == 567) {
            return "CALL_FAIL_OEM_CAUSE_3";
        }
        if (o == 568) {
            return "CALL_FAIL_OEM_CAUSE_4";
        }
        if (o == 569) {
            return "CALL_FAIL_OEM_CAUSE_5";
        }
        if (o == 570) {
            return "CALL_FAIL_OEM_CAUSE_6";
        }
        if (o == 571) {
            return "CALL_FAIL_OEM_CAUSE_7";
        }
        if (o == 572) {
            return "CALL_FAIL_OEM_CAUSE_8";
        }
        if (o == 573) {
            return "CALL_FAIL_OEM_CAUSE_9";
        }
        if (o == 574) {
            return "CALL_FAIL_OEM_CAUSE_10";
        }
        if (o == 575) {
            return "CALL_FAIL_OEM_CAUSE_11";
        }
        if (o == 576) {
            return "CALL_FAIL_OEM_CAUSE_12";
        }
        if (o == 577) {
            return "CALL_FAIL_OEM_CAUSE_13";
        }
        if (o == 578) {
            return "CALL_FAIL_OEM_CAUSE_14";
        }
        if (o == 579) {
            return "CALL_FAIL_OEM_CAUSE_15";
        }
        if (o == 580) {
            return "CALL_FAIL_NO_CSFB_IN_CS_ROAM";
        }
        if (o == 581) {
            return "CALL_FAIL_SRV_NOT_REGISTERED";
        }
        if (o == 582) {
            return "CALL_FAIL_CALL_TYPE_NOT_ALLOWED";
        }
        if (o == 583) {
            return "CALL_FAIL_EMRG_CALL_ONGOING";
        }
        if (o == 584) {
            return "CALL_FAIL_CALL_SETUP_ONGOING";
        }
        if (o == 585) {
            return "CALL_FAIL_MAX_CALL_LIMIT_REACHED";
        }
        if (o == 586) {
            return "CALL_FAIL_UNSUPPORTED_SIP_HDRS";
        }
        if (o == 587) {
            return "CALL_FAIL_CALL_TRANSFER_ONGOING";
        }
        if (o == 588) {
            return "CALL_FAIL_PRACK_TIMEOUT";
        }
        if (o == 589) {
            return "CALL_FAIL_QOS_FAILURE";
        }
        if (o == 590) {
            return "CALL_FAIL_ONGOING_HANDOVER";
        }
        if (o == 591) {
            return "CALL_FAIL_VT_WITH_TTY_NOT_ALLOWED";
        }
        if (o == 592) {
            return "CALL_FAIL_CALL_UPGRADE_ONGOING";
        }
        if (o == 593) {
            return "CALL_FAIL_CONFERENCE_WITH_TTY_NOT_ALLOWED";
        }
        if (o == 594) {
            return "CALL_FAIL_CALL_CONFERENCE_ONGOING";
        }
        if (o == 595) {
            return "CALL_FAIL_VT_WITH_AVPF_NOT_ALLOWED";
        }
        if (o == 596) {
            return "CALL_FAIL_ENCRYPTION_CALL_ONGOING";
        }
        if (o == 597) {
            return "CALL_FAIL_CALL_ONGOING_CW_DISABLED";
        }
        if (o == 598) {
            return "CALL_FAIL_CALL_ON_OTHER_SUB";
        }
        if (o == 599) {
            return "CALL_FAIL_1X_COLLISION";
        }
        if (o == 600) {
            return "CALL_FAIL_UI_NOT_READY";
        }
        if (o == 601) {
            return "CALL_FAIL_CS_CALL_ONGOING";
        }
        if (o == 602) {
            return "CALL_FAIL_SIP_USER_MARKED_UNWANTED";
        }
        if (o == 603) {
            return "CALL_FAIL_REJECTED_ELSEWHERE";
        }
        if (o == 604) {
            return "CALL_FAIL_USER_REJECTED_SESSION_MODIFICATION";
        }
        if (o == 605) {
            return "CALL_FAIL_USER_CANCELLED_SESSION_MODIFICATION";
        }
        if (o == 606) {
            return "CALL_FAIL_SESSION_MODIFICATION_FAILED";
        }
        if (o == 607) {
            return "CALL_FAIL_SIP_METHOD_NOT_ALLOWED";
        }
        if (o == 608) {
            return "CALL_FAIL_SIP_PROXY_AUTHENTICATION_REQUIRED";
        }
        if (o == 609) {
            return "CALL_FAIL_SIP_REQUEST_ENTITY_TOO_LARGE";
        }
        if (o == 610) {
            return "CALL_FAIL_SIP_REQUEST_URI_TOO_LARGE";
        }
        if (o == 611) {
            return "CALL_FAIL_SIP_EXTENSION_REQUIRED";
        }
        if (o == 612) {
            return "CALL_FAIL_SIP_INTERVAL_TOO_BRIEF";
        }
        if (o == 613) {
            return "CALL_FAIL_SIP_CALL_OR_TRANS_DOES_NOT_EXIST";
        }
        if (o == 614) {
            return "CALL_FAIL_SIP_LOOP_DETECTED";
        }
        if (o == 615) {
            return "CALL_FAIL_SIP_TOO_MANY_HOPS";
        }
        if (o == 616) {
            return "CALL_FAIL_SIP_AMBIGUOUS";
        }
        if (o == 617) {
            return "CALL_FAIL_SIP_REQUEST_PENDING";
        }
        if (o == 618) {
            return "CALL_FAIL_SIP_UNDECIPHERABLE";
        }
        if (o == 619) {
            return "CALL_FAIL_RETRY_ON_IMS_WITHOUT_RTT";
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
        if ((o & 550) == 550) {
            list.add("CALL_FAIL_SIP_ALTERNATE_EMERGENCY_CALL");
            flipped |= 550;
        }
        if ((o & 551) == 551) {
            list.add("CALL_FAIL_RADIO_OFF");
            flipped |= 551;
        }
        if ((o & 552) == 552) {
            list.add("CALL_FAIL_OUT_OF_SERVICE");
            flipped |= 552;
        }
        if ((o & 553) == 553) {
            list.add("CALL_FAIL_NO_VALID_SIM");
            flipped |= 553;
        }
        if ((o & 554) == 554) {
            list.add("CALL_FAIL_RADIO_INTERNAL_ERROR");
            flipped |= 554;
        }
        if ((o & 555) == 555) {
            list.add("CALL_FAIL_NETWORK_RESP_TIMEOUT");
            flipped |= 555;
        }
        if ((o & 556) == 556) {
            list.add("CALL_FAIL_NETWORK_REJECT");
            flipped |= 556;
        }
        if ((o & 557) == 557) {
            list.add("CALL_FAIL_RADIO_ACCESS_FAILURE");
            flipped |= 557;
        }
        if ((o & 558) == 558) {
            list.add("CALL_FAIL_RADIO_LINK_FAILURE");
            flipped |= 558;
        }
        if ((o & 559) == 559) {
            list.add("CALL_FAIL_RADIO_LINK_LOST");
            flipped |= 559;
        }
        if ((o & 560) == 560) {
            list.add("CALL_FAIL_RADIO_UPLINK_FAILURE");
            flipped |= 560;
        }
        if ((o & 561) == 561) {
            list.add("CALL_FAIL_RADIO_SETUP_FAILURE");
            flipped |= 561;
        }
        if ((o & 562) == 562) {
            list.add("CALL_FAIL_RADIO_RELEASE_NORMAL");
            flipped |= 562;
        }
        if ((o & 563) == 563) {
            list.add("CALL_FAIL_RADIO_RELEASE_ABNORMAL");
            flipped |= 563;
        }
        if ((o & 564) == 564) {
            list.add("CALL_FAIL_NETWORK_DETACH");
            flipped |= 564;
        }
        if ((o & 565) == 565) {
            list.add("CALL_FAIL_OEM_CAUSE_1");
            flipped |= 565;
        }
        if ((o & 566) == 566) {
            list.add("CALL_FAIL_OEM_CAUSE_2");
            flipped |= 566;
        }
        if ((o & 567) == 567) {
            list.add("CALL_FAIL_OEM_CAUSE_3");
            flipped |= 567;
        }
        if ((o & 568) == 568) {
            list.add("CALL_FAIL_OEM_CAUSE_4");
            flipped |= 568;
        }
        if ((o & 569) == 569) {
            list.add("CALL_FAIL_OEM_CAUSE_5");
            flipped |= 569;
        }
        if ((o & 570) == 570) {
            list.add("CALL_FAIL_OEM_CAUSE_6");
            flipped |= 570;
        }
        if ((o & 571) == 571) {
            list.add("CALL_FAIL_OEM_CAUSE_7");
            flipped |= 571;
        }
        if ((o & 572) == 572) {
            list.add("CALL_FAIL_OEM_CAUSE_8");
            flipped |= 572;
        }
        if ((o & 573) == 573) {
            list.add("CALL_FAIL_OEM_CAUSE_9");
            flipped |= 573;
        }
        if ((o & 574) == 574) {
            list.add("CALL_FAIL_OEM_CAUSE_10");
            flipped |= 574;
        }
        if ((o & 575) == 575) {
            list.add("CALL_FAIL_OEM_CAUSE_11");
            flipped |= 575;
        }
        if ((o & 576) == 576) {
            list.add("CALL_FAIL_OEM_CAUSE_12");
            flipped |= 576;
        }
        if ((o & 577) == 577) {
            list.add("CALL_FAIL_OEM_CAUSE_13");
            flipped |= 577;
        }
        if ((o & 578) == 578) {
            list.add("CALL_FAIL_OEM_CAUSE_14");
            flipped |= 578;
        }
        if ((o & 579) == 579) {
            list.add("CALL_FAIL_OEM_CAUSE_15");
            flipped |= 579;
        }
        if ((o & 580) == 580) {
            list.add("CALL_FAIL_NO_CSFB_IN_CS_ROAM");
            flipped |= 580;
        }
        if ((o & 581) == 581) {
            list.add("CALL_FAIL_SRV_NOT_REGISTERED");
            flipped |= 581;
        }
        if ((o & 582) == 582) {
            list.add("CALL_FAIL_CALL_TYPE_NOT_ALLOWED");
            flipped |= 582;
        }
        if ((o & 583) == 583) {
            list.add("CALL_FAIL_EMRG_CALL_ONGOING");
            flipped |= 583;
        }
        if ((o & 584) == 584) {
            list.add("CALL_FAIL_CALL_SETUP_ONGOING");
            flipped |= 584;
        }
        if ((o & 585) == 585) {
            list.add("CALL_FAIL_MAX_CALL_LIMIT_REACHED");
            flipped |= 585;
        }
        if ((o & 586) == 586) {
            list.add("CALL_FAIL_UNSUPPORTED_SIP_HDRS");
            flipped |= 586;
        }
        if ((o & 587) == 587) {
            list.add("CALL_FAIL_CALL_TRANSFER_ONGOING");
            flipped |= 587;
        }
        if ((o & 588) == 588) {
            list.add("CALL_FAIL_PRACK_TIMEOUT");
            flipped |= 588;
        }
        if ((o & 589) == 589) {
            list.add("CALL_FAIL_QOS_FAILURE");
            flipped |= 589;
        }
        if ((o & 590) == 590) {
            list.add("CALL_FAIL_ONGOING_HANDOVER");
            flipped |= 590;
        }
        if ((o & 591) == 591) {
            list.add("CALL_FAIL_VT_WITH_TTY_NOT_ALLOWED");
            flipped |= 591;
        }
        if ((o & 592) == 592) {
            list.add("CALL_FAIL_CALL_UPGRADE_ONGOING");
            flipped |= 592;
        }
        if ((o & 593) == 593) {
            list.add("CALL_FAIL_CONFERENCE_WITH_TTY_NOT_ALLOWED");
            flipped |= 593;
        }
        if ((o & 594) == 594) {
            list.add("CALL_FAIL_CALL_CONFERENCE_ONGOING");
            flipped |= 594;
        }
        if ((o & 595) == 595) {
            list.add("CALL_FAIL_VT_WITH_AVPF_NOT_ALLOWED");
            flipped |= 595;
        }
        if ((o & 596) == 596) {
            list.add("CALL_FAIL_ENCRYPTION_CALL_ONGOING");
            flipped |= 596;
        }
        if ((o & 597) == 597) {
            list.add("CALL_FAIL_CALL_ONGOING_CW_DISABLED");
            flipped |= 597;
        }
        if ((o & 598) == 598) {
            list.add("CALL_FAIL_CALL_ON_OTHER_SUB");
            flipped |= 598;
        }
        if ((o & 599) == 599) {
            list.add("CALL_FAIL_1X_COLLISION");
            flipped |= 599;
        }
        if ((o & 600) == 600) {
            list.add("CALL_FAIL_UI_NOT_READY");
            flipped |= 600;
        }
        if ((o & 601) == 601) {
            list.add("CALL_FAIL_CS_CALL_ONGOING");
            flipped |= 601;
        }
        if ((o & 602) == 602) {
            list.add("CALL_FAIL_SIP_USER_MARKED_UNWANTED");
            flipped |= 602;
        }
        if ((o & CALL_FAIL_REJECTED_ELSEWHERE) == 603) {
            list.add("CALL_FAIL_REJECTED_ELSEWHERE");
            flipped |= CALL_FAIL_REJECTED_ELSEWHERE;
        }
        if ((o & CALL_FAIL_USER_REJECTED_SESSION_MODIFICATION) == 604) {
            list.add("CALL_FAIL_USER_REJECTED_SESSION_MODIFICATION");
            flipped |= CALL_FAIL_USER_REJECTED_SESSION_MODIFICATION;
        }
        if ((o & CALL_FAIL_USER_CANCELLED_SESSION_MODIFICATION) == 605) {
            list.add("CALL_FAIL_USER_CANCELLED_SESSION_MODIFICATION");
            flipped |= CALL_FAIL_USER_CANCELLED_SESSION_MODIFICATION;
        }
        if ((o & CALL_FAIL_SESSION_MODIFICATION_FAILED) == 606) {
            list.add("CALL_FAIL_SESSION_MODIFICATION_FAILED");
            flipped |= CALL_FAIL_SESSION_MODIFICATION_FAILED;
        }
        if ((o & CALL_FAIL_SIP_METHOD_NOT_ALLOWED) == 607) {
            list.add("CALL_FAIL_SIP_METHOD_NOT_ALLOWED");
            flipped |= CALL_FAIL_SIP_METHOD_NOT_ALLOWED;
        }
        if ((o & CALL_FAIL_SIP_PROXY_AUTHENTICATION_REQUIRED) == 608) {
            list.add("CALL_FAIL_SIP_PROXY_AUTHENTICATION_REQUIRED");
            flipped |= CALL_FAIL_SIP_PROXY_AUTHENTICATION_REQUIRED;
        }
        if ((o & CALL_FAIL_SIP_REQUEST_ENTITY_TOO_LARGE) == 609) {
            list.add("CALL_FAIL_SIP_REQUEST_ENTITY_TOO_LARGE");
            flipped |= CALL_FAIL_SIP_REQUEST_ENTITY_TOO_LARGE;
        }
        if ((o & CALL_FAIL_SIP_REQUEST_URI_TOO_LARGE) == 610) {
            list.add("CALL_FAIL_SIP_REQUEST_URI_TOO_LARGE");
            flipped |= CALL_FAIL_SIP_REQUEST_URI_TOO_LARGE;
        }
        if ((o & CALL_FAIL_SIP_EXTENSION_REQUIRED) == 611) {
            list.add("CALL_FAIL_SIP_EXTENSION_REQUIRED");
            flipped |= CALL_FAIL_SIP_EXTENSION_REQUIRED;
        }
        if ((o & CALL_FAIL_SIP_INTERVAL_TOO_BRIEF) == 612) {
            list.add("CALL_FAIL_SIP_INTERVAL_TOO_BRIEF");
            flipped |= CALL_FAIL_SIP_INTERVAL_TOO_BRIEF;
        }
        if ((o & CALL_FAIL_SIP_CALL_OR_TRANS_DOES_NOT_EXIST) == 613) {
            list.add("CALL_FAIL_SIP_CALL_OR_TRANS_DOES_NOT_EXIST");
            flipped |= CALL_FAIL_SIP_CALL_OR_TRANS_DOES_NOT_EXIST;
        }
        if ((o & CALL_FAIL_SIP_LOOP_DETECTED) == 614) {
            list.add("CALL_FAIL_SIP_LOOP_DETECTED");
            flipped |= CALL_FAIL_SIP_LOOP_DETECTED;
        }
        if ((o & CALL_FAIL_SIP_TOO_MANY_HOPS) == 615) {
            list.add("CALL_FAIL_SIP_TOO_MANY_HOPS");
            flipped |= CALL_FAIL_SIP_TOO_MANY_HOPS;
        }
        if ((o & CALL_FAIL_SIP_AMBIGUOUS) == 616) {
            list.add("CALL_FAIL_SIP_AMBIGUOUS");
            flipped |= CALL_FAIL_SIP_AMBIGUOUS;
        }
        if ((o & CALL_FAIL_SIP_REQUEST_PENDING) == 617) {
            list.add("CALL_FAIL_SIP_REQUEST_PENDING");
            flipped |= CALL_FAIL_SIP_REQUEST_PENDING;
        }
        if ((o & CALL_FAIL_SIP_UNDECIPHERABLE) == 618) {
            list.add("CALL_FAIL_SIP_UNDECIPHERABLE");
            flipped |= CALL_FAIL_SIP_UNDECIPHERABLE;
        }
        if ((o & CALL_FAIL_RETRY_ON_IMS_WITHOUT_RTT) == 619) {
            list.add("CALL_FAIL_RETRY_ON_IMS_WITHOUT_RTT");
            flipped |= CALL_FAIL_RETRY_ON_IMS_WITHOUT_RTT;
        }
        if (o != flipped) {
            list.add("0x" + Integer.toHexString((~flipped) & o));
        }
        return String.join(" | ", list);
    }
}
