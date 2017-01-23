package com.windfindtech.icommon.mvp.presenter.pts;

import com.windfindtech.icommon.jsondata.points.PointsRecordItem;
import com.windfindtech.icommon.jsondata.points.PointsRecords;
import com.windfindtech.icommon.mvp.presenter.IFragmentPresenter;
import com.windfindtech.icommon.mvp.view.pts.IPointsRecordsView;
import com.windfindtech.icommon.rx.RXLocalSubscriber;
import com.windfindtech.icommon.webservice.WSManager;

import java.util.List;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.observables.GroupedObservable;
import rx.schedulers.Schedulers;

/**
 * Created by py on 2016/8/23.
 */
public class PointsRecordsPresenter extends IFragmentPresenter<IPointsRecordsView> {


	@Override
	protected IPointsRecordsView createDummy() {
		return IPointsRecordsView.dummy;
	}


	@SuppressWarnings("unchecked")
	public void retrieveMyPointHistory() {
		WSManager.instance().httpGet("api/benefit/points_log", PointsRecords.class)
			.observeOn(Schedulers.io())
			.flatMap(new Func1<PointsRecords, Observable<PointsRecordItem>>() {
				@Override
				public Observable<PointsRecordItem> call(PointsRecords pointsRecordsResponse) {
					return Observable.from(pointsRecordsResponse.getData());
				}
			})
			.groupBy(new Func1<PointsRecordItem, String>() {
				@Override
				public String call(PointsRecordItem item) {
					return item.getDirection();
				}
			})
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(new RXLocalSubscriber<GroupedObservable<String, PointsRecordItem>>() {
				@Override
				public void onError(Throwable throwable) {
					/// on error goes here
					getView().onDataGetFailed();
				}

				@Override
				public void onNext(GroupedObservable<String, PointsRecordItem> observable) {
					final String key = observable.getKey();
					observable.observeOn(Schedulers.io())
						.toList()
						.observeOn(AndroidSchedulers.mainThread())
						.subscribe(new RXLocalSubscriber<List<PointsRecordItem>>() {
							@Override
							public void onNext(List<PointsRecordItem> pointsRecordItems) {
								if (key.equals("income")) {
									getView().onIncomeListSuccess(pointsRecordItems);
								} else if (key.equals("outcome")) {
									getView().onOutcomeListSuccess(pointsRecordItems);
								}
							}
						});
				}
			});
	}
}
