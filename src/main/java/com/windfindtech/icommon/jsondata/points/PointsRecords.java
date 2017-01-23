package com.windfindtech.icommon.jsondata.points;

/**
 * Created by cplu on 2016/11/10.
 */

public class PointsRecords {
	private int pageIndex;
	private int pageCount;
	private int totalReturnRecords;
	private PointsRecordItem[] data;

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

	public int getTotalReturnRecords() {
		return totalReturnRecords;
	}

	public void setTotalReturnRecords(int totalReturnRecords) {
		this.totalReturnRecords = totalReturnRecords;
	}

	public PointsRecordItem[] getData() {
		return data;
	}

	public void setData(PointsRecordItem[] data) {
		this.data = data;
	}
}
