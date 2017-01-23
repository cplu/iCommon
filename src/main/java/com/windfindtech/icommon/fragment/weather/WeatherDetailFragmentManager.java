package com.windfindtech.icommon.fragment.weather;

import android.animation.ValueAnimator;
import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.text.TextUtils;
import android.view.View;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.bitmap.DrawableManager;
import com.windfindtech.icommon.fragment.renderer.WeatherDailyForecastGraph;
import com.windfindtech.icommon.fragment.renderer.WeatherHourlyForecastGraph;
import com.windfindtech.icommon.iCommon;
import com.windfindtech.icommon.jsondata.webservice.AQISiteData;
import com.windfindtech.icommon.jsondata.webservice.AQISiteItem;
import com.windfindtech.icommon.jsondata.webservice.DayDetailInfo;
import com.windfindtech.icommon.jsondata.webservice.ForecastDayInfo;
import com.windfindtech.icommon.jsondata.webservice.ForecastWeekInfo;
import com.windfindtech.icommon.jsondata.webservice.WeatherInfo;
import com.windfindtech.icommon.jsondata.webservice.WeekDetailInfo;
import com.windfindtech.icommon.util.ResUtil;
import com.windfindtech.icommon.view.animator.TextViewDigitalAnimator;
import com.windfindtech.icommon.webservice.weather.WeatherMapping;

import org.pmw.tinylog.Logger;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;

/**
 * Created by cplu on 2015/8/18.
 */
public class WeatherDetailFragmentManager {

	private final Context m_ctx;
	private final TextView m_currentTempView;
	private final TextView m_weatherView;
	private final ImageView m_iconWeatherView;
	private final TextView m_todayTempView;
	private final TextView m_humidityView;
	private final TextView m_windySpeedView;
	private final TextView m_ultravioletView;
	private final ImageView m_imageHumidity;
	private final ImageView m_imageWindySpeed;
	private final ImageView m_imageUltraviolet;
	private final TextView m_oneDayTitle;
	private final TextView mDayHighTemp;
	private final TextView mDayMiddleTemp;
	private final TextView mDayLowTemp;
	private final WeatherHourlyForecastGraph m_dayGraph;
	private final GridView m_indexesView;
	private final WeatherDailyForecastGraph m_curveView;
	private View m_rootView;
//	private WeatherInfo m_weatherData;
//	private int[] m_highTempValues;   // = new int[WeatherTempGraph.MAX_ITEM_COUNT];
//	private int[] m_lowTempValues;    // = new int[WeatherTempGraph.MAX_ITEM_COUNT];
//	private int[] m_oneDayTempValues;// = new int[WeatherDayGraph.MAX_ITEM_COUNTS];

	/// controllers
	private final TextView m_aqiAverageIndexTextView;
	private final TextView m_aqiAverageQualityTextView;
	private final TextView m_aqiAverageNameTextView;
	private final TextView m_aqiAveragePM10;
	private final TextView m_aqiAveragePM25;
	private final TextView m_aqiAverageNO2;
	private final TextView m_aqiAverageSO2;
	private final TextView m_aqiAverageO3;
	private final TextView m_aqiAverageCO;
	private final ListView m_aqiSiteList;
	private final AQISiteAdapter m_aqiSiteAdapter;
	private ValueAnimator m_tempAnim;

