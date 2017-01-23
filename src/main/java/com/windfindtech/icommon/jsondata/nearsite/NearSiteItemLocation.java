package com.windfindtech.icommon.jsondata.nearsite;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by py on 2016/6/22.
 */
public class NearSiteItemLocation implements Parcelable{
    private double latitude;
    private double longitude;

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeDouble(this.latitude);
        dest.writeDouble(this.longitude);
    }

    public NearSiteItemLocation(double latitude, double longitude) {
        this.latitude = latitude;
        this.longitude = longitude;
    }

    protected NearSiteItemLocation(Parcel in) {
        this.latitude = in.readDouble();
        this.longitude = in.readDouble();
    }

    public static final Creator<NearSiteItemLocation> CREATOR = new Creator<NearSiteItemLocation>() {
        @Override
        public NearSiteItemLocation createFromParcel(Parcel source) {
            return new NearSiteItemLocation(source);
        }

        @Override
        public NearSiteItemLocation[] newArray(int size) {
            return new NearSiteItemLocation[size];
        }
    };
}
