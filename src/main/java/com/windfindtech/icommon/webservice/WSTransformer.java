package com.windfindtech.icommon.webservice;

import rx.Observable;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by cplu on 2016/7/20.
 * apply subscribeOn/observeOn/... on observable
 * this will not change the type of observable
 */
public class WSTransformer<T> implements Observable.Transformer<T, T> {

	private static final WSTransformer instance = new WSTransformer();

	@SuppressWarnings("unchecked")
	public static <T> WSTransformer<T> get() {
		return (WSTransformer<T>) instance;
	}

//	/**
//	 * check if response is successful
//	 * if not, parse WSErrorResponse and throw
//	 */
//	final Action1 actionDoOnNext = new Action1<Response>() {
//		@Override
//		public void call(Response response) {
////			Logger.debug("retrofit actionDoOnNext thread: " + Thread.currentThread().getName());
//			/// this should be called on io thread
//			if (!response.isSuccessful()) {
//				WSErrorResponse wsErrorResponse;
//				try {
//					/// note that response.errorBody() could only be read once
//					wsErrorResponse = GsonUtil.getGson().fromJson(response.errorBody().string(), WSErrorResponse.class);
//				} catch (Exception e) {
//					Logger.error(e);
//					wsErrorResponse = new WSErrorResponse();
//				}
//				wsErrorResponse.setStatusCode(response.code());
//				throw Exceptions.propagate(wsErrorResponse);
//			}
//		}
//	};

	@Override
	public Observable<T> call(Observable<T> tObservable) {
		return tObservable.subscribeOn(Schedulers.io())  /// set subscription to be run on io thread
//			.map(new Func1<Response<T>, Response<T>>() {
//				@Override
//				public Response<T> call(Response<T> response) {
//					if (!response.isSuccessful()) {
//						WSErrorResponse wsErrorResponse;
//						try {
//							/// note that response.errorBody() could only be read once
//							wsErrorResponse = GsonUtil.getGson().fromJson(response.errorBody().string(), WSErrorResponse.class);
//						} catch (Exception e) {
//							Logger.error(e);
//							wsErrorResponse = new WSErrorResponse();
//						}
//						wsErrorResponse.setStatusCode(response.code());
//						throw Exceptions.propagate(wsErrorResponse);
//					}
//					return response;
//				}
//			})
			.observeOn(AndroidSchedulers.mainThread()) /// next chains are called on main thread;
			.unsubscribeOn(Schedulers.io());
	}
}
