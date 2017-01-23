package com.windfindtech.icommon.jsondata.life;

import android.graphics.Bitmap;

/**
 * Created by yu on 2015/8/8.
 */
public class Provider {

    private String id;
    private String logo;

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }

    private Bitmap bitmap;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

}
