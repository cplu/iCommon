package com.windfindtech.icommon.mvp.presenter.activity;

import android.content.Intent;
import android.net.ConnectivityManager;

import com.windfindtech.icommon.config.SharedAuthInfo;
import com.windfindtech.icommon.config.WFTConfig;
import com.windfindtech.icommon.jsondata.enumtype.AdvertSource;
import com.windfindtech.icommon.jsondata.webservice.LoginResponse;
import com.windfindtech.icommon.jsondata.webservice.LogoutResponse;
import com.windfindtech.icommon.jsondata.webservice.VersionCheck;
import com.windfindtech.icommon.jsondata.webservice.WSErrorResponse;
import com.windfindtech.icommon.mvp.model.MLocalReceiver;
import com.windfindtech.icommon.mvp.model.local.DeviceManager;
import com.windfindtech.icommon.mvp.model.local.ReservedMessageManager;
import com.windfindtech.icommon.mvp.model.local.VersionManager;
import com.windfindtech.icommon.mvp.presenter.IActivityPresenter;
import com.windfindtech.icommon.mvp.view.activity.IMainView;
import com.windfindtech.icommon.rx.RXIntervalSubscriber;
import com.windfindtech.icommon.rx.RXLocalSubscriber;
import com.windfindtech.icommon.rx.RXNetworkReceiver;
import com.windfindtech.icommon.rx.RXNetworkSubscriber;
import com.windfindtech.icommon.rx.RXTimerTask;
import com.windfindtech.icommon.rx.SingleTask;
import com.windfindtech.icommon.util.Gather;
import com.windfindtech.icommon.util.Utils;
import com.windfindtech.icommon.webservice.WSCallback;
import com.windfindtech.icommon.webservice.WSManager;
import com.windfindtech.icommon.wifimanager.CheckCallback;
import com.windfindtech.icommon.wifimanager.WFTErrorCode;
import com.windfindtech.icommon.wifimanager.WFTNetworkInterface;
import com.windfindtech.icommon.wifimanager.WFTNetworkMonitor;
import com.windfindtech.icommon.wifimanager.WFTSuccessCode;
import com.windfindtech.icommon.wifimanager.WifiAuth;
import com.windfindtech.icommon.wifimanager.WisprCallback;

import org.pmw.tinylog.Logger;

import rx.Subscription;

/**
 * Created by cplu on 2016/7/26.
 * Presenter for main activity
 * Warning: this should only be used by Activity, instead of Fragment
 */
public class MainPresenter extends IActivityPresenter<IMainView> {
	private static final long VERSION_CHECK_TIME_SPAN = 6 * 60 * 60 * 1000; // 6 hours
	private static boolean bShowAdOnNonWFT = true; /// show ad on non WFT, only once since app restart

	private Subscription versionCheckSubscriber;

	private Subscription networkBroadcastSubscriber;

	private static final int CHECK_TIME_PERIOD = 5 * 60 * 1000;
	private Subscription timerSubscription;
	private boolean m_enableAd = false;

	public MainPresenter() {
	}

	@Override
	public void attach(IMainView view, int notifiers) {
		super.attach(view, notifiers);

		networkBroadcastSubscriber = RXNetworkReceiver.register(new RXNetworkSubscriber() {
			@Override
			public void onNext(Intent intent) {
				Logger.debug(getClass().getSimpleName() + " network changed.");
				//m_ws_login_status = WSLoginStatus.logged_out;
				String action = intent.getAction();
				if (action.equals(ConnectivityManager.CONNECTIVITY_ACTION)) {
					Logger.debug("intent: " + intent.toString() + " " + intent.getExtras());
					try {
						/// update network info in stats
						Gather.instance().updateInfoNetwork();
					} catch (Exception e) {
						Logger.error(e);
					}

					String ssid = WFTNetworkMonitor.instance().getCurrentSsid();
					WSManager.instance().resetBySSID(ssid);
					ensureUserAuthInfoAndCheckNetwork();
				}
			}
		});

		versionCheckSubscriber = VersionManager.versionNotifier.register(new RXLocalSubscriber<VersionCheck>() {
			@Override
			public void onNext(VersionCheck response) {
				try {
					parseVersionCheck(response, true);
				} catch (Exception e) {
					Logger.error(e);
				}
			}
		}, false);

		timerSubscription = RXTimerTask.createAndStart(CHECK_TIME_PERIOD, new RXIntervalSubscriber() {
			@Override
			public void onNext(Long delta) {
				ensureUserAuthInfoAndCheckNetwork();
			}
		});
	}

	@Override
	protected IMainView createDummy() {
		return IMainView.dummy;
	}

	@Override
	public void detach() {
		RXNetworkReceiver.unregister(networkBroadcastSubscriber);
		VersionManager.versionNotifier.unregister(versionCheckSubscriber);

		RXTimerTask.stop(timerSubscription);

		super.detach();
	}

