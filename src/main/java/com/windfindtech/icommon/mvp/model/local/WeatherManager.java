package com.windfindtech.icommon.mvp.model.local;

import com.windfindtech.icommon.jsondata.webservice.WeatherInfo;
import com.windfindtech.icommon.mvp.model.MNotifierManager;
import com.windfindtech.icommon.webservice.WSManager;

import rx.Observable;

/**
 * Created by cplu on 2015/9/11.
 */
public class WeatherManager extends MNotifierManager<WeatherInfo>{

	public static WeatherManager weatherNotifier = new WeatherManager();

	private static final int WEATHER_FETCHING_TIME_GAP = 10 * 60 * 1000;    /// 10 minutes

	public static final String city = "shanghai";

	/**
	 * create a manager for WeatherInfo
	 */
	public WeatherManager() {
		super();
	}

	@Override
	public Observable<WeatherInfo> getNetworkObservable() {
		return WSManager.instance().httpGet(String.format("api/info/weather/%s/details", city),
				WeatherInfo.class, null, WEATHER_FETCHING_TIME_GAP, true);
	}
}
