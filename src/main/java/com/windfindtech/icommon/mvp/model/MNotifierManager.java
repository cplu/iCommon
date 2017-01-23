package com.windfindtech.icommon.mvp.model;

import com.windfindtech.icommon.rx.RXLocalSubscriber;

import retrofit2.Response;
import rx.Subscription;

/**
 * Created by cplu on 2016/7/28.
 */
public abstract class MNotifierManager<Type> implements MNotifierProxy<Type> {

	protected MNotifier<Type> m_mNotifier;

	public MNotifierManager(){
		m_mNotifier = new MNotifier<>();
	}

	@Override
	public void updateData() {
		m_mNotifier.updateData(getNetworkObservable());
	}

	@Override
	public void notifySubscribers() {
		m_mNotifier.notifySubscribers();
	}

	@Override
	public Subscription register(RXLocalSubscriber<Type> subscriber, boolean updateForced) {
		return m_mNotifier.register(getNetworkObservable(), subscriber, updateForced);
	}

	@Override
	public void unregister(Subscription subscription) {
		m_mNotifier.unregister(subscription);
	}

	@Override
	public void clearData() {
		m_mNotifier.clearData();
	}

	@Override
	public Type getData() {
		return m_mNotifier.getData();
	}

	@Override
	public void setData(Type data) {
		m_mNotifier.setData(data);
	}


	@Override
	public Type getOrUpdateData() {
		return m_mNotifier.getOrUpdateData(getNetworkObservable());
	}

	@Override
	public void subscribeNow(RXLocalSubscriber<Type> subscriber) {
		m_mNotifier.subscribeNow(getNetworkObservable(), subscriber);
	}
}
