package com.windfindtech.icommon.jsondata.changning;

import java.util.ArrayList;

/**
 * Created by yu on 2015/8/28.
 */
public class TravelAgencyJson {

    private int pageIndex;
    private int pageCount;
    private ArrayList<TravelAgency> data;

    public int getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public int getPageCount() {
        return pageCount;
    }

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public ArrayList<TravelAgency> getData() {
        return data;
    }

    public void setData(ArrayList<TravelAgency> data) {
        this.data = data;
    }
}
