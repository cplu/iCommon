package com.windfindtech.icommon.jsondata.shanghai;

import android.content.Context;
import android.util.Pair;

import com.windfindtech.icommon.R;

import java.util.ArrayList;
import java.util.Date;

/**
 * Created by cplu on 2015/3/12.
 */
public class NecessityData extends ISHBaseData{
    private Date release;
    private ArrayList<NecessityItemData> prices;

    public Date getRelease() {
        return release;
    }

    public void setRelease(Date release) {
        this.release = release;
    }

    public ArrayList<NecessityItemData> getPrices() {
        return prices;
    }

    public void setPrices(ArrayList<NecessityItemData> prices) {
        this.prices = prices;
    }

    @Override
    public boolean isValid() {
        return getPrices() != null && getPrices().size() > 0;
    }

    @Override
    public boolean getBriefInfos(String[] infos) throws Exception {
        NecessityItemData necessity_data = getPrices().get(0);
        infos[0] = necessity_data.getName();
        return true;
    }

    @Override
    public Pair<Integer, String> getTextColor() throws Exception {
        return Pair.create(R.color.default_purple, "purple");
    }

    @Override
    public Pair<Integer, String> getBriefValue() throws Exception {
        NecessityItemData necessity_data = getPrices().get(0);
        return Pair.create(0, String.format("%.2f", necessity_data.getPrice()));
    }
}
