package com.windfindtech.icommon.jsondata.shanghai;

import android.util.Pair;

/**
 * Created by cplu on 2015/6/8.
 */
public class FuelTrendData extends ISHBaseData{
    private FuelData[] data;    /// Warning: "data" is a static string that is used in parsing json

    public FuelData[] getData() {
        return data;
    }

    public void setData(FuelData[] data) {
        this.data = data;
    }

    @Override
    public boolean isValid() {
        return data.length > 0 && data[0] != null && data[0].isValid();
    }

    @Override
    public boolean getBriefInfos(String[] infos) throws Exception {
        return false;
    }

    @Override
    public Pair<Integer, String> getTextColor() throws Exception{
        return null;
    }

    @Override
    public Pair<Integer, String> getBriefValue() throws Exception {
        return null;
    }
}
