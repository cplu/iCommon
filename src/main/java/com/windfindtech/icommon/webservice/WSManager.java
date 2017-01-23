package com.windfindtech.icommon.webservice;

import android.content.Context;
import android.graphics.Bitmap;

import com.windfindtech.icommon.bitmap.DrawableManager;
import com.windfindtech.icommon.config.WFTConfig;
import com.windfindtech.icommon.gson.GsonUtil;
import com.windfindtech.icommon.http.HttpsTrustManager;
import com.windfindtech.icommon.http.NetworkTester;
import com.windfindtech.icommon.iCommon;
import com.windfindtech.icommon.jsondata.advertisement.DefaultAdModel;
import com.windfindtech.icommon.jsondata.enumtype.DeviceStatus;
import com.windfindtech.icommon.jsondata.enumtype.EventType;
import com.windfindtech.icommon.jsondata.event.EventAward;
import com.windfindtech.icommon.jsondata.event.EventCheckinInfo;
import com.windfindtech.icommon.jsondata.event.EventDetail;
import com.windfindtech.icommon.jsondata.event.EventImage;
import com.windfindtech.icommon.jsondata.event.EventList;
import com.windfindtech.icommon.jsondata.event.EventParticipationModel;
import com.windfindtech.icommon.jsondata.event.EventPromotionInfo;
import com.windfindtech.icommon.jsondata.nearsite.NearSiteItemPostBody;
import com.windfindtech.icommon.jsondata.nearsite.NearSiteItemWrapper;
import com.windfindtech.icommon.jsondata.radio.AudioList;
import com.windfindtech.icommon.jsondata.site.RatingResult;
import com.windfindtech.icommon.jsondata.site.SubmitNewSiteItem;
import com.windfindtech.icommon.jsondata.usageinfo.UsageInfo;
import com.windfindtech.icommon.jsondata.user.UserData;
import com.windfindtech.icommon.jsondata.webservice.BaseResponse;
import com.windfindtech.icommon.jsondata.webservice.DeviceData;
import com.windfindtech.icommon.jsondata.webservice.FeedbackInfo;
import com.windfindtech.icommon.jsondata.webservice.LoginResponse;
import com.windfindtech.icommon.jsondata.webservice.LogoutResponse;
import com.windfindtech.icommon.jsondata.webservice.RankResponse;
import com.windfindtech.icommon.jsondata.webservice.RatingCheck;
import com.windfindtech.icommon.jsondata.webservice.ServerStatus;
import com.windfindtech.icommon.jsondata.webservice.VersionCheck;
import com.windfindtech.icommon.mvp.model.local.UsrDataManager;
import com.windfindtech.icommon.retrofit.JsonParserTransformer;
import com.windfindtech.icommon.retrofit.ResponseJsonParserTransformer;
import com.windfindtech.icommon.retrofit.StreamParser;
import com.windfindtech.icommon.util.Gather;
import com.windfindtech.icommon.util.Utils;
import com.windfindtech.icommon.wifimanager.WFTNetworkMonitor;
import com.windfindtech.icommon.wifimanager.WifiAuth;

import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.net.CookieManager;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.Observable;
import rx.Subscription;
import rx.functions.Action1;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by cplu on 2016/7/15.
 */
public class WSManager {
	private static final String TEST_BASE_URL = "http://10.0.4.2:82/";
	private static final String TEST_BASE_URL_SECURE = "https://10.0.4.2/";
	private static final int DEFAULT_IMAGE_CACHE_SECOND = 86400;
	public static final int WS_FORBIDDEN = 403;
	//	private static final int DEFAULT_REQUEST_CACHE_MS = 86400000;
	private final Context m_ctx;
	private String m_baseUrl;
	private String m_baseUrlSecure;
	private String m_prodBaseUrl;
	private String m_prodBaseUrlSecure;
	private String m_testBaseUrl;
	private String m_testBaseUrlSecure;
	private Retrofit.Builder m_retrofitBuilder;
	private Retrofit.Builder m_retrofitBuilderSecure;
	private static WSManager m_instance;
	private WFTWebserviceInterface wsService;
	private WFTWebserviceInterfaceSecure wsServiceSecure;
	private ImageInterface wsImageService;
	private boolean m_isTestEnv = false;
	private String m_appName = "ishanghai";
	private static final int DEFAULT_NETWORK_TIMEOUT = 10000;

	private WSManager(Context ctx, String product_base_url, String product_base_url_secure,
	                  String test_base_url, String test_base_url_secure) {
		m_ctx = ctx;
		m_baseUrl = m_prodBaseUrl = product_base_url;
		m_baseUrlSecure = m_prodBaseUrlSecure = product_base_url_secure;
		m_testBaseUrl = test_base_url;
		m_testBaseUrlSecure = test_base_url_secure;

		m_retrofitBuilder = new Retrofit.Builder()
			.addConverterFactory(GsonConverterFactory.create(GsonUtil.getGson()))
			.addCallAdapterFactory(RxJavaCallAdapterFactory.create());

		m_retrofitBuilderSecure = new Retrofit.Builder()
			.addConverterFactory(GsonConverterFactory.create(GsonUtil.getGson()))
			.addCallAdapterFactory(RxJavaCallAdapterFactory.create());

		createRetrofitServices(m_baseUrl, m_baseUrlSecure, false);
	}

