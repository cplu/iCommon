package com.windfindtech.icommon.jsondata.nearsite;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 * geometry types.
 */
public  class Geometry implements Parcelable{

    private String type;
    private double[] coordinates;

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public double[] getCoordinates() {
        return coordinates;
    }

    public void setCoordinates(double[] coordinates) {
        this.coordinates = coordinates;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.type);
        dest.writeDoubleArray(this.coordinates);
    }

    public Geometry() {
    }

    protected Geometry(Parcel in) {
        this.type = in.readString();
        this.coordinates = in.createDoubleArray();
    }

    public static final Creator<Geometry> CREATOR = new Creator<Geometry>() {
        @Override
        public Geometry createFromParcel(Parcel source) {
            return new Geometry(source);
        }

        @Override
        public Geometry[] newArray(int size) {
            return new Geometry[size];
        }
    };
}
