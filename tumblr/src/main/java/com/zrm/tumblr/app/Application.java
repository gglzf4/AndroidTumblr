package com.zrm.tumblr.app;

import android.content.Context;
import android.os.Build;
import android.telephony.TelephonyManager;
import android.util.DisplayMetrics;

import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiscCache;
import com.nostra13.universalimageloader.cache.disc.naming.HashCodeFileNameGenerator;
import com.nostra13.universalimageloader.cache.memory.impl.LruMemoryCache;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.decode.BaseImageDecoder;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.nostra13.universalimageloader.utils.StorageUtils;
import com.zrm.tumblr.utils.AppUtils;
import com.zrm.tumblr.utils.Logger;

import org.apache.commons.lang3.StringUtils;

import java.io.File;


public class Application extends android.app.Application {
    public static String TAG = Application.class.getSimpleName();

    public static Context context;

    private static TelephonyManager telephonyManager;
    private static String baseInfo;
    private static String deviceInfo;

    public static DisplayMetrics metrics;


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
        File cacheDir = StorageUtils.getCacheDirectory(context);
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .memoryCacheExtraOptions(480, 800) // default = device screen dimensions
                .diskCacheExtraOptions(480, 800, null)
                .threadPoolSize(3) // default
                .threadPriority(Thread.NORM_PRIORITY - 2) // default
                .tasksProcessingOrder(QueueProcessingType.FIFO) // default
                .denyCacheImageMultipleSizesInMemory()
                .memoryCache(new LruMemoryCache(2 * 1024 * 1024))
                .memoryCacheSize(2 * 1024 * 1024)
                .memoryCacheSizePercentage(13) // default
                .diskCache(new UnlimitedDiscCache(cacheDir)) // default
                .diskCacheSize(50 * 1024 * 1024)
                .diskCacheFileCount(100)
                .diskCacheFileNameGenerator(new HashCodeFileNameGenerator()) // default
                .imageDownloader(new BaseImageDownloader(context)) // default
                .imageDecoder(new BaseImageDecoder(AppUtils.isDevEnv())) // default
                .defaultDisplayImageOptions(DisplayImageOptions.createSimple()) // default
                .writeDebugLogs()
                .build();

        ImageLoader.getInstance().init(config);

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
