package com.windfindtech.icommon.mvp.model.local;

import com.windfindtech.icommon.jsondata.webservice.AQISiteData;
import com.windfindtech.icommon.mvp.model.MNotifierManager;
import com.windfindtech.icommon.webservice.WSManager;

import rx.Observable;

/**
 * Created by cplu on 2016/8/5.
 */
public class AQIManager extends MNotifierManager<AQISiteData> {

	public static AQIManager aqiNotifier = new AQIManager();

	private static final int AQI_FETCHING_TIME_GAP = 30 * 60 * 1000;    /// 30 minutes


	/**
	 * create a manager for AQISiteData
	 */
	public AQIManager() {
		super();
	}

	@Override
	public Observable<AQISiteData> getNetworkObservable() {
		return WSManager.instance().httpGet("api/info/air_quality",
				AQISiteData.class, null, AQI_FETCHING_TIME_GAP, true);
	}
}
