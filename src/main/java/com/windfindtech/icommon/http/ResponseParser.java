package com.windfindtech.icommon.http;

import com.google.gson.JsonSyntaxException;

/**
 * Created by cplu on 2015/8/19.
 */
public interface ResponseParser<Type> {
	Type parseResponse(String rawJsonData) throws JsonSyntaxException;
}
