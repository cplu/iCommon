package com.windfindtech.icommon.mvp.presenter.navigate;

import android.graphics.Bitmap;

import com.windfindtech.icommon.bitmap.DrawableManager;
import com.windfindtech.icommon.iCommon;
import com.windfindtech.icommon.jsondata.user.UserData;
import com.windfindtech.icommon.jsondata.webservice.BaseResponse;
import com.windfindtech.icommon.jsondata.webservice.DeviceData;
import com.windfindtech.icommon.jsondata.webservice.VersionCheck;
import com.windfindtech.icommon.jsondata.webservice.WSErrorResponse;
import com.windfindtech.icommon.mvp.model.MType;
import com.windfindtech.icommon.mvp.model.local.AvatarManager;
import com.windfindtech.icommon.mvp.model.local.DeviceManager;
import com.windfindtech.icommon.mvp.model.local.ReservedMessageManager;
import com.windfindtech.icommon.mvp.model.local.UsrDataManager;
import com.windfindtech.icommon.mvp.model.local.VersionManager;
import com.windfindtech.icommon.mvp.presenter.IFragmentPresenter;
import com.windfindtech.icommon.mvp.view.navigate.INavigateView;
import com.windfindtech.icommon.rx.RXLocalSubscriber;
import com.windfindtech.icommon.webservice.WSCallback;
import com.windfindtech.icommon.webservice.WSManager;

import org.pmw.tinylog.Logger;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.ArrayList;

import rx.Observable;
import rx.Subscriber;
import rx.Subscription;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cplu on 2016/7/29.
 */
public abstract class INavigatePresenter<ViewType extends INavigateView> extends IFragmentPresenter<ViewType> {

	private Subscription m_avatarSubscription;
	private Subscription m_userDataSubscription;

	private Subscription m_bitmapHandlerSubscription;


	private static boolean s_bImageCaptureParsed = false;

	/**
	 * handle image capture result from camera
	 *
	 * @param inputStream  inputStream of bitmap
	 * @param orientation  orientation of bitmap
	 * @param uploadBitmap whether we should upload the bitmap to webservice, generally true
	 */
	public void handleImageCapture(final InputStream inputStream, final int orientation, final boolean uploadBitmap) {
		s_bImageCaptureParsed = true;
		m_bitmapHandlerSubscription = Observable.create(new Observable.OnSubscribe<Bitmap>() {
			@Override
			public void call(final Subscriber<? super Bitmap> subscriber) {
				try {
					final Bitmap bitmapFromUri = getBitmapFrom(inputStream, orientation);
					inputStream.close();
					if (uploadBitmap) {
						ByteArrayOutputStream bos = new ByteArrayOutputStream();
						bitmapFromUri.compress(Bitmap.CompressFormat.JPEG, 90, bos);
						final byte[] bytes = bos.toByteArray();
						bos.close();
						WSManager.instance().doUpdateAccountAvatar(bytes, new WSCallback<BaseResponse>() {
							@Override
							public void onSuccess(BaseResponse ret) {
								subscriber.onNext(bitmapFromUri);
								subscriber.onCompleted();
							}

							@Override
							public void onFailed(WSErrorResponse reason) {
								subscriber.onError(reason);
							}
						});
					} else {
						subscriber.onNext(bitmapFromUri);
						subscriber.onCompleted();
					}
				} catch (Exception e) {
					subscriber.onError(e);
				}
			}
		})
			.subscribeOn(Schedulers.computation())
			.observeOn(AndroidSchedulers.mainThread())
			.subscribe(new Subscriber<Bitmap>() {
				@Override
				public void onCompleted() {
					s_bImageCaptureParsed = false;
					getView().onAvatarUploadResult(true);
				}

				@Override
				public void onError(Throwable throwable) {
					s_bImageCaptureParsed = false;
					getView().onAvatarUploadResult(false);
					/// notify current cached bitmap
					AvatarManager.avatarNotifier.notifySubscribers();
					Bitmap bitmap = AvatarManager.avatarNotifier.getData();
					if (bitmap != null) {
						getView().onAvatar(bitmap);
					}
				}

				@Override
				public void onNext(Bitmap bitmap) {
					Logger.debug("test:bitmap upload notified");
					/// notify uploaded bitmap
					AvatarManager.avatarNotifier.setData(bitmap);
					AvatarManager.avatarNotifier.notifySubscribers();
				}
			});
	}

