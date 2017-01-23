package com.windfindtech.icommon.fragment.pts;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.R2;
import com.windfindtech.icommon.fragment.management.WrappedFragment;
import com.windfindtech.icommon.mvp.presenter.pts.PointsStorePresenter;
import com.windfindtech.icommon.mvp.view.pts.IPointsStoreView;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * Created by py on 2016/8/23.
 * 积分商城
 */
public class PointsStoreFragment extends WrappedFragment<IPointsStoreView,PointsStorePresenter> implements IPointsStoreView{

    @BindView(R2.id.webview_points_store)
    WebView m_webView;
    private View m_rootView;


    @Override
    protected PointsStorePresenter createPresenter() {
        return new PointsStorePresenter();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
         m_rootView = inflater.inflate(R.layout.fragment_points_store,container,false);
        return m_rootView;
    }

    public PointsStoreFragment() {
        setPageName("PointsStoreFragment");
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        m_rootView.setBackgroundColor(getResources().getColor(R.color.blue_purple));
        m_webView.loadUrl("http://10.0.4.2:8080/apps/story/demo/demostory.html");
    }

    @OnClick(R2.id.points_store_back)
    public void onNaviBack(){
        m_primaryCallback.onNaviBack();
    }

}
