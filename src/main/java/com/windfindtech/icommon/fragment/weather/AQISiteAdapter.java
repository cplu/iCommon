package com.windfindtech.icommon.fragment.weather;


import android.content.Context;
import android.view.View;
import android.widget.TextView;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.jsondata.webservice.AQISiteItem;
import com.windfindtech.icommon.util.ResUtil;
import com.windfindtech.icommon.view.adapter.CustomAdapter;

/**
 * Created by cplu on 2015/12/18.
 */
public class AQISiteAdapter extends CustomAdapter<AQISiteItem, AQISiteItemHolder> {

	/**
	 * @param ctx
	 */
	public AQISiteAdapter(Context ctx) {
		super(ctx, R.layout.list_item_aqi_site);
	}

	@Override
	protected AQISiteItemHolder initHolder(View convertView) {
		AQISiteItemHolder holder = new AQISiteItemHolder();
		holder.m_nameView = (TextView) convertView.findViewById(R.id.aqi_site_name);
		holder.m_indexView = (TextView) convertView.findViewById(R.id.aqi_site_index);
		holder.m_qualityView = (TextView) convertView.findViewById(R.id.aqi_site_quality);
		holder.m_primaryPollutantView = (TextView) convertView.findViewById(R.id.aqi_site_primary_pollutant);
		return holder;
	}

	@Override
	protected void setHolderData(int position, View convertView, AQISiteItemHolder holder, AQISiteItem item, String tag) {
		holder.m_nameView.setText(item.getName());
//		indexValue = iCommon.getRandom(0, 500);
		holder.m_indexView.setText(String.valueOf(item.getIndex()));
		holder.m_qualityView.setText(item.getQuality());
		int indexValue = Integer.parseInt(item.getIndex());
		holder.m_qualityView.setBackgroundDrawable(m_ctx.getResources().getDrawable(ResUtil.getAQIQualityBackgroundID(indexValue)));
		holder.m_primaryPollutantView.setText(item.getPrimaryPollutant());
	}
}

class AQISiteItemHolder{
	TextView m_nameView;
	TextView m_indexView;
	TextView m_qualityView;
	TextView m_primaryPollutantView;
}