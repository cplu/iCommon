package com.windfindtech.icommon.jsondata.nearsite;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * Created by py on 2016/6/22.
 */
public class NearSiteItemWrapper implements Parcelable {


	private NearSiteItemDetail properties;

	private NearSiteItemLocation location;

	private transient float distance = 0;

	public float getDistance() {
		return distance;
	}

	public void setDistance(float distance) {
		this.distance = distance;
	}

	public NearSiteItemDetail getProperties() {
		return properties;
	}

	public void setProperties(NearSiteItemDetail properties) {
		this.properties = properties;
	}

	public NearSiteItemLocation getLocation() {
		return location;
	}

	public void setLocation(NearSiteItemLocation location) {
		this.location = location;
	}


	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeParcelable(this.properties, flags);
		dest.writeParcelable(this.location, flags);
	}

	public NearSiteItemWrapper() {
	}

	protected NearSiteItemWrapper(Parcel in) {
		this.properties = in.readParcelable(NearSiteItemDetail.class.getClassLoader());
		this.location = in.readParcelable(NearSiteItemLocation.class.getClassLoader());
	}

	public static final Creator<NearSiteItemWrapper> CREATOR = new Creator<NearSiteItemWrapper>() {
		@Override
		public NearSiteItemWrapper createFromParcel(Parcel source) {
			return new NearSiteItemWrapper(source);
		}

		@Override
		public NearSiteItemWrapper[] newArray(int size) {
			return new NearSiteItemWrapper[size];
		}
	};
}
