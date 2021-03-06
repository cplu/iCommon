package com.windfindtech.icommon.mvp.view;

import com.windfindtech.icommon.mvp.model.MLocalReceiver;

/**
 * Created by cplu on 2016/7/26.
 */
public interface IActivityView {

	/**
	 * change ui by network status
	 * @param status
	 */
	void onNetworkChanged(@MLocalReceiver.NetworkStatus int status);

	/**
	 * triggered by wifi status changed
	 *
	 * @param status one of MLocalReceiver#WIFI_XXXX
	 */
	void onWifiChanged(@MLocalReceiver.WifiStatus int status);

	/**
	 * triggered by webservice status changed
	 *
	 * @param status one of the following values:
	 *               <ul>
	 *               <li>{@link MLocalReceiver#WS_NOT_LOGGED_IN}
	 *               <li>{@link MLocalReceiver#WS_IN_PROGRESS
	 *               <li>{@link MLocalReceiver#WS_LOGGED_IN}
	 *               </ul>
	 */
	void onWSChanged(@MLocalReceiver.WebserviceStatus int status);
}
