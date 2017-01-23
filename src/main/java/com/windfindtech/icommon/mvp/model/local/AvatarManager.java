package com.windfindtech.icommon.mvp.model.local;

import android.graphics.Bitmap;

import com.windfindtech.icommon.mvp.model.MNotifierManager;
import com.windfindtech.icommon.webservice.WSManager;

import rx.Observable;


/**
 * Created by cplu on 2016/7/29.
 */
public class AvatarManager extends MNotifierManager<Bitmap> {
	public static AvatarManager avatarNotifier = new AvatarManager();

	public static final int AVATAR_WIDTH_LIMIT = 512;
	public static final int AVATAR_HEIGHT_LIMIT = 512;

	@SuppressWarnings("unchecked")
	private AvatarManager() {
		super();
	}

	@Override
	public Observable<Bitmap> getNetworkObservable() {
		return WSManager.instance().getAccountAvatar(AVATAR_WIDTH_LIMIT, AVATAR_HEIGHT_LIMIT);
	}
}
