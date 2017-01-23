package com.windfindtech.icommon.mvp.presenter;

import com.windfindtech.icommon.mvp.model.MLocalReceiver;
import com.windfindtech.icommon.mvp.model.MType;
import com.windfindtech.icommon.mvp.view.IFragmentView;
import com.windfindtech.icommon.rx.RXLocalSubscriber;

import java.lang.ref.WeakReference;
import java.util.ArrayList;

import rx.Subscription;

/**
 * Created by cplu on 2016/7/26.
 * Presenter for base fragment
 * including common functions for all fragments
 */
public abstract class IFragmentPresenter<ViewType extends IFragmentView> {

	protected WeakReference<ViewType> m_iView;
	protected ViewType m_dummy;

	@MLocalReceiver.WebserviceStatus
	protected int m_wsStatus;
	@MLocalReceiver.WifiStatus
	protected int m_wifiStatus;
	@MLocalReceiver.NetworkStatus
	protected int m_networkStatus;

	protected ArrayList<Subscription> m_subscriptions = new ArrayList<>();

	protected abstract ViewType createDummy();

	protected ViewType getView() {
		ViewType view = m_iView.get();
		return view != null ? view : m_dummy;
	}

	/**
	 * register notifiers
	 *
	 * @param notifiers one or more values from MType
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
					m_wifiStatus = integer;
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
	 * unregister all notifiers
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
	 * get webservice status
	 *
	 * @return
	 */
	public int getWSStatus() {
		return m_wsStatus;
	}

	/**
	 * get network status
	 *
	 * @return
	 */
	public int getNetworkStatus() {
		return m_networkStatus;
	}

	/**
	 * get wifi status
	 * @return
	 */
	public int getWifiStatus() {
		return m_wifiStatus;
	}
}
