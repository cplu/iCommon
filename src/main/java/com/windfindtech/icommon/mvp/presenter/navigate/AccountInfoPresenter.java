package com.windfindtech.icommon.mvp.presenter.navigate;

import com.windfindtech.icommon.jsondata.user.UserData;
import com.windfindtech.icommon.jsondata.webservice.BaseResponse;
import com.windfindtech.icommon.jsondata.webservice.WSErrorResponse;
import com.windfindtech.icommon.mvp.model.local.ReservedMessageManager;
import com.windfindtech.icommon.mvp.view.navigate.IAccountInfoView;
import com.windfindtech.icommon.webservice.WSCallback;
import com.windfindtech.icommon.webservice.WSManager;

/**
 * Created by cplu on 2016/8/2.
 */
public class AccountInfoPresenter extends INavigatePresenter<IAccountInfoView> {
	@Override
	protected IAccountInfoView createDummy() {
		return IAccountInfoView.dummy;
	}

	/**
	 * update account info
	 *
	 * @param userData
	 * @param position
	 */
	public void updateAccountInfo(final UserData userData, final int position) {
		WSManager.instance().doUpdateAccountInfo(userData, new WSCallback<BaseResponse>() {
			@Override
			public void onSuccess(BaseResponse ret) {
				getView().onUpdateAccountInfoStatus(userData, position, true);
			}

			@Override
			public void onFailed(WSErrorResponse reason) {
				getView().onUpdateAccountInfoStatus(userData, position, false);
			}
		});
	}

	public void sendReservedMsg(final String reservedMsg) {
		WSManager.instance().doSendPersonalMessage(reservedMsg, new WSCallback<BaseResponse>() {
			@Override
			public void onSuccess(BaseResponse ret) {
				ReservedMessageManager.reservedMsgNotifier.updateData();
				getView().onReservedMsgSent(true, reservedMsg);
			}

			@Override
			public void onFailed(WSErrorResponse reason) {
				getView().onReservedMsgSent(false, reservedMsg);
			}
		});
	}
}
