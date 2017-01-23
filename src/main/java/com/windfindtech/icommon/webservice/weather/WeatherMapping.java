package com.windfindtech.icommon.webservice.weather;

import android.content.Context;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.bitmap.DrawableManager;

import org.pmw.tinylog.Logger;

import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by cplu on 2015/7/3.
 */
public class WeatherMapping {
	private static LinkedHashMap<String, String> s_weatherStringMapping;
	private static LinkedHashMap<String, AnimationDrawable> s_weatherStringMappingAnim;
//	private Context m_ctx;
	private static final int ANIMATION_DURATION = 600;

	private WeatherMapping() {
//		m_ctx = ctx;
	}

	private static void initWeatherMapping(Context ctx) {
		s_weatherStringMapping = new LinkedHashMap<>();
		/// s_weatherStringMapping must be in order of priority, e.g. "晴转多云" should be in front of "晴" and "多云"
//        m_weather_string_mapping.put(getContext().getString(R.string.weather_sunny_to_cloudy), "icon_cloudy_to_sunny");
		s_weatherStringMapping.put(ctx.getString(R.string.weather_cloudy), "weather/icon_weather_cloudy");
		s_weatherStringMapping.put(ctx.getString(R.string.weather_sunny), "weather/icon_weather_sunny");
		s_weatherStringMapping.put(ctx.getString(R.string.weather_light_rain), "weather/icon_weather_light_rain");
		s_weatherStringMapping.put(ctx.getString(R.string.weather_middle_rain), "weather/icon_weather_middle_rain");
		s_weatherStringMapping.put(ctx.getString(R.string.weather_heavy_rain), "weather/icon_weather_heavy_rain");
		s_weatherStringMapping.put(ctx.getString(R.string.weather_rainstorm), "weather/icon_weather_rainstorm");
		s_weatherStringMapping.put(ctx.getString(R.string.weather_thunderstorm), "weather/icon_weather_thunderstorm");
		s_weatherStringMapping.put(ctx.getString(R.string.weather_shower), "weather/icon_weather_shower");
		s_weatherStringMapping.put(ctx.getString(R.string.weather_rain), "weather/icon_weather_light_rain");
//        m_weather_string_mapping.put(getContext().getString(R.string.weather_foggy), "icon_foggy");
		s_weatherStringMapping.put(ctx.getString(R.string.weather_gloomy), "weather/icon_weather_gloomy");
//        m_weather_string_mapping.put(getContext().getString(R.string.weather_sandstorm), "icon_sandstorm");
		s_weatherStringMapping.put(ctx.getString(R.string.weather_light_snow), "weather/icon_weather_light_snow");
		s_weatherStringMapping.put(ctx.getString(R.string.weather_middle_snow), "weather/icon_weather_middle_snow");
		s_weatherStringMapping.put(ctx.getString(R.string.weather_heavy_snow), "weather/icon_weather_heavy_snow");
		s_weatherStringMapping.put(ctx.getString(R.string.weather_snow_shower), "weather/icon_weather_snow_shower");

	}

	private static void initWeatherAnimMapping(Context ctx) {
		s_weatherStringMappingAnim = new LinkedHashMap<>();
		try {
			AnimationDrawable drawable = new AnimationDrawable();
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_duoyun_1"), ANIMATION_DURATION / 3);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_duoyun_2"), ANIMATION_DURATION / 3);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_duoyun_3"), ANIMATION_DURATION / 3);
			drawable.setOneShot(false);
			s_weatherStringMappingAnim.put(ctx.getString(R.string.weather_cloudy), drawable);

