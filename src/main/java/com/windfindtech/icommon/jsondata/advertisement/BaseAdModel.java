package com.windfindtech.icommon.jsondata.advertisement;

import android.os.Parcelable;

/**
 * Created by cplu on 2016/4/11.
 * extension of DefaultAdModel for 3rd party advertisement access
 * Note: this is only a base class, derived classes should implement all methods
 */
public abstract class BaseAdModel implements Parcelable {
	/**
	 * return the image url of this AD
	 * @return
	 */
	public abstract String getImageUrl();

	/**
	 * return the href we are going to jump to if user click the AD
	 * @return
	 */
	public abstract String getHrefUrl();

	/**
	 * return the Viewing url
	 * @return
	 */
	public abstract String[] getPVUrl();

	/**
	 * return the click reporting url to report click event from user
	 * @return
	 */
	public abstract String[] getClickUrl();
}
