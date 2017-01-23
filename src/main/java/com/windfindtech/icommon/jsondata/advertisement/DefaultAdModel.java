package com.windfindtech.icommon.jsondata.advertisement;

import android.os.Parcel;

import com.windfindtech.icommon.jsondata.enumtype.AdType;
import com.windfindtech.icommon.jsondata.enumtype.AdVendor;

import java.net.URI;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by cplu on 2016/4/8.
 */
public class DefaultAdModel extends BaseAdModel {

	private String id;
	private String spot;
	private AdVendor vendor;
	private AdType type;
	private String title;
	private String baseUrl; /// set by app itself, not from webservice
	private String url;
	private String href;    /// open system browser and navigate to href
	private List<String> trackUrls;     /// pv
	private List<String> trackClickUrls;    /// clicks
	private Map<String, Object> properties;

	protected DefaultAdModel(Parcel in) {
		id = in.readString();
		spot = in.readString();
		try {
			vendor = AdVendor.valueOf(in.readString());
		} catch (Exception e) {
			vendor = null;
		}
		try {
			type = AdType.valueOf(in.readString());
		} catch (Exception e) {
			type = null;
		}
		title = in.readString();
		baseUrl = in.readString();
		url = in.readString();
		href = in.readString();
		trackUrls = in.createStringArrayList();
		trackClickUrls = in.createStringArrayList();
		properties = new HashMap<>();
		final int N = in.readInt();
		for (int i = 0; i < N; i++) {
			String key = in.readString();
			String dat = in.readString();
			properties.put(key, dat);
		}
	}

	public static final Creator<DefaultAdModel> CREATOR = new Creator<DefaultAdModel>() {
		@Override
		public DefaultAdModel createFromParcel(Parcel in) {
			return new DefaultAdModel(in);
		}

		@Override
		public DefaultAdModel[] newArray(int size) {
			return new DefaultAdModel[size];
		}
	};

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getSpot() {
		return spot;
	}

	public void setSpot(String spot) {
		this.spot = spot;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public String getHref() {
		return href;
	}

	public void setHref(String href) {
		this.href = href;
	}

	public List<String> getTrackUrls() {
		return trackUrls;
	}

	public void setTrackUrls(List<String> trackUrls) {
		this.trackUrls = trackUrls;
	}

	public List<String> getTrackClickUrls() {
		return trackClickUrls;
	}

	public void setTrackClickUrls(List<String> trackClickUrls) {
		this.trackClickUrls = trackClickUrls;
	}

	public Map<String, Object> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, Object> properties) {
		this.properties = properties;
	}

	public AdType getType() {
		return type;
	}

	public void setType(AdType type) {
		this.type = type;
	}

	public AdVendor getVendor() {
		return vendor;
	}

	public void setVendor(AdVendor vendor) {
		this.vendor = vendor;
	}

	public String getBaseUrl() {
		return baseUrl;
	}

	public void setBaseUrl(String baseUrl) {
		this.baseUrl = baseUrl;
	}

	@Override
	public String getImageUrl() {
		try {
			URI uri = new URI(baseUrl);
			return uri.resolve(url).toString();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String getHrefUrl() {
		try {
			URI uri = new URI(baseUrl);
			return uri.resolve(href).toString();
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String[] getPVUrl() {
		try {
			URI uri = new URI(baseUrl);
			String[] ret = new String[trackUrls.size()];
			for (int i = 0; i < ret.length; i++) {
				ret[i] = uri.resolve(trackUrls.get(i)).toString();
			}
			return ret;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public String[] getClickUrl() {
		try {
			URI uri = new URI(baseUrl);
			String[] ret = new String[trackClickUrls.size()];
			for (int i = 0; i < ret.length; i++) {
				ret[i] = uri.resolve(trackClickUrls.get(i)).toString();
			}
			return ret;
		} catch (Exception e) {
			return null;
		}
	}

	@Override
	public int describeContents() {
		return 0;
	}

	@Override
	public void writeToParcel(Parcel dest, int flags) {
		dest.writeString(id);
		dest.writeString(spot);
		dest.writeString(vendor != null ? vendor.toString() : null);
		dest.writeString(type != null ? type.toString() : null);
		dest.writeString(title);
		dest.writeString(baseUrl);
		dest.writeString(url);
		dest.writeString(href);
		dest.writeStringList(trackUrls);
		dest.writeStringList(trackClickUrls);
		if(properties != null) {
			final int N = properties.size();
			dest.writeInt(N);
			if (N > 0) {
				for (Map.Entry<String, Object> entry : properties.entrySet()) {
					dest.writeString(entry.getKey());
					dest.writeString(entry.getValue().toString());
				}
			}
		} else{
			dest.writeInt(0);
		}
	}
}
