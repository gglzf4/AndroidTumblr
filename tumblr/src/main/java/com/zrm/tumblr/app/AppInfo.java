package com.zrm.tumblr.app;

import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.ApplicationInfo;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;

/**
 * Created by zhangrm on 2015/3/29 0029.
 */
public class AppInfo {


    //private static SharedPreferences preferences;

    public static int CHANEL_NO;
    public static boolean IS_DEV_ENV;//是否为开发版本

    public static String APP_KEY,APP_SECRET;

    public static void init(Context context){
        PackageInfo pinfo = null;
        ApplicationInfo applicationInfo = null;

        try {
            PackageManager packageManager = context.getPackageManager();
            pinfo = packageManager.getPackageInfo(context.getPackageName(), PackageManager.GET_CONFIGURATIONS);
            applicationInfo = packageManager.getApplicationInfo(context.getPackageName(), PackageManager.GET_META_DATA);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }

        //preferences = context.getSharedPreferences(APP_NAME, Context.MODE_PRIVATE);

        if(applicationInfo != null){
            CHANEL_NO = applicationInfo.metaData.getInt("UMENG_CHANNEL",0);

            APP_KEY = applicationInfo.metaData.getString("APP_KEY");
            APP_SECRET = applicationInfo.metaData.getString("APP_SECRET");
            IS_DEV_ENV = applicationInfo.metaData.getBoolean("IS_DEV_ENV", false);
        }
    }


    public static boolean isDevEnv() {
        return IS_DEV_ENV;
    }

}
