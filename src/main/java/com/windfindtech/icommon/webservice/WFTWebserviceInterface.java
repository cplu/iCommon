package com.windfindtech.icommon.webservice;

import com.windfindtech.icommon.jsondata.advertisement.DefaultAdModel;
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
import com.windfindtech.icommon.jsondata.user.UserData;
import com.windfindtech.icommon.jsondata.webservice.BaseResponse;
import com.windfindtech.icommon.jsondata.webservice.DeviceData;
import com.windfindtech.icommon.jsondata.webservice.FeedbackInfo;
import com.windfindtech.icommon.jsondata.webservice.RatingCheck;
import com.windfindtech.icommon.jsondata.webservice.ServerStatus;
import com.windfindtech.icommon.jsondata.webservice.VersionCheck;

import java.util.Map;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.PartMap;
import retrofit2.http.Path;
import retrofit2.http.Query;
import retrofit2.http.Url;
import rx.Observable;

/**
 * Created by cplu on 2016/7/18.
 * interfaces for webservice
 * urls start with "http" instead of "https"
 * every function should return "Observable<Type>"
 */
public interface WFTWebserviceInterface {
	@POST("api/account/getPassword")
	@FormUrlEncoded
	Observable<BaseResponse> getPassword(
			@Field("userName") String username
	);

	@PUT("api/account/devices/last/token")
	@FormUrlEncoded
	Observable<BaseResponse> updateDeviceToken(
			@Field("deviceOs") String deviceOs,
			@Field("token") String token
	);

	@PUT("api/account/avatar")
	@Multipart
	Observable<BaseResponse> updateAccountAvatar(
			@PartMap() Map<String, RequestBody> imageParts
	);

	@POST("api/feedback/user")
	Observable<BaseResponse> submitFeedback(
			@Body FeedbackInfo feedbackInfo
	);

	@POST("api/sites/submit")
	Observable<BaseResponse> submitNewSiteInfo(
			@Body SubmitNewSiteItem newSiteItem
	);

	@POST("api/account/devices/master/request")
	Observable<BaseResponse> deviceRegister();

	@POST("api/account/devices/master/change")
	@FormUrlEncoded
	Observable<BaseResponse> deviceChange(
			@Field("challenge") String code,
	        @Field("deviceOs") String deviceOs,
	        @Field("deviceType") String deviceType
	);

	@GET("api/account/devices")
	Observable<DeviceData[]> getDeviceList();

	@POST("api/account/devices/master/verify")
	Observable<Boolean> verifyMasterDevice();

	@POST("api/account/devices/master/verify")
	Observable<Boolean> verifyMasterDevice2();

	@GET("api/echo/exceptions")
	Observable<String> doTestException();

	@PUT("api/account/devices/{devicename}/name")
	@FormUrlEncoded
	Observable<BaseResponse> changeDeviceName(
			@Path("devicename") String devicename,
	        @Field("name") String newName
	);

	@PUT("api/account/devices/{devicename}/status")
	@FormUrlEncoded
	Observable<BaseResponse> changeDeviceStatus(
			@Path("devicename") String devicename,
			@Field("status") String newStatus
	);

	@DELETE("api/account/devices/{devicename}")
	Observable<BaseResponse> deleteDevice(
			@Path("devicename") String devicename
	);

	@PUT("api/account/personal_message")
	@FormUrlEncoded
	Observable<BaseResponse> sendPersonalMessage(
			@Field("message") String message
	);

	@GET("api/account/has_personal_message")
	Observable<Boolean> hasPersonalMessage();

	@GET("api/ranking/site/last")
	Observable<RatingCheck> checkRatingRequired();

	@GET("api/ranking/site/reasons")
	Observable<String[]> getRatingReasons();

	@POST("api/ranking/site/rank")
	Observable<BaseResponse> sendRatingResultForLastSite(
			@Body RatingResult ratingResult
	);

	@POST("api/sites/{sitecode}/rank")
	Observable<BaseResponse> sendRatingResult(
			@Path("sitecode") String siteCode,
			@Body RatingResult ratingResult
	);

