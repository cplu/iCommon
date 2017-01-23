package com.windfindtech.icommon.webservice;

import org.pmw.tinylog.Logger;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

import okhttp3.Headers;

/**
 * Created by cplu on 2016/7/20.
 */
public class WSResponseWithHeaders {
	private transient Headers m_headers;

	public WSResponseWithHeaders() {

	}

	public void setHeaders(Headers headers) {
		m_headers = headers;
	}

	public Date getResponseDate() {
		if(m_headers != null) {
			try {
				String date = m_headers.get("Date");
				SimpleDateFormat format = new SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss zzz", Locale.ENGLISH);
				return format.parse(date);
			} catch (ParseException e) {
				Logger.error(e);
				return null;
			}
		}
		return null;
	}
}
