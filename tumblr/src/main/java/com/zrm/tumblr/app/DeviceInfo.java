package com.zrm.tumblr.app;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import org.apache.commons.lang3.StringUtils;

/**
 * Created by zhangrm on 2015/3/31 0031.
 */
public class DeviceInfo {

    private static String baseInfo;
    private static String deviceInfo;

    private static DisplayMetrics metrics;
    private static TelephonyManager telephonyManager;

    private static Context context;

    public static void init(Context context){
        DeviceInfo.context = context;
        metrics = context.getResources().getDisplayMetrics();
        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);
    }

    public static int getPhoneWidth(){
        return metrics.widthPixels;
    }

    public static int getPhoneHeight(){
        return metrics.heightPixels;
    }

    public static float getPhoneDensity(){
        return metrics.density;
    }

    public static synchronized String getTelePhoneBaseInfo() {
        if (baseInfo == null) {
            baseInfo = Build.MANUFACTURER.replace("-", "_") + "_" +
                    Build.MODEL.replace("-", "_") + "_" +
                    Build.PRODUCT.replace("-", "_") + "_" +
                    Build.DEVICE.replace("-", "_") + "-" +
                    Build.VERSION.RELEASE + "-" +
                    telephonyManager.getSimSerialNumber() + "_" +
                    telephonyManager.getDeviceId() ;
        }
        return baseInfo;
    }

    public static String getLine1Number() {
        String phoneNumber = telephonyManager.getLine1Number();
        if (StringUtils.isNotEmpty(phoneNumber) && phoneNumber.length() > 11) {
            phoneNumber = phoneNumber.substring(phoneNumber.length() - 11, phoneNumber.length());
        }
        return phoneNumber == null ? "" : phoneNumber;
    }

    public static String getDeviceInfo() {
        if (deviceInfo == null) {
            try {
                org.json.JSONObject json = new org.json.JSONObject();
                String device_id = telephonyManager.getDeviceId();
                android.net.wifi.WifiManager wifi = (android.net.wifi.WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                String mac = wifi.getConnectionInfo().getMacAddress();
                json.put("mac", mac);
                if (device_id == null || "".equals(device_id)) {
                    device_id = mac;
                }

                if (device_id == null || "".equals(device_id)) {
                    device_id = android.provider.Settings.Secure.getString(context.getContentResolver(), android.provider.Settings.Secure.ANDROID_ID);
                }

                json.put("device_id", device_id);
                deviceInfo = json.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        return deviceInfo;
    }

}
