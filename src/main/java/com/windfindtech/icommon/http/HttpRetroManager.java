package com.windfindtech.icommon.http;

import android.content.Context;
import android.support.annotation.NonNull;

import com.windfindtech.icommon.gson.GsonUtil;
import com.windfindtech.icommon.jsondata.advertisement.RequestZamplusModel;
import com.windfindtech.icommon.jsondata.advertisement.ZamplusExtModel;
import com.windfindtech.icommon.retrofit.JsonParserTransformer;
import com.windfindtech.icommon.retrofit.ResponseJsonParserTransformer;
import com.windfindtech.icommon.retrofit.StreamParser;
import com.windfindtech.icommon.xml.CTNetPasswordReply;
import com.windfindtech.icommon.xml.WisprReplyRoot;

import org.pmw.tinylog.Logger;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.net.CookieManager;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.Cache;
import okhttp3.Interceptor;
import okhttp3.JavaNetCookieJar;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.HttpException;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.simplexml.SimpleXmlConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.FieldMap;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.HEAD;
import retrofit2.http.Header;
import retrofit2.http.HeaderMap;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by cplu on 2016/7/21.
 */
public class HttpRetroManager {

	private static final long DEFAULT_HTTP_TIMEOUT = 10000;
	private static final long DOWNLOAD_RETRY_TIMES = 2;
	private final GsonInterface gsonService;
	private final XMLInterface xmlService;
	private Context m_ctx;
	private static HttpRetroManager s_instance;
	private static JavaNetCookieJar s_cookieJar = new JavaNetCookieJar(new CookieManager());

	/**
	 * base url is only used for path that does not have a host name
	 * for full url, base url has no effect
	 */
	private static final String BASE_URL = "http://i-Shanghai.windfindtech.com/";

	private HttpRetroManager(Context ctx) {
		m_ctx = ctx;

		gsonService = new Retrofit.Builder()
			.addConverterFactory(GsonConverterFactory.create(GsonUtil.getGson()))
			.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
			.baseUrl(BASE_URL)
			.client(getHttpClient(true, false))
			.build()
			.create(GsonInterface.class);

		xmlService = new Retrofit.Builder()
			.addConverterFactory(SimpleXmlConverterFactory.createNonStrict())
			.addCallAdapterFactory(RxJavaCallAdapterFactory.create())
			.baseUrl(BASE_URL)
			.client(getHttpClient(true, true))
			.build()
			.create(XMLInterface.class);
	}

	public static HttpRetroManager instance() {
		return s_instance;
	}

	public static void init(Context ctx) {
		if (s_instance == null) {
			s_instance = new HttpRetroManager(ctx);
		}
	}

