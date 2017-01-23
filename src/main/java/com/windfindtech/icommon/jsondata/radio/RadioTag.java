package com.windfindtech.icommon.jsondata.radio;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by yu on 2015/8/12.
 */
public class RadioTag implements Parcelable{

    private int id;
    private String name;

    protected RadioTag(Parcel in) {
        id = in.readInt();
        name = in.readString();
    }

    public static final Creator<RadioTag> CREATOR = new Creator<RadioTag>() {
        @Override
        public RadioTag createFromParcel(Parcel in) {
            return new RadioTag(in);
        }

        @Override
        public RadioTag[] newArray(int size) {
            return new RadioTag[size];
        }
    };

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(id);
        dest.writeString(name);
    }
}