	private WSManager(Context ctx, String product_base_url, String product_base_url_secure) {
		this(ctx, product_base_url, product_base_url_secure, TEST_BASE_URL, TEST_BASE_URL_SECURE);
	}

	public static WSManager instance() {
		return m_instance;
	}

	public static void init(Context ctx, String product_base_url, String product_base_url_secure) {
		if (m_instance == null) {
			m_instance = new WSManager(ctx, product_base_url, product_base_url_secure);
		}
	}

	/**
	 * @param ctx
	 * @param product_base_url
	 * @param product_base_url_secure
	 * @param app_name
	 */
	public static void init(Context ctx, String product_base_url, String product_base_url_secure, String app_name) {
		if (m_instance == null) {
			m_instance = new WSManager(ctx, product_base_url, product_base_url_secure);
			m_instance.setAppName(app_name);
		}
	}

	private void setAppName(String app_name) {
		m_appName = app_name;
	}


	public static void init(Context ctx, String test_base_url, String test_base_url_secure, String product_base_url, String product_base_url_secure) {
		if (m_instance == null) {
			m_instance = new WSManager(ctx, product_base_url, product_base_url_secure, test_base_url, test_base_url_secure);
		}
	}

	public void resetBySSID(String ssid) {
		if (WifiAuth.instance().isConnectedToInternalSsid(ssid)) {
			m_baseUrl = m_testBaseUrl;
			m_baseUrlSecure = m_testBaseUrlSecure;
			m_isTestEnv = true;
			createRetrofitServices(m_baseUrl, m_baseUrlSecure, true);
		} else if (WFTNetworkMonitor.instance().isOnline()) {
			m_baseUrl = m_prodBaseUrl;
			m_baseUrlSecure = m_prodBaseUrlSecure;
			m_isTestEnv = false;
			createRetrofitServices(m_baseUrl, m_baseUrlSecure, false);
		}
	}

	private void createRetrofitServices(String url, String urlSecure, boolean sslSelfSigned) {
		/// create webservice interfaces
		Retrofit retrofit = m_retrofitBuilder
			.baseUrl(url)
			.client(getClient(false))
			.build();
		Retrofit retrofitSecure = m_retrofitBuilderSecure
			.baseUrl(urlSecure)
			.client(getClient(sslSelfSigned))
			.build();
		wsService = retrofit.create(WFTWebserviceInterface.class);
		wsServiceSecure = retrofitSecure.create(WFTWebserviceInterfaceSecure.class);

		/// create image service interfaces
		Retrofit retrofitImage = new Retrofit.Builder()
			.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
			.baseUrl(url)
			.client(getImageClient())
			.build();
		wsImageService = retrofitImage.create(ImageInterface.class);
	}

	private static JavaNetCookieJar s_cookieJar = new JavaNetCookieJar(new CookieManager());

	private OkHttpClient getClient(boolean sslSelfSigned) {
		File httpCacheDirectory = new File(m_ctx.getCacheDir(), "restResponses");
		int cacheSize = 10 * 1024 * 1024; // 10 MiB
		Cache cache = new Cache(httpCacheDirectory, cacheSize);

		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.retryOnConnectionFailure(false)
			.cache(cache)
			.cookieJar(s_cookieJar)
			.connectTimeout(DEFAULT_NETWORK_TIMEOUT, TimeUnit.MILLISECONDS)
			.readTimeout(DEFAULT_NETWORK_TIMEOUT, TimeUnit.MILLISECONDS)
			.writeTimeout(DEFAULT_NETWORK_TIMEOUT, TimeUnit.MILLISECONDS)
			.addNetworkInterceptor(new Interceptor() {
				@Override
				public okhttp3.Response intercept(Chain chain) throws IOException {
					Request request = chain.request().newBuilder()
						.addHeader("Accept", "application/json")
						.addHeader("Accept-Language", "zh")
						.build();
					Logger.debug("request url: " + request.url());
					okhttp3.Response originalResponse = chain.proceed(request);
					if (request.method().equals("GET")
					    && originalResponse.header("Cache-Control") == null    /// no cache control in response
					    && request.header("Local-Cache-Control") != null       /// custom defined cache control in request
						) {
						/// we create artificial Cache-Control in response for local cache
						String local_cache_control = request.header("Local-Cache-Control");
						return originalResponse.newBuilder()
							.header("Cache-Control", String.format("max-age=%s, only-if-cached, max-stale=%d", local_cache_control, 0))
							.build();
					} else {
						return originalResponse;
					}
				}
			});
		if (sslSelfSigned) {
			try {
				builder.sslSocketFactory(HttpsTrustManager.getTestEnvSSLSocketFactory(m_ctx))
					.hostnameVerifier(HttpsTrustManager.getTestEnvHostnameVerifier());
			} catch (Exception e) {
				Logger.error(e);
			}
		}
		return builder.build();
	}

