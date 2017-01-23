package com.windfindtech.icommon.jsondata.enumtype;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cplu on 2015/12/30.
 */
public enum LocationType {
	@SerializedName("GPS")
	GPS("gps"),
	@SerializedName("SAME_REQ")
	SAME_REQ("same_req"),
	@SerializedName("CACHE")
	CACHE("cache"),
	@SerializedName("WIFI")
	WIFI("wifi"),
	@SerializedName("CELL")
	CELL("cell"),
	@SerializedName("AMAP")
	AMAP("amap"),
	@SerializedName("OFFLINE")
	OFFLINE("offline"),
	@SerializedName("MOCK")
	MOCK("mock"),
	@SerializedName("BYHAND")
	BYHAND("byhand"),;
	String m_value;

	LocationType(String value) {
		m_value = value;
	}


	@Override
	public String toString() {
		return m_value;
	}
}
