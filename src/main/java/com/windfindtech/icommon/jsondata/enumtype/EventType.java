package com.windfindtech.icommon.jsondata.enumtype;

import com.google.gson.annotations.SerializedName;

/**
 * Created by cplu on 2014/11/20.
 */
public enum EventType {
    @SerializedName("sweepstakes")      /// name to match when being serialized/deserialized
    Sweepstakes("sweepstakes"),          /// name in composing url
    @SerializedName("check-in")
    CheckIn("checkins"),
    @SerializedName("promotion")
    Promotion("promotions"),;
    String m_value;

    EventType(String value){
        m_value = value;
    }


    @Override
    public String toString() {
        return m_value;
    }
}
