package com.windfindtech.icommon.mvp.view.navigate;

import android.graphics.Bitmap;

import com.windfindtech.icommon.jsondata.enumtype.DeviceStatus;
import com.windfindtech.icommon.jsondata.user.UserData;
import com.windfindtech.icommon.jsondata.webservice.DeviceData;
import com.windfindtech.icommon.jsondata.webservice.VersionCheck;
import com.windfindtech.icommon.mvp.model.MLocalReceiver;

import java.util.ArrayList;

/**
 * Created by cplu on 2016/8/2.
 */
public interface IDeviceDetailView extends INavigateView{

	IDeviceDetailView dummy = new IDeviceDetailView() {
		@Override
		public void onNameChanged(String new_nick_name) {

		}

		@Override
		public void onNameUnchanged() {

		}

		@Override
		public void onDeviceStatus(DeviceStatus status, boolean changed) {

		}

		@Override
		public void onDeleteResult(boolean success) {

		}

		@Override
		public boolean avatarConcerned() {
			return false;
		}

		@Override
		public boolean usrDataConcerned() {
			return false;
		}

		@Override
		public void onAvatar(Bitmap bitmap) {

		}

		@Override
		public void onAvatarUploadResult(boolean success) {

		}

		@Override
		public void onUserData(UserData userData) {

		}

		@Override
		public void onNetworkChanged(@MLocalReceiver.NetworkStatus int status) {

		}

		@Override
		public void onWifiChanged(@MLocalReceiver.WifiStatus int status) {

		}

		@Override
		public void onWSChanged(@MLocalReceiver.WebserviceStatus int status) {

		}

		@Override
		public void onDeviceListChanged(ArrayList<DeviceData>[] devices) {

		}

		@Override
		public void onVersionCheck(VersionCheck versionCheck) {

		}

		@Override
		public void onReservedMessage(Boolean hasReservedMessage) {

		}
	};

	void onNameChanged(String new_nick_name);

	void onNameUnchanged();

	/**
	 * notify device status
	 * @param status        current status
	 * @param changed       whether status has changed
	 */
	void onDeviceStatus(DeviceStatus status, boolean changed);

	/**
	 * notify device deletion result
	 * @param success       whether deletion succeeds
	 */
	void onDeleteResult(boolean success);
}
