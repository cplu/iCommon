package com.windfindtech.icommon.jsondata.nearsite;


import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by py
 */
public class NearSiteItemPostBody implements Parcelable {
    private int distance;
    private Geometry geometry;

    public int getDistance() {
        return distance;
    }

    public void setDistance(int distance) {
        this.distance = distance;
    }

    public Geometry getGeometry() {
        return geometry;
    }

    public void setGeometry(Geometry geometry) {
        this.geometry = geometry;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.distance);
        dest.writeParcelable(this.geometry, flags);
    }

    public NearSiteItemPostBody() {
    }

    protected NearSiteItemPostBody(Parcel in) {
        this.distance = in.readInt();
        this.geometry = in.readParcelable(Geometry.class.getClassLoader());
    }

    public static final Creator<NearSiteItemPostBody> CREATOR = new Creator<NearSiteItemPostBody>() {
        @Override
        public NearSiteItemPostBody createFromParcel(Parcel source) {
            return new NearSiteItemPostBody(source);
        }

        @Override
        public NearSiteItemPostBody[] newArray(int size) {
            return new NearSiteItemPostBody[size];
        }
    };
}
