package com.windfindtech.icommon.dialog.callback;

import com.windfindtech.icommon.jsondata.webservice.RatingCheck;

/**
 * Created by cplu on 2015/10/20.
 */
public interface RatingCallback extends BaseCallback{
	/**
	 * notify whether rating succeeds
	 * @param id
	 * @param success
	 */
	void onRatingFinished(long id, boolean success);

	/**
	 * notify that we would like to show map containing position of rating site
	 * @param id
	 */
	void onShowMap(long id, RatingCheck ratingCheck, int width, int height);
}
