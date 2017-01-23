package com.windfindtech.icommon.http;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cplu on 2016/7/21.
 * apply subscribeOn/observeOn/... on observable
 * this will not change the type of observable
 */
public class HttpTransformer<T> implements Observable.Transformer<T, T> {

	private static final HttpTransformer instance = new HttpTransformer();

	@SuppressWarnings("unchecked")
	public static <T> HttpTransformer<T> get() {
		return (HttpTransformer<T>) instance;
	}

	@Override
	public Observable<T> call(Observable<T> observable) {
		return observable.subscribeOn(Schedulers.io())  /// set subscription to be run on io thread
			.observeOn(AndroidSchedulers.mainThread()) /// next chains are called on main thread;
			.unsubscribeOn(Schedulers.io());
	}
}
