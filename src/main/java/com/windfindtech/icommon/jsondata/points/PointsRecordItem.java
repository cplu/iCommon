package com.windfindtech.icommon.jsondata.points;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by py on 2016/8/26.
 */
public class PointsRecordItem implements Parcelable {
    private String description;
    private Date time;
    private String amount;
    private String direction;

    public PointsRecordItem(String description, Date time, String amount) {
        this.description = description;
        this.time = time;
        this.amount = amount;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getAmount() {
        return amount;
    }

    public void setAmount(String amount) {
        this.amount = amount;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.description);
        dest.writeLong(this.time != null ? this.time.getTime() : -1);
        dest.writeString(this.amount);
    }

    public PointsRecordItem() {
    }

    protected PointsRecordItem(Parcel in) {
        this.description = in.readString();
        long tmpTime = in.readLong();
        this.time = tmpTime == -1 ? null : new Date(tmpTime);
        this.amount = in.readString();
    }

    public static final Creator<PointsRecordItem> CREATOR = new Creator<PointsRecordItem>() {
        @Override
        public PointsRecordItem createFromParcel(Parcel source) {
            return new PointsRecordItem(source);
        }

        @Override
        public PointsRecordItem[] newArray(int size) {
            return new PointsRecordItem[size];
        }
    };

    public String getDirection() {
        return direction;
    }

    public void setDirection(String direction) {
        this.direction = direction;
    }
}
