package com.windfindtech.icommon.jsondata.advertisement;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by cplu on 2016/4/11.
 * part of response model from zamplus
 */
public class ZamplusUrl implements Parcelable {
	private String url;     /// image url
	private String clk;     /// click to navigate to this url
	private String imp_mon; /// address for pv monitor in zamplus
	private String clk_mon; /// address for click monitor in zamplus

	protected ZamplusUrl(Parcel in) {
		url = in.readString();
		clk = in.readString();
		imp_mon = in.readString();
		clk_mon = in.readString();
	}

	public static final Creator<ZamplusUrl> CREATOR = new Creator<ZamplusUrl>() {
		@Override
		public ZamplusUrl createFromParcel(Parcel in) {
			return new ZamplusUrl(in);
		}

		@Override
		public ZamplusUrl[] newArray(int size) {
			return new ZamplusUrl[size];
		}
	};

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getClk() {
		return clk;
	}

	public void setClk(String clk) {
		this.clk = clk;
	}

	public String getImp_mon() {
		return imp_mon;
	}

	public void setImp_mon(String imp_mon) {
		this.imp_mon = imp_mon;
	}

	public String getClk_mon() {
		return clk_mon;
	}

	public void setClk_mon(String clk_mon) {
		this.clk_mon = clk_mon;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(url);
		dest.writeString(clk);
		dest.writeString(imp_mon);
		dest.writeString(clk_mon);
	}
}
