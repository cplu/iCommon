package com.windfindtech.icommon.mvp.view.pts;

import com.windfindtech.icommon.jsondata.points.PointsRecordItem;
import com.windfindtech.icommon.mvp.model.MLocalReceiver;
import com.windfindtech.icommon.mvp.view.IFragmentView;

import java.util.List;

/**
 * Created by py on 2016/8/23.
 */
public interface IPointsRecordsView extends IFragmentView{
    IPointsRecordsView dummy=new IPointsRecordsView() {

        @Override
        public void onDataGetFailed() {

        }

        @Override
        public void onIncomeListSuccess(List<PointsRecordItem> pointsRecordItems) {

        }

        @Override
        public void onOutcomeListSuccess(List<PointsRecordItem> pointsRecordItems) {

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

    void onDataGetFailed();

    void onIncomeListSuccess(List<PointsRecordItem> pointsRecordItems);

    void onOutcomeListSuccess(List<PointsRecordItem> pointsRecordItems);
}
