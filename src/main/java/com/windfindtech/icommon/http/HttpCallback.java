package com.windfindtech.icommon.http;

import okhttp3.MediaType;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by cplu on 2016/7/21.
 */
public abstract class HttpCallback<SuccessType> extends Subscriber<SuccessType> {
	public static final Response RESPONSE_UNEXPECTED = Response.error(400, ResponseBody.create(MediaType.parse("text/plain"), ""));
	public abstract void onSuccess(SuccessType ret);
	public abstract void onFailed(Response reason);

	@Override
	public void onCompleted() {

	}

	@Override
	public void onError(Throwable throwable) {
		/// this should be called on main thread
		if (throwable instanceof HttpException) {
			HttpException httpException = (HttpException) throwable;
			onFailed(httpException.response());
		} else {
			/// create an artificial error
			onFailed(RESPONSE_UNEXPECTED);
		}
	}

	@Override
	public void onNext(SuccessType ret) {
		onSuccess(ret);
	}
}
