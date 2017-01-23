package com.windfindtech.icommon.jsondata.advertisement;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by cplu on 2016/10/9.
 */

public class AdinallSlot implements Parcelable {
	private String[] thclkurl;
	private String clickurl;
	private int admt;
	private String[] imgtracking;
	private String displaytitle;
	private String displaytext;
	private int adct;
	private String imgurl;

	protected AdinallSlot(Parcel in) {
		thclkurl = in.createStringArray();
		clickurl = in.readString();
		admt = in.readInt();
		imgtracking = in.createStringArray();
		displaytitle = in.readString();
		displaytext = in.readString();
		adct = in.readInt();
		imgurl = in.readString();
	}

	public static final Creator<AdinallSlot> CREATOR = new Creator<AdinallSlot>() {
		@Override
		public AdinallSlot createFromParcel(Parcel in) {
			return new AdinallSlot(in);
		}

		@Override
		public AdinallSlot[] newArray(int size) {
			return new AdinallSlot[size];
		}
	};

	public String[] getThclkurl() {
		return thclkurl;
	}

	public void setThclkurl(String[] thclkurl) {
		this.thclkurl = thclkurl;
	}

	public String getClickurl() {
		return clickurl;
	}

	public void setClickurl(String clickurl) {
		this.clickurl = clickurl;
	}

	public int getAdmt() {
		return admt;
	}

	public void setAdmt(int admt) {
		this.admt = admt;
	}

	public String[] getImgtracking() {
		return imgtracking;
	}

	public void setImgtracking(String[] imgtracking) {
		this.imgtracking = imgtracking;
	}

	public String getDisplaytitle() {
		return displaytitle;
	}

	public void setDisplaytitle(String displaytitle) {
		this.displaytitle = displaytitle;
	}

	public String getDisplaytext() {
		return displaytext;
	}

	public void setDisplaytext(String displaytext) {
		this.displaytext = displaytext;
	}

	public int getAdct() {
		return adct;
	}

	public void setAdct(int adct) {
		this.adct = adct;
	}

	public String getImgurl() {
		return imgurl;
	}

	public void setImgurl(String imgurl) {
		this.imgurl = imgurl;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeStringArray(thclkurl);
		dest.writeString(clickurl);
		dest.writeInt(admt);
		dest.writeStringArray(imgtracking);
		dest.writeString(displaytitle);
		dest.writeString(displaytext);
		dest.writeInt(adct);
		dest.writeString(imgurl);
	}
}
