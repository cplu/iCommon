package com.windfindtech.icommon.config;

import android.content.Context;
import android.content.SharedPreferences;

import com.windfindtech.icommon.rx.RXThreadTask;
import com.windfindtech.icommon.rx.SingleTask;
import com.windfindtech.icommon.util.SimpleCrypto;

import org.pmw.tinylog.Logger;

import rx.Scheduler;
import rx.schedulers.Schedulers;

/**
 * 2016/11/03
 * use ContentProvider to store user auth info, and consider backward compatibility
 * <p>
 * 2016/05/19
 * moved and renamed from iShanghai to iCommon
 * This is the configuration of current user.
 * 2015/12/11
 * Note: For backward compatibility, SharedPreferences in this class should not be removed or replaced by Utils.saveXXX/Utils.getXXX
 *
 * @author cplu
 *         modified since 2014/07/29
 */
public class WFTConfig {
	/// prefix for SharedPreferences, if no suffix, it stores the latest phone number
	/// suffix is the phone number of user
	public static String CONF_NAME_PREFIX = "iShanghai_account_";
	/// common keys in common_settings
	public static String PHONE_NUMBER_KEY = "phone_number";
	/// individual keys in user_info_settings
	public static String PASSWORD_KEY = "ish_pw";
	/// auth keys
	private static final String AUTH_KEY_TEST = "ish_auth_key_test";
	private static final String AUTH_KEY_PROD = "ish_auth_key_prod";
	///
	//private static final String IMAGE_DIR = "image_directory";
	public static String m_sd = "piauobpjalkjepoiuf";    /// secret key
	private static WFTConfig instance;
	private final Scheduler m_threadScheduler;
	//	private final String m_packageName;
//	private final String[] m_otherPackages;
	private Context m_ctx;
	//    private ISHUser m_current_user;
	private SharedPreferences m_userConfig;
	//private SharedPreferences m_current_user_settings;
//	private SharedPreferences m_userInfoSettings;

	//	private String m_phoneNumber;  /// Cache the current phone number.
//	private String m_password;
//	private String m_authToken;
	private SharedAuthInfo m_sharedAuthInfo = new SharedAuthInfo();
	private boolean m_isMaster = false; /// whether this is master device

	private WFTConfig(Context context) {
		m_ctx = context;
		m_userConfig = m_ctx.getSharedPreferences(CONF_NAME_PREFIX, Context.MODE_PRIVATE);
		m_threadScheduler = Schedulers.newThread();

//		try {
//			Context friendContext = context.createPackageContext("com.windfindtech.ishanghai.debug", Context.CONTEXT_IGNORE_SECURITY);
//			SharedPreferences sp = friendContext.getSharedPreferences(CONF_NAME_PREFIX, Context.MODE_PRIVATE);
//			String phone = sp.getString(PHONE_NUMBER_KEY, "222");
//			Logger.debug(friendContext);
//		} catch (PackageManager.NameNotFoundException e) {
//			e.printStackTrace();
//		}
//
//		String[] s = ICommonProvider.getUserAuthInfo();
//		if(s != null && s[0] != null) {
//
//		} else {
//			ICommonProvider.createDefaultUserAuthInfo();
//		}
//
//		updateOtherPassword("abcde");

//		getUserAuthInfoFromContentProvider();
//        m_current_user = new ISHUser();
//		load_user_info_locally();
	}

	public static synchronized void init(Context context) {
		instance = new WFTConfig(context);
	}

	public static synchronized WFTConfig instance() {
		return instance;
	}

	public void storePhoneNumber(final String phone_number) {
		/// save current phone number
		m_sharedAuthInfo.setUserName(phone_number);
		m_userConfig.edit().putString(PHONE_NUMBER_KEY, phone_number).apply();
	}

	public void storePassword(final String password) {
		m_sharedAuthInfo.setPassword(password);
		RXThreadTask.run(m_threadScheduler, new SingleTask() {
			@Override
			public void onSuccess(Object o) {
				try {
					String encrypted = password != null ? SimpleCrypto.encryptWithSHA1PRNG(m_sd, password) : null;
					m_sharedAuthInfo.setPasswordEnc(encrypted);
					m_userConfig.edit().putString(PASSWORD_KEY, encrypted).apply();
//					throttleLast.onNext(m_sharedAuthInfo);
				} catch (Exception e) {
					Logger.error(e);
				}
			}
		});
	}

