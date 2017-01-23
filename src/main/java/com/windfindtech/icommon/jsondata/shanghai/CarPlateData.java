package com.windfindtech.icommon.jsondata.shanghai;

import android.content.Context;
import android.util.Pair;

import com.windfindtech.icommon.R;


/**
 * Created by cplu on 2015/3/11.
 * car plate data per month
 */
public class CarPlateData extends ISHBaseData{
    private CarPlateItem[] data;    /// Warning: "data" is a static string that is used in parsing json

    public CarPlateItem[] getData() {
        return data;
    }

    public void setData(CarPlateItem[] data) {
        this.data = data;
    }

    @Override
    public boolean isValid() {
        return data.length > 0 && data[0] != null && data[0].getLowest() > 0;
    }

    @Override
    public boolean getBriefInfos(String[] infos) throws Exception {
        return false;
    }

    @Override
    public Pair<Integer, String> getTextColor() throws Exception {
        return Pair.create(R.color.default_blue, "blue");
    }

    @Override
    public Pair<Integer, String> getBriefValue() throws Exception {
        int value=(int)data[1].getPriceCap();
        if (value>0) {
            String CarportValue = String.format("%,d",value);
            return Pair.create(0, CarportValue);
        }else{
            return Pair.create(0, "待公布");
        }
//        return Pair.create(0, String.valueOf((int) data[0].getLowest()));
    }
}
