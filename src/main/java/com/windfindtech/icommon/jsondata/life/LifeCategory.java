package com.windfindtech.icommon.jsondata.life;

import android.os.Parcel;
import android.os.Parcelable;


/**
 * Created by yu on 2015/8/8.
 */
public class LifeCategory implements Parcelable{

    private String name;
    private int color;

    public LifeCategory() {

    }

    protected LifeCategory(Parcel in) {
        name = in.readString();
        color = in.readInt();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(name);
        dest.writeInt(color);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<LifeCategory> CREATOR = new Creator<LifeCategory>() {
        @Override
        public LifeCategory createFromParcel(Parcel in) {
            return new LifeCategory(in);
        }

        @Override
        public LifeCategory[] newArray(int size) {
            return new LifeCategory[size];
        }
    };

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getColor() {
        return color;
    }

    public void setColor(int color) {
        this.color = color;
    }
}