	public void storeAuthKeyTest(final String authKeyTest) {
		m_sharedAuthInfo.setTokenTest(authKeyTest);
		RXThreadTask.run(m_threadScheduler, new SingleTask() {
			@Override
			public void onSuccess(Object o) {
				try {
					String encrypted = authKeyTest != null ? SimpleCrypto.encryptWithSHA1PRNG(m_sd, authKeyTest) : null;
					m_sharedAuthInfo.setTokenTestEnc(encrypted);
					m_userConfig.edit().putString(AUTH_KEY_TEST, encrypted).apply();
//					throttleLast.onNext(m_sharedAuthInfo);
				} catch (Exception e) {
					Logger.error(e);
				}
			}
		});
	}

	public void storeAuthKeyProd(final String authKeyProd) {
		m_sharedAuthInfo.setTokenProd(authKeyProd);
		RXThreadTask.run(m_threadScheduler, new SingleTask() {
			@Override
			public void onSuccess(Object o) {
				try {
					String encrypted = authKeyProd != null ? SimpleCrypto.encryptWithSHA1PRNG(m_sd, authKeyProd) : null;
					m_sharedAuthInfo.setTokenProdEnc(encrypted);
					m_userConfig.edit().putString(AUTH_KEY_PROD, encrypted).apply();
//					throttleLast.onNext(m_sharedAuthInfo);
				} catch (Exception e) {
					Logger.error(e);
				}
			}
		});
	}

	public void storeAuthKey(boolean isTest, String authKey) {
		if (isTest) {
			storeAuthKeyTest(authKey);
		} else {
			storeAuthKeyProd(authKey);
		}
	}

	public SharedAuthInfo getAll() {
		return m_sharedAuthInfo;
	}

	public String getPhoneNumber() {
		return m_sharedAuthInfo.getUserName();
	}

	public String getPassword() {
		return m_sharedAuthInfo.getPassword();
	}

	public String getAuthKey(boolean isTest) {
		return isTest ? m_sharedAuthInfo.getTokenTest() : m_sharedAuthInfo.getTokenProd();
	}

	public void setMaster(boolean master) {
		m_isMaster = master;
	}

	public boolean isMaster() {
		return m_isMaster;
	}


	/**
	 * prepare user auth info and store the result to local fields in memory
	 * task is run after the operations
	 *
	 * @param task
	 * @return true if already prepared and no need to do db operations, false if task should be run after initialization
	 */
	public boolean prepareUserAuthInfo(SingleTask<SharedAuthInfo> task) {
		if (m_sharedAuthInfo.getUserName() != null) {
			return true;
		}
		RXThreadTask.run(m_threadScheduler, new RXThreadTask.Task() {
			@Override
			public SharedAuthInfo runTask() {
				SharedAuthInfo tuple = new SharedAuthInfo();
				tuple.setUserName(m_userConfig.getString(PHONE_NUMBER_KEY, null));
				tuple.setPasswordEnc(m_userConfig.getString(PASSWORD_KEY, null));
				tuple.setTokenTestEnc(m_userConfig.getString(AUTH_KEY_TEST, null));
				tuple.setTokenProdEnc(m_userConfig.getString(AUTH_KEY_PROD, null));
				try {
					if (tuple.getPasswordEnc() != null) {
						tuple.setPassword(SimpleCrypto.decryptWithSHA1PRNG(m_sd, tuple.getPasswordEnc()));
					} else {
						/// for backward compatibility
						SharedPreferences sp = m_ctx.getSharedPreferences(CONF_NAME_PREFIX + tuple.getUserName(), Context.MODE_PRIVATE);
						String passwordEncLegacy = sp.getString(PASSWORD_KEY, null);
						if(passwordEncLegacy != null) {
							tuple.setPassword(SimpleCrypto.decryptWithSHA1PRNG(m_sd, passwordEncLegacy));
						}
					}
					if (tuple.getTokenTestEnc() != null) {
						tuple.setTokenTest(SimpleCrypto.decryptWithSHA1PRNG(m_sd, tuple.getTokenTestEnc()));
					}
					if (tuple.getTokenProdEnc() != null) {
						tuple.setTokenProd(SimpleCrypto.decryptWithSHA1PRNG(m_sd, tuple.getTokenProdEnc()));
					}
				} catch (Exception e) {
					Logger.error(e);
				}
				m_sharedAuthInfo = tuple;

				return tuple;
			}
		}, task);
		return false;
	}

	public void clearAuthToken() {
		storeAuthKeyTest(null);
		storeAuthKeyProd(null);
	}
}
