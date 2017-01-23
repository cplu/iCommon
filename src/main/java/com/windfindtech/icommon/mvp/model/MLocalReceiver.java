package com.windfindtech.icommon.mvp.model;

import android.content.Context;
import android.support.annotation.IntDef;

import com.windfindtech.icommon.rx.RXLocalSubscriber;
import com.windfindtech.icommon.wifimanager.WFTNetworkInterface;

import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cplu on 2016/7/26.
 */
public class MLocalReceiver {
	private static final int RECEIVER_COUNT = 3;
	private static final Observable[] s_localObservables = new Observable[RECEIVER_COUNT];
	private static final Subscriber[] s_localSubscribers = new Subscriber[RECEIVER_COUNT];    /// store ReplaySubscribers
	private static Context m_ctx;

	private static final int IDX_NETWORK = 0;
	private static final int IDX_WIFI = 1;
	private static final int IDX_WS = 2;

	@IntDef({NETWORK_FAILED, NETWORK_OK, NETWORK_CONNECTING})
	@Retention(RetentionPolicy.SOURCE)
	public @interface NetworkStatus {
	}

	public static final int NETWORK_FAILED = 0;
	public static final int NETWORK_OK = 1;
	public static final int NETWORK_CONNECTING = 2;

	@IntDef({WIFI_NONE, WIFI_ISH, WIFI_IGD, WIFI_IPD, WIFI_OTHER, WIFI_INTERNAL})
	@Retention(RetentionPolicy.SOURCE)
	public @interface WifiStatus {
	}

	public static final int WIFI_NONE = 0;
	public static final int WIFI_ISH = 1;   /// i-Shanghai
	public static final int WIFI_IGD = 2;   /// i-Guangdong
	public static final int WIFI_IPD = 3;   /// i-PudongFree
	public static final int WIFI_OTHER = 4;
	public static final int WIFI_INTERNAL = 5;  /// internal test

	@IntDef({WS_NOT_LOGGED_IN, WS_LOGGED_IN, WS_IN_PROGRESS})
	@Retention(RetentionPolicy.SOURCE)
	public @interface WebserviceStatus {
	}

	public static final int WS_NOT_LOGGED_IN = 0;
	public static final int WS_LOGGED_IN = 1;
	public static final int WS_IN_PROGRESS = 2;

	private static final Object[] s_defaultValues = {
		NETWORK_FAILED,     /// network status
		WIFI_NONE,          /// wifi status
		WS_NOT_LOGGED_IN,   /// webservice status
	};

	public static void init(Context ctx) {
		m_ctx = ctx;

		for (int i = 0; i < RECEIVER_COUNT; i++) {
			s_localObservables[i] = Observable.create(new ReplayOnSubscriber(i))
				.distinctUntilChanged()
				.replay(1)
				.autoConnect()
				.subscribeOn(Schedulers.io())
				.observeOn(AndroidSchedulers.mainThread())
				.defaultIfEmpty(s_defaultValues[i]);

			s_localObservables[i].subscribe();

		}
	}

	public static Subscription registerWebservice(RXLocalSubscriber<Integer> subscriber) {
		return s_localObservables[IDX_WS].subscribe(subscriber);
	}

	public static void unregisterWebservice(Subscription subscription) {
		if (subscription != null && !subscription.isUnsubscribed()) {
			subscription.unsubscribe();
		}
	}

	public static void notifyWebservice(int value) {
		s_localSubscribers[IDX_WS].onNext(value);
	}

	public static Subscription registerNetwork(RXLocalSubscriber<Integer> subscriber) {
		return s_localObservables[IDX_NETWORK].subscribe(subscriber);
	}

	public static void unregisterNetwork(Subscription subscriber) {
		subscriber.unsubscribe();
	}

	public static void notifyNetwork(int value) {
		s_localSubscribers[IDX_NETWORK].onNext(value);
	}

	public static Subscription registerWIFI(RXLocalSubscriber<Integer> subscriber) {
		return s_localObservables[IDX_WIFI].subscribe(subscriber);
	}

	public static void unregisterWIFI(Subscription subscriber) {
		subscriber.unsubscribe();
	}

	public static void notifyWIFI(int value) {
		s_localSubscribers[IDX_WIFI].onNext(value);
	}

	public static void notifyWIFI(WFTNetworkInterface nw_interface) {
		notifyWIFI(nw_interface.getValue());
//		if (nw_interface == WFTNetworkInterface.WIFI_OTHER) {
//			notifyWIFI(WIFI_OTHER);
//		} else if (nw_interface == WFTNetworkInterface.ISHANGHAI) {
//			notifyWIFI(WIFI_ISH);
//		} else if (nw_interface == WFTNetworkInterface.IGUANGDONG) {
//			notifyWIFI(WIFI_IGD);
//		} else if (nw_interface == WFTNetworkInterface.IPUDONG) {
//			notifyWIFI(WIFI_IPD);
//		} else {
//			notifyWIFI(WIFI_NONE);
//		}
	}

	private static class ReplayOnSubscriber implements Observable.OnSubscribe<Object> {
		private final int m_index;

		public ReplayOnSubscriber(int index) {
			this.m_index = index;
		}

		@Override
		public void call(Subscriber<? super Object> subscriber) {
			s_localSubscribers[m_index] = subscriber;
		}
	}
}
