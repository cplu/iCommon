package com.windfindtech.icommon.jsondata.advertisement;

/**
 * Created by cplu on 2016/4/11.
 * part of request model for zamplus
 */
public class RequestZamplusSlot {
	private String slot_id; //广告位ID，可以为空
	private int screenwidth;    //屏幕宽度，像素
	private int screenheight;   //屏幕位高度, 像素
	private RequestZamplusInsert insert;
	private RequestZamplusBanner banner;

	public RequestZamplusInsert getInsert() {
		return insert;
	}

	public void setInsert(RequestZamplusInsert insert) {
		this.insert = insert;
	}

	public RequestZamplusBanner getBanner() {
		return banner;
	}

	public void setBanner(RequestZamplusBanner banner) {
		this.banner = banner;
	}

	public String getSlot_id() {
		return slot_id;
	}

	public void setSlot_id(String slot_id) {
		this.slot_id = slot_id;
	}

	public int getScreenwidth() {
		return screenwidth;
	}

	public void setScreenwidth(int screenwidth) {
		this.screenwidth = screenwidth;
	}

	public int getScreenheight() {
		return screenheight;
	}

	public void setScreenheight(int screenheight) {
		this.screenheight = screenheight;
	}
}
