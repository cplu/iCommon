package com.windfindtech.icommon.mvp.model.local;

import com.windfindtech.icommon.mvp.model.MNotifierManager;
import com.windfindtech.icommon.webservice.WSManager;

import rx.Observable;

/**
 * Created by cplu on 2016/7/28.
 */
public class ReservedMessageManager extends MNotifierManager<Boolean> {
	public static ReservedMessageManager reservedMsgNotifier = new ReservedMessageManager();

	@SuppressWarnings("unchecked")
	private ReservedMessageManager() {
		super();
	}

	public boolean isMessageRequired() {
		return getData() != null && !getData();
	}

	@Override
	public Observable<Boolean> getNetworkObservable() {
		return WSManager.instance().doHasPersonalMessage();
	}
}
