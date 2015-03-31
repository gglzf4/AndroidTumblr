package com.zrm.tumblr.app;

import android.content.Context;
import android.content.SharedPreferences;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by zhangrm on 2015/3/31 0031.
 */
public class Session {


    public static final String SP_KEY_ACCESS_TOKEN = "SP_KEY_ACCESS_TOKEN";//ACCESS_TOKEN


    private static SharedPreferences preferences;
    private static SharedPreferences.Editor editor;

    public static void init(Context context){
        preferences = context.getSharedPreferences(AppInfo.APP_NAME, Context.MODE_PRIVATE);
        editor = preferences.edit();
    }

    /**
     * 用户是否登录
     */
    public static boolean isLogin() {
        return StringUtils.isNotBlank(preferences.getString(SP_KEY_ACCESS_TOKEN,null));
    }

    /**
     *
     * @param accessToken
     */
    public static void saveInfo(String accessToken) {
        editor.putString(SP_KEY_ACCESS_TOKEN, accessToken);
        editor.commit();
    }

    public static String getAccessToken() {
        return preferences.getString(SP_KEY_ACCESS_TOKEN, "");
    }
}
