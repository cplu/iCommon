package com.windfindtech.icommon.fragment.management;

import com.windfindtech.icommon.fragment.navigate.AccountDetailFragment;
import com.windfindtech.icommon.fragment.navigate.AccountDevicesFragment;
import com.windfindtech.icommon.fragment.navigate.AccountInfoFragment;
import com.windfindtech.icommon.fragment.navigate.AppAboutFragment;
import com.windfindtech.icommon.fragment.navigate.AppFeedbackFragment;
import com.windfindtech.icommon.fragment.navigate.AppSettingsFragment;
import com.windfindtech.icommon.fragment.navigate.ChangePasswordFragment;
import com.windfindtech.icommon.fragment.navigate.DeviceDetailFragment;
import com.windfindtech.icommon.fragment.navigate.DeviceRegisterFragment;
import com.windfindtech.icommon.fragment.pts.MyRankFragment;
import com.windfindtech.icommon.fragment.pts.MyAchievementFragment;
import com.windfindtech.icommon.fragment.pts.MyPointsFragment;
import com.windfindtech.icommon.fragment.pts.PointsRecordsFragment;
import com.windfindtech.icommon.fragment.pts.PointsRulesFragment;
import com.windfindtech.icommon.fragment.pts.PointsStoreFragment;

import java.util.HashMap;

/**
 * Created by cplu on 2016/5/19.
 */
public class TagUser {
	public static final String FRG_ACCOUNT_DEVICE = "icommon:fragment_account_device";
	public static final String FRG_APP_SETTING = "icommon:fragment_app_setting";
	public static final String FRG_APP_ABOUNT = "icommon:fragment_app_about";
	public static final String FRG_APP_FEEDBACK = "icommon:fragment_app_feedback";
	public static final String FRG_ACCOUNT_INFO = "icommon:fragment_account_info";
	public static final String FRG_DEVICE_REGISTER = "icommon:fragment_device_register";
	public static final String FRG_ACCOUNT_DETAIL = "icommon:fragment_account_detail";
	public static final String FRG_CHANGE_PASSWORD = "icommon:fragment_change_password";
	public static final String FRG_DEVICE_DETAIL = "icommon:fragment_device_detail";
	public static final String FRG_MY_POINTS = "icommon:fragment_my_points";
	public static final String FRG_POINTS_STORE = "icommon:fragment_points_store";
	public static final String FRG_POINTS_RECORDS = "icommon:fragment_points_records" ;
	public static final String FRG_MY_ACHIEVEMENT = "icommon:fragment_my_achievement" ;
	public static final String FRG_POINTS_RULES="icommon:fragment_points_rules";
	public static final String FRG_MY_RANK="icommon:fragment_my_rank";
	public static final HashMap<String, Class> s_FragmentTags = new HashMap() {
		{
			put(FRG_ACCOUNT_DEVICE, AccountDevicesFragment.class);
			put(FRG_APP_SETTING, AppSettingsFragment.class);
			put(FRG_APP_ABOUNT, AppAboutFragment.class);
			put(FRG_APP_FEEDBACK, AppFeedbackFragment.class);
			put(FRG_ACCOUNT_INFO, AccountInfoFragment.class);
			put(FRG_DEVICE_REGISTER, DeviceRegisterFragment.class);
			put(FRG_ACCOUNT_DETAIL, AccountDetailFragment.class);
			put(FRG_CHANGE_PASSWORD, ChangePasswordFragment.class);
			put(FRG_DEVICE_DETAIL, DeviceDetailFragment.class);
			put(FRG_MY_POINTS, MyPointsFragment.class);
			put(FRG_POINTS_STORE, PointsStoreFragment.class);
			put(FRG_POINTS_RECORDS, PointsRecordsFragment.class);
			put(FRG_MY_ACHIEVEMENT, MyAchievementFragment.class);
			put(FRG_POINTS_RULES, PointsRulesFragment.class);
			put(FRG_MY_RANK, MyRankFragment.class);
		}
	};
}
