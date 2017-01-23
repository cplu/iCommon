package com.windfindtech.icommon.retrofit;

import java.io.InputStreamReader;

/**
 * Created by cplu on 2016/7/19.
 */
public interface StreamParser<Type> {
	/**
	 * parse reader to a given type, may return null for invalid result
	 * @param reader
	 * @return
	 */
	Type parse(InputStreamReader reader);
}
