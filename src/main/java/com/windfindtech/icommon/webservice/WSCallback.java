package com.windfindtech.icommon.webservice;

import android.support.annotation.NonNull;

import com.windfindtech.icommon.gson.GsonUtil;
import com.windfindtech.icommon.jsondata.webservice.WSErrorResponse;
import com.windfindtech.icommon.util.Utils;

import org.pmw.tinylog.Logger;

import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Subscriber;

/**
 * Created by cplu on 2016/7/21.
 */
public abstract class WSCallback<SuccessType> extends Subscriber<SuccessType> {
	public abstract void onSuccess(SuccessType ret);
	public abstract void onFailed(@NonNull WSErrorResponse reason);

	@Override
	public void onCompleted() {

	}

	@Override
	public void onError(Throwable throwable) {
		Logger.debug("response error: " + throwable);
		/// this should be called on main thread
		if (throwable instanceof HttpException) {
			Response response = ((HttpException) throwable).response();
			if (response != null) {
				WSErrorResponse wsErrorResponse;
				try {
					// note that response.errorBody() could only be read once
					wsErrorResponse = GsonUtil.getGson().fromJson(response.errorBody().string(), WSErrorResponse.class);
				} catch (Exception e) {
					Logger.error(e);
					wsErrorResponse = new WSErrorResponse();
				}
				wsErrorResponse.setStatusCode(response.code());
				onFailed(wsErrorResponse);
			}
		}
		WSErrorResponse wsErrorResponse = new WSErrorResponse();
		wsErrorResponse.setMessage(Utils.instance().getNetworkErrorString());
		onFailed(wsErrorResponse);   /// null represents unknown error (e.g. network error)
	}

	@Override
	public void onNext(SuccessType ret) {
		onSuccess(ret);
	}
}
