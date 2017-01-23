package com.windfindtech.icommon.jsondata.points;

public class MyPointsItem{
    private String code;
    private String name;
    private String period;
    private int counts;
    private int points;
    private int curCounts;
    private boolean finished;

    public String getCode() {
        return code;
    }

    public void setCode(String code) {
        this.code = code;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPeriod() {
        return period;
    }

    public void setPeriod(String period) {
        this.period = period;
    }

    public int getCounts() {
        return counts;
    }

    public void setCounts(int counts) {
        this.counts = counts;
    }

    public int getPoints() {
        return points;
    }

    public void setPoints(int points) {
        this.points = points;
    }

    public int getCurCounts() {
        return curCounts;
    }

    public void setCurCounts(int curCounts) {
        this.curCounts = curCounts;
    }

    public boolean isFinished() {
        return finished;
    }

    public void setFinished(boolean finished) {
        this.finished = finished;
    }
}
