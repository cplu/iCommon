package com.windfindtech.icommon.jsondata.points;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by py on 2016/11/3.
 */

public class MyRankItem implements Parcelable {
    private String userId;
    private RankItem level;
    private int points;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public RankItem getLevel() {
        return level;
    }

    public void setLevel(RankItem level) {
        this.level = level;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.userId);
        dest.writeParcelable(this.level, flags);
        dest.writeInt(this.points);
    }

    public MyRankItem() {
    }

    protected MyRankItem(Parcel in) {
        this.userId = in.readString();
        this.level = in.readParcelable(RankItem.class.getClassLoader());
        this.points = in.readInt();
    }

    public static final Creator<MyRankItem> CREATOR = new Creator<MyRankItem>() {
        @Override
        public MyRankItem createFromParcel(Parcel source) {
            return new MyRankItem(source);
        }

        @Override
        public MyRankItem[] newArray(int size) {
            return new MyRankItem[size];
        }
    };
}
