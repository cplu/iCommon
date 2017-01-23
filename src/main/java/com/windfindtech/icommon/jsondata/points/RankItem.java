package com.windfindtech.icommon.jsondata.points;

import android.os.Parcel;
import android.os.Parcelable;

public class RankItem implements Parcelable {

    private int level;
    private String name;
    private String speed;
    private int countLoginSitesInMonth;
    private String countLoginsInMonth;

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public int getCountLoginSitesInMonth() {
        return countLoginSitesInMonth;
    }

    public void setCountLoginSitesInMonth(int countLoginSitesInMonth) {
        this.countLoginSitesInMonth = countLoginSitesInMonth;
    }

    public String getCountLoginsInMonth() {
        return countLoginsInMonth;
    }

    public void setCountLoginsInMonth(String countLoginsInMonth) {
        this.countLoginsInMonth = countLoginsInMonth;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeInt(this.level);
        dest.writeString(this.name);
        dest.writeString(this.speed);
        dest.writeInt(this.countLoginSitesInMonth);
        dest.writeString(this.countLoginsInMonth);
    }

    public RankItem() {
    }

    protected RankItem(Parcel in) {
        this.level = in.readInt();
        this.name = in.readString();
        this.speed = in.readString();
        this.countLoginSitesInMonth = in.readInt();
        this.countLoginsInMonth = in.readString();
    }

    public static final Creator<RankItem> CREATOR = new Creator<RankItem>() {
        @Override
        public RankItem createFromParcel(Parcel source) {
            return new RankItem(source);
        }

        @Override
        public RankItem[] newArray(int size) {
            return new RankItem[size];
        }
    };
}
