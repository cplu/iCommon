package com.windfindtech.icommon.jsondata.radio;

import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

/**
 * Created by cplu on 2015/8/8.
 */
public class RadioList implements Parcelable {
	private int pageIndex;
	private int pageCount;
	private int totalRecords;
	private int totalReturnRecords;
	private ArrayList<RadioData> data;

	public RadioList() {

	}

	protected RadioList(Parcel in) {
		pageIndex = in.readInt();
		pageCount = in.readInt();
		totalRecords = in.readInt();
		totalReturnRecords = in.readInt();
		data = in.createTypedArrayList(RadioData.CREATOR);
	}

	public static final Creator<RadioList> CREATOR = new Creator<RadioList>() {
		@Override
		public RadioList createFromParcel(Parcel in) {
			return new RadioList(in);
		}

		@Override
		public RadioList[] newArray(int size) {
			return new RadioList[size];
		}
	};

	public int getPageIndex() {
		return pageIndex;
	}

	public void setPageIndex(int pageIndex) {
		this.pageIndex = pageIndex;
	}

	public int getPageCount() {
		return pageCount;
	}

	public void setPageCount(int pageCount) {
		this.pageCount = pageCount;
	}

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getTotalReturnRecords() {
		return totalReturnRecords;
	}

	public void setTotalReturnRecords(int totalReturnRecords) {
		this.totalReturnRecords = totalReturnRecords;
	}

	public ArrayList<RadioData> getData() {
		return data;
	}

	public void setData(ArrayList<RadioData> data) {
		this.data = data;
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeInt(pageIndex);
		dest.writeInt(pageCount);
		dest.writeInt(totalRecords);
		dest.writeInt(totalReturnRecords);
		dest.writeTypedList(data);
	}
}
