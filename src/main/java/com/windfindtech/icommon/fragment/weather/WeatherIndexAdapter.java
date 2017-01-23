package com.windfindtech.icommon.fragment.weather;

import android.content.Context;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.bitmap.DrawableManager;
import com.windfindtech.icommon.jsondata.webservice.IndexesDetailInfo;
import com.windfindtech.icommon.jsondata.webservice.IndexesInfo;
import com.windfindtech.icommon.view.adapter.CustomAdapter;

import java.util.ArrayList;

/**
 * Created by yu on 2015/7/3.
 */
public class WeatherIndexAdapter extends CustomAdapter<IndexesDetailInfo, WeatherViewHolder> {

    private static String[] s_iconIndexes = {"weather/icon_weather_yd","weather/icon_weather_ssd","weather/icon_weather_gm","weather/icon_weather_fs"};

    public WeatherIndexAdapter(Context context, IndexesInfo indexesInfo) {
        super(context, R.layout.list_item_weather_index);
        m_adapterData = new ArrayList<>();
        m_adapterData.add(indexesInfo.getYd());
        m_adapterData.add(indexesInfo.getSsd());
        m_adapterData.add(indexesInfo.getGm());
        m_adapterData.add(indexesInfo.getFs());
    }

    @Override
    protected WeatherViewHolder initHolder(View convertView) {
        WeatherViewHolder holder = new WeatherViewHolder();
        holder.m_iconView = (ImageView) convertView.findViewById(R.id.imageview_index);
        holder.m_hintView = (TextView) convertView.findViewById(R.id.textview_index_hint);
        holder.m_nameView = (TextView) convertView.findViewById(R.id.textview_index_name);
        return holder;
    }

    @Override
    protected void setHolderData(int position, View convertView, WeatherViewHolder holder, IndexesDetailInfo item, String tag) {
        holder.m_nameView.setText(item.getName());
        holder.m_hintView.setText(item.getHint());
        holder.m_iconView.setImageDrawable(DrawableManager.instance().getAssetPng(s_iconIndexes[position]));
    }
}
