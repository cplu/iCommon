package com.windfindtech.icommon.jsondata.advertisement;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cplu on 2016/4/11.
 * part of response model from zamplus
 */
public class ZamplusSlot implements Parcelable{
	private String slot_id;
	private String ad_id;
	private ZamplusUrl url;

	protected ZamplusSlot(Parcel in) {
		slot_id = in.readString();
		ad_id = in.readString();
		url = in.readParcelable(ZamplusUrl.class.getClassLoader());
	}

	public static final Creator<ZamplusSlot> CREATOR = new Creator<ZamplusSlot>() {
		@Override
		public ZamplusSlot createFromParcel(Parcel in) {
			return new ZamplusSlot(in);
		}

		@Override
		public ZamplusSlot[] newArray(int size) {
			return new ZamplusSlot[size];
		}
	};

	public String getSlot_id() {
		return slot_id;
	}

	public void setSlot_id(String slot_id) {
		this.slot_id = slot_id;
	}

	public String getAd_id() {
		return ad_id;
	}

	public void setAd_id(String ad_id) {
		this.ad_id = ad_id;
	}

	public ZamplusUrl getUrl() {
		return url;
	}

	public void setUrl(ZamplusUrl url) {
		this.url = url;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(slot_id);
		dest.writeString(ad_id);
		dest.writeParcelable(url, flags);
	}
}
