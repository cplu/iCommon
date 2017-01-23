package com.windfindtech.icommon.webservice;

import okhttp3.ResponseBody;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by cplu on 2016/7/18.
 */
public interface ImageInterface {
	String LOCAL_TIME_START_KEY = "Local-Time-Start";

	@GET
	@Streaming
	Observable<ResponseBody> getImage(
			@Url String path,
	        @Header("Local-Cache-Control") int local_cache_control,
			@Header("Cache-Control") String cache_control,
	        @Header(LOCAL_TIME_START_KEY) long local_time_start
	);
}
