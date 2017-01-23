package com.windfindtech.icommon.advert;

import android.app.Activity;
import android.content.Intent;
import android.graphics.Bitmap;
import android.text.TextUtils;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.WFTApplication;
import com.windfindtech.icommon.activity.AdvertActivity;
import com.windfindtech.icommon.http.HttpRetroManager;
import com.windfindtech.icommon.iCommon;
import com.windfindtech.icommon.jsondata.advertisement.AdinallExtModel;
import com.windfindtech.icommon.jsondata.advertisement.BaseAdModel;
import com.windfindtech.icommon.jsondata.advertisement.DefaultAdModel;
import com.windfindtech.icommon.jsondata.advertisement.RequestZamplusDevice;
import com.windfindtech.icommon.jsondata.advertisement.RequestZamplusInsert;
import com.windfindtech.icommon.jsondata.advertisement.RequestZamplusModel;
import com.windfindtech.icommon.jsondata.advertisement.RequestZamplusSlot;
import com.windfindtech.icommon.jsondata.advertisement.ZamplusExtModel;
import com.windfindtech.icommon.jsondata.enumtype.AdVendor;
import com.windfindtech.icommon.jsondata.enumtype.AdvertSource;
import com.windfindtech.icommon.rx.RXLocalSubscriber;
import com.windfindtech.icommon.util.Utils;
import com.windfindtech.icommon.webservice.WSManager;

import org.pmw.tinylog.Logger;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by cplu on 2016/4/8.
 */
public class AdManager {
	private static final int RESULT_ADVERT_ACTIVITY = 20;

	//	private static final int TIMEOUT = 5000;   /// time out for fetching DefaultAdModel
	private static final String ZAMPLUS_ONLINE_URL = //"http://sandbox.zampdsp.com/rtb?vendor=VENDOR_ISH_MOB&f=json";
		"http://gw.gtags.net/rtb?vendor=VENDOR_ISH_MOB&f=json";

	private static final String ADINALL_TEST_URL = "http://app-test.adinall.com/api.m?adtype=3&width=720&height=1280&pkgname=&appname=&ua=&os=0&osv=&carrier=&conn=&ip=&density=&brand=&model=&uuid=&anid=&adid=";
	private static final String ADINALL_PROD_URL = "http://app.adinall.com/api.m?adtype=3&width=720&height=1280&pkgname=&appname=&ua=&os=0&osv=&carrier=&conn=&ip=&density=&brand=&model=&uuid=&anid=&adid=";

	private static final String URL_INTERNAL = "http://10.0.4.2:8090/ads/?spot=";
	private static final String URL_PROD = "https://portal.windfindtech.com/ads/?spot=";
	private static final String WIFI_WFT_ID = "app_fullscreen_online_android01";
	private static final String WIFI_OTHER_ID = "app_fullscreen_offline_android01";
//	private static final String WIFI_WFT_ID = "app_fullscreen_offline_01";
//	private static final String WIFI_OTHER_ID = "app_fullscreen_offline_01";

	public static final int IMAGE_WIDTH_LIMIT = 450;
	public static final int IMAGE_HEIGHT_LIMIT = 800;

	static class AdvertError extends Exception {
		AdvertError(String msg) {
			super(msg);
		}
	}

	/**
	 * prepare advertisement DefaultAdModel and associated image
	 * by default, there's a short timeout for image fetching in volley (1000ms) and 2 retry times, there's no need to change it here.
	 *
	 * @param current      current activity that should start AdActivity
	 * @param advertSource advertisement data
	 */
	public static final void prepareAdvertisement(final Activity current, AdvertSource advertSource) {
		String spot = advertSource == AdvertSource.FROM_OTHER ? WIFI_OTHER_ID : WIFI_WFT_ID;
		final String adBaseUrl = WSManager.instance().getBaseUrl() + "ads/";
		WSManager.instance().getAdvertisementBySpot(spot)
			.observeOn(Schedulers.io())
			.flatMap(new Func1<DefaultAdModel[], Observable<? extends BaseAdModel>>() {
				@Override
				public Observable<? extends BaseAdModel> call(DefaultAdModel[] adModels) {
					if (adModels != null && adModels.length > 0) {
						adModels[0].setBaseUrl(adBaseUrl);
						return parseAdModel(adModels[0]);
					} else {
						return Observable.error(new AdvertError("failed to fetch advertisement"));
					}
				}
			})
			.toList()  /// get two AdModel at the same time in onNext
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(new RXLocalSubscriber<List<BaseAdModel>>() {
				@Override
				public void onNext(List<BaseAdModel> baseAdModels) {
					showAdActivity(current, baseAdModels.get(0), baseAdModels.get(1));
				}
			});
	}

