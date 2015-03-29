package com.zrm.tumblr.utils;

import android.util.Log;

/**
 * Created by zhangrm on 2014/7/31.
 */
public class Logger {

    public static void v(String tag, String msg) {
        if(AppUtils.isDevEnv()){
            Log.v(tag,msg);
        }
    }


    public static void d(String tag, String msg) {
        if(AppUtils.isDevEnv()){
            Log.d(tag,msg);
        }
    }




    public static void i(String tag, String msg) {
        if(AppUtils.isDevEnv()){
            Log.i(tag,msg);
        }
    }

    public static void e(String tag, String msg) {
        if(AppUtils.isDevEnv()){
            Log.e(tag,msg);
        }
    }

}
