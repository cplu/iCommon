package com.windfindtech.icommon.jsondata.shanghai;

import java.util.Date;

/**
 * Created by cplu on 2015/5/21.
 */
public class CarPlateItem {
    private Date release;
    private float priceCap;
    private float lowest;
    private float average;
    private float successRate;
    private boolean finished;
    private int totalReleased;
    private String lowestInfo;
    private int totalPeople;
    private Date nextAuction;

    public Date getRelease() {
        return release;
    }

    public void setRelease(Date release) {
        this.release = release;
    }

    public float getPriceCap() {
        return priceCap;
    }

    public void setPriceCap(float priceCap) {
        this.priceCap = priceCap;
    }

    public float getLowest() {
        return lowest;
    }

    public void setLowest(float lowest) {
        this.lowest = lowest;
    }

    public float getAverage() {
        return average;
    }

    public void setAverage(float average) {
        this.average = average;
    }

    public float getSuccessRate() {
        return successRate;
    }

    public void setSuccessRate(float successRate) {
        this.successRate = successRate;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }

    public int getTotalReleased() {
        return totalReleased;
    }

    public void setTotalReleased(int totalReleased) {
        this.totalReleased = totalReleased;
    }

    public String getLowestInfo() {
        return lowestInfo;
    }

    public void setLowestInfo(String lowestInfo) {
        this.lowestInfo = lowestInfo;
    }

    public int getTotalPeople() {
        return totalPeople;
    }

    public void setTotalPeople(int totalPeople) {
        this.totalPeople = totalPeople;
    }

    public Date getNextAuction() {
        return nextAuction;
    }

    public void setNextAuction(Date nextAuction) {
        this.nextAuction = nextAuction;
    }
}
