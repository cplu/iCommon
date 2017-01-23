package com.windfindtech.icommon.mvp.presenter.navigate;

import com.windfindtech.icommon.mvp.view.navigate.IAppSettingsView;

/**
 * Created by cplu on 2016/8/2.
 */
public class AppSettingsPresenter extends INavigatePresenter<IAppSettingsView> {
	@Override
	protected IAppSettingsView createDummy() {
		return IAppSettingsView.dummy;
	}
}
