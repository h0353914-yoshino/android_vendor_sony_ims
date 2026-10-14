package org.codeaurora.ims;

/* JADX INFO: loaded from: classes.dex */
public final class GeoLocationInfo {
    private double mLatitude;
    private double mLongitude;

    public GeoLocationInfo(double latitude, double longitude) {
        this.mLatitude = latitude;
        this.mLongitude = longitude;
    }

    public double getLat() {
        return this.mLatitude;
    }

    public double getLon() {
        return this.mLongitude;
    }

    public String toString() {
        return "GeoLocationInfo latitude: " + this.mLatitude + " longitude: " + this.mLongitude;
    }
}
