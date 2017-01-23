package com.windfindtech.icommon.jsondata.life;

import java.util.ArrayList;

/**
 * Created by yu on 2015/8/8.
 */
public class LifeJson {
    private String pageIndex;
    private String pageCount;
    private String totalReturnRecords;
    private ArrayList<LifeDetail> data;

    public String getPageIndex() {
        return pageIndex;
    }

    public void setPageIndex(String pageIndex) {
        this.pageIndex = pageIndex;
    }

    public String getPageCount() {
        return pageCount;
    }

    public void setPageCount(String pageCount) {
        this.pageCount = pageCount;
    }

    public String getTotalReturnRecords() {
        return totalReturnRecords;
    }

    public void setTotalReturnRecords(String totalReturnRecords) {
        this.totalReturnRecords = totalReturnRecords;
    }

    public ArrayList<LifeDetail> getData() {
        return data;
    }

    public void setData(ArrayList<LifeDetail> data) {
        this.data = data;
    }
}
