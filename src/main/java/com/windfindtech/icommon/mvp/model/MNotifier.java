package com.windfindtech.icommon.mvp.model;

import com.windfindtech.icommon.rx.RXLocalSubscriber;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cplu on 2016/7/27.
 * local cached data
 */
public class MNotifier<Type> {

	private Type m_dataCached;
	private Observable<Type> m_notifier;
	//	private Observable<Response<Type>> m_networkObservable;
	private Subscriber<? super Type> m_subscriber;
	private Subscription m_networkSubscription; /// network subscription used to avoid duplicated request
//	private Subscription m_subscription;

	public MNotifier() {
//		m_networkObservable = networkObservable;
		m_notifier = Observable.create(new Observable.OnSubscribe<Type>() {
			@Override
			public void call(Subscriber<? super Type> subscriber) {
				m_subscriber = subscriber;
//				subscriber.onNext(null);
			}
		})
			.replay(1)      /// replay 1 cached data when new subscriber subscribes
			.autoConnect()
//				.throttleFirst(1000, TimeUnit.MILLISECONDS)
			.subscribeOn(Schedulers.io())
			.observeOn(AndroidSchedulers.mainThread());    /// avoid duplicate item emission within 1 second

		m_notifier.subscribe(); /// trigger subscribe to make m_subscriber available
	}

//	/**
//	 * update data by fetching from network
//	 */
//	public void refreshData() {
//		networkObservable.subscribe();
//	}
//
//	/**
//	 * get data from cache, or from network if no cache
//	 */
//	public void getCachedData() {
//		m_notifier.subscribe();
//	}

	/**
	 * update data from network
	 * if succeeds, store result to {@code m_dataCached} and notify all subscribers
	 */
	public void updateData(Observable<Type> observable) {
		m_networkSubscription = observable.subscribe(new Subscriber<Type>() {
			@Override
			public void onCompleted() {

			}

			@Override
			public void onError(Throwable throwable) {
				/// do not print error since too many errors occur when network failed
//				Logger.error(throwable);
			}

			@Override
			public void onNext(Type typeResponse) {
				m_dataCached = typeResponse;
				m_subscriber.onNext(m_dataCached);
			}
		});
	}

	public void notifySubscribers() {
		if (m_dataCached != null) {
			m_subscriber.onNext(m_dataCached);
		}
	}

	/**
	 * register a data callback
	 * note: this will trigger an update on data if necessary
	 */
	public Subscription register(Observable<Type> observable, RXLocalSubscriber<Type> subscriber, boolean updateForced) {
		Subscription subscription = m_notifier.subscribe(subscriber);
		if (m_dataCached == null    /// no cached data found
		    || updateForced         /// update is forced
			) {
			updateData(observable);
		}
		return subscription;
	}

	public void unregister(Subscription subscription) {
		if (subscription != null && !subscription.isUnsubscribed()) {
			subscription.unsubscribe();
		}
	}

	public void clearData() {
		m_dataCached = null;
	}

	public Type getData() {
		return m_dataCached != null ? m_dataCached : null;
	}

	public void setData(Type data) {
		m_dataCached = data;
	}

	public Type getOrUpdateData(Observable<Type> observable) {
		if (m_dataCached != null) {
			return m_dataCached;
		} else {
			updateData(observable);
			return null;
		}
	}

	/**
	 * subscribe immediately with an update and get callback in subscriber
	 *
	 * @param subscriber subscriber to receive result of updating
	 */
	public void subscribeNow(Observable<Type> observable, RXLocalSubscriber<Type> subscriber) {
		observable.subscribe(subscriber);
	}
}
