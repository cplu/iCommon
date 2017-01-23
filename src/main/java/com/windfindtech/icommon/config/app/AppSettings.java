package com.windfindtech.icommon.config.app;

import com.windfindtech.icommon.util.Utils;

/**
 * Created by cplu on 2015/3/20.
 * local settings for app
 */
public class AppSettings {
    private static AppSettings s_instance;
//    private boolean join_user_experience;

//    private static final String PREF_NAME_FOR_APP_SETTINGS = "ish_app_settings";
//    private static final String USER_EXPERIENCE_PLAN_KEY = "user_experience_plan";

    public static AppSettings instance(){
        if(s_instance == null){
            s_instance = new AppSettings();
        }
        return s_instance;
    }

    public void setAppExperiencePlanJoined(boolean joined){
        Utils.saveBoolean(Utils.APP_USER_EXPERIENCE_PLAN_KEY, joined);
    }

    public boolean getAppExperiencePlanJoined(){
        return Utils.getBoolean(Utils.APP_USER_EXPERIENCE_PLAN_KEY, true);
    }
}