	private OkHttpClient getImageClient() {
		File httpCacheDirectory = new File(m_ctx.getCacheDir(), "imageResponses");
		int cacheSize = 20 * 1024 * 1024; // 20 MiB
		Cache cache = new Cache(httpCacheDirectory, cacheSize);

		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.retryOnConnectionFailure(false)
			.cache(cache)
			.cookieJar(s_cookieJar)
			.connectTimeout(DEFAULT_NETWORK_TIMEOUT, TimeUnit.MILLISECONDS)
			.readTimeout(DEFAULT_NETWORK_TIMEOUT, TimeUnit.MILLISECONDS)
			.writeTimeout(DEFAULT_NETWORK_TIMEOUT, TimeUnit.MILLISECONDS)
			.addNetworkInterceptor(new Interceptor() {
				@Override
				public okhttp3.Response intercept(Chain chain) throws IOException {
					Request request = chain.request();
					Logger.debug("request image: " + request.url());
					okhttp3.Response originalResponse = chain.proceed(request);
					if (request.header(ImageInterface.LOCAL_TIME_START_KEY) != null
					    && originalResponse.isSuccessful() && originalResponse.body() != null) {
						long start_time = Long.parseLong(request.header(ImageInterface.LOCAL_TIME_START_KEY));
						long elapsed = System.currentTimeMillis() - start_time;
						Logger.debug("image fetcher time elapsed: " + elapsed);
						NetworkTester.instance().addTestCase((int) originalResponse.body().contentLength(), elapsed);
					}
					if (request.method().equals("GET")
					    && originalResponse.header("Cache-Control") == null    /// no cache control in response
					    && request.header("Local-Cache-Control") != null       /// custom defined cache control in request
						) {
						/// we create artificial Cache-Control in response for local cache
						String local_cache_control = request.header("Local-Cache-Control");
						return originalResponse.newBuilder()
							.header("Cache-Control", String.format("max-age=%s, only-if-cached, max-stale=%d", local_cache_control, 0))
							.build();
					}
//					else if(originalResponse.cacheControl() != null
//						&& originalResponse.cacheControl().maxAgeSeconds() > 0) {
//						/// Trick: if response contains Cache-Control, remove other 304 related headers to avoid bug:
//						/// https://github.com/square/okhttp/pull/2818/files
//						/// expect fix of okhttp in the near future
//						return originalResponse.newBuilder()
//							.removeHeader("Last-Modified")
//							.removeHeader("ETag")
//							.build();
//					}
					else {
						return originalResponse;
					}
				}
			});
		return builder.build();
	}

	/// start of url request interfaces
	public void doLogIn(String username, String password, boolean quickLogin, final WSCallback<LoginResponse> callback) {
		final long start_of_log_in_in_ms = System.currentTimeMillis();

		String authToken = null;
		if(quickLogin) {
			authToken = WFTConfig.instance().getAuthKey(m_isTestEnv);
			if(authToken != null) {
				/// use auth token to do log in and no password required
				Logger.debug("ws quick login");
				password = null;
			}
		}

		wsServiceSecure.logIn(m_appName,
			username,
			password,
			Utils.instance().getUDID(),
			iCommon.DEVICE_OS,
			Utils.instance().getAppVersionCode(),
			authToken
		)
			.compose(WSTransformer.<LoginResponse>get())
			/// additional operations before onNext
			.doOnNext(new Action1<LoginResponse>() {
				@Override
				public void call(LoginResponse o) {
					Logger.debug("retrofit doOnNext2 thread: " + Thread.currentThread().getName());
					Gather.instance().addWSLoginUsageInfo(start_of_log_in_in_ms, true);
					UsrDataManager.usrDat.clearData();
					UsrDataManager.usrDat.updateData();
//						UserData.instance().setUserConfigExpired(); /// set expired to allow user info being refreshed later on
				}
			})
			/// additional operations before onError
			.doOnError(new Action1<Throwable>() {
				@Override
				public void call(Throwable throwable) {
					Logger.debug("retrofit doOnError2 thread: " + Thread.currentThread().getName());
					Gather.instance().addWSLoginUsageInfo(start_of_log_in_in_ms, false);
				}
			})
			.subscribe(callback);
	}

	public void doLogOut(final WSCallback<LogoutResponse> callback) {
		wsServiceSecure.logOut()
			.compose(WSTransformer.<LogoutResponse>get())
			.subscribe(callback);
	}

	public void doGetPassword(String phone_number, final WSCallback<BaseResponse> callback) {
		wsService.getPassword(phone_number)
			.compose(WSTransformer.<BaseResponse>get())
			.subscribe(callback);
	}

	public void doGetAccountInfo(final WSCallback<UserData> callback) {
		wsServiceSecure.getAccountInfo()
			.compose(WSTransformer.<UserData>get())
			.subscribe(callback);
	}

	public Observable<UserData> getAccountInfo() {
		return wsServiceSecure.getAccountInfo()
			.compose(WSTransformer.<UserData>get());
	}

