package com.windfindtech.icommon.jsondata.shanghai;

import android.content.Context;
import android.util.Pair;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cplu on 2015/7/6.
 */
public class MovieData extends ISHBaseData {
    private Date time;
    private ArrayList<MovieItemData> data = new ArrayList<MovieItemData>();

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public ArrayList<MovieItemData> getData() {
        return data;
    }

    public void setData(ArrayList<MovieItemData> data) {
        this.data = data;
    }


    @Override
    public boolean isValid() {
        return data.size() > 0;
    }

    @Override
    public boolean getBriefInfos(String[] infos) throws Exception {
        return false;
    }

    @Override
    public Pair<Integer, String> getTextColor() throws Exception {
        /// not work!
        return null;
    }

    @Override
    public Pair<Integer, String> getBriefValue() throws Exception {
        return null;
    }
}