	/**
	 * do webservice login
	 */
	public void doWSLogin(boolean quickLogin) {
		if (m_wsStatus != MLocalReceiver.WS_NOT_LOGGED_IN) {
			/// already logged in or in progress
			Logger.debug("web service already logged in or in progress");
			return;
		}
		if (!checkIfPhoneNumberExists(false)) {
			return;
		}
		getView().onWSLoginInProgress();
		MLocalReceiver.notifyWebservice(MLocalReceiver.WS_IN_PROGRESS);
		WSManager.instance().doLogIn(
			WFTConfig.instance().getPhoneNumber(),
			WFTConfig.instance().getPassword(),
			quickLogin,
			new WSCallback<LoginResponse>() {
				@Override
				public void onSuccess(LoginResponse ret) {
					/// ret is the code of logIn return, "MASTER" means main device
					//logger.debug("web service log in success");
					//iCommon.showToast(ISHMainContentActivity.this, getString(R.string.webservice_login_success));
					try {
						Gather.instance().setInfoUser(WFTConfig.instance().getPhoneNumber());
						getView().onWSLoginSuccess(ret);
						WFTConfig.instance().setMaster(ret.isMaster());
						MLocalReceiver.notifyWebservice(MLocalReceiver.WS_LOGGED_IN);
//							VersionManager.versionNotifier.updateData();
//							parseVersionCheck(ret.getVersion(), true);

						VersionManager.versionNotifier.updateData();
						DeviceManager.deviceNotifier.updateData();
						if (ret.isMaster()) {
							/// only master device can query user reserved message
							ReservedMessageManager.reservedMsgNotifier.updateData();
						}
					} catch (Exception e) {
						Logger.error(e);
						getView().onWSLoginException(ret);
						MLocalReceiver.notifyWebservice(MLocalReceiver.WS_NOT_LOGGED_IN);
					}
				}

				@Override
				public void onFailed(WSErrorResponse reason) {
					WFTConfig.instance().setMaster(false);
					getView().onWSLoginFailed(reason);
					MLocalReceiver.notifyWebservice(MLocalReceiver.WS_NOT_LOGGED_IN);
				}
			});
	}

	/**
	 * do webservice logout
	 */
	public void doWSLogout() {
		WSManager.instance().doLogOut(new WSCallback<LogoutResponse>() {
			@Override
			public void onSuccess(LogoutResponse ret) {
//                finish_this();
			}

			@Override
			public void onFailed(WSErrorResponse reason) {

			}
		});
		MLocalReceiver.notifyWebservice(MLocalReceiver.WS_NOT_LOGGED_IN);
	}

	/**
	 * Check if current phone number exists
	 *
	 * @param wifi_auth_required true if app is now trying to log in to wifi, false if app is just want to log in to webservice
	 * @return true if current phone number exists, false otherwise
	 */
	public boolean checkIfPhoneNumberExists(boolean wifi_auth_required) {
		String phone_number = WFTConfig.instance().getPhoneNumber();
		String password = WFTConfig.instance().getPassword();
		if (phone_number == null || phone_number.isEmpty() || password == null ||
		    password.isEmpty()) {
			getView().onNoAuthenticationInfo(wifi_auth_required);
			return false;
		} else {
			return true;
		}
	}

	/**
	 * @param version_check
	 * @param considerTimeElapsed whether we should consider time elapsed if new version is found, but app is not forced to update
	 * @return true only if new version is found and we are forced to update
	 * @throws Exception
	 */
	public void parseVersionCheck(final VersionCheck version_check, boolean considerTimeElapsed) throws Exception {
		if (!version_check.isLatest()) {
			if (version_check.isForce()) {
				if (version_check.getDownloadUrl() != null) {
					getView().onNewVersionFound(version_check, true);
				}
				return;
			} else {
				long last_checking_time = Utils.getLong(Utils.VERSION_CHECK_DATE_KEY, 0);
				final long currentTimeMS = System.currentTimeMillis();
				if (considerTimeElapsed &&
				    currentTimeMS - last_checking_time < VERSION_CHECK_TIME_SPAN) {
					/// not enough time elapsed
					return;
				}
				if (version_check.getDownloadUrl() != null) {
					getView().onNewVersionFound(version_check, false);
				}

				Utils.saveLong(Utils.VERSION_CHECK_DATE_KEY, currentTimeMS);
				return;
			}
		}
		return;
	}