	/**
	 * Report device token to webservice
	 *
	 * @param token Device token used for push manager. In fact, token is retrieved from tencent-xg
	 */
	public Observable<BaseResponse> updateDeviceToken(String token) {
		return wsService.updateDeviceToken(iCommon.DEVICE_OS, token)
			.compose(WSTransformer.<BaseResponse>get());
	}

	public Observable<Bitmap> getImageObservable(String url, boolean use_cache,
	                                     final int width_limit, final int height_limit) {
		String cache_control = use_cache ?
			String.format("public, max-age=%d", DEFAULT_IMAGE_CACHE_SECOND)
			: "no-cache";
		return wsImageService.getImage(url, DEFAULT_IMAGE_CACHE_SECOND, cache_control, System.currentTimeMillis())
			.observeOn(Schedulers.io())
			.flatMap(new Func1<ResponseBody, Observable<Bitmap>>() {
				@Override
				public Observable<Bitmap> call(ResponseBody bitmapResponse) {
					try {
						byte[] imageData = bitmapResponse.bytes();
						Bitmap bitmap = DrawableManager.instance().getBitmapLimited(imageData, width_limit, height_limit);
						return Observable.just(bitmap);
					} catch (Exception e) {
						Logger.error(e);
						return Observable.error(e);
					}
				}
			})
			.compose(WSTransformer.<Bitmap>get());
	}

//	public Observable<Bitmap> getBitmapObservable(String url, boolean use_cache,
//	                                              final int width_limit, final int height_limit) {
//		return getImageObservable(url, use_cache, width_limit, height_limit);
//	}

	/**
	 * get observable of fetching account avatar (without cache)
	 *
	 * @param width_limit
	 * @param height_limit
	 * @return
	 */
	public Observable<Bitmap> getAccountAvatar(final int width_limit, final int height_limit) {
		return getImageObservable("api/account/avatar", false, width_limit, height_limit);
	}

	/**
	 * get image from network
	 * Note: for image fetching, caching is enabled by default, while for http json get, caching is disabled by default.
	 *
	 * @param user_name    for the sake of distinguishing images of different user
	 * @param url          path of the image in webservice, or full url starting with "http" or "https"
	 * @param use_cache    whether caching is applied
	 * @param width_limit  width limit of the returned bitmap
	 * @param height_limit height limit of the returned bitmap
	 * @param callback     callback for notifying the result
	 */
	@SuppressWarnings("unchecked")
	public void doGetImage(final String user_name, String url, boolean use_cache,
	                       final int width_limit, final int height_limit,
	                       final WSCallback<Bitmap> callback) {
		url = addUrlSuffix(url, user_name);
		Logger.debug("[doGetImageByUrl] " + url);

		getImageObservable(url, use_cache, width_limit, height_limit)
			.subscribe(callback);
	}

	/**
	 * get image with caching
	 *
	 * @param url
	 * @param width_limit
	 * @param height_limit
	 * @param callback
	 */
	public void doGetImage(String url, final int width_limit, final int height_limit, final WSCallback<Bitmap> callback) {
		doGetImage(null, url, true, width_limit, height_limit, callback);
	}

	/**
	 * get image without caching
	 *
	 * @param url
	 * @param width_limit
	 * @param height_limit
	 * @param callback
	 */
	public void doUpdateImage(String url, final int width_limit, final int height_limit, final WSCallback<Bitmap> callback) {
		doGetImage(null, url, false, width_limit, height_limit, callback);
	}

	/**
	 * @param user_config
	 * @param callback
	 */
	public void doUpdateAccountInfo(final UserData user_config, final WSCallback<BaseResponse> callback) {
		Logger.debug("[doUpdateAccountInfo] - user:\n" + user_config);
		wsServiceSecure.updateAccountInfo(user_config)
			.compose(WSTransformer.<BaseResponse>get())
			.subscribe(callback);
	}

	public void doChangePassword(String oldPassword, final String currentPassword, final WSCallback<BaseResponse> callback) {
		Logger.debug("[doChangePassword]");

		wsServiceSecure.changePassword(oldPassword, currentPassword)
			.compose(WSTransformer.<BaseResponse>get())
			.subscribe(callback);
	}

	/**
	 * Needn't be called in ui-thread anymore with volley!
	 *
	 * @param image_bytes
	 * @param callback
	 */
	public void doUpdateAccountAvatar(byte[] image_bytes, final WSCallback<BaseResponse> callback) {
		Logger.debug("[doUpdateAccountAvatar]");

		HashMap<String, RequestBody> map = new HashMap<>();
		RequestBody imageBody = RequestBody.create(MediaType.parse("image/*"), image_bytes);
		map.put("image\"; filename=\"avatar.jpg", imageBody);
		wsService.updateAccountAvatar(map)
			.compose(WSTransformer.<BaseResponse>get())
			.subscribe(callback);
	}

	public void doSubmitFeedback(FeedbackInfo feedback_info, final WSCallback<BaseResponse> callback) {
		Logger.debug("[doSubmitFeedback]");

		wsService.submitFeedback(feedback_info)
			.compose(WSTransformer.<BaseResponse>get())
			.subscribe(callback);
	}

