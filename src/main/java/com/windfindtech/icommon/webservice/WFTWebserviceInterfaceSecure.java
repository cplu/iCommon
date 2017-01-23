package com.windfindtech.icommon.webservice;

import com.windfindtech.icommon.jsondata.usageinfo.UsageInfo;
import com.windfindtech.icommon.jsondata.user.UserData;
import com.windfindtech.icommon.jsondata.webservice.BaseResponse;
import com.windfindtech.icommon.jsondata.webservice.LoginResponse;
import com.windfindtech.icommon.jsondata.webservice.LogoutResponse;
import com.windfindtech.icommon.jsondata.webservice.RankResponse;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by cplu on 2016/7/18.
 * interfaces for webservice
 * urls start with "https" instead of "http"
 * every function should return "Observable<Response<Type>>"
 */
public interface WFTWebserviceInterfaceSecure {
	@POST("api/account/login")
	@FormUrlEncoded
	Observable<LoginResponse> logIn(
			@Query("app") String appname,
			@Field("username") String username,
			@Field("password") String password,
			@Field("client") String client,
			@Field("deviceOs") String deviceOs,
			@Field("version") int version,
	        @Field("authToken") String authToken    // auth token for quick login, only available when no password is provided
	);

	@GET("api/account/logout")
	Observable<LogoutResponse> logOut();

	@GET("api/account")
	Observable<UserData> getAccountInfo();

	@PUT("api/account")
	Observable<BaseResponse> updateAccountInfo(
			@Body UserData userConfig
	);

	@POST("api/account/password/change")
	@FormUrlEncoded
	Observable<BaseResponse> changePassword(
			@Field("oldpassword") String oldassword,
	        @Field("password") String newpassword
	);

	@GET("api/account/achievement/general")
	Observable<RankResponse> getAchievementData();

	@POST("api/feedback/app")
	Observable<BaseResponse> sendUsageinfo(
			@Body UsageInfo usageInfo
	);

	@POST("api/feedback/operations")
	@Multipart
	Observable<BaseResponse> sendStatistics(
			@PartMap() Map<String, RequestBody> gzParts
	);

	@POST("api/feedback/crash_report")
	@Multipart
	Observable<BaseResponse> uploadAppLog(
			@PartMap() Map<String, RequestBody> multipartBody
	);

	@GET
	Observable<ResponseBody> httpGet(
		@Url String path,
		@Header("Local-Cache-Control") int local_cache_control,
		@Header("Cache-Control") String cache_control
	);
}
