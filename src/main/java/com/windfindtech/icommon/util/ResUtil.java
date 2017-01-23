package com.windfindtech.icommon.util;

import android.graphics.drawable.BitmapDrawable;
import android.util.Pair;

import com.windfindtech.icommon.R;
import com.windfindtech.icommon.bitmap.DrawableManager;

/**
 * Created by cplu on 2015/12/10.
 */
public class ResUtil {
	/**
	 * Parse the current level and return a string ID
	 *
	 * @return the string ID representing the level
	 */
	public static int getWaterLevelStringID(String level) {
		if (level == null) {
			return 0;
		}
		if (level.equals("\u2160")) {
			return R.string.surface_water_1;
		} else if (level.equals("\u2161")) {
			return R.string.surface_water_2;
		} else if (level.equals("\u2162")) {
			return R.string.surface_water_3;
		} else if (level.equals("\u2163")) {
			return R.string.surface_water_4;
		} else if (level.equals("\u2164")) {
			return R.string.surface_water_5;
		} else if (level.equals("\u2164+")) {
			return R.string.surface_water_5p;
		} else {
			return R.string.surface_water_unknown;
		}
	}

	/**
	 * Parse the current level and return a color ID
	 *
	 * @return the color ID representing the level
	 */
	public static int getWaterLevelColorID(String level) {
		if (level == null) {
			return 0;
		}
		if (level.equals("\u2160")) {
			return R.color.default_blue;
		} else if (level.equals("\u2161")) {
			return R.color.default_green;
		} else if (level.equals("\u2162")) {
			return R.color.default_orange;
		} else if (level.equals("\u2163")) {
			return R.color.default_pink;
		} else if (level.equals("\u2164")) {
			return R.color.grayscale2;
		} else if (level.equals("\u2164+")) {
			return R.color.default_black;
		} else {
			return R.color.default_blue;
		}
	}

	/**
	 * get color based on water quality
	 * @param level
	 * @return  the corresponding color id and color name, name is used as file name suffix
	 */
	public static Pair<Integer, String> getWaterLevelColorIDPair(String level) {
		if (level.equals("\u2162")) {
			return Pair.create(R.color.default_orange, "orange");
		} else if (level.equals("\u2163")) {
			return Pair.create(R.color.default_pink, "red");
		} else if (level.equals("\u2164")
		           || level.equals("\u2164+")) {
			return Pair.create(R.color.grayscale_half, "gray");
		} else {
			return Pair.create(R.color.default_blue, "blue");
		}
	}

	/**
	 * Get aqi quality background resource id
	 * @param index
	 * @return  a rounded corner background corresponding to the aqi index
	 */
	public static int getAQIQualityBackgroundID(int index) {
		if(index >= 0 && index <= 50){
			return R.drawable.bg_green_with_rounded_corner;
		}
		else if(index >= 51 && index <= 100){
			return R.drawable.bg_yellow_with_rounded_corner;
		}
		else if(index >= 101 && index <= 150){
			return R.drawable.bg_orange_with_rounded_corner;
		}
		else if(index >= 151 && index <= 200){
			return R.drawable.bg_red_with_rounded_corner;
		}
		else if(index >= 201 && index <= 300){
			return R.drawable.bg_lightpurple_with_rounded_corner;
		}
		else if(index > 300){
			return R.drawable.bg_darkpurple_with_rounded_corner;
		}
		else{
			return R.drawable.bg_green_with_rounded_corner;
		}
	}

	/**
	 * Get rank level background resource id
	 * @param level
	 * @return  a rounded corner background corresponding to the rank level
	 */
	public static int getRankLevelBackgroundID(int level) {
		if(level >= 0 && level <= 1){
			return R.drawable.bg_user_level_lv1;
		}
		else if(level > 1 && level <= 2){
			return R.drawable.bg_user_level_lv2;
		}
		else if(level >2 && level <= 3){
			return R.drawable.bg_user_level_lv3;
		}
		else if(level >3 && level <= 4){
			return R.drawable.bg_user_level_lv4;
		}
		else if(level >4 && level <= 5){
			return R.drawable.bg_user_level_lv5;
		}
		else if(level > 5&&level<=6){
			return R.drawable.bg_user_level_lv6;
		}
		else{
			return R.drawable.bg_orange_with_rounded_corner;
		}
	}

	public static int getRankLevelTextColorId(int level) {
		if(level >= 0 && level <= 1){
			return R.color.user_level_lv1_purple;
		}
		else if(level > 1 && level <= 2){
			return R.color.user_level_lv2_cyan;
		}
		else if(level >2 && level <= 3){
			return R.color.user_level_lv3_blue;
		}
		else if(level >3 && level <= 4){
			return R.color.user_level_lv4_green;
		}
		else if(level >4 && level <= 5){
			return R.color.user_level_lv5_orange;
		}
		else if(level > 5&&level<=6){
			return R.color.user_level_lv6_red_purple;
		}
		else{
			return R.color.default_orange;
		}
	}

	public static int getRankLevelStringID(int level){
		if(level >= 0 && level <= 50){
			return R.string.rank_normal_user;
		}
		else if(level >= 51 && level <= 100){
			return R.string.rank_changke_user;
		}
		else if(level >= 101 && level <= 150){
			return R.string.rank_fans_user;
		}
		else if(level >= 151 && level <= 200){
			return R.string.rank_boneash_user;
		}
		else if(level >= 201 && level <= 300){
			return R.string.rank_top_user;
		}
		else if(level > 300){
			return R.string.rank_waner_feile;
		}
		else{
			return R.string.rank_undefined;
		}
	}

	public static BitmapDrawable getDrawableResByLevel(int level) {
		switch (level){
			case 1:
				return DrawableManager.instance().getAssetPng("my_rank_lv1");
			case 2:
				return DrawableManager.instance().getAssetPng("my_rank_lv2");
			case 3:
				return DrawableManager.instance().getAssetPng("my_rank_lv3");
			case 4:
				return DrawableManager.instance().getAssetPng("my_rank_lv4");
			case 5:
				return DrawableManager.instance().getAssetPng("my_rank_lv5");
			case 6:
				return DrawableManager.instance().getAssetPng("my_rank_lv6");
			case 7:
				return DrawableManager.instance().getAssetPng("my_rank_lv7");
			case 8:
				return DrawableManager.instance().getAssetPng("my_rank_lv8");
			default:
				return DrawableManager.instance().getAssetPng("my_rank_lv1");
		}
	}

	public static BitmapDrawable getGrayDrawableResByLevel(int level) {
		switch (level){
			case 1:
				return DrawableManager.instance().getAssetPng("my_rank_lv1_gray");
			case 2:
				return DrawableManager.instance().getAssetPng("my_rank_lv2_gray");
			case 3:
				return DrawableManager.instance().getAssetPng("my_rank_lv3_gray");
			case 4:
				return DrawableManager.instance().getAssetPng("my_rank_lv4_gray");
			case 5:
				return DrawableManager.instance().getAssetPng("my_rank_lv5_gray");
			case 6:
				return DrawableManager.instance().getAssetPng("my_rank_lv6_gray");
			case 7:
				return DrawableManager.instance().getAssetPng("my_rank_lv7_gray");
			case 8:
				return DrawableManager.instance().getAssetPng("my_rank_lv8_gray");
			default:
				return DrawableManager.instance().getAssetPng("my_rank_lv1_gray");
		}
	}
}
