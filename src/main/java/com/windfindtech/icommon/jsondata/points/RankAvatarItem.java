package com.windfindtech.icommon.jsondata.points;

import android.graphics.Bitmap;

public class RankAvatarItem {

    private int level;
    private String name;
    private String speed;
    private int countLoginSitesInMonth;
    private String countLoginsInMonth;
    private Bitmap bitmap;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSpeed() {
        return speed;
    }

    public void setSpeed(String speed) {
        this.speed = speed;
    }

    public int getCountLoginSitesInMonth() {
        return countLoginSitesInMonth;
    }

    public void setCountLoginSitesInMonth(int countLoginSitesInMonth) {
        this.countLoginSitesInMonth = countLoginSitesInMonth;
    }

    public String getCountLoginsInMonth() {
        return countLoginsInMonth;
    }

    public void setCountLoginsInMonth(String countLoginsInMonth) {
        this.countLoginsInMonth = countLoginsInMonth;
    }
}
