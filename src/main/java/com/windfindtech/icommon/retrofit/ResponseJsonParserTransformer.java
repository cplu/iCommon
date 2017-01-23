package com.windfindtech.icommon.retrofit;

import com.windfindtech.icommon.gson.GsonUtil;

import org.pmw.tinylog.Logger;

import java.io.InputStream;
import java.io.InputStreamReader;

import okhttp3.ResponseBody;
import retrofit2.Response;
import rx.Observable;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

/**
 * Created by cplu on 2016/7/20.
 */
public class ResponseJsonParserTransformer<Type> implements Observable.Transformer<Response<ResponseBody>, Response<Type>> {
	private final Class<Type> m_clazz;
	private final StreamParser<Type> m_parser;

	public ResponseJsonParserTransformer(StreamParser<Type> parser, Class<Type> clazz) {
		m_parser = parser;
		m_clazz = clazz;
	}

	@Override
	public Observable<Response<Type>> call(Observable<Response<ResponseBody>> observable) {
		return observable.observeOn(Schedulers.io())
			.map(new Func1<Response<ResponseBody>, Response<Type>>() {
				@Override
				public Response<Type> call(Response<ResponseBody> responseBody) {
					if (responseBody.isSuccessful()) {
						InputStream inputStream = responseBody.body().byteStream();
						Type data;
						try {
							InputStreamReader reader = new InputStreamReader(inputStream);
							if(m_parser != null) {
								data = m_parser.parse(reader);
							} else {
								data = GsonUtil.getGson().fromJson(reader, m_clazz);
							}
							reader.close();
							if(data != null) {
								return Response.success(data, responseBody.raw());
							}
						} catch (Exception e) {
							Logger.error(e);
						}
					}
					/// error response should have a code >= 400, use 400 for convenience
					return Response.error(400, responseBody.errorBody());
				}
			});
	}
}
