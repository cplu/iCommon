package com.windfindtech.icommon.jsondata.changning;

import java.util.List;

/**
 * Created by py on 2015/12/15.
 */
public class CreativityJson {

    /**
     * data : [{"href":"www.baidu.com","id":"3-actdist","images":[{"etag":"28255fa162fbcc86c1bfd61f82bfb212","ext":".jpg","id":"3-actdist_B","lastModified":"2015-12-07T15:41:02.827+0800","type":"origin"}],"title":"测试一12.7"}]
     * pageCount : 10
     * pageIndex : 0
     */

    private int pageCount;
    private int pageIndex;
    /**
     * href : www.baidu.com
     * id : 3-actdist
     * images : [{"etag":"28255fa162fbcc86c1bfd61f82bfb212","ext":".jpg","id":"3-actdist_B","lastModified":"2015-12-07T15:41:02.827+0800","type":"origin"}]
     * title : 测试一12.7
     */

    private List<CreativityDataEntity> data;

    public void setPageCount(int pageCount) {
        this.pageCount = pageCount;
    }

    public void setPageIndex(int pageIndex) {
        this.pageIndex = pageIndex;
    }

    public void setData(List<CreativityDataEntity> data) {
        this.data = data;
    }

    public int getPageCount() {
        return pageCount;
    }

    public int getPageIndex() {
        return pageIndex;
    }

    public List<CreativityDataEntity> getData() {
        return data;
    }


}
