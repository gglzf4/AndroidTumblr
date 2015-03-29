package com.zrm.tumblr.app;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.zrm.tumblr.utils.Logger;

import org.apache.commons.lang3.StringUtils;



public class Application extends android.app.Application {
    public static String TAG = Application.class.getSimpleName();

    public static Context context;

    private static TelephonyManager telephonyManager;
    private static String baseInfo;
    private static String deviceInfo;

    public static DisplayMetrics metrics;

    //用户所在城市，是否改变城市     true以改变    false为改变
    public static String userCiy;
    public static boolean isChanged;

    //public static int CHANEL_NO;
    //public static boolean IS_DEV_ENV;//是否为开发版本



    @Override
    public void onCreate() {
        super.onCreate();

        Logger.i(TAG, "----------------Application init start------------------");
        context = getApplicationContext();

        /********读取AndroidManifest.xml自定义内容*******/
        AppInfo.init(context);

        telephonyManager = (TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE);

        initImageLoader(context);








        initDisplayMetrics();

    }





    @Override
    public void onTerminate() {
        //应用程序退出时会被系统调用
        Logger.i(TAG, "--onTerminate--");
    }

    private void initDisplayMetrics() {
        metrics = context.getResources().getDisplayMetrics();
    }


    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2)
                .denyCacheImageMultipleSizesInMemory()
                .discCacheFileNameGenerator(new Md5FileNameGenerator())
                .tasksProcessingOrder(QueueProcessingType.LIFO)
                .writeDebugLogs() // Remove for release app
                .build();
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config);
        ;
    }

    public static TelephonyManager getTelephonyManager() {
        return telephonyManager;
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
