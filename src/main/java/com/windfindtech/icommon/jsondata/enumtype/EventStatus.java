package com.windfindtech.icommon.jsondata.enumtype;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cplu on 2014/11/21.
 * Warning: the sequence of members in this enum is critical for sorting EventModels
 */
public enum EventStatus {
    @SerializedName("active")
    Active,
    @SerializedName("initial")
    Initial,
    @SerializedName("completed")
    Completed,
    @SerializedName("expired")
    Expired,
}
