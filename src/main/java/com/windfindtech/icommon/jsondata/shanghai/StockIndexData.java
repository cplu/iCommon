package com.windfindtech.icommon.jsondata.shanghai;

import android.content.Context;
import android.util.Pair;

import com.windfindtech.icommon.R;

import java.util.Date;

/**
 * Created by cplu on 2015/3/11.
 */
public class StockIndexData extends ISHBaseData{
    private String index;
    private Date time;
    private float current;
    private float minPri;
    private float maxPri;
    private float change;
    private float percentChange;
    private float openPri;
    private float closePri;
    private long volume;
    private long turnover;

    public String getIndex() {
        return index;
    }

    public void setIndex(String index) {
        this.index = index;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public float getCurrent() {
        return current;
    }

    public void setCurrent(float current) {
        this.current = current;
    }

    public float getMinPri() {
        return minPri;
    }

    public void setMinPri(float minPri) {
        this.minPri = minPri;
    }

    public float getMaxPri() {
        return maxPri;
    }

    public void setMaxPri(float maxPri) {
        this.maxPri = maxPri;
    }

    public float getChange() {
        return change;
    }

    public void setChange(float change) {
        this.change = change;
    }

    public float getPercentChange() {
        return percentChange;
    }

    public void setPercentChange(float percentChange) {
        this.percentChange = percentChange;
    }

    public float getOpenPri() {
        return openPri;
    }

    public void setOpenPri(float openPri) {
        this.openPri = openPri;
    }

    public float getClosePri() {
        return closePri;
    }

    public void setClosePri(float closePri) {
        this.closePri = closePri;
    }

    public long getVolume() {
        return volume;
    }

    public void setVolume(long volume) {
        this.volume = volume;
    }

    public long getTurnover() {
        return turnover;
    }

    public void setTurnover(long turnover) {
        this.turnover = turnover;
    }

    @Override
    public boolean isValid() {
        return true;
    }

    @Override
    public boolean getBriefInfos(String[] infos) throws Exception {
        if (getChange() >= 0) {
            infos[1] = "↑";
        }
        else {
            infos[1] = "↓";
        }
        return true;
    }

    @Override
    public Pair<Integer, String> getTextColor() throws Exception {
        if (getChange() >= 0) {
            return Pair.create(R.color.default_pink, "red");
        }
        else {
            return Pair.create(R.color.default_green, "green");
        }
    }

    @Override
    public Pair<Integer, String> getBriefValue() throws Exception {
        return Pair.create(0, String.format("%.2f", getCurrent()));
    }

    @Override
    public boolean isArrowColorChanged() {
        return true;
    }
}
