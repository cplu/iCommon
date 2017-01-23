package com.windfindtech.icommon.jsondata.enumtype;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cplu on 2015/7/31.
 */
public enum Gender {
	@SerializedName("male")
	Male("male"),
	@SerializedName("female")
	Female("female"),;
	String m_value;

	Gender(String value){
		m_value = value;
	}


	@Override
	public String toString() {
		return m_value;
	}
}