	/**
	 * whether we are uploading avatar now
	 */
	public boolean isAvatarUploading() {
		return m_bitmapHandlerSubscription != null && !m_bitmapHandlerSubscription.isUnsubscribed();
	}

	private Bitmap getBitmapFrom(InputStream inputStream, int orientation) throws Exception {
		final Bitmap bitmap = DrawableManager.instance().getBitmapLimited(inputStream,
			AvatarManager.AVATAR_WIDTH_LIMIT, AvatarManager.AVATAR_HEIGHT_LIMIT);
		if (bitmap == null) {
			return null;
		}

		final Bitmap rotated = iCommon.getRotatedBitmap(bitmap, orientation,
			AvatarManager.AVATAR_WIDTH_LIMIT, AvatarManager.AVATAR_HEIGHT_LIMIT);
		return rotated;
	}

	@Override
	public void detach() {
		AvatarManager.avatarNotifier.unregister(m_avatarSubscription);
		AvatarManager.avatarNotifier.unregister(m_userDataSubscription);

		super.detach();
	}

	@Override
	public void attach(ViewType view, int notifiers) {
		super.attach(view, notifiers);

		if ((notifiers & MType.DEVICE_NOTIFIER) > 0) {
			m_subscriptions.add(
				DeviceManager.deviceNotifier.register(new RXLocalSubscriber<ArrayList<DeviceData>[]>() {
					@Override
					public void onNext(ArrayList<DeviceData>[] devices) {
						getView().onDeviceListChanged(devices);
					}
				}, (notifiers & MType.DEVICE_UPDATE_BIT) > 0)
			);
		}
		if ((notifiers & MType.RESERVED_MESSAGE_NOTIFIER) > 0) {
			m_subscriptions.add(
				ReservedMessageManager.reservedMsgNotifier.register(new RXLocalSubscriber<Boolean>() {
					@Override
					public void onNext(Boolean hasReservedMessage) {
						getView().onReservedMessage(hasReservedMessage);
					}
				}, (notifiers & MType.RESERVED_MESSAGE_UPDATE_BIT) > 0)
			);
		}
		if ((notifiers & MType.VERSION_NOTIFIER) > 0) {
			m_subscriptions.add(
				VersionManager.versionNotifier.register(new RXLocalSubscriber<VersionCheck>() {
					@Override
					public void onNext(VersionCheck versionCheck) {
						getView().onVersionCheck(versionCheck);
					}
				}, (notifiers & MType.VERSION_UPDATE_BIT) > 0)
			);
		}

		if (getView().avatarConcerned()) {
			m_avatarSubscription = AvatarManager.avatarNotifier.register(new RXLocalSubscriber<Bitmap>() {
				@Override
				public void onNext(Bitmap bitmap) {
					getView().onAvatar(bitmap);
				}
			}, false);
		}
		if (getView().usrDataConcerned()) {
			m_userDataSubscription = UsrDataManager.usrDat.register(new RXLocalSubscriber<UserData>() {
				@Override
				public void onNext(UserData userData) {
					getView().onUserData(userData);
				}
			}, true);
		}
	}

	public void getUserData() {
		UserData data = UsrDataManager.usrDat.getOrUpdateData();
		if (data != null) {
			getView().onUserData(data);
		}
	}

	public void getUserAvatar() {
		if (!s_bImageCaptureParsed) {
			AvatarManager.avatarNotifier.updateData();
		} else {

		}
//		if(avatar != null) {
//			getView().onAvatar(avatar);
//		}
	}

	public void getReservedMsgRequired() {
		Boolean ret = ReservedMessageManager.reservedMsgNotifier.getData();
		if (ret != null) {
			getView().onReservedMessage(ret);
		}
	}
}
