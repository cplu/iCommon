package com.windfindtech.icommon.mvp.presenter.navigate;

import com.windfindtech.icommon.mvp.view.navigate.IAccountDevicesView;

/**
 * Created by cplu on 2016/8/2.
 */
public class AccountDevicesPresenter extends INavigatePresenter<IAccountDevicesView>{
	@Override
	protected IAccountDevicesView createDummy() {
		return IAccountDevicesView.dummy;
	}
}