	private CheckCallback m_checkCallback = new CheckCallback() {
		@Override
		public void onSuccess(WFTSuccessCode code) {
			MLocalReceiver.notifyNetwork(MLocalReceiver.NETWORK_OK);
			if (m_enableAd) {
				if (bShowAdOnNonWFT) {
					bShowAdOnNonWFT = false;
					if (code == WFTSuccessCode.WIFI_CONNECTED    /// wifi but not i-WFT
					    || code == WFTSuccessCode.WWLAN_CONNECTED   /// 2G/3G/4G
						) {
						/// show ad on non-i-Shanghai wifi or wwlan
						/// only once since app restarted
						getView().prepareAdvertisement(AdvertSource.FROM_OTHER);
					} else if (code == WFTSuccessCode.WFT_ALREADY_CONNECTED) {
						/// i-WFT already connected, no need to connect again
						/// only once since app restarted
						getView().prepareAdvertisement(AdvertSource.FROM_WFT);
					}
				}
			}
			if (checkIfPhoneNumberExists(false)) {
				doWSLogin(true);
			}
		}

		@Override
		public void onFailed(WFTErrorCode code, String message) {
			Logger.debug("WifiActionHandler - onFailed is called!");
//			dismissCheckingNetworkDlg(false);
//            if(code == WFTErrorCode.DUPLICATE_ATTEMPT
//               || code == WFTErrorCode.SUCCESS_ALREADY
//                    ){
//                /// do not do anything if duplicated
//                return;
//            }
			if(code == WFTErrorCode.CHECKING_NETWORK_DUPLICATED) {
				/// do not do anything if duplicated
				return;
			}
			MLocalReceiver.notifyNetwork(MLocalReceiver.NETWORK_FAILED);
			MLocalReceiver.notifyWebservice(MLocalReceiver.WS_NOT_LOGGED_IN);
//	        change_ui_according_to_network_status(BroadcastWrapper.NETWORK_FAILED);
			getView().onNetworkCheckFailed();
		}
	};

	/**
	 *
	 */
	private void ensureUserAuthInfoAndCheckNetwork() {
		if(WFTConfig.instance().prepareUserAuthInfo(new SingleTask<SharedAuthInfo>() {
			@Override
			public void onSuccess(SharedAuthInfo tuple) {
				checkNetworkStatus();
			}
		})) {
			checkNetworkStatus();
		}
	}

	/**
	 * check current network and a status callback is triggered
	 */
	private void checkNetworkStatus() {
		try {
			Logger.debug("ensureUserAuthInfoAndCheckNetwork");
			/// clearAll ish nearby hint
			/// this will be set only when app is not connnected to i-Shanghai and there's nearby i-Shanghai found

//			int status = BroadcastWrapper.WIFI_NONE;
//			if (network_interface == WFTNetworkMonitor.ISHNetworkInterface.WIFI) {
//				status = BroadcastWrapper.WIFI_OTHER;
//			}
//			else if (network_interface == WFTNetworkMonitor.ISHNetworkInterface.IWindfind) {
//				status = BroadcastWrapper.WIFI_WFT;
//			}

			WFTNetworkInterface network_interface = WFTNetworkMonitor.instance().getCurrentNetInterface();
			MLocalReceiver.notifyWIFI(network_interface);
			WifiAuth.instance().doCheckNetwork(m_checkCallback, network_interface);
//            doWifiOp(WFTWifiManager.OP.OP_CHECK, null, null, m_status_callback);
		} catch (Exception e) {
			Logger.warn(e);
		}
	}

	/**
	 * start wifi authentication
	 */
	public void tryWifiAuthenticate() {
		if (!checkIfPhoneNumberExists(true)) {
			return;
		}
		getView().onWifiAuthInProgress();
		WifiAuth.instance().wisprLogin(WFTConfig.instance().getPhoneNumber(),
			WFTConfig.instance().getPassword(),
			false, new WisprCallback() {
				@Override
				public void onSuccess(WFTSuccessCode code) {
					getView().onWifiAuthSuccess();
					if (code != WFTSuccessCode.WFT_ALREADY_CONNECTED) {
						if (m_enableAd && code == WFTSuccessCode.AUTHENTICATION_SUCCESS) {
							getView().prepareAdvertisement(AdvertSource.FROM_WFT);
						}
					}
					doWSLogin(true);
				}

				@Override
				public void onFailed(WFTErrorCode code, String message) {
					if(code == WFTErrorCode.LOG_IN_DUPLICATED) {
						getView().onWifiAuthInProgress();
					} else {
						getView().onWifiAuthenticationFailed(code, message);
					}
				}
			});
	}

	public void tryWifiLogoff() {
		WifiAuth.instance().logOff();
	}

	/**
	 * related view onDestroy
	 */
	public void onDestroy() {
		MLocalReceiver.notifyWebservice(MLocalReceiver.WS_NOT_LOGGED_IN);
	}

	public void setAdvertisementEnabled(boolean enableAd) {
		m_enableAd = enableAd;
	}

	public void doOnAuthSuccess(WFTSuccessCode code) {
		Gather.instance().setInfoUser(WFTConfig.instance().getPhoneNumber());
		if (m_enableAd && code == WFTSuccessCode.AUTHENTICATION_SUCCESS) {
			getView().prepareAdvertisement(AdvertSource.FROM_WFT);
		}
	}
}
