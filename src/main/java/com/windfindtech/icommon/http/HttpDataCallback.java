package com.windfindtech.icommon.http;

/**
 * Created by cplu on 2015/9/11.
 * Callback with only one abstract method on success
 */
public abstract class HttpDataCallback<DataType> {
	/**
	 * whether we should update data to ui
	 * @param data              data to be updated
	 *
	 */
	public abstract void onUpdateData(DataType data);
}