	private OkHttpClient getHttpClient(boolean followRedirection, boolean useCookie) {

		File httpCacheDirectory = new File(m_ctx.getCacheDir(), "externalResponses");
		int cacheSize = 10 * 1024 * 1024; // 10 MiB
		Cache cache = new Cache(httpCacheDirectory, cacheSize);

		OkHttpClient.Builder builder = new OkHttpClient.Builder();
		builder.retryOnConnectionFailure(false)
			.followRedirects(followRedirection)
			.followSslRedirects(followRedirection)
			.cache(cache)
			.connectTimeout(DEFAULT_HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
			.readTimeout(DEFAULT_HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
			.writeTimeout(DEFAULT_HTTP_TIMEOUT, TimeUnit.MILLISECONDS)
			.addNetworkInterceptor(new Interceptor() {
				@Override
				public okhttp3.Response intercept(Chain chain) throws IOException {
					Request request = chain.request();
					Logger.debug("request: " + request.url());
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
		if (useCookie) {
			builder.cookieJar(s_cookieJar);
		}
		return builder.build();
	}

	/// start of api

	/**
	 * @param url
	 * @param clazz
	 * @param parser
	 * @param headers
	 * @param expire    cache expire, in millisecond
	 * @param use_cache
	 * @param callback
	 * @param <Type>
	 */
	public <Type> void getGson(String url, final Class<Type> clazz, final StreamParser<Type> parser,
	                           Map<String, String> headers, int expire, boolean use_cache, final HttpCallback<Type> callback) {
		Logger.debug("getGson with url " + url);

		expire /= 1000;

		if (headers == null) {
			headers = new HashMap<>();
		}

		String cache_control = use_cache ?
			String.format("public, max-age=%d", expire)
			: "no-cache";
		gsonService.httpGet(url, expire, cache_control, headers)
			.compose(new JsonParserTransformer<>(parser, clazz))
			.compose(HttpTransformer.<Type>get())
			.subscribe(callback);
	}

	public <Type> Observable<Type> getGsonObservable(String url, final Class<Type> clazz, final StreamParser<Type> parser,
	                                                 Map<String, String> headers, int expire, boolean use_cache) {
		Logger.debug("getGson with url " + url);

		expire /= 1000;

		if (headers == null) {
			headers = new HashMap<>();
		}

		String cache_control = use_cache ?
			String.format("public, max-age=%d", expire)
			: "no-cache";
		return gsonService.httpGet(url, expire, cache_control, headers)
			.compose(new JsonParserTransformer<>(parser, clazz))
			.compose(HttpTransformer.<Type>get());
	}

	public <Type> Observable<Response<Type>> responseGetGsonObservable(String url, final Class<Type> clazz, final StreamParser<Type> parser,
	                                                         Map<String, String> headers, int expire, boolean use_cache) {
		Logger.debug("httpGetObservable with url " + url);

		expire /= 1000;
		if (headers == null) {
			headers = new HashMap<>();
		}

		String cache_control = use_cache ?
			String.format("public, max-age=%d", expire)
			: "no-cache";
		return gsonService.responseHttpGet(url, expire, cache_control, headers)
			.compose(new ResponseJsonParserTransformer<>(parser, clazz))
			.compose(HttpTransformer.<Response<Type>>get());
	}

	public <Type> void getGson(String url, final Class<Type> clazz, final StreamParser<Type> parser,
	                           int expire, boolean use_cache, final HttpCallback<Type> callback) {
		getGson(url, clazz, parser, null, expire, use_cache, callback);
	}

	public <Type> void getGson(String url, Class<Type> clazz, HttpCallback<Type> callback) {
		getGson(url, clazz, null, null, 0, false, callback);
	}

	public <Type> Observable<Type> getGsonObservable(String url, Class<Type> clazz) {
		return getGsonObservable(url, clazz, null, null, 0, false);
	}

	public <Type> void getGson(String url, Class<Type> clazz, StreamParser<Type> parser, HttpCallback<Type> callback) {
		getGson(url, clazz, parser, null, 0, false, callback);
	}

	public void getRaw(String url, HttpCallback<ResponseBody> callback) {
		gsonService.httpGetRaw(url)
			.compose(HttpTransformer.<ResponseBody>get())
			.subscribe(callback);
	}

	public void headRaw(String url, String ifModifiedSince, HttpCallback<Response<Void>> callback) {
		gsonService.responseHttpHeadRaw(url, ifModifiedSince)
			.compose(HttpTransformer.<Response<Void>>get())
			.subscribe(callback);
	}

	public <Type> void postGson(String url, final Class<Type> clazz, final StreamParser<Type> parser, Map<String, String> params, final HttpCallback<Type> callback) {
		Logger.debug("getGson with url " + url);

		gsonService.httpPost(url, params)
			.compose(new JsonParserTransformer<>(parser, clazz))
			.compose(HttpTransformer.<Type>get())
			.subscribe(callback);
	}

	public Observable<ZamplusExtModel> postZamplusAdInfo(String url, final StreamParser<ZamplusExtModel> parser, RequestZamplusModel body) {
		Logger.debug("getGson with url " + url);

		return gsonService.httpPost(url, body)
			.compose(new JsonParserTransformer<>(parser, ZamplusExtModel.class))
			.compose(HttpTransformer.<ZamplusExtModel>get());
	}

	public void postRaw(String url, @NonNull Map<String, String> headers, Map<String, String> params, HttpCallback<ResponseBody> callback) {
		gsonService.httpPostRaw(url, headers, params)
			.compose(HttpTransformer.<ResponseBody>get())
			.subscribe(callback);
	}

	public void postXMLWithoutParse(String url, @NonNull Map<String, String> headers, Map<String, String> params, HttpCallback<ResponseBody> callback) {
		xmlService.httpPostRaw(url, headers, params)
			.compose(HttpTransformer.<ResponseBody>get())
			.subscribe(callback);
	}

	public void getWisprReply(String url, @NonNull Map<String, String> headers, HttpCallback<WisprReplyRoot> callback) {
		xmlService.getWisprReply(url, headers)
			.compose(HttpTransformer.<WisprReplyRoot>get())
			.subscribe(callback);
	}

	public Observable<WisprReplyRoot> getWisprReply(String url, @NonNull Map<String, String> headers) {
		return xmlService.getWisprReply(url, headers)
			.compose(HttpTransformer.<WisprReplyRoot>get());
	}

	public Observable<CTNetPasswordReply> getCTNetPasswordReply(String url, @NonNull Map<String, String> headers) {
		return xmlService.getCTNetPasswordReply(url, headers)
			.compose(HttpTransformer.<CTNetPasswordReply>get());
	}

	public void postWisprReply(String url, @NonNull Map<String, String> headers, Map<String, String> params, HttpCallback<WisprReplyRoot> callback) {
		xmlService.postWisprReply(url, headers, params)
			.compose(HttpTransformer.<WisprReplyRoot>get())
			.subscribe(callback);
	}

	public Observable<WisprReplyRoot> postWisprReply(String url, @NonNull Map<String, String> headers, Map<String, String> params) {
		return xmlService.postWisprReply(url, headers, params)
			.compose(HttpTransformer.<WisprReplyRoot>get());
	}


	private static final long RangePartLength = 131072;

	/**
	 * download file from url, starting from startIndex
	 *
	 * @param url
	 * @param localFile  file to store the result
	 * @param startIndex
	 * @param callback   report downloading status and progress
	 */
	public void downloadFile(final String url, final File localFile, final long startIndex,
	                         final FileRangeCallback<String> callback) {
		final RandomAccessFile file;
		try {
			file = new RandomAccessFile(localFile, "rws");
		} catch (Exception e) {
			Logger.error(e);
			return;
		}
//		long currentIndex = 0;
//		final long totalLength = 0;

		HttpCallback<FileRangeInfo> httpCallback = new HttpCallback<FileRangeInfo>() {
			@Override
			public void onSuccess(FileRangeInfo ret) {
				if (ret != null) {
					long totalLength = ret.getSize();
					long end = ret.getByteStart() + ret.getBytesReceived();
					if (end >= totalLength) {
//						Logger.debug("download success with totalLength " + totalLength);
						callback.onSuccess("success");
						closeFile();
					} else {
						Logger.debug("download progress " + end);
						int percent = totalLength > 0 ? (int) (end * 100 / totalLength) : 0;
						if (callback.onProgress(end, percent)) {
							downloadFilePart(url, end, file, this);
						} else {
							/// downloader is paused
							closeFile();
						}
					}
				} else {
					callback.onFailed(HttpCallback.RESPONSE_UNEXPECTED);
					closeFile();
				}
			}

			@Override
			public void onFailed(Response reason) {
				callback.onFailed(reason);
				closeFile();
			}

			private void closeFile() {
				try {
					file.close();
				} catch (Exception e) {
					Logger.error(e);
				}
			}
		};

		downloadFilePart(url, startIndex, file, httpCallback);
	}

	/**
	 * download file with range request
	 *
	 * @param url          file url
	 * @param startIndex   start index to download, in bytes
	 * @param file         file to store
	 * @param httpCallback callback to report downloading status (success or failed)
	 */
	public void downloadFilePart(String url, final long startIndex, final RandomAccessFile file, HttpCallback<FileRangeInfo> httpCallback) {
		gsonService.downloadFile(url, String.format("bytes=%d-%d", startIndex, startIndex + RangePartLength - 1))
			.observeOn(Schedulers.io())
			.flatMap(new Func1<Response<ResponseBody>, Observable<FileRangeInfo>>() {
				@Override
				public Observable<FileRangeInfo> call(Response<ResponseBody> response) {
					if (response.isSuccessful()) {
						try {
							byte[] data = response.body().bytes();
							file.seek(startIndex);
							file.write(data);
							long length = HttpHeaderParser.parseContentRangeLength(response.headers());
							FileRangeInfo fileRangeInfo = new FileRangeInfo();
							fileRangeInfo.setSize(length);
							fileRangeInfo.setByteStart(startIndex);
							fileRangeInfo.setBytesReceived(data.length);
							return Observable.just(fileRangeInfo);
						} catch (Exception e) {
							Logger.error(e);
						}
					}
					return Observable.error(new HttpException(Response.error(response.code(), response.errorBody())));
				}
			})
			.compose(HttpTransformer.<FileRangeInfo>get())
			.retry(DOWNLOAD_RETRY_TIMES)
			.subscribe(httpCallback);
	}

	/// end of api

	private interface GsonInterface {
		@GET
		Observable<ResponseBody> httpGet(
			@Url String path,
			@Header("Local-Cache-Control") int local_cache_control,
			@Header("Cache-Control") String cache_control,
			@HeaderMap Map<String, String> headers
		);

		@GET
		Observable<Response<ResponseBody>> responseHttpGet(
			@Url String path,
			@Header("Local-Cache-Control") int local_cache_control,
			@Header("Cache-Control") String cache_control,
			@HeaderMap Map<String, String> headers
		);

		@GET
		Observable<ResponseBody> httpGetRaw(
			@Url String path
		);

		@HEAD
		@Headers("Connection: close")
		Observable<Response<Void>> responseHttpHeadRaw(
			@Url String path,
			@Header("If-Modified-Since") String ifModifiedSince
		);

		@POST
		@FormUrlEncoded
		Observable<ResponseBody> httpPost(
			@Url String path,
			@FieldMap Map<String, String> params
		);

		@POST
		Observable<ResponseBody> httpPost(
			@Url String path,
			@Body RequestZamplusModel body
		);

		@POST
		@FormUrlEncoded
		Observable<ResponseBody> httpPostRaw(
			@Url String path,
			@HeaderMap Map<String, String> headers,
			@FieldMap Map<String, String> params
		);

		@GET
		@Streaming
		Observable<Response<ResponseBody>> downloadFile(
			@Url String fileUrl,
			@Header("Range") String range   /// range part of downloading, in the format "bytes=%d-%d"
		);
	}

	private interface XMLInterface {
		@GET
		@Headers("Connection: close")
			/// set Connection: close to avoid unexpected end of stream during wispr authentication
		Observable<WisprReplyRoot> getWisprReply(
			@Url String path,
			@HeaderMap Map<String, String> headers
		);

		@POST
		@Headers("Connection: close") /// set Connection: close to avoid unexpected end of stream during wispr authentication
		@FormUrlEncoded
		Observable<WisprReplyRoot> postWisprReply(
			@Url String path,
			@HeaderMap Map<String, String> headers,
			@FieldMap Map<String, String> params
		);

		@GET
		@Headers("Connection: close")
			/// set Connection: close to avoid unexpected end of stream during wispr authentication
		Observable<CTNetPasswordReply> getCTNetPasswordReply(
			@Url String path,
			@HeaderMap Map<String, String> headers
		);

		@POST
		@FormUrlEncoded
		Observable<ResponseBody> httpPostRaw(
			@Url String path,
			@HeaderMap Map<String, String> headers,
			@FieldMap Map<String, String> params
		);
	}
}