	/**
	 * register as master device
	 *
	 * @param callback
	 */
	public void doDeviceRegister(final WSCallback<BaseResponse> callback) {
		Logger.debug("[doDeviceRegister]");
		wsService.deviceRegister()
			.compose(WSTransformer.<BaseResponse>get())
			.subscribe(callback);
	}

	/**
	 * change master device, with the given register code
	 *
	 * @param callback
	 */
	public void doDeviceChange(String code, final WSCallback<BaseResponse> callback) {
		Logger.debug("[doDeviceChange]");
		wsService.deviceChange(code, iCommon.DEVICE_OS, Utils.instance().getDeviceType())
			.compose(WSTransformer.<BaseResponse>get())
			.doOnNext(new Action1<BaseResponse>() {
				@Override
				public void call(BaseResponse response) {
					WFTConfig.instance().setMaster(true);
				}
			})
			.subscribe(callback);
	}

	/**
	 * get device list for current user
	 *
	 * @param callback
	 */
	public void doGetDeviceList(final WSCallback<DeviceData[]> callback) {
		Logger.debug("[doGetDeviceList]");
		wsService.getDeviceList()
			.compose(WSTransformer.<DeviceData[]>get())
			.subscribe(callback);
	}

	public void doDeviceMasterVerity(final WSCallback<Boolean> callback) {
		Logger.debug("[doDeviceMasterVerity]");
		wsService.verifyMasterDevice()
			.compose(WSTransformer.<Boolean>get())
			.subscribe(callback);
	}

	public void doTestException(final WSCallback<String> callback) {
		Logger.debug("[doTestException]");
		wsService.doTestException()
			.compose(WSTransformer.<String>get())
			.subscribe(callback);
	}

	/**
	 * get observable for doGetDeviceList
	 */
	public Observable doGetDeviceList() {
		Logger.debug("[doGetDeviceList]");
		return wsService.getDeviceList()
			.observeOn(Schedulers.io())
			.map(new Func1<DeviceData[], ArrayList<DeviceData>[]>() {
				@Override
				public ArrayList<DeviceData>[] call(DeviceData[] response) {

					ArrayList<DeviceData>[] deviceSections = new ArrayList[DeviceStatus.values().length];
					for (int i = 0; i < DeviceStatus.values().length; i++) {
						deviceSections[i] = new ArrayList<>();
					}
					for (DeviceData device : response) {
						if (device != null) {
							boolean is_self = false;
							String mac_addr = WFTNetworkMonitor.instance().getMacAddress();
							if (mac_addr != null) {
								is_self = mac_addr.equalsIgnoreCase(device.getMac());
							}
							device.setSelf(is_self);
							DeviceStatus status = device.getStatus();
							deviceSections[status.ordinal()].add(device);
						}
					}
					return deviceSections;
				}
			})
			.compose(WSTransformer.<ArrayList<DeviceData>[]>get());
	}

	/**
	 * change device name, this is a nick name for device
	 *
	 * @param device_id
	 * @param new_name
	 * @param callback
	 */
	public void doChangeDeviceName(String device_id, String new_name, final WSCallback<BaseResponse> callback) {
		Logger.debug("[doChangeDeviceName]");
		wsService.changeDeviceName(device_id, new_name)
			.compose(WSTransformer.<BaseResponse>get())
			.subscribe(callback);
	}

	/**
	 * change device status
	 *
	 * @param device_id
	 * @param new_status
	 * @param callback
	 */
	public void doChangeDeviceStatus(String device_id, String new_status, final WSCallback<BaseResponse> callback) {
		Logger.debug("[doChangeDeviceStatus]");
		wsService.changeDeviceStatus(device_id, new_status)
			.compose(WSTransformer.<BaseResponse>get())
			.subscribe(callback);
	}

	/**
	 * delete the device by device_id
	 *
	 * @param device_id
	 * @param callback
	 */
	public void doDeleteDevice(String device_id, final WSCallback<BaseResponse> callback) {
		Logger.debug("[doDeleteDevice]");
		wsService.deleteDevice(device_id)
			.compose(WSTransformer.<BaseResponse>get())
			.subscribe(callback);
	}

	public void doGetAchievementData(final WSCallback<RankResponse> callback) {
		Logger.debug("doGetAchievementData");
		wsServiceSecure.getAchievementData()
			.compose(WSTransformer.<RankResponse>get())
			.subscribe(callback);
	}

	/**
	 * send statistics to web service
	 *
	 * @param usageInfo
	 * @param callback
	 */
	public void doSendUsageinfo(UsageInfo usageInfo, final WSCallback<BaseResponse> callback) {
		Logger.debug("[doSendUsageinfo]");
		wsServiceSecure.sendUsageinfo(usageInfo)
			.compose(WSTransformer.<BaseResponse>get())
			.subscribe(callback);
	}

