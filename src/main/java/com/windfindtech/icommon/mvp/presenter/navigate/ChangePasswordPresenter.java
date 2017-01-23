package com.windfindtech.icommon.mvp.presenter.navigate;

import com.windfindtech.icommon.config.WFTConfig;
import com.windfindtech.icommon.jsondata.webservice.BaseResponse;
import com.windfindtech.icommon.jsondata.webservice.WSErrorResponse;
import com.windfindtech.icommon.mvp.view.navigate.IChangePasswordView;
import com.windfindtech.icommon.webservice.WSCallback;
import com.windfindtech.icommon.webservice.WSManager;

/**
 * Created by cplu on 2016/8/2.
 */
public class ChangePasswordPresenter extends INavigatePresenter<IChangePasswordView> {
	@Override
	protected IChangePasswordView createDummy() {
		return IChangePasswordView.dummy;
	}

	/**
	 * change the password
	 * @param old_pw
	 * @param new_pw
     */
	public void changePassword(String old_pw, final String new_pw) {
		WSManager.instance().doChangePassword(old_pw, new_pw, new WSCallback<BaseResponse>() {

			@Override
			public void onSuccess(BaseResponse ret) {
				WFTConfig.instance().storePassword(new_pw);
				getView().onChangePasswordSuccess(ret);
			}

			@Override
			public void onFailed(WSErrorResponse reason) {
				getView().onChangePasswordFailed(reason);
			}
		});
	}
}
