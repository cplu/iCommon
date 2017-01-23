package com.windfindtech.icommon.jsondata.points;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by py on 2016/9/5.
 */
public class MyAchievementItem implements Parcelable{
    private String imgID;
    private String code;
    private String name;
    private String description;
    private int points;
    private int curCounts;
    private boolean finished;
    private String introPage;
    private String icon;

    public MyAchievementItem(String imgID, String name, String description, String code) {
        this.imgID = imgID;
        this.name = name;
        this.description = description;
        this.code = code;
    }

    public String getImgID() {
        return imgID;
    }

    public void setImgID(String imgID) {
        this.imgID = imgID;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getCurCounts() {
        return curCounts;
    }

    public void setCurCounts(int curCounts) {
        this.curCounts = curCounts;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.imgID);
        dest.writeString(this.code);
        dest.writeString(this.name);
        dest.writeString(this.description);
        dest.writeInt(this.points);
        dest.writeInt(this.curCounts);
        dest.writeByte(this.finished ? (byte) 1 : (byte) 0);
    }

    protected MyAchievementItem(Parcel in) {
        this.imgID = in.readString();
        this.code = in.readString();
        this.name = in.readString();
        this.description = in.readString();
        this.points = in.readInt();
        this.curCounts = in.readInt();
        this.finished = in.readByte() != 0;
    }

    public static final Creator<MyAchievementItem> CREATOR = new Creator<MyAchievementItem>() {
        @Override
        public MyAchievementItem createFromParcel(Parcel source) {
            return new MyAchievementItem(source);
        }

        @Override
        public MyAchievementItem[] newArray(int size) {
            return new MyAchievementItem[size];
        }
    };

    public String getIntroPage() {
        return introPage;
    }

    public void setIntroPage(String introPage) {
        this.introPage = introPage;
    }

    public String getIcon() {
        return icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }
}
