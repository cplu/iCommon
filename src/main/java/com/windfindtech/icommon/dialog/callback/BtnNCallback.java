package com.windfindtech.icommon.dialog.callback;

/**
 * Created by cplu on 2015/10/21.
 */
public interface BtnNCallback<Type> {
	/**
	 * nth button clicked
	 * @param id        unique id of dialog
	 * @param index     btn index
	 * @param result    result of dialog callback
	 */
	void onNthClicked(long id, int index, Type result);
}
