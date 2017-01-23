package com.windfindtech.icommon.mvp.view.activity;

import com.windfindtech.icommon.jsondata.enumtype.AdvertSource;
import com.windfindtech.icommon.jsondata.webservice.LoginResponse;
import com.windfindtech.icommon.jsondata.webservice.VersionCheck;
import com.windfindtech.icommon.jsondata.webservice.WSErrorResponse;
import com.windfindtech.icommon.mvp.model.MLocalReceiver;
import com.windfindtech.icommon.mvp.view.IActivityView;
import com.windfindtech.icommon.wifimanager.WFTErrorCode;

/**
 * Created by cplu on 2016/8/2.
 */
public interface IMainView extends IActivityView {
	IMainView dummy = new IMainView() {
		@Override
		public void onNoAuthenticationInfo(boolean auth_required) {

		}

		@Override
		public void onWSLoginInProgress() {

		}

		@Override
		public void onWSLoginSuccess(LoginResponse response) {

		}

		@Override
		public void onWSLoginException(LoginResponse response) {

		}

		@Override
		public void onWSLoginFailed(WSErrorResponse reason) {

		}

		@Override
		public void onNewVersionFound(VersionCheck version_check, boolean force_update) {

		}

		@Override
		public void onNetworkCheckFailed() {

		}

		@Override
		public void onWifiAuthInProgress() {

		}

		@Override
		public void onWifiAuthSuccess() {

		}

		@Override
		public void onWifiAuthenticationFailed(WFTErrorCode code, String reason) {

		}

		@Override
		public void prepareAdvertisement(AdvertSource advertSource) {

		}

		@Override
		public void onNetworkChanged(int status) {

		}

		@Override
		public void onWifiChanged(@MLocalReceiver.WifiStatus int status) {

		}

		@Override
		public void onWSChanged(@MLocalReceiver.WebserviceStatus int status) {

		}
	};

	/**
	 * @param auth_required     Whether authentication is required. If false, authentication is not required and perhaps we need to log in to webservice.
	 */
	void onNoAuthenticationInfo(boolean auth_required);

	/**
	 * notify webservice log in in progress
	 */
	void onWSLoginInProgress();

	/**
	 * notify webservice log in success
	 * @param response
	 */
	void onWSLoginSuccess(LoginResponse response);

	/**
	 * notify exception caught during parsing LoginResponse
	 * @param response
	 */
	void onWSLoginException(LoginResponse response);

	/**
	 * notify webservice log in failed
	 * @param reason
	 */
	void onWSLoginFailed(WSErrorResponse reason);

	/**
	 * app new version is found
	 * @param force_update      whether we are forced to update app to new version
	 */
	void onNewVersionFound(final VersionCheck version_check, boolean force_update);

	/**
	 * notify network check failed
	 */
	void onNetworkCheckFailed();

	/**
	 * notify wifi authentication in progress
	 */
	void onWifiAuthInProgress();

	/**
	 * notify wifi authentication success
	 */
	void onWifiAuthSuccess();

	/**
	 * notify wifi authentication failed
	 * @param code
	 * @param reason
	 */
	void onWifiAuthenticationFailed(WFTErrorCode code, String reason);

	void prepareAdvertisement(AdvertSource advertSource);
}
