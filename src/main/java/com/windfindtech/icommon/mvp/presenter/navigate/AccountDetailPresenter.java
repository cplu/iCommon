package com.windfindtech.icommon.mvp.presenter.navigate;

import com.windfindtech.icommon.jsondata.user.UserData;
import com.windfindtech.icommon.jsondata.webservice.BaseResponse;
import com.windfindtech.icommon.jsondata.webservice.WSErrorResponse;
import com.windfindtech.icommon.mvp.view.navigate.IAccountDetailView;
import com.windfindtech.icommon.webservice.WSCallback;
import com.windfindtech.icommon.webservice.WSManager;

/**
 * Created by cplu on 2016/8/2.
 */
public class AccountDetailPresenter extends INavigatePresenter<IAccountDetailView> {
	@Override
	protected IAccountDetailView createDummy() {
		return IAccountDetailView.dummy;
	}

	/**
	 * update account info
	 *
	 * @param position
	 * @param userData
	 */
	public void updateAccountInfo(final int position, final UserData userData) {
		WSManager.instance().doUpdateAccountInfo(userData, new WSCallback<BaseResponse>() {
			@Override
			public void onSuccess(BaseResponse ret) {
				getView().onUpdateAccountInfoSuccess(position, userData);
			}

			@Override
			public void onFailed(WSErrorResponse reason) {
				getView().onUpdateAccountInfoFailed(reason);
			}
		});
	}
}
