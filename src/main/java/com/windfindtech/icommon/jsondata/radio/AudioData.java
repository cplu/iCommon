package com.windfindtech.icommon.jsondata.radio;

import java.util.Date;

/**
 * Created by cplu on 2015/8/8.
 */
public class AudioData {
	private String id;
	private String radioId;
	private String name;
	private String href;
	private String description;
	private Date createDate;
	private String cover;
	private String coverThumb;
	private String url;
	private String urlLow;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getRadioId() {
		return radioId;
	}

	public void setRadioId(String radioId) {
		this.radioId = radioId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getCover() {
		return cover;
	}

	public void setCover(String cover) {
		this.cover = cover;
	}

	public String getCoverThumb() {
		return coverThumb;
	}

	public void setCoverThumb(String coverThumb) {
		this.coverThumb = coverThumb;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getUrlLow() {
		return urlLow;
	}

	public void setUrlLow(String urlLow) {
		this.urlLow = urlLow;
	}
}
