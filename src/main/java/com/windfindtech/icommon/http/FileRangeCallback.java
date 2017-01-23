package com.windfindtech.icommon.http;

/**
 * Created by cplu on 2016/6/24.
 */
public abstract class FileRangeCallback<T> extends HttpCallback<T> {
	/**
	 * notify the current progress of downloading, and tells if we should continue downloading
	 * @param progress
	 * @param percent
	 * @return  true if downloading should be continued, false otherwise
	 */
	public abstract boolean onProgress(long progress, int percent);
}