	/**
	 * note that adModel has a base url before calling this method
	 * Warning: adModel may be changed within this function
	 *
	 * @param adModel
	 */
	private static Observable<? extends BaseAdModel> parseAdModel(final DefaultAdModel adModel) {
//		URI uri = new URI(adModel.getBaseUrl());
		if (AdVendor.zamplus == adModel.getVendor()) {
			/// parse vendor zamplus
			RequestZamplusModel requestZamplusModel = generateRequestZamplusModel(adModel.getProperties());
			return HttpRetroManager.instance().postZamplusAdInfo(ZAMPLUS_ONLINE_URL, null, requestZamplusModel)
				.flatMap(new Func1<ZamplusExtModel, Observable<BaseAdModel>>() {
					@Override
					public Observable<BaseAdModel> call(final ZamplusExtModel zamplusExtModel) {
						String imgUrl = zamplusExtModel.getImageUrl();
						Logger.debug("imgUrl: " + imgUrl);
						if (!TextUtils.isEmpty(imgUrl)) {
							return WSManager.instance().getImageObservable(imgUrl, false, IMAGE_WIDTH_LIMIT, IMAGE_HEIGHT_LIMIT)
								.flatMap(new Func1<Bitmap, Observable<BaseAdModel>>() {
									@Override
									public Observable<BaseAdModel> call(Bitmap bitmap) {
										if(bitmap != null) {
											return Observable.just(adModel, zamplusExtModel);
										} else {
											return Observable.error(new AdvertError("no image got"));
										}
									}
								});
						}
						return Observable.error(new AdvertError("failed to fetch bitmap for advertisement"));
					}
				});
		} else if (AdVendor.adinall == adModel.getVendor()) {
			String adinall_url = WSManager.instance().isInTestEnv() ? ADINALL_TEST_URL : ADINALL_PROD_URL;
			return HttpRetroManager.instance().getGsonObservable(adinall_url + adModel.getId(), AdinallExtModel.class)
				.flatMap(new Func1<AdinallExtModel, Observable<BaseAdModel>>() {
					@Override
					public Observable<BaseAdModel> call(final AdinallExtModel adinallExtModel) {
						String imgUrl = adinallExtModel.getImageUrl();
						Logger.debug("imgUrl: " + imgUrl);
						if (!TextUtils.isEmpty(imgUrl)) {
							return WSManager.instance().getImageObservable(imgUrl, false, IMAGE_WIDTH_LIMIT, IMAGE_HEIGHT_LIMIT)
								.flatMap(new Func1<Bitmap, Observable<BaseAdModel>>() {
									@Override
									public Observable<BaseAdModel> call(Bitmap bitmap) {
										if(bitmap != null) {
											return Observable.just(adModel, adinallExtModel);
										} else {
											return Observable.error(new AdvertError("no image got"));
										}
									}
								});
						}
						return Observable.error(new AdvertError("failed to fetch bitmap for advertisement"));
					}
				});
		} else {
			String imgUrl = adModel.getImageUrl();
			Logger.debug("imgUrl: " + imgUrl);
			if (!TextUtils.isEmpty(imgUrl)) {
				return WSManager.instance().getImageObservable(imgUrl, false, IMAGE_WIDTH_LIMIT, IMAGE_HEIGHT_LIMIT)
					.flatMap(new Func1<Bitmap, Observable<DefaultAdModel>>() {
						@Override
						public Observable<DefaultAdModel> call(Bitmap bitmap) {
							/// return null so that no extended model is required
							return Observable.just(adModel, null);
						}
					});
			}
			return Observable.error(new AdvertError("failed to fetch bitmap for advertisement"));
		}
	}

	private static RequestZamplusModel generateRequestZamplusModel(Map<String, Object> properties) {
		String slot_id = (String) properties.get("slotId");
		//"1159395429071192164";
		ArrayList tagList = (ArrayList) properties.get("tags");

		RequestZamplusModel model = new RequestZamplusModel();
		model.setRequest_id(String.format("%d", 10000 + new Random().nextInt(90000)));
		/// create slot
		RequestZamplusSlot slot = new RequestZamplusSlot();
		slot.setSlot_id(slot_id);
		slot.setScreenwidth(Utils.s_deviceWidth);
		slot.setScreenheight(Utils.s_deviceHeight);
		RequestZamplusInsert insert = new RequestZamplusInsert();
		insert.setWidth(Utils.s_deviceWidth);
		insert.setHeight(Utils.s_deviceHeight);
		insert.setMaterial_type(1);
		slot.setInsert(insert);
		ArrayList<RequestZamplusSlot> slots = new ArrayList<>();
		slots.add(slot);
		model.setSlot(slots);
		/// create device
		RequestZamplusDevice device = new RequestZamplusDevice();
		device.setUa("chrome");
		device.setIp("58.247.81.182");
		device.setLanguage("zh");
		device.setOs(iCommon.DEVICE_OS);
		device.setPackname("iShanghai");
		if (tagList != null && tagList.size() > 0) {
			String[] tags = new String[tagList.size()];
			for (int i = 0; i < tags.length; i++) {
				tags[i] = (String) tagList.get(i);
			}
			device.setTags(tags);
		}
		model.setDevice(device);

		return model;
	}

	private static void showAdActivity(Activity current, BaseAdModel adModel, BaseAdModel extAdModel) {
		Logger.debug("show advert activity");
		if (!WFTApplication.inForeground) {
			return;
		}
		try {
			final Intent intent = new Intent(current, AdvertActivity.class);
			//intent.addFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
			intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
			intent.putExtra(AdvertActivity.ADMODEL_KEY, adModel);
			if (extAdModel != null) {
				intent.putExtra(AdvertActivity.EXTADMODEL_KEY, extAdModel);
			}
			current.startActivityForResult(intent, RESULT_ADVERT_ACTIVITY);
			current.overridePendingTransition(R.anim.alpha_enter, R.anim.alpha_exit);
		} catch (Exception e) {
			Logger.error(e);
		}
	}
}
