package com.windfindtech.icommon.mvp.presenter.pts;

import android.support.annotation.NonNull;

import com.windfindtech.icommon.jsondata.points.MyAchievementItem;
import com.windfindtech.icommon.jsondata.points.MyPointsItem;
import com.windfindtech.icommon.jsondata.points.MyRankItem;
import com.windfindtech.icommon.jsondata.webservice.WSErrorResponse;
import com.windfindtech.icommon.mvp.presenter.IFragmentPresenter;
import com.windfindtech.icommon.mvp.view.pts.IMyPointsView;
import com.windfindtech.icommon.webservice.WSCallback;
import com.windfindtech.icommon.webservice.WSManager;

/**
 * Created by py on 2016/8/23.
 */
public class MyPointsPresenter extends IFragmentPresenter<IMyPointsView> {
	private MyRankItem m_myRankItem;

	@Override
	protected IMyPointsView createDummy() {
		return IMyPointsView.dummy;
	}

	public void retrieveMyPoints() {
		WSManager.instance().doHttpGet("api/benefit/tasks/mine", MyPointsItem[].class, new WSCallback<MyPointsItem[]>() {
			@Override
			public void onSuccess(MyPointsItem[] ret) {
				getView().onMyPointsResult(ret);
			}

			@Override
			public void onFailed(@NonNull WSErrorResponse reason) {
				getView().onMyPointsResult(null);
			}
		});
	}

	public void retrieveMyAchievements() {
		WSManager.instance().doHttpGet("api/benefit/achievements/mine", MyAchievementItem[].class, new WSCallback<MyAchievementItem[]>() {
			@Override
			public void onSuccess(MyAchievementItem[] ret) {
				getView().onMyAchievementsResult(ret);
			}

			@Override
			public void onFailed(@NonNull WSErrorResponse reason) {
				getView().onMyAchievementsResult(null);
			}
		});
	}

	public void setMyRankItem(MyRankItem item) {
		m_myRankItem = item;
	}

//	public MyPointsItem[] getCompletedValue() {
//		MyPointsItem item1 = new MyPointsItem("使用i-Shanghai", "每次50分/一天2次", "已完成1次");
//		MyPointsItem item2 = new MyPointsItem("分享场点", "每次30分/一天3次", "已完成");
//		MyPointsItem item3 = new MyPointsItem("评价场点", "每次30分/一天3次", "已完成2次");
//		return new MyPointsItem[]{item1, item2, item3};
//	}
}
