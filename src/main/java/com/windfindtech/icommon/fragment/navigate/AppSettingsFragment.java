package com.windfindtech.icommon.fragment.navigate;

import android.os.Bundle;
import android.view.View;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.mvp.presenter.navigate.AppSettingsPresenter;
import com.windfindtech.icommon.mvp.view.navigate.IAppSettingsView;

/**
 * 个人中心-->设置界面
 *
 * @author cplu
 */
public class AppSettingsFragment extends NavigateFragment<IAppSettingsView, AppSettingsPresenter> implements IAppSettingsView{
//    private ConfirmDialog m_confirmDlg;
    private AppSettingsAdapter m_appSettingsAdapter;

    public AppSettingsFragment() {
        setPageName("SettingView");
    }

    @Override
    protected AppSettingsPresenter createPresenter() {
        return new AppSettingsPresenter();
    }

    @Override
    protected int getLayoutID() {
        return R.layout.fragment_settings;
    }

    @Override
    protected int getTitleID() {
        return R.string.title_settings;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        m_appSettingsAdapter = new AppSettingsAdapter(getActivity());
        if(m_listRecycled != null) {
            m_listRecycled.setAdapter(m_appSettingsAdapter);
        }
    }

    @Override
    protected NavListAdapter createNavAdapter() {
        return null;
    }

    @Override
    public void onResume(){
        super.onResume();

    }

    @Override
    public void onPause(){
        super.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
