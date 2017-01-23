package com.windfindtech.icommon.mvp.model.local;

import com.windfindtech.icommon.jsondata.webservice.VersionCheck;
import com.windfindtech.icommon.mvp.model.MNotifierManager;
import com.windfindtech.icommon.webservice.WSManager;

import rx.Observable;

/**
 * Created by cplu on 2016/7/28.
 * version check manager
 */
public class VersionManager extends MNotifierManager<VersionCheck> {
	public static VersionManager versionNotifier = new VersionManager();

	@SuppressWarnings("unchecked")
	private VersionManager() {
		super();
	}

	public boolean isNewVersionFound() {
		VersionCheck versionCheck = getData();
		return versionCheck != null && !versionCheck.isLatest();
	}

	@Override
	public Observable<VersionCheck> getNetworkObservable() {
		return WSManager.instance().getLeastVersionInfo();
	}
}