	public void doSendStatistics(byte[] user_stat, WSCallback<BaseResponse> callback) {
		Logger.debug("[doSendStatistics]");

		HashMap<String, RequestBody> map = new HashMap<>();
		RequestBody statBody = RequestBody.create(MediaType.parse("application/gzip"), user_stat);
		map.put("file\"; filename=\"stat.gz", statBody);
		wsServiceSecure.sendStatistics(map)
			.compose(WSTransformer.<BaseResponse>get())
			.subscribe(callback);
	}

	public void doSendPersonalMessage(String message, final WSCallback<BaseResponse> callback) {
		Logger.debug("[doSendPersonalMessage]");
		wsService.sendPersonalMessage(message)
			.compose(WSTransformer.<BaseResponse>get())
			.subscribe(callback);
	}

	public void doSendNewSiteInfos(SubmitNewSiteItem newSiteItem, final WSCallback<BaseResponse> callback) {
		Logger.debug("[doSendPersonalMessage]");
		wsService.submitNewSiteInfo(newSiteItem)
			.compose(WSTransformer.<BaseResponse>get())
			.subscribe(callback);
	}

	public void doHasPersonalMessage(final WSCallback<Boolean> callback) {
		Logger.debug("[doHasPersonalMessage]");
		wsService.hasPersonalMessage()
			.compose(WSTransformer.<Boolean>get())
			.subscribe(callback);
	}

	public Observable<Boolean> doHasPersonalMessage() {
		Logger.debug("[doHasPersonalMessage]");
		return wsService.hasPersonalMessage()
			.compose(WSTransformer.<Boolean>get());
	}

	public void doCheckRatingRequired(final WSCallback<RatingCheck> callback) {
		Logger.debug("[doCheckRatingRequired]");
		wsService.checkRatingRequired()
			.compose(WSTransformer.<RatingCheck>get())
			.subscribe(callback);
	}

	public void doGetRatingReasons(final WSCallback<String[]> callback) {
		Logger.debug("[doGetRatingReasons]");
		wsService.getRatingReasons()
			.compose(WSTransformer.<String[]>get())
			.subscribe(callback);
	}

	/**
	 * send rating result
	 * used only in iShanghai
	 *
	 * @param ratingResult
	 * @param callback
	 */
	public void doSendRatingResult(RatingResult ratingResult, final WSCallback<BaseResponse> callback) {
		Logger.debug("[doSendRatingResult]");
		wsService.sendRatingResultForLastSite(ratingResult)
			.compose(WSTransformer.<BaseResponse>get())
			.subscribe(callback);
	}

	/**
	 * 2016/06/16, do rating in new url
	 * this is used for iCity now
	 *
	 * @param ratingResult
	 * @param callback
	 */
	public void doRatingSite(RatingResult ratingResult, final WSCallback<BaseResponse> callback) {
		Logger.debug("[doSendRatingResult]");
		wsService.sendRatingResult(ratingResult.getSiteCode(), ratingResult)
			.compose(WSTransformer.<BaseResponse>get())
			.subscribe(callback);
	}

	public void doSendNoMoreRanking(final WSCallback<BaseResponse> callback) {
		Logger.debug("[doSendNoMoreRanking]");
		wsService.sendNoMoreRanking()
			.compose(WSTransformer.<BaseResponse>get())
			.subscribe(callback);
	}

	public void getAllEvents(String type, final WSCallback<EventList> callback) {
		Logger.debug("getAllEvents of type " + type);
		wsService.getAllEvents(type)
			.compose(WSTransformer.<EventList>get())
			.subscribe(callback);
	}

