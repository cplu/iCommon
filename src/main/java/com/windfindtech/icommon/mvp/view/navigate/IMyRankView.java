package com.windfindtech.icommon.mvp.view.navigate;

import android.graphics.Bitmap;

import com.windfindtech.icommon.jsondata.points.RankItem;
import com.windfindtech.icommon.mvp.model.MLocalReceiver;
import com.windfindtech.icommon.mvp.view.IFragmentView;

/**
 * Created by py on 2016/8/23.
 */
public interface IMyRankView extends IFragmentView {
	IMyRankView dummy = new IMyRankView() {
		@Override
		public void onRankDataResult(RankItem[] ret) {

		}

		@Override
		public void onAvatar(Bitmap bitmap) {

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

	void onRankDataResult(RankItem[] ret);

	void onAvatar(Bitmap bitmap);
}
