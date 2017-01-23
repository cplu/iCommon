package com.windfindtech.icommon.fragment.pts;

import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.R2;
import com.windfindtech.icommon.bitmap.DrawableManager;
import com.windfindtech.icommon.fragment.management.WrappedFragment;
import com.windfindtech.icommon.iCommon;
import com.windfindtech.icommon.jsondata.points.PointsRecordItem;
import com.windfindtech.icommon.mvp.presenter.pts.PointsRecordsPresenter;
import com.windfindtech.icommon.mvp.view.pts.IPointsRecordsView;

import java.util.ArrayList;
import java.util.List;

import butterknife.OnClick;

/**
 * Created by py on 2016/8/23.
 * 积分记录
 */
public class PointsRecordsFragment extends WrappedFragment<IPointsRecordsView, PointsRecordsPresenter> implements IPointsRecordsView {


    private static final int POINTS_TAB_NUMBER = 2;
    private static final int POINTS_INCOME_INDEX = 0;
    private static final int POINTS_EXPENSES_INDEX = 1;
    private TextView[] m_pointsTabs = new TextView[POINTS_TAB_NUMBER];
    private RecyclerView[] m_contentViews=new RecyclerView[POINTS_TAB_NUMBER];
    private PointsRecordsAdapter m_incomeAdapter,m_expensesAdapter;
    private View m_rootView;



    @Override
    protected PointsRecordsPresenter createPresenter() {
        return new PointsRecordsPresenter();
    }

    public PointsRecordsFragment() {
        setPageName("PointsRecordsFragment");
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        m_rootView = inflater.inflate(R.layout.fragment_points_records, container, false);
        return m_rootView;
    }

    @Override
    public void onViewCreated(View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        m_rootView.setBackgroundDrawable(DrawableManager.instance().getAssetImage("bg_user_fragment.png"));
        m_pointsTabs[POINTS_INCOME_INDEX]= (TextView) m_rootView.findViewById(R.id.btn_points_income);
        m_pointsTabs[POINTS_EXPENSES_INDEX]= (TextView) m_rootView.findViewById(R.id.btn_points_expenses);
        m_contentViews[POINTS_INCOME_INDEX]= (RecyclerView) m_rootView.findViewById(R.id.recycler_income);
        m_contentViews[POINTS_EXPENSES_INDEX]= (RecyclerView) m_rootView.findViewById(R.id.recycler_expenses);
        m_incomeAdapter = new PointsRecordsAdapter(getContext());
        m_expensesAdapter = new PointsRecordsAdapter(getContext());
        m_contentViews[POINTS_INCOME_INDEX].setAdapter(m_incomeAdapter);
        m_contentViews[POINTS_EXPENSES_INDEX].setAdapter(m_expensesAdapter);
        for (int i = 0; i < POINTS_TAB_NUMBER; i++) {
            m_contentViews[i].setLayoutManager(new LinearLayoutManager(getContext(),LinearLayoutManager.VERTICAL,false));
//            m_contentViews[i].addItemDecoration(new DividerItemDecoration(getContext(),LinearLayoutManager.VERTICAL,10,getResources().getColor(R.color.blue_purple)));
            m_contentViews[i].setHasFixedSize(true);
            m_contentViews[i].setItemAnimator(new DefaultItemAnimator());
        }
        m_presenter.retrieveMyPointHistory();
        selectBtn(POINTS_INCOME_INDEX);
    }

    @OnClick(R2.id.points_records_back)
    public void onNaviBack() {
        m_primaryCallback.onNaviBack();
    }

    @OnClick(R2.id.btn_points_income)
    public void onBtnPointsIncomeClick() {
        selectBtn(POINTS_INCOME_INDEX);
    }


    @OnClick(R2.id.btn_points_expenses)
    public void onBtnPointsExpensesClick() {
        selectBtn(POINTS_EXPENSES_INDEX);
    }


    /**
     * hightlight the selected tabZ
     * @param selectIndex
     */
    private void selectBtn(int selectIndex) {
         int PADDING_LEFT= (int) getResources().getDimension(R.dimen.large_padding_1_5x);
         int PADDING_RIGHT= (int) getResources().getDimension(R.dimen.large_padding_1_5x);
         int PADDING_TOP_LARGE= (int) getResources().getDimension(R.dimen.default_padding_1_2x);
         int PADDING_BOTTOM_LARGE= (int) getResources().getDimension(R.dimen.default_padding_1_2x);
        for (int i = 0; i < POINTS_TAB_NUMBER; i++) {
            m_pointsTabs[i].setBackgroundResource(R.drawable.bg_gray);
            m_pointsTabs[i].setPadding(0,0,0,0);
            m_pointsTabs[i].setTextColor(getResources().getColor(R.color.grayscale3));
            m_pointsTabs[i].setTypeface(Typeface.DEFAULT);
            m_contentViews[i].setVisibility(View.INVISIBLE);
        }
        m_pointsTabs[selectIndex].setBackgroundResource(R.drawable.bg_purple);
        m_pointsTabs[selectIndex].setPadding(PADDING_LEFT,PADDING_TOP_LARGE,PADDING_RIGHT,PADDING_BOTTOM_LARGE);
        m_pointsTabs[selectIndex].setTextColor(getResources().getColor(R.color.default_white));
        m_pointsTabs[selectIndex].setTypeface(Typeface.DEFAULT_BOLD);
        m_contentViews[selectIndex].setVisibility(View.VISIBLE);
    }



    @Override
    public void onDataGetFailed() {
        iCommon.showToast(getContext(),getString(R.string.retrieve_kpi_data_failed));
    }

    @Override
    public void onIncomeListSuccess(List<PointsRecordItem> pointsRecordItems) {
        if (pointsRecordItems!=null) {
            m_incomeAdapter.setData((ArrayList<PointsRecordItem>) pointsRecordItems, true);
        }
    }

    @Override
    public void onOutcomeListSuccess(List<PointsRecordItem> pointsRecordItems) {
        if (pointsRecordItems!=null) {
            m_expensesAdapter.setData((ArrayList<PointsRecordItem>) pointsRecordItems, true);
        }
    }
}
