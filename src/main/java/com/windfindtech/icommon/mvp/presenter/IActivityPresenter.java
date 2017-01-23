package com.windfindtech.icommon.mvp.presenter;

import com.windfindtech.icommon.mvp.model.MLocalReceiver;
import com.windfindtech.icommon.mvp.model.MType;
import com.windfindtech.icommon.mvp.view.IActivityView;
import com.windfindtech.icommon.rx.RXLocalSubscriber;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import rx.Subscription;

/**
 * Created by cplu on 2016/7/26.
 */
public abstract class IActivityPresenter<ViewType extends IActivityView> {

	private WeakReference<ViewType> m_iView;
	protected ViewType m_dummy;

	protected ArrayList<Subscription> m_subscriptions = new ArrayList<>();

	@MLocalReceiver.WebserviceStatus
	protected int m_wsStatus;
	@MLocalReceiver.NetworkStatus
	protected int m_networkStatus;

	protected abstract ViewType createDummy();

	protected ViewType getView() {
		ViewType view = m_iView.get();
		return view != null ? view : m_dummy;
	}

	/**
	 * register all receivers, including local receivers and network receivers
	 * create weak reference
	 * @param view
	 */
	public void attach(ViewType view, int notifiers) {
		m_iView = new WeakReference<>(view);
		m_dummy = createDummy();

		if ((notifiers & MType.NETWORK_RECEIVER) > 0) {
			m_subscriptions.add(MLocalReceiver.registerNetwork(new RXLocalSubscriber<Integer>() {
				@Override
				public void onNext(Integer integer) {
					m_networkStatus = integer;
					getView().onNetworkChanged(integer);
				}
			}));
		}
		if ((notifiers & MType.WIFI_RECEIVER) > 0) {
			m_subscriptions.add(MLocalReceiver.registerWIFI(new RXLocalSubscriber<Integer>() {
				@Override
				public void onNext(Integer integer) {
					getView().onWifiChanged(integer);
				}
			}));
		}
		if ((notifiers & MType.WS_RECEIVER) > 0) {
			m_subscriptions.add(MLocalReceiver.registerWebservice(new RXLocalSubscriber<Integer>() {
				@Override
				public void onNext(Integer integer) {
					m_wsStatus = integer;
					getView().onWSChanged(integer);
				}
			}));
		}
	}

	/**
	 * unregister all receivers
	 * clear weak reference
	 */
	public void detach() {
		for (Subscription subscription : m_subscriptions) {
			if (subscription != null && !subscription.isUnsubscribed()) {
				subscription.unsubscribe();
			}
		}
		m_subscriptions.clear();

		if (m_iView != null) {
			m_iView.clear();
//			m_iView = null;
		}
	}

	/**
	 * check if is now connecting
	 * @return
	 */
	public boolean isConnecting() {
		return (m_networkStatus == MLocalReceiver.NETWORK_CONNECTING) ||
		       (m_wsStatus == MLocalReceiver.WS_IN_PROGRESS);
	}
}
