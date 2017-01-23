package com.windfindtech.icommon.retrofit;

import com.windfindtech.icommon.gson.GsonUtil;

import org.pmw.tinylog.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.adapter.rxjava.HttpException;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by cplu on 2016/7/20.
 */
public class JsonParserTransformer<Type> implements Observable.Transformer<ResponseBody, Type> {
	private final Class<Type> m_clazz;
	private final StreamParser<Type> m_parser;

	public JsonParserTransformer(StreamParser<Type> parser, Class<Type> clazz) {
		m_parser = parser;
		m_clazz = clazz;
	}

	@Override
	public Observable<Type> call(Observable<ResponseBody> observable) {
		return observable.observeOn(Schedulers.io())
			.flatMap(new Func1<ResponseBody, Observable<Type>>() {
				@Override
				public Observable<Type> call(ResponseBody responseBody) {
					InputStream inputStream = responseBody.byteStream();
					Type data;
					try {
						InputStreamReader reader = new InputStreamReader(inputStream);
						if (m_parser != null) {
							data = m_parser.parse(reader);
						} else {
							data = GsonUtil.getGson().fromJson(reader, m_clazz);
						}
						reader.close();
						if (data != null) {
							return Observable.just(data);
						}
					} catch (Exception e) {
						Logger.error(e);
//						throw Exceptions.propagate(e);
					}
					/// error response should have a code >= 400, use 400 for convenience
					return Observable.error(new HttpException(Response.error(400, responseBody)));
				}
			});
	}
}
