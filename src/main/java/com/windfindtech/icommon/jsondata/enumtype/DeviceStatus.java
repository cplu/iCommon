package com.windfindtech.icommon.jsondata.enumtype;

import com.google.gson.annotations.SerializedName;

public enum DeviceStatus {
    @SerializedName("pending")
    pending("pending"),
    @SerializedName("approved")
    approved("approved"),
    @SerializedName("denied")
    denied("denied");
    private final String status;

    DeviceStatus(String status) {
        this.status = status;
    }

    public String toString() {
        return this.status;
    }
}

//class DeviceStatusDeserializer implements JsonDeserializer<DeviceStatus> {
//    @Override
//    public DeviceStatus deserialize(JsonElement json, Type typeOfT,
//                                    JsonDeserializationContext context) throws JsonParseException {
//        DeviceStatus[] statuses = DeviceStatus.values();
//        for (DeviceStatus status : statuses) {
//            if (status.status.equals(json.getAsString()))
//                return status;
//        }
//        return null;
//    }
//}