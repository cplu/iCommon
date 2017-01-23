package com.windfindtech.icommon.jsondata.advertisement;

import java.util.ArrayList;

/**
 * Created by cplu on 2016/4/11.
 * request model for zamplus
 */
public class RequestZamplusModel {
	private String request_id;   //请求ID
	private ArrayList<RequestZamplusSlot> slot; //广告位对象，目前一次请求只支持一个广告位
	private RequestZamplusDevice device;

	public String getRequest_id() {
		return request_id;
	}

	public void setRequest_id(String request_id) {
		this.request_id = request_id;
	}

	public ArrayList<RequestZamplusSlot> getSlot() {
		return slot;
	}

	public void setSlot(ArrayList<RequestZamplusSlot> slot) {
		this.slot = slot;
	}

	public RequestZamplusDevice getDevice() {
		return device;
	}

	public void setDevice(RequestZamplusDevice device) {
		this.device = device;
	}
}
