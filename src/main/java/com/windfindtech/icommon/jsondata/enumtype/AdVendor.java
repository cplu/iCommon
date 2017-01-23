package com.windfindtech.icommon.jsondata.enumtype;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cplu on 2016/4/8.
 */
public enum AdVendor {
	@SerializedName("zamplus")
	zamplus("zamplus"),
	@SerializedName("cloudcross")
	cloudcross("cloudcross"),
	@SerializedName("adinall")
	adinall("adinall"),
	@SerializedName("zamplusrtb")
	zamplusrtb("zamplusrtb");
	String vendor;

	AdVendor(String vendor) {
		this.vendor = vendor;
	}

	public String toString() {
		return this.vendor;
	}
}
