package com.windfindtech.icommon.jsondata.event;

/**
 * Created by cplu on 2014/11/21.
 */
public class EventList {
    private int pageIndex;
    private int pageCount;
    private EventModel[] data;

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

    public EventModel[] getData() {
        return data;
    }

    public void setData(EventModel[] data) {
        this.data = data;
    }
}
