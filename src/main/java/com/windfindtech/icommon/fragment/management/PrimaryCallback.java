package com.windfindtech.icommon.fragment.management;

import android.os.Parcelable;

import com.windfindtech.icommon.jsondata.webservice.VersionCheck;

/**
 * Created by cplu on 2015/12/22.
 */
public interface PrimaryCallback {
	void onForward(String tag);
	void onForward(String tag, int[] animations);
	void onForward(String tag, Parcelable params, int[] animations);
	void onForward(String tag, Parcelable[] params, int[] animations);
	void onForward(String tag, String[] params, int[] animations);
	void onForwardWebView(String url, String title);
	void onNaviBack();
	void onPopbackToRoot();
	void onWsNotLoggedIn();
	void onReconnect();
	void onLogout();
	void onVersionUpdateRequired(VersionCheck ret);
	void onShowBottomToast(String text, int duration);
	void onStoreData(String key, Object data);
	Object getData(String key);
}
