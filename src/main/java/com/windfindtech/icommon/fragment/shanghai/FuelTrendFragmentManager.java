package com.windfindtech.icommon.fragment.shanghai;

import android.content.Context;
import android.view.View;
import android.widget.ProgressBar;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.fragment.renderer.FuelTrendGraph;
import com.windfindtech.icommon.fragment.renderer.FuelTrendXAxis;
import com.windfindtech.icommon.iCommon;
import com.windfindtech.icommon.jsondata.shanghai.FuelData;
import com.windfindtech.icommon.jsondata.shanghai.FuelTrendData;
import com.windfindtech.icommon.jsondata.shanghai.ISHBaseData;
import com.windfindtech.icommon.jsondata.webservice.WSErrorResponse;
import com.windfindtech.icommon.webservice.WSCallback;
import com.windfindtech.icommon.webservice.kpi.DataManager;

import java.util.ArrayList;
import java.util.Calendar;

/**
 * Created by cplu on 2015/8/19.
 */
public class FuelTrendFragmentManager {
	private final ProgressBar m_progressbar;
	private View m_rootView;
	private FuelTrendXAxis m_XAxis;
	private FuelTrendGraph m_graph01;   /// 89号汽油
	private FuelTrendGraph m_graph02;   /// 92号汽油
	private FuelTrendGraph m_graph03;   /// 95号汽油
	private FuelTrendGraph m_graph04;   /// 0号柴油
	private static final int MAX_VALUE_SHOWN = 6;
	private Context m_ctx;

	public FuelTrendFragmentManager(Context ctx, View root) {
		m_rootView = root;
		m_ctx = ctx;

		m_XAxis = (FuelTrendXAxis)root.findViewById(R.id.fuel_trend_x_axis);
		m_graph01 = (FuelTrendGraph)root.findViewById(R.id.fuel_trend_graph01);
//        float[] values1 = {6.50f, 6.40f, 6.20f, 5.00f, 5.20f, 5.60f, 6.12f, 6.20f, 6.23f, 7.00f};
//        m_graph01.updateValues(values1, R.color.default_blue);
		m_graph02 = (FuelTrendGraph)root.findViewById(R.id.fuel_trend_graph02);
//        float[] values2 = {6.50f, 6.40f, 6.20f, 5.00f, 5.20f, 5.60f, 6.12f, 6.20f, 6.23f, 7.00f};
//        m_graph02.updateValues(values2, R.color.default_orange);
		m_graph03 = (FuelTrendGraph)root.findViewById(R.id.fuel_trend_graph03);
//        float[] values3 = {6.50f, 6.40f, 6.20f, 5.00f, 5.20f, 5.60f, 6.12f, 6.20f, 6.23f, 7.00f};
//        m_graph03.updateValues(values3, R.color.default_blue);
		m_graph04 = (FuelTrendGraph)root.findViewById(R.id.fuel_trend_graph04);
//        float[] values4 = {6.50f, 6.40f, 6.20f, 5.00f, 5.20f, 5.60f, 6.12f, 6.20f, 6.23f, 7.00f};
//        m_graph04.updateValues(values4, R.color.default_orange);
		m_progressbar = (ProgressBar) root.findViewById(R.id.loading_progressbar);
	}

	public void refreshFuelData(boolean force_ui_update){
		m_progressbar.setVisibility(View.VISIBLE);
		DataManager.instance().retrieveShData(DataManager.FUEL_TREND_IDX, force_ui_update, new WSCallback<ISHBaseData>() {
			@Override
			public void onSuccess(ISHBaseData data) {
				m_progressbar.setVisibility(View.INVISIBLE);
				update_fuel_trend_data((FuelTrendData) data);
			}

			@Override
			public void onFailed(WSErrorResponse str) {
				m_progressbar.setVisibility(View.INVISIBLE);
				iCommon.showToast(m_ctx, R.string.retrieve_fuel_trend_failed);
			}
		});
	}

	private void update_fuel_trend_data(FuelTrendData trend_data) {
		if(!trend_data.isValid()){
			return;
		}
		try {
			FuelData[] datas = trend_data.getData();
			ArrayList<String> months = new ArrayList<String>();
			ArrayList<Integer> years = new ArrayList<Integer>();
			ArrayList<Float> ron89 = new ArrayList<Float>();
			ArrayList<Float> ron92 = new ArrayList<Float>();
			ArrayList<Float> ron95 = new ArrayList<Float>();
			ArrayList<Float> diesel = new ArrayList<Float>();
			Calendar cal = Calendar.getInstance();
			int index = 0;
			if(datas.length > MAX_VALUE_SHOWN){
				index = datas.length - MAX_VALUE_SHOWN;
			}
			for (; index < datas.length; index++) {
				cal.setTime(datas[index].getRelease());
				months.add(String.format("%02d.%02d", cal.get(Calendar.MONTH) + 1, cal.get(Calendar.DAY_OF_MONTH)));
				years.add(cal.get(Calendar.YEAR));
				ron89.add(datas[index].getRon89());
				ron92.add(datas[index].getRon92());
				ron95.add(datas[index].getRon95());
				diesel.add(datas[index].getDiesel());
			}
			m_XAxis.updateValues(months, years);
			m_graph01.updateValues(ron89, R.color.default_blue);
			m_graph02.updateValues(ron92, R.color.default_orange);
			m_graph03.updateValues(ron95, R.color.default_blue);
			m_graph04.updateValues(diesel, R.color.default_orange);
		}
		catch (Exception e){

		}

		return;
	}
}
