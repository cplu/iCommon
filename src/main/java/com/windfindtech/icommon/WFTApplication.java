package com.windfindtech.icommon;

import android.app.Application;

import com.windfindtech.icommon.bitmap.DrawableManager;
import com.windfindtech.icommon.error.CrashHandler;
import com.windfindtech.icommon.http.HttpRetroManager;
import com.windfindtech.icommon.logger.WFTLogger;
import com.windfindtech.icommon.mvp.model.MLocalReceiver;
import com.windfindtech.icommon.rx.RXNetworkReceiver;
import com.windfindtech.icommon.util.Utils;
import com.windfindtech.icommon.wifimanager.WFTNetworkMonitor;

/**
 * Created by cplu on 2015/10/22.
 */
public abstract class WFTApplication extends Application{
	public static boolean inForeground = true;

	@Override
	public void onCreate() {
		super.onCreate();

		// set if is in debug mode as earlier as possible
		iCommon.isDebug = getString(R.string.compile_level).equals(iCommon.COMPILE_LEVEL_DEBUG);

		/// init singletons
//		WFTWifiManager.init(this.getApplicationContext());
//		PingManager.init(this.getApplicationContext());
		WFTNetworkMonitor.init(this.getApplicationContext());
//		BroadcastWrapper.init(this.getApplicationContext());
		RXNetworkReceiver.init(this.getApplicationContext());
		MLocalReceiver.init(this.getApplicationContext());
		DrawableManager.init(this.getApplicationContext());
		HttpRetroManager.init(this.getApplicationContext());

		/// init logger level
		WFTLogger.initLogger(this);

		/// application crash handler
		CrashHandler crashHandler = CrashHandler.getInstance();
		crashHandler.init(getApplicationContext());

		Utils.init(this.getApplicationContext());

		iCommon.checkCpuCapability();
	}
}
