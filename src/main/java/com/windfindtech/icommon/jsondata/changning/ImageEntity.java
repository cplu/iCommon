package com.windfindtech.icommon.jsondata.changning;

/**
 * Created by cplu on 2015/12/17.
 */
public class ImageEntity {
	private String etag;
	private String ext;
	private String id;
	private String lastModified;
	private String type;

	public void setEtag(String etag) {
		this.etag = etag;
	}

	public void setExt(String ext) {
		this.ext = ext;
	}

	public void setId(String id) {
		this.id = id;
	}

	public void setLastModified(String lastModified) {
		this.lastModified = lastModified;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getEtag() {
		return etag;
	}

	public String getExt() {
		return ext;
	}

	public String getId() {
		return id;
	}

	public String getLastModified() {
		return lastModified;
	}

	public String getType() {
		return type;
	}
}
