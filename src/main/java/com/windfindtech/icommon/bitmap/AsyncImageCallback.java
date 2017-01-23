package com.windfindtech.icommon.bitmap;

import android.graphics.Bitmap;

import rx.Subscriber;

/**
 * Created by cplu on 2015/10/22.
 */
public abstract class AsyncImageCallback extends Subscriber<Bitmap>{
	public void onCompleted() {
	}

	public void onError(Throwable var1) {

	}
}
