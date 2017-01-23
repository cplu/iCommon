package com.windfindtech.icommon.fragment.pts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.R2;
import com.windfindtech.icommon.fragment.management.WrappedFragment;
import com.windfindtech.icommon.mvp.presenter.pts.PointsRulesPresenter;
import com.windfindtech.icommon.mvp.view.pts.IPointsRulesView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by py on 2016/8/23.
 * 积分规则
 */
public class PointsRulesFragment extends WrappedFragment<IPointsRulesView, PointsRulesPresenter> implements IPointsRulesView {

	@BindView(R2.id.webview_points_rules)
	WebView m_webView;
	private View m_rootView;


	@Override
	protected PointsRulesPresenter createPresenter() {
		return new PointsRulesPresenter();
	}

	public PointsRulesFragment() {
		setPageName("PointsRulesFragment");
	}

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
		m_rootView = inflater.inflate(R.layout.fragment_points_rules, container, false);
		return m_rootView;
	}

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		m_rootView.setBackgroundColor(getResources().getColor(R.color.blue_purple));
		m_webView.loadUrl("http://10.0.4.2:8080/apps/story/demo/demostory.html");
	}

	@OnClick(R2.id.points_rules_back)
	public void onNaviBack() {
		m_primaryCallback.onNaviBack();
	}

}
