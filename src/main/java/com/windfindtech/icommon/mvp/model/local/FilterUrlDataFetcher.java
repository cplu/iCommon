package com.windfindtech.icommon.mvp.model.local;

import android.graphics.Bitmap;

import com.windfindtech.icommon.rx.RXLocalSubscriber;
import com.windfindtech.icommon.webservice.WSManager;

import org.pmw.tinylog.Logger;

import java.util.concurrent.ConcurrentHashMap;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


/**
 * 过滤重复url请求的manager
 * Created by py on 2016/12/13.
 */
public class FilterUrlDataFetcher {

	private static final int ICON_WIDTH_LIMIT = 180;
	private static final int ICON_HEIGHT_LIMIT = 180;
	private static final int MAX_URL_MAP_SIZE = 32;
	public static ConcurrentHashMap<String, FilterUrlDataFetcher> s_urlMaps = new ConcurrentHashMap<>();
	private String m_iconUrl;
	//    private Type m_dataCached;
//    private Bitmap m_dataCached;
	private Observable<Bitmap> m_notifier;
	//	private Observable<Response<Type>> m_networkObservable;
	private Subscriber<? super Bitmap> m_subscriber;
	//    private Subscription m_Subscription; /// network subscription used to avoid duplicated request
	//	private Subscription m_subscription;
	private Subscription m_subscription;

	public String getIconUrl() {
		return m_iconUrl;
	}

	public void setIconUrl(String m_iconUrl) {
		this.m_iconUrl = m_iconUrl;
	}

	public Observable<Bitmap> getNotifier() {
		return m_notifier;
	}

	public void setNotifier(Observable<Bitmap> m_notifier) {
		this.m_notifier = m_notifier;
	}

	public FilterUrlDataFetcher() {
//        if (s_urlMaps == null) {
//            s_urlMaps = new ConcurrentHashMap<>();
//        }
	}

	private void initNotifier() {
		m_notifier = Observable.create(new Observable.OnSubscribe<Bitmap>() {
			@Override
			public void call(Subscriber<? super Bitmap> subscriber) {
				m_subscriber = subscriber;
			}
		})
//				.replay(1)      /// replay 1 cached data when new subscriber subscribes
//				.autoConnect()
//				.throttleFirst(1000, TimeUnit.MILLISECONDS)
//              .share()
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread());    /// avoid duplicate item emission within 1 second

		m_notifier.subscribe(); /// trigger subscribe to make m_subscriber available
	}


	private Observable<Bitmap> getObservable(String url) {
		return WSManager.instance().getImageObservable(url, true, ICON_WIDTH_LIMIT, ICON_HEIGHT_LIMIT);
	}

	/**
	 * register a url data fetcher
	 */
	public Subscription register(Observable<Bitmap> observable, RXLocalSubscriber<Bitmap> subscriber) {
		Subscription subscription = m_notifier.subscribe(subscriber);
		if (m_subscription == null || m_subscription.isUnsubscribed()) {
			m_subscription = observable.subscribe(new Subscriber<Bitmap>() {
				@Override
				public void onCompleted() {

				}

				@Override
				public void onError(Throwable throwable) {
					/// do not print error since too many errors occur when network failed
//				Logger.error(throwable);
				}

				@Override
				public void onNext(Bitmap bitmap) {
//                    m_dataCached = typeResponse.body();
					m_subscriber.onNext(bitmap);
					m_subscriber.onCompleted();
					Logger.debug("FilterManager:" + "updateData()");
				}
			});

		}
		return subscription;
	}

//    public static void unregister(Subscription subscription) {
//        if (subscription != null && !subscription.isUnsubscribed()) {
//            subscription.unsubscribe();
//        }
//    }

//    public void clearData() {
//        m_dataCached = null;
//    }
//
//    public Type getData() {
//        return m_dataCached != null ? m_dataCached : null;
//    }
//
//    public void setData(Type data) {
//        m_dataCached = data;
//    }


//    public Type getOrUpdateData(Observable<Response<Type>> observable, RXLocalSubscriber<Type> subscriber) {
//        if (m_normalCached != null) {
//            return m_dataCached;
//        } else {
//            updateData(observable, subscriber);
//            return null;
//        }
//    }

	public static Subscription registerUrl(String iconUrl, RXLocalSubscriber<Bitmap> subscriber) {
		FilterUrlDataFetcher dataFetcher = initFilterUrlData(iconUrl);
		return dataFetcher.register(dataFetcher.getObservable(iconUrl), subscriber);
	}

	private static FilterUrlDataFetcher initFilterUrlData(String iconUrl) {
		FilterUrlDataFetcher filterUrlData = s_urlMaps.get(iconUrl);
		if (filterUrlData != null) {
			return filterUrlData;
		} else {
			filterUrlData = new FilterUrlDataFetcher();
			filterUrlData.initNotifier();
			filterUrlData.setIconUrl(iconUrl);
			if (s_urlMaps.size() > MAX_URL_MAP_SIZE) {
				s_urlMaps.clear();
			}
			s_urlMaps.put(iconUrl, filterUrlData);
			return filterUrlData;
		}
	}
}
