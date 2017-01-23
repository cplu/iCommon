package com.windfindtech.icommon.jsondata.radio;

import java.util.ArrayList;

/**
 * Created by cplu on 2015/8/8.
 */
public class AudioList {
	private int pageIndex;
	private int pageCount;
	private int totalRecords;
	private int totalReturnRecords;
	private ArrayList<AudioData> data;

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

	public int getTotalRecords() {
		return totalRecords;
	}

	public void setTotalRecords(int totalRecords) {
		this.totalRecords = totalRecords;
	}

	public int getTotalReturnRecords() {
		return totalReturnRecords;
	}

	public void setTotalReturnRecords(int totalReturnRecords) {
		this.totalReturnRecords = totalReturnRecords;
	}

	public ArrayList<AudioData> getData() {
		return data;
	}

	public void setData(ArrayList<AudioData> data) {
		this.data = data;
	}
}
