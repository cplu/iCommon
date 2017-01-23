package com.windfindtech.icommon.jsondata.enumtype;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cplu on 2014/11/24.
 */
public enum EventCategory {
    @SerializedName("movie")
    Movie("movie"),
    @SerializedName("food")
    Food("food"),;
    String m_value;

    EventCategory(String value){
        m_value = value;
    }


    @Override
    public String toString() {
        return m_value;
    }
}
