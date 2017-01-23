package com.windfindtech.icommon.mvp.presenter.navigate;

import android.support.annotation.NonNull;

import com.windfindtech.icommon.jsondata.webservice.BaseResponse;
import com.windfindtech.icommon.jsondata.webservice.WSErrorResponse;
import com.windfindtech.icommon.mvp.view.navigate.IDeviceRegisterView;
import com.windfindtech.icommon.webservice.WSCallback;
import com.windfindtech.icommon.webservice.WSManager;

/**
 * Created by cplu on 2016/8/1.
 */
public class DeviceRegisterPresenter extends INavigatePresenter<IDeviceRegisterView>{
//	private final IDeviceRegisterView m_iView;

	public void changeMasterDevice(String code) {
		WSManager.instance().doDeviceChange(code, new WSCallback<BaseResponse>() {
			@Override
			public void onSuccess(BaseResponse ret) {
				getView().onDeviceChangeSuccess(ret);
			}

			@Override
			public void onFailed(@NonNull WSErrorResponse reason) {
				getView().onDeviceChangeFailed(reason);
			}
		});
	}

	public void deviceRegister() {
		WSManager.instance().doDeviceRegister(new WSCallback<BaseResponse>() {

			@Override
			public void onSuccess(BaseResponse ret) {
				getView().onDeviceRegisterSuccess(ret);
			}

			@Override
			public void onFailed(WSErrorResponse reason) {
				///
				getView().onDeviceRegisterFailed(reason);

			}
		});
	}

	@Override
	protected IDeviceRegisterView createDummy() {
		return IDeviceRegisterView.dummy;
	}
}
