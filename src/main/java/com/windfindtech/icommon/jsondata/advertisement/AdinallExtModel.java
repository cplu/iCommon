package com.windfindtech.icommon.jsondata.advertisement;

import android.os.Parcel;

import java.util.ArrayList;

/**
 * Created by cplu on 2016/10/9.
 */

public class AdinallExtModel extends BaseAdModel {
	private String adid;
	private String returncode;
	private String adnum;
	private ArrayList<AdinallSlot> ads;

	protected AdinallExtModel(Parcel in) {
		adid = in.readString();
		returncode = in.readString();
		adnum = in.readString();
		ads = in.createTypedArrayList(AdinallSlot.CREATOR);
	}

	public static final Creator<AdinallExtModel> CREATOR = new Creator<AdinallExtModel>() {
		@Override
		public AdinallExtModel createFromParcel(Parcel in) {
			return new AdinallExtModel(in);
		}

		@Override
		public AdinallExtModel[] newArray(int size) {
			return new AdinallExtModel[size];
		}
	};

	@Override
	public String getImageUrl() {
		try {
			return ads.get(0).getImgurl();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getHrefUrl() {
		try {
			return ads.get(0).getClickurl();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String[] getPVUrl() {
		try {
			return ads.get(0).getImgtracking();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String[] getClickUrl() {
		try {
			return ads.get(0).getThclkurl();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(adid);
		dest.writeString(returncode);
		dest.writeString(adnum);
		dest.writeTypedList(ads);
	}

	public String getAdid() {
		return adid;
	}

	public void setAdid(String adid) {
		this.adid = adid;
	}

	public String getReturncode() {
		return returncode;
	}

	public void setReturncode(String returncode) {
		this.returncode = returncode;
	}

	public String getAdnum() {
		return adnum;
	}

	public void setAdnum(String adnum) {
		this.adnum = adnum;
	}

	public ArrayList<AdinallSlot> getAds() {
		return ads;
	}

	public void setAds(ArrayList<AdinallSlot> ads) {
		this.ads = ads;
	}
}
