package com.windfindtech.icommon.mvp.model;

import com.windfindtech.icommon.rx.RXLocalSubscriber;

import rx.Observable;
import rx.Subscription;

/**
 * Created by cplu on 2016/7/28.
 */
public interface MNotifierProxy<Type> {
	void updateData();

	void notifySubscribers();

	/**
	 * register a data callback
	 * @param subscriber
	 * @param updateForced  this will trigger an update on data if necessary
	 * @return  subscription for unregister
	 */
	Subscription register(RXLocalSubscriber<Type> subscriber, boolean updateForced);

	void unregister(Subscription subscription);

	void clearData();

	Type getData();

	void setData(Type data);

	Type getOrUpdateData();

	void subscribeNow(RXLocalSubscriber<Type> subscriber);

	Observable<Type> getNetworkObservable();
}
