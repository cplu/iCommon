package com.windfindtech.icommon.jsondata.changning;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.Date;

/**
 * Created by yu on 2015/8/28.
 */
public class TravelPlace implements Parcelable{

    private String id;
    private String name;
    private String address;
    private Date releaseTime;
    private String description;
    private String image;
    private String ticketPrice;
    private String telephone;
    private String star;

    protected TravelPlace(Parcel in) {
        id = in.readString();
        name = in.readString();
        address = in.readString();
        long time = in.readLong();
        releaseTime = time != 0 ? new Date(time) : null;
        description = in.readString();
        image = in.readString();
        ticketPrice = in.readString();
        telephone = in.readString();
        star = in.readString();
    }

    public static final Creator<TravelPlace> CREATOR = new Creator<TravelPlace>() {
        @Override
        public TravelPlace createFromParcel(Parcel in) {
            return new TravelPlace(in);
        }

        @Override
        public TravelPlace[] newArray(int size) {
            return new TravelPlace[size];
        }
    };

    public String getTelephone() {
        return telephone;
    }

    public void setTelephone(String telephone) {
        this.telephone = telephone;
    }

    public String getStar() {
        return star;
    }

    public void setStar(String star) {
        this.star = star;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public Date getReleaseTime() {
        return releaseTime;
    }

    public void setReleaseTime(Date releaseTime) {
        this.releaseTime = releaseTime;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getTicketPrice() {
        return ticketPrice;
    }

    public void setTicketPrice(String ticketPrice) {
        this.ticketPrice = ticketPrice;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(id);
        dest.writeString(name);
        dest.writeString(address);
        dest.writeLong(releaseTime != null ? releaseTime.getTime() : 0);
        dest.writeString(description);
        dest.writeString(image);
        dest.writeString(ticketPrice);
        dest.writeString(telephone);
        dest.writeString(star);
    }
}
