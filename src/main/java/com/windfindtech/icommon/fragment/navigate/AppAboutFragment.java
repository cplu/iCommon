package com.windfindtech.icommon.fragment.navigate;

import android.os.Bundle;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.view.View;
import android.widget.TextView;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.R2;
import com.windfindtech.icommon.iCommon;
import com.windfindtech.icommon.jsondata.webservice.VersionCheck;
import com.windfindtech.icommon.mvp.model.MType;
import com.windfindtech.icommon.mvp.model.local.VersionManager;
import com.windfindtech.icommon.mvp.presenter.navigate.AppAboutPresenter;
import com.windfindtech.icommon.mvp.view.navigate.IAppAboutView;

import butterknife.BindView;
import butterknife.OnClick;

public class AppAboutFragment extends NavigateFragment<IAppAboutView, AppAboutPresenter>
	implements IAppAboutView {

	@BindView(R2.id.about_logo_txt)
	TextView m_logoTxt;
	@BindView(R2.id.windfindtech_info)
	TextView m_companyTxt;
	@BindView(R2.id.windfindtech_mainpage)
	TextView m_companyMainPageTxt;

	public AppAboutFragment() {
		setPageName("AboutView");
	}

	@Override
	protected int getLayoutID() {
		return R.layout.fragment_about;
	}

	@Override
	protected int getTitleID() {
		return R.string.title_about;
	}

	@Override
	protected int getNotificationType() {
		return MType.WS_RECEIVER
		       | MType.VERSION_NOTIFIER;
	}

	@Override
	protected AppAboutPresenter createPresenter() {
		return new AppAboutPresenter();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		m_listTags = new String[]{
			null,   // 用户须知
			null    // 版本更新
		};
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		SpannableString spannableString = m_presenter.getLogoSpannableString(m_logoTxt.getText().toString());
		if (spannableString != null) {
			m_logoTxt.setText(spannableString);
		}
		SpannableString spanable_info = m_presenter.getCompanySpannableString(m_companyTxt.getText().toString(),
			new ForegroundColorSpan(getResources().getColor(R.color.default_link_color)));
		m_companyTxt.setText(spanable_info);
		m_companyTxt.setMovementMethod(LinkMovementMethod.getInstance());
	}

	@Override
	protected NavListAdapter createNavAdapter() {
		return new AppAboutAdapter(getActivity(), R.layout.list_item_with_icon);
	}

	@OnClick(R2.id.windfindtech_mainpage)
	public void onMainPageClick() {
		m_primaryCallback.onForwardWebView(m_companyMainPageTxt.getText().toString(), null);
	}

	@Override
	public void performItemClick(final int position, long id) {
		if (position == AppAboutAdapter.USER_AGREEMENT_INDEX) {
			m_primaryCallback.onForwardWebView(
				iCommon.getTermsURL(getActivity()), getString(R.string.user_agreement_link));
		} else if (position == AppAboutAdapter.VERSION_UPDATE_INDEX) {
			VersionCheck versionCheck = VersionManager.versionNotifier.getData();
			if (versionCheck != null) {
				m_primaryCallback.onVersionUpdateRequired(versionCheck);
			}
		}
	}

	@Override
	public void onVersionCheck(VersionCheck versionCheck) {
		showVersionHint(versionCheck);
//		if(isStarted) {
//			m_primaryCallback.onVersionUpdateRequired(versionCheck);
//		}
	}

	private void showVersionHint(VersionCheck versionCheck) {
		boolean ret = versionCheck != null && !versionCheck.isLatest();
		m_adapter.setDataHintNumber(AppAboutAdapter.VERSION_UPDATE_INDEX, ret ? 1 : 0, true);
	}
}
