package com.windfindtech.icommon.jsondata.nearsite;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by py on 2016/6/22.
 */
public class NearSiteItemDetail implements Parcelable{
    private String id;
    private String address;
    private String objectId;
    private String name;
    private String brand;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getObjectId() {
        return objectId;
    }

    public void setObjectId(String objectId) {
        this.objectId = objectId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.id);
        dest.writeString(this.address);
        dest.writeString(this.objectId);
        dest.writeString(this.name);
        dest.writeString(this.brand);
    }

    public NearSiteItemDetail() {
    }

    protected NearSiteItemDetail(Parcel in) {
        this.id = in.readString();
        this.address = in.readString();
        this.objectId = in.readString();
        this.name = in.readString();
        this.brand = in.readString();
    }

    public static final Creator<NearSiteItemDetail> CREATOR = new Creator<NearSiteItemDetail>() {
        @Override
        public NearSiteItemDetail createFromParcel(Parcel source) {
            return new NearSiteItemDetail(source);
        }

        @Override
        public NearSiteItemDetail[] newArray(int size) {
            return new NearSiteItemDetail[size];
        }
    };
}
