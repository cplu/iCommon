package com.windfindtech.icommon.jsondata.changning;


import java.util.List;

/**
 * Created by cplu on 2015/12/17.
 */
public class CreativityDataEntity {
	private String href;
	private String id;
	private String title;

	public String getActive() {
		return active;
	}

	public void setActive(String active) {
		this.active = active;
	}

	private String active;
	/**
	 * etag : 28255fa162fbcc86c1bfd61f82bfb212
	 * ext : .jpg
	 * id : 3-actdist_B
	 * lastModified : 2015-12-07T15:41:02.827+0800
	 * type : origin
	 */

	private List<ImageEntity> images;

	public void setHref(String href) {
		this.href = href;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public void setImages(List<ImageEntity> images) {
		this.images = images;
	}

	public String getHref() {
		return href;
	}

	public String getId() {
		return id;
	}

	public String getTitle() {
		return title;
	}

	public List<ImageEntity> getImages() {
		return images;
	}

}