	public Observable<BaseResponse> uploadAppLog(byte[] log_bytes, String userFeedback) {
		try {
			HashMap<String, RequestBody> map = new HashMap<>();
			/// here we create a new UsageInfo and setApp to store error information, UsageInfo used in sending error report should be a new object every time we call this function
			UsageInfo info = Gather.instance().getDefaultUsageInfo();
			String appStr = m_appName + ":" + (userFeedback != null ? userFeedback : "[crash]");
			if (iCommon.isDebug) {
				appStr = "D:" + appStr;
			}
			info.setApp(appStr);
			String info_json = GsonUtil.getGson().toJson(info, UsageInfo.class);
			map.put("userDescription", RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), info_json));
			map.put("deviceOs", RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), iCommon.DEVICE_OS));
			RequestBody logBytes = RequestBody.create(MediaType.parse("application/gzip"), log_bytes);
			map.put("file\"; filename=\"log.gz", logBytes);
			return wsServiceSecure.uploadAppLog(map)
				.compose(WSTransformer.<BaseResponse>get());
		} catch (Exception e) {
			Logger.error(e);
			return null;
		}
	}

	public void getEventDetail(String type, String id, final WSCallback<EventDetail> callback) {
		Logger.debug("getEventDetail of type " + type + " and id " + id);
		wsService.getEventDetail(type, id)
			.compose(WSTransformer.<EventDetail>get())
			.subscribe(callback);
	}

	public void getEventImageList(String type, String id, final WSCallback<EventImage[]> callback) {
		Logger.debug("getEventImageList of type " + type + " and id " + id);
		wsService.getEventImageList(type, id)
			.compose(WSTransformer.<EventImage[]>get())
			.subscribe(callback);
	}

	public void getLastAward(String type, String id, final WSCallback<EventAward[]> callback) {
		Logger.debug("getLastAward of type " + type + " and id " + id);
		wsService.getLastAward(type, id)
			.compose(WSTransformer.<EventAward[]>get())
			.subscribe(callback);
	}

	public void getEventHistoryLast(String type, String id, final WSCallback<EventParticipationModel> callback) {
		Logger.debug("get_event_history of type " + type + " and id " + id);
		wsService.getEventHistoryLast(type, id)
			.compose(WSTransformer.<EventParticipationModel>get())
			.subscribe(callback);
	}

	public void participateSweepstake(String id, byte[] image_bytes, final WSCallback<BaseResponse> callback) {
		Logger.debug("[participateSweepstake]");
		try {
			HashMap<String, RequestBody> map = new HashMap<>();
			RequestBody logBytes = RequestBody.create(MediaType.parse("image/*"), image_bytes);
			map.put("image\"; filename=\"photo.jpg", logBytes);
			wsService.participateSweepstake(EventType.Sweepstakes.toString(), id, map)
				.compose(WSTransformer.<BaseResponse>get())
				.subscribe(callback);
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	public void participateCheckin(String id, final WSCallback<EventCheckinInfo> callback) {
		Logger.debug("[participateCheckin]");
		wsService.participateCheckin(EventType.CheckIn.toString(), id)
			.compose(WSTransformer.<EventCheckinInfo>get())
			.subscribe(callback);
	}

	public void participatePromotion(String id, final WSCallback<EventPromotionInfo> callback) {
		Logger.debug("[participatePromotion]");
		wsService.participatePromotion(EventType.Promotion.toString(), id)
			.compose(WSTransformer.<EventPromotionInfo>get())
			.subscribe(callback);
	}

	/**
	 * Perform an http get request with expiration.
	 *
	 * @param url
	 * @param clazz     either clazz or parser should be provided
	 * @param parser    either clazz or parser should be provided
	 * @param expire    The expiration of the response in milliseconds, should be greater than 0 if use_cache is true
	 * @param use_cache Whether the request should use cache before being sent
	 * @param callback
	 * @param <Type>    The response type of data, already parsed json
	 */
	public <Type> Subscription doHttpGet(String url, final Class<Type> clazz, final StreamParser<Type> parser,
	                                     int expire, boolean use_cache, final WSCallback<Type> callback) {
		Logger.debug("doHttpGet with url " + url);

		expire /= 1000;

		String cache_control = use_cache ?
			String.format("public, max-age=%d", expire)
			: "no-cache";
		return wsService.httpGet(url, expire, cache_control)
			.compose(new JsonParserTransformer<>(parser, clazz))
			.compose(WSTransformer.<Type>get())
			.subscribe(callback);
	}

	public <Type> Observable<Type> httpGet(String url, final Class<Type> clazz, final StreamParser<Type> parser,
	                                 int expire, boolean use_cache) {
		Logger.debug("httpGet url " + url);

		expire /= 1000;

		String cache_control = use_cache ?
			String.format("public, max-age=%d", expire)
			: "no-cache";
		return wsService.httpGet(url, expire, cache_control)
			.compose(new JsonParserTransformer<>(parser, clazz))
			.compose(WSTransformer.<Type>get());
	}

	public <Type> Observable<Type> httpsGet(String url, final Class<Type> clazz, final StreamParser<Type> parser,
	                                                 int expire, boolean use_cache) {
		Logger.debug("httpsGet url " + url);

		expire /= 1000;

		String cache_control = use_cache ?
			String.format("public, max-age=%d", expire)
			: "no-cache";
		return wsServiceSecure.httpGet(url, expire, cache_control)
			.compose(new JsonParserTransformer<>(parser, clazz))
			.compose(WSTransformer.<Type>get());
	}

	public <Type> Subscription doHttpGet(String url, Class<Type> clazz, final WSCallback<Type> callback) {
		return doHttpGet(url, clazz, null, 0, false, callback);
	}

	public <Type> Observable<Type> httpGet(String url, Class<Type> clazz) {
		return httpGet(url, clazz, null, 0, false);
	}

	public <Type> Subscription doHttpGet(String url, final StreamParser<Type> parser, final WSCallback<Type> callback) {
		return doHttpGet(url, null, parser, 0, false, callback);
	}

	public void getLizhiAudioList(String id, int count, final WSCallback<AudioList> callback) {
		Logger.debug("getLizhiAudioList id: " + id);
		wsService.getLizhiAudioList(id, count, 0)
			.compose(WSTransformer.<AudioList>get())
			.subscribe(callback);
	}

	public void getLeastVersionInfo(final WSCallback<VersionCheck> callback) {
		Logger.debug("getLeastVersionInfo");
		wsService.getLeastVersionInfo(m_appName, iCommon.DEVICE_OS, Utils.instance().getAppVersionCode())
			.compose(WSTransformer.<VersionCheck>get())
			.subscribe(callback);
	}

	public Observable<VersionCheck> getLeastVersionInfo() {
		Logger.debug("getLeastVersionInfo");
		return wsService.getLeastVersionInfo(m_appName, iCommon.DEVICE_OS, Utils.instance().getAppVersionCode())
			.compose(WSTransformer.<VersionCheck>get());
	}

	public <T extends WSResponseWithHeaders> void doHttpGetWithHeaders(String url, Class<T> clazz, WSCallback<T> callback) {
		Logger.debug("doHttpGetWithHeaders");
		wsService.responseHttpGet(url, 0, "no-cache")
			.compose(new ResponseJsonParserTransformer<>(null, clazz))
			.map(new Func1<Response<T>, T>() {
				@Override
				public T call(Response<T> t) {
					T ret = t.body();
					if (t.isSuccessful()) {
						ret.setHeaders(t.headers());
					}
					return ret;
				}
			})
			.compose(WSTransformer.<T>get())
			.subscribe(callback);
	}

	public <T> void doSendTweet(String message, String geoAddress, double lng, double lat, byte[][] imgs,
	                            Class<T> clazz, final WSCallback<T> callback) {
		Logger.debug("[doSendTweet]");

		HashMap<String, RequestBody> map = new HashMap<>();
		map.put("message", RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), message));
		if (geoAddress != null) {
			map.put("geoAddress", RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), geoAddress));
		}
		map.put("lng", RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), String.valueOf(lng)));
		map.put("lat", RequestBody.create(MediaType.parse("text/plain; charset=utf-8"), String.valueOf(lat)));
		if (imgs != null) {
			for (byte[] bytes : imgs) {
				if (bytes != null) {
					RequestBody logBytes = RequestBody.create(MediaType.parse("image/jpeg"), bytes);
					map.put("file\"; filename=\"file.jpg", logBytes);
				}
			}
		}

		wsService.sendTweet(map)
			.compose(new JsonParserTransformer<>(null, clazz))
			.compose(WSTransformer.<T>get())
			.subscribe(callback);
	}

	public <T> void doSendMessageReply(String reply, String tweetId, Class<T> clazz, WSCallback<T> callback) {
		Logger.debug("[doSendMessageReply]");
		wsService.sendMessageReply(tweetId, reply)
			.compose(new JsonParserTransformer<>(null, clazz))
			.compose(WSTransformer.<T>get())
			.subscribe(callback);
	}

	public <T> void doMessageDelete(String tweetId, Class<T> clazz, WSCallback<T> callback) {
		Logger.debug("[doMessageDelete]");
		wsService.messageDelete(tweetId)
			.compose(new JsonParserTransformer<>(null, clazz))
			.compose(WSTransformer.<T>get())
			.subscribe(callback);
	}

	public <T> void doReadComments(long[] commentIds, WSCallback<BaseResponse> callback) {
		Logger.debug("[doReadComments]");
		wsService.readComments(commentIds)
			.compose(WSTransformer.<BaseResponse>get())
			.subscribe(callback);
	}

	public Observable<DefaultAdModel[]> getAdvertisementBySpot(String spot) {
		Logger.debug("[getAdvertisementBySpot]");
		String client = Utils.instance().getIMEI();
		return wsService.getAdvertisementBySpot(spot, client)
			.compose(WSTransformer.<DefaultAdModel[]>get());
	}

	public void getServerStatus(WSCallback<ServerStatus> callback) {
		Logger.debug("getServerStatus");
		wsService.getServerStatus()
			.compose(WSTransformer.<ServerStatus>get())
			.subscribe(callback);
	}

	public <T> void doGetMarkerItemUrl(String siteId, Class<T> clazz, final WSCallback<T> callback) {
		Logger.debug("[doGetMarkerItemUrl]");
		wsService.getMarkerItemUrl(siteId)
			.compose(new JsonParserTransformer<>(null, clazz))
			.compose(WSTransformer.<T>get())
			.subscribe(callback);
	}

	public void getNearSite(NearSiteItemPostBody nearSiteItemPostBody, WSCallback<NearSiteItemWrapper[]> callback) {
		wsService.getNearSite(16, nearSiteItemPostBody)
			.compose(WSTransformer.<NearSiteItemWrapper[]>get())
			.subscribe(callback);
	}

	/// end of url request interfaces

	/// start of functions of assist

	/**
	 * add suffix to url to distinguish different users
	 *
	 * @param url       url to request
	 * @param user_name user name
	 * @return
	 */
	private String addUrlSuffix(String url, String user_name) {
		if (user_name != null) {
			if (url.contains("?")) {
				url = url + "&userLocalNameFromAndroid=" + user_name;
			} else {
				url = url + "?userLocalNameFromAndroid=" + user_name;
			}
		}
		return url;
	}

	public String getBaseUrl() {
		return m_baseUrl;
	}

	public String getBaseUrlSecure() {
		return m_baseUrlSecure;
	}

	public boolean isInTestEnv() {
		return m_isTestEnv;
	}
}