	@POST("api/ranking/do_not_bother_me")
	Observable<BaseResponse> sendNoMoreRanking();

	@GET("api/activities/{eventtype}?deviceOs=android")
	Observable<EventList> getAllEvents(
			@Path("eventtype") String eventType
	);

	@GET("api/activities/{eventtype}/{eventid}")
	Observable<EventDetail> getEventDetail(
			@Path("eventtype") String eventType,
	        @Path("eventid") String eventId
	);

	@GET("api/activities/{eventtype}/{eventid}/images")
	Observable<EventImage[]> getEventImageList(
			@Path("eventtype") String eventType,
			@Path("eventid") String eventId
	);

	@GET("api/activities/{eventtype}/{eventid}/awards/last")
	Observable<EventAward[]> getLastAward(
			@Path("eventtype") String eventType,
			@Path("eventid") String eventId
	);

	@GET("api/activities/{eventtype}/{eventid}/participations/last")
	Observable<EventParticipationModel> getEventHistoryLast(
			@Path("eventtype") String eventType,
			@Path("eventid") String eventId
	);

	@POST("api/activities/{eventtype}/{eventid}/participate")
	@Multipart
	Observable<BaseResponse> participateSweepstake(
			@Path("eventtype") String eventType,
			@Path("eventid") String eventId,
			@PartMap() Map<String, RequestBody> imageBody
	);

	@POST("api/activities/{eventtype}/{eventid}/participate")
	Observable<EventCheckinInfo> participateCheckin(
			@Path("eventtype") String eventType,
			@Path("eventid") String eventId
	);

	@POST("api/activities/{eventtype}/{eventid}/participate")
	Observable<EventPromotionInfo> participatePromotion(
			@Path("eventtype") String eventType,
			@Path("eventid") String eventId
	);

	@GET
	Observable<ResponseBody> httpGet(
			@Url String path,
			@Header("Local-Cache-Control") int local_cache_control,
			@Header("Cache-Control") String cache_control
	);

	@GET
	Observable<Response<ResponseBody>> responseHttpGet(
		@Url String path,
		@Header("Local-Cache-Control") int local_cache_control,
		@Header("Cache-Control") String cache_control
	);

	@GET("api/radio/{id}")
	Observable<AudioList> getLizhiAudioList(
			@Path("id") String id,
	        @Query("pageCount") int pageCount,
	        @Query("pageIndex") int pageIndex
	);

	@GET("api/system/app_version")
	Observable<VersionCheck> getLeastVersionInfo(
			@Query("app") String appName,
	        @Query("deviceOs") String deviceOs,
	        @Query("version") int versionNum
	);

	@POST("api/tweets")
	@Multipart
	Observable<ResponseBody> sendTweet(
			@PartMap() Map<String, RequestBody> tweetBody
	);

	@POST("api/tweets/{id}/comments")
	@FormUrlEncoded
	Observable<ResponseBody> sendMessageReply(
			@Path("id") String id,
	        @Field("message") String message
	);

	@DELETE("api/tweets/{id}")
	Observable<ResponseBody> messageDelete(
			@Path("id") String id
	);

	@POST("api/tweets/comment/read")
	Observable<BaseResponse> readComments(
			@Body long[] commentIds
	);

	@GET("ads/")
	Observable<DefaultAdModel[]> getAdvertisementBySpot(
			@Query("spot") String spot,
	        @Query("client") String client
	);

	@GET("api/system/server/status")
	Observable<ServerStatus> getServerStatus();

	@POST("api/share/usage")
	@FormUrlEncoded
	Observable<ResponseBody> getMarkerItemUrl(
			@Field("siteObjectId") String siteObjectId
	);

	@GET("api/account")
	Observable<UserData> getAccountInfo();

	@POST("api/sites/near")
	Observable<NearSiteItemWrapper[]> getNearSite(
			@Query("z") int zoom,
			@Body NearSiteItemPostBody body
	);
}
