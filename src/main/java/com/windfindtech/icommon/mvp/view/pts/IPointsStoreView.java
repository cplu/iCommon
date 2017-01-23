package com.windfindtech.icommon.mvp.view.pts;

import com.windfindtech.icommon.mvp.model.MLocalReceiver;
import com.windfindtech.icommon.mvp.view.IFragmentView;

/**
 * Created by py on 2016/8/23.
 */
public interface IPointsStoreView extends IFragmentView{
    IPointsStoreView dummy=new IPointsStoreView() {


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
}