	@SuppressLint("SetTextI18n")
	public WeatherDetailFragmentManager(Context ctx, View root, final WeatherDetailFragmentCallback callback) {
		m_ctx = ctx;
		m_rootView = root;

		TextView current_city_view = (TextView) get_view(R.id.textview_current_city);
		current_city_view.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View v) {
				callback.onNaviBack();
			}
		});

		/// part 1: overview
		m_currentTempView = (TextView) get_view(R.id.textview_current_temperature);
		m_weatherView = (TextView) get_view(R.id.textview_weather_detail);
		m_iconWeatherView = (ImageView) get_view(R.id.imageview_icon_weather_detail);
		m_todayTempView = (TextView) get_view(R.id.textview_today_temperature);
		m_humidityView = (TextView) get_view(R.id.textview_humidity_value);
		m_windySpeedView = (TextView) get_view(R.id.textview_windy_speed_value);
		m_ultravioletView = (TextView) get_view(R.id.textview_ultraviolet_value);
		m_imageHumidity = (ImageView) get_view(R.id.image_humidity);
		m_imageWindySpeed = (ImageView) get_view(R.id.image_windy_speed);
		m_imageUltraviolet = (ImageView) get_view(R.id.image_ultraviolet);
		m_imageHumidity.setImageDrawable(DrawableManager.instance().getAssetPng("weather/icon_shidu"));
		m_imageWindySpeed.setImageDrawable(DrawableManager.instance().getAssetPng("weather/icon_fengsu"));
		m_imageUltraviolet.setImageDrawable(DrawableManager.instance().getAssetPng("weather/icon_ziwaixian"));

		/// part 2: weekly forecast

		/// part 3: daily forecast
		m_oneDayTitle = (TextView) get_view(R.id.textview_today_date);
		mDayHighTemp = (TextView) get_view(R.id.textview_day_high_temp);
		mDayMiddleTemp = (TextView) get_view(R.id.textview_day_middle_temp);
		mDayLowTemp = (TextView) get_view(R.id.textview_day_low_temp);
		m_dayGraph = (WeatherHourlyForecastGraph) get_view(R.id.view_day_curve_pic);

		/// part 4: aqi (data should be retrieved afterward)
		m_aqiAverageIndexTextView = (TextView) get_view(R.id.aqi_average_index);
		m_aqiAverageQualityTextView = (TextView) get_view(R.id.aqi_average_quality);
		m_aqiAverageNameTextView = (TextView) get_view(R.id.aqi_average_name);
		m_aqiAveragePM10 = (TextView) get_view(R.id.aqi_average_pm10);
		m_aqiAveragePM25 = (TextView) get_view(R.id.aqi_average_pm25);
		m_aqiAverageNO2 = (TextView) get_view(R.id.aqi_average_no2);
		m_aqiAverageSO2 = (TextView) get_view(R.id.aqi_average_so2);
		m_aqiAverageO3 = (TextView) get_view(R.id.aqi_average_o3);
		m_aqiAverageCO = (TextView) get_view(R.id.aqi_average_co);
		m_aqiSiteList = (ListView) get_view(R.id.aqi_site_list);
		m_aqiSiteAdapter = new AQISiteAdapter(ctx);
		m_aqiSiteList.setAdapter(m_aqiSiteAdapter);
		m_aqiSiteList.setFocusable(false);

		/// part 5: kinds of indexes
		m_indexesView = (GridView) get_view(R.id.gridview_indexes);
		m_curveView = (WeatherDailyForecastGraph) get_view(R.id.view_week_curve_pic);
	}

	@TargetApi(Build.VERSION_CODES.HONEYCOMB)
	public void updateWeatherInfo(WeatherInfo info) {
		/// update data
		m_tempAnim = TextViewDigitalAnimator.get_animator(m_currentTempView, 0, Integer.parseInt(info.getTemp()));
		if (info != null) {
			/// part 1 data
			String temperature = info.getTemp() != null ?
					(info.getTemp()) : "";
			if (m_tempAnim != null && iCommon.getApiLevel() >= Build.VERSION_CODES.HONEYCOMB) {
				m_tempAnim.start();
			}
			m_currentTempView.setText(temperature);
			m_weatherView.setText(info.getWeather());
			m_iconWeatherView.setImageDrawable(WeatherMapping.getWeatherDrawable(m_ctx, info.getWeather()));
			if (info.getTempMin() != null && info.getTempMax() != null) {
				String today_temp_str = info.getTempMin().substring(0,
						info.getTempMin().length() - 1) +
				                        m_ctx.getString(R.string.weather_space_temp) +
				                        info.getTempMax().substring(0,
						                        info.getTempMax().length() - 1) +
				                        m_ctx.getString(R.string.degree_centigrade);
				m_todayTempView.setText(today_temp_str);
			}
			m_humidityView.setText(info.getHumidity());
			m_windySpeedView.setText(info.getWindSpeed());
			try {
				m_ultravioletView.setText(info.getIndexes().getZwxqd().getHint());
			} catch (Exception e) {
				m_ultravioletView.setText(m_ctx.getString(R.string.weather_undefined));
			}

			/// part 2 data
			ForecastWeekInfo weekInfo = info.getForecast1w();
			if (weekInfo != null) {
				ArrayList<WeekDetailInfo> infos = weekInfo.getData();
				if (infos != null && infos.size() > 0) {
//					SimpleDateFormat date_format = new SimpleDateFormat("MM/dd");
//					SimpleDateFormat date_format_source = new SimpleDateFormat("MMMddd", Locale.CHINA);
					int count = infos.size() >
					            WeatherDailyForecastGraph.MAX_ITEM_COUNT ? WeatherDailyForecastGraph.MAX_ITEM_COUNT : infos.size();
					ArrayList<Integer> highTempValues = new ArrayList<>();
					ArrayList<Integer> lowTempValues = new ArrayList<>();
					String[] weekDays = new String[count];
					String[] daysInMonth = new String[count];
					String[] weatherStatus = new String[count];
					for (int i = 0; i < count; i++) {
						try {
							highTempValues.add(Integer.parseInt(infos.get(i).getTempMax()));
							lowTempValues.add(Integer.parseInt(infos.get(i).getTempMin()));
							if (TextUtils.isEmpty(infos.get(i).getDay())) {
								weekDays[i] = m_ctx.getString(R.string.empty_txt);
								daysInMonth[i] = m_ctx.getString(R.string.empty_txt);
							} else {
								weekDays[i] = infos.get(i).getDay();
								daysInMonth[i] = infos.get(i).getDate();
							}
							weatherStatus[i] = infos.get(i).getWeather();
						} catch (Exception e) {
							Logger.error(e);
						}
					}
					m_curveView.updateValues(highTempValues, lowTempValues, weekDays, daysInMonth, weatherStatus);
				}
			}

			/// part 3 data
			ForecastDayInfo dayInfo = info.getForecast1d();
			if (dayInfo != null) {
				ArrayList<DayDetailInfo> day_detail_infos = dayInfo.getData();
				if (day_detail_infos != null && day_detail_infos.size() > 0) {
					int value_high_temp = Integer.parseInt(day_detail_infos.get(0).getTemp());
					float value_middle_temp;
					int value_low_temp = Integer.parseInt(day_detail_infos.get(0).getTemp());
					int count = (day_detail_infos.size() >
					             WeatherHourlyForecastGraph.MAX_ITEM_COUNTS * 2) ? WeatherHourlyForecastGraph.MAX_ITEM_COUNTS :
							day_detail_infos.size() / 2;
					int[] oneDayTempValues = new int[count];
					for (int i = 0; i < count; i++) {
						oneDayTempValues[i] = Integer.parseInt(day_detail_infos.get(i * 2).getTemp());
						if (oneDayTempValues[i] < value_low_temp) {
							value_low_temp = oneDayTempValues[i];
						} else if (oneDayTempValues[i] > value_high_temp) {
							value_high_temp = oneDayTempValues[i];
						}
					}
					value_middle_temp = (value_high_temp + value_low_temp) * 1f / 2;
					String format_middle_temp;
					if ((value_high_temp + value_low_temp) % 2 == 1) {
						format_middle_temp = new DecimalFormat("#.0").format(value_middle_temp);
					} else {
						format_middle_temp = new DecimalFormat("#").format(value_middle_temp);
					}

					SimpleDateFormat date_format_source = new SimpleDateFormat(String.format("M%sdd%s", m_ctx.getString(R.string.month), m_ctx.getString(R.string.day)));
					m_oneDayTitle.setText(date_format_source.format(day_detail_infos.get(0).getTime()));
					mDayHighTemp.setText(
							String.valueOf(value_high_temp) + m_ctx.getString(R.string.temperature_suffix));
					mDayMiddleTemp.setText(format_middle_temp + m_ctx.getString(R.string.temperature_suffix));
					mDayLowTemp.setText(
							String.valueOf(value_low_temp) + m_ctx.getString(R.string.temperature_suffix));
					m_dayGraph.updateData(day_detail_infos);
				}
			}

			/// part 5 data
			if (info.getIndexes() != null) {
				m_indexesView.setAdapter(new WeatherIndexAdapter(m_ctx, info.getIndexes()));
			}
		}
	}

	public void updateAQI(AQISiteData aqiInfo) {
		try {
			ArrayList<AQISiteItem> siteItems = aqiInfo.getData();
			if (siteItems.size() > 0) {
				/// get last item and remove last
				AQISiteItem lastItem = siteItems.get(siteItems.size() - 1);
				siteItems.remove(siteItems.size() - 1);
				/// set data to ui
				m_aqiAverageIndexTextView.setText(lastItem.getIndex());
				m_aqiAverageQualityTextView.setText(lastItem.getQuality());
				int indexValue = Integer.parseInt(lastItem.getIndex());
				m_aqiAverageQualityTextView.setBackgroundDrawable(m_ctx.getResources().getDrawable(ResUtil.getAQIQualityBackgroundID(indexValue)));
				m_aqiAverageNameTextView.setText(lastItem.getName());
				m_aqiAveragePM10.setText(lastItem.getPm10Index());
				m_aqiAveragePM25.setText(lastItem.getPm25Index());
				m_aqiAverageNO2.setText(lastItem.getNo2Index());
				m_aqiAverageSO2.setText(lastItem.getSo2Index());
				m_aqiAverageO3.setText(lastItem.getO3Index());
				m_aqiAverageCO.setText(lastItem.getCoIndex());
				/// set data to list
				int size = siteItems.size();
				if (size > 0) {
					LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) m_aqiSiteList.getLayoutParams();
					int height = size *
					             (m_ctx.getResources().getDimensionPixelSize(R.dimen.ui_preferred_subtitle_height) +
					              m_ctx.getResources().getDimensionPixelSize(R.dimen.aqi_list_divider_height));
					params.height = height;
					m_aqiSiteList.setLayoutParams(params);
					m_aqiSiteAdapter.setData(siteItems, true);
				}
			}
		} catch (Exception e) {
			Logger.error(e);
		}
	}

	private View get_view(int res_id) {
		return m_rootView.findViewById(res_id);
	}
}
