package com.windfindtech.icommon.jsondata.advertisement;

/**
 * Created by cplu on 2016/4/11.
 * part of request model for zamplus
 */
public class RequestZamplusInsert {
	private int width;  //广告位宽度，像素
	private int height; //广告位高度, 像素
	private int material_type;  //目前只支持图片 1 :图片

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public int getMaterial_type() {
		return material_type;
	}

	public void setMaterial_type(int material_type) {
		this.material_type = material_type;
	}
}
