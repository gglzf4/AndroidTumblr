package com.zrm.tumblr.utils;

import android.content.Context;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;


import java.io.File;

/**
 * Created by zhangrm on 2013-11-28.
 */
public class AppUtils {

    private final static String TAG = AppUtils.class.getSimpleName();



    private static File appFile = null;


    public static void init() {
        if (AppUtils.isDevEnv()) {

        }
    }



    public static void clear() {
        File tempRoot = getAppTempFile();
        if (tempRoot != null) {
            File[] files = tempRoot.listFiles();
            if (files != null) {
                for (File file : files) {
                    if (file != null) {
                        String fileName = file.getName().toLowerCase();
                        /*if (fileName.endsWith(SUFFIX_MP3) || fileName.endsWith(SUFFIX_APK)) {
                            file.delete();
                        }*/
                    }
                }
            }
        }
    }

    public static File getAppTempFile() {
        if (appFile == null) {
            File root = Environment.getExternalStorageDirectory();
            if (root.exists()) {
                /*String appPath = root.getAbsolutePath() + File.separator + AppUtils.APP_NAME + File.separator;
                appFile = new File(appPath);
                if (!appFile.exists()) {
                    boolean flag = appFile.mkdirs();
                }*/
            } else {
                Log.e(TAG, "getAppTempFile ERROR");
            }
        }
        return appFile;
    }

    //友盟
    public static String getDeviceInfo(Context context) {
        try {
            org.json.JSONObject json = new org.json.JSONObject();
            android.telephony.TelephonyManager tm = (android.telephony.TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);

            String device_id = tm.getDeviceId();

            android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);

            String mac = wifi.getConnectionInfo().getMacAddress();
            json.put("mac", mac);

            if (TextUtils.isEmpty(device_id)) {
                device_id = mac;
            }

            if (TextUtils.isEmpty(device_id)) {
                device_id = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
            }

            json.put("device_id", device_id);

            return json.toString();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * @return
     */
    public static boolean isDevEnv() {
        //return !AppUtils.APP_ENV.BASE_URL.equals(AppEnv.Env.REA.BASE_URL);
        return true;
    }




    public static long getServerTime() {
        return System.currentTimeMillis();
    }

    public static boolean isBeyondOneDay(long timestamp) {
        /*return Math.abs((getServerTime() - timestamp)) > 24*60*60*1000;*/
        /*Logger.e(TAG, timestamp + "是否过期");
        Logger.e(TAG, (Math.abs((getServerTime() - timestamp)) > 24 * 60 * 60 * 1000) + "是否过期");*/
        return Math.abs((getServerTime() - timestamp)) > 24 * 60 * 60 * 1000;
    }

    public static boolean isBeyondOneWeek(long timestamp) {
        return Math.abs((getServerTime() - timestamp)) > 7 * 24 * 60 * 60 * 1000;
    }

    public static boolean isBeyondOneMonth(long timestamp) {
        return Math.abs((getServerTime() - timestamp)) > 30 * 24 * 60 * 60 * 1000;
    }



}