			drawable = new AnimationDrawable();
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_qing_1"), ANIMATION_DURATION / 3);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_qing_2"), ANIMATION_DURATION / 3);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_qing_3"), ANIMATION_DURATION / 3);
			drawable.setOneShot(false);
			s_weatherStringMappingAnim.put(ctx.getString(R.string.weather_sunny), drawable);

			drawable = new AnimationDrawable();
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_xiaoyu_1"),
					ANIMATION_DURATION / 4);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_xiaoyu_2"), ANIMATION_DURATION / 4);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_xiaoyu_3"), ANIMATION_DURATION / 4);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_xiaoyu_4"), ANIMATION_DURATION / 4);
			drawable.setOneShot(false);
			s_weatherStringMappingAnim.put(ctx.getString(R.string.weather_light_rain), drawable);

			drawable = new AnimationDrawable();
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_dayu_1"), ANIMATION_DURATION / 3);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_dayu_2"), ANIMATION_DURATION / 3);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_dayu_3"), ANIMATION_DURATION / 3);
			drawable.setOneShot(false);
			s_weatherStringMappingAnim.put(ctx.getString(R.string.weather_heavy_rain), drawable);

			drawable = new AnimationDrawable();
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_baoyu_1"),
					ANIMATION_DURATION / 3);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_baoyu_2"), ANIMATION_DURATION / 3);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_baoyu_3"), ANIMATION_DURATION / 3);
			drawable.setOneShot(false);
			s_weatherStringMappingAnim.put(ctx.getString(R.string.weather_rainstorm), drawable);

			drawable = new AnimationDrawable();
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_leizhenyu_1"),
					ANIMATION_DURATION / 4);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_leizhenyu_2"),
					ANIMATION_DURATION / 4);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_leizhenyu_3"),
					ANIMATION_DURATION / 4);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_leizhenyu_4"),
					ANIMATION_DURATION / 4);
			drawable.setOneShot(false);
			s_weatherStringMappingAnim.put(ctx.getString(R.string.weather_thunderstorm), drawable);

			drawable = new AnimationDrawable();
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_zhenyu_1"), ANIMATION_DURATION / 4);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_zhenyu_2"), ANIMATION_DURATION / 4);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_zhenyu_3"), ANIMATION_DURATION / 4);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_zhenyu_4"), ANIMATION_DURATION / 4);
			drawable.setOneShot(false);
			s_weatherStringMappingAnim.put(ctx.getString(R.string.weather_shower), drawable);

			drawable = new AnimationDrawable();
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_zhongyu_1"), ANIMATION_DURATION / 3);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_zhongyu_2"), ANIMATION_DURATION / 3);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_zhongyu_3"), ANIMATION_DURATION / 3);
			drawable.setOneShot(false);
			s_weatherStringMappingAnim.put(ctx.getString(R.string.weather_middle_rain), drawable);
			s_weatherStringMappingAnim.put(ctx.getString(R.string.weather_rain), drawable);

			drawable = new AnimationDrawable();
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_yin_1"), ANIMATION_DURATION);
			drawable.setOneShot(false);
			s_weatherStringMappingAnim.put(ctx.getString(R.string.weather_gloomy), drawable);

			drawable = new AnimationDrawable();
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_xiaoxue_1"), ANIMATION_DURATION / 4);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_xiaoxue_2"), ANIMATION_DURATION / 4);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_xiaoxue_3"), ANIMATION_DURATION / 4);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_xiaoxue_4"), ANIMATION_DURATION / 4);
			drawable.setOneShot(false);
			s_weatherStringMappingAnim.put(ctx.getString(R.string.weather_light_snow), drawable);

			drawable = new AnimationDrawable();
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_zhongxue_1"),
					ANIMATION_DURATION / 4);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_zhongxue_2"),
					ANIMATION_DURATION / 4);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_zhongxue_3"),
					ANIMATION_DURATION / 4);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_zhongxue_4"),
					ANIMATION_DURATION / 4);
			drawable.setOneShot(false);
			s_weatherStringMappingAnim.put(ctx.getString(R.string.weather_middle_snow), drawable);

			drawable = new AnimationDrawable();
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_daxue_1"), ANIMATION_DURATION / 3);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_daxue_2"), ANIMATION_DURATION / 3);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_daxue_3"), ANIMATION_DURATION / 3);
			drawable.setOneShot(false);
			s_weatherStringMappingAnim.put(ctx.getString(R.string.weather_heavy_snow), drawable);

			drawable = new AnimationDrawable();
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_zhenxue_1"), ANIMATION_DURATION / 3);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_zhenxue_2"), ANIMATION_DURATION / 3);
			drawable.addFrame(DrawableManager.instance().getAssetPng("weather/icon_weather_zhenxue_3"), ANIMATION_DURATION / 3);
			drawable.setOneShot(false);
			s_weatherStringMappingAnim.put(ctx.getString(R.string.weather_snow_shower), drawable);
		} catch (Exception e) {
			Logger.info(e, "[ init weather mapping exception ]");
		}
	}

	public static Drawable getWeatherDrawable(Context ctx, String weather_status) {
		if (s_weatherStringMapping == null) {
			initWeatherMapping(ctx);
		}
		if(weather_status == null) {
			return DrawableManager.instance().getAssetPng("icon_weather_sunny");
		}
		for (Map.Entry<String, String> entry : s_weatherStringMapping.entrySet()) {
			if (weather_status.contains(entry.getKey())) {
				return DrawableManager.instance().getAssetPng(entry.getValue());
			}
		}
		/// default to sunny if not match any one
		return DrawableManager.instance().getAssetPng("icon_weather_sunny");
	}

	public static Drawable getWeatherDrawableAnim(Context ctx, String weather_status) {
		if (s_weatherStringMappingAnim == null) {
			initWeatherAnimMapping(ctx);
		}
		if(weather_status == null) {
			return s_weatherStringMappingAnim.get(ctx.getString(R.string.weather_sunny));
		}
		for (Map.Entry<String, AnimationDrawable> entry : s_weatherStringMappingAnim.entrySet()) {
			if (weather_status.contains(entry.getKey())) {
				return entry.getValue();
			}
		}
		/// default to sunny if not match any one
		return s_weatherStringMappingAnim.get(ctx.getString(R.string.weather_sunny));
	}

}
