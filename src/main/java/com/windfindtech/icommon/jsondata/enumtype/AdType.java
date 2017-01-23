package com.windfindtech.icommon.jsondata.enumtype;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cplu on 2016/4/8.
 */
public enum AdType {
	@SerializedName("image")
	image("image"),
	@SerializedName("video")
	video("video"),
	@SerializedName("audio")
	audio("audio");
	String type;

	AdType(String vendor) {
		this.type = vendor;
	}

	public String toString() {
		return this.type;
	}
}
