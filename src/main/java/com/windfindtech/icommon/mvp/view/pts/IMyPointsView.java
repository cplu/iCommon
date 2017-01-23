package com.windfindtech.icommon.mvp.view.pts;

import com.windfindtech.icommon.jsondata.points.MyAchievementItem;
import com.windfindtech.icommon.jsondata.points.MyPointsItem;
import com.windfindtech.icommon.mvp.model.MLocalReceiver;
import com.windfindtech.icommon.mvp.view.IFragmentView;

/**
 * Created by py on 2016/8/23.
 */
public interface IMyPointsView extends IFragmentView {
	IMyPointsView dummy = new IMyPointsView() {

		@Override
		public void onMyPointsResult(MyPointsItem[] ret) {

		}

		@Override
		public void onMyAchievementsResult(MyAchievementItem[] ret) {

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
	};

	void onMyPointsResult(MyPointsItem[] ret);

	void onMyAchievementsResult(MyAchievementItem[] ret);
}
