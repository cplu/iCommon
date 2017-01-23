package com.windfindtech.icommon.jsondata.advertisement;

import android.os.Parcel;

import java.util.ArrayList;

/**
 * Created by cplu on 2016/4/11.
 * response model from zamplus
 */
public class ZamplusExtModel extends BaseAdModel{
	private int code;
	private String message;
	private String request_id;
	private ArrayList<ZamplusSlot> slot;

	protected ZamplusExtModel(Parcel in) {
		code = in.readInt();
		message = in.readString();
		request_id = in.readString();
		slot = in.createTypedArrayList(ZamplusSlot.CREATOR);
	}

	public static final Creator<ZamplusExtModel> CREATOR = new Creator<ZamplusExtModel>() {
		@Override
		public ZamplusExtModel createFromParcel(Parcel in) {
			return new ZamplusExtModel(in);
		}

		@Override
		public ZamplusExtModel[] newArray(int size) {
			return new ZamplusExtModel[size];
		}
	};

	public int getCode() {
		return code;
	}

	public void setCode(int code) {
		this.code = code;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getRequest_id() {
		return request_id;
	}

	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}

	public ArrayList<ZamplusSlot> getSlot() {
		return slot;
	}

	public void setSlot(ArrayList<ZamplusSlot> slot) {
		this.slot = slot;
	}

	@Override
	public String getImageUrl() {
		try {
			return slot.get(0).getUrl().getUrl();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getHrefUrl() {
		try {
			return slot.get(0).getUrl().getClk();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String[] getPVUrl() {
		try {
			String[] ret = new String[slot.size()];
			for(int i = 0; i < ret.length; i++) {
				ret[i] = slot.get(i).getUrl().getImp_mon();
			}
			return ret;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String[] getClickUrl() {
		try {
			String[] ret = new String[slot.size()];
			for(int i = 0; i < ret.length; i++) {
				ret[i] = slot.get(i).getUrl().getClk_mon();
			}
			return ret;
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
		dest.writeInt(code);
		dest.writeString(message);
		dest.writeString(request_id);
		dest.writeTypedList(slot);
	}
}
