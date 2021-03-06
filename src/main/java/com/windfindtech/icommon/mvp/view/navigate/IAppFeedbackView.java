package com.windfindtech.icommon.mvp.view.navigate;

import android.graphics.Bitmap;

import com.windfindtech.icommon.jsondata.user.UserData;
import com.windfindtech.icommon.jsondata.webservice.BaseResponse;
import com.windfindtech.icommon.jsondata.webservice.DeviceData;
import com.windfindtech.icommon.jsondata.webservice.VersionCheck;
import com.windfindtech.icommon.jsondata.webservice.WSErrorResponse;
import com.windfindtech.icommon.mvp.model.MLocalReceiver;

import java.util.ArrayList;

/**
 * Created by cplu on 2016/8/2.
 */
public interface IAppFeedbackView extends INavigateView{

	IAppFeedbackView dummy = new IAppFeedbackView() {
		@Override
		public void onSubmitFeedbackSuccess(BaseResponse ret) {

		}

		@Override
		public void onSubmitFeedbackFailed(WSErrorResponse reason) {

		}

		@Override
		public void onSendErrorReportSuccess(BaseResponse ret) {

		}

		@Override
		public void onSendErrorReportFailed(WSErrorResponse reason) {

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

	void onSubmitFeedbackSuccess(BaseResponse ret);

	void onSubmitFeedbackFailed(WSErrorResponse reason);

	void onSendErrorReportSuccess(BaseResponse ret);

	void onSendErrorReportFailed(WSErrorResponse reason);

}
