package com.zrm.tumblr.utils;

import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import com.zrm.tumblr.app.AppInfo;
import com.zrm.tumblr.net.DataAcquire;


import java.io.File;
import java.io.FileNotFoundException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Map;

/**
 * Created by leitang on 8/23/13.
 */
public class RestClient {
    private final static String TAG = "RestClient" ;



    public final static String httpMethod = "POST";
    public static String firstOpen = "false";

    public static String BASE_URL = AppUtils.APP_ENV.BASE_URL;//final




    /*public static void get(String url, RequestParams params, AsyncHttpResponseHandler responseHandler) {
        client.get(getAbsoluteUrl(url), params, responseHandler);
    }*/

    public static void post(String url, Map<String, String> map,AsyncHttpResponseHandler responseHandler) {
        String baseInfo = createUserBaseInfo();
        map.put("base_info", baseInfo);
        map.put(DataAcquire.APP_KEY, AppInfo.APP_KEY);
        map.put(DataAcquire.SIG, EncryptUtils.getSignature(map,AppInfo.APP_SECRET));
        //map.put("time_token", String.valueOf(System.currentTimeMillis() + DataAcquire.timeGap));
        RequestParams params = new RequestParams(map);
        String urls = getAbsoluteUrl(url);
        /*Log.e(TAG,urls);
        Log.e(TAG,map.toString());*/
        AsyncHttpClient client = new AsyncHttpClient();
        client.post(urls, params, responseHandler);
    }

    public static void post(String url, Map<String, String> map, File file,AsyncHttpResponseHandler responseHandler) {
        map.put("base_info", createUserBaseInfo());
        map.put(DataAcquire.APP_KEY, AppInfo.APP_KEY);
        map.put(DataAcquire.SIG, EncryptUtils.getSignature(map,AppInfo.APP_SECRET));
        //map.put("time_token", String.valueOf(System.currentTimeMillis() + DataAcquire.timeGap));
        //map.put("sig", CreateSig.createSig(httpMethod, getAbsoluteUrl(url), map, androidSec));
        RequestParams params = new RequestParams(map);
        try {
            params.put("voice_message", file);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }

        AsyncHttpClient client = new AsyncHttpClient();
        client.post(getAbsoluteUrl(url), params, responseHandler);
    }



    public static String getAbsoluteUrl(String relativeUrl) {
        return BASE_URL + relativeUrl;
    }


    public static synchronized String createUserBaseInfo() {
        //String s = UILApplication.getTelePhoneBaseInfo()+firstOpen;
        return "createUserBaseInfo";
    }

    public static String md5(final String password) {
        try {
            MessageDigest digest = MessageDigest.getInstance("MD5");
            digest.update(password.getBytes());
            byte messageDigest[] = digest.digest();

            StringBuffer hexString = new StringBuffer();
            for (int i = 0; i < messageDigest.length; i++) {
                String h = Integer.toHexString(0xFF & messageDigest[i]);
                while (h.length() < 2)
                    h = "0" + h;
                hexString.append(h);
            }
            return hexString.toString();
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        return "";
    }

    public String hashKeyForDisk(String key) {
        String cacheKey;
        try {
            final MessageDigest mDigest = MessageDigest.getInstance("MD5");
            mDigest.update(key.getBytes());
            cacheKey = bytesToHexString(mDigest.digest());
        } catch (NoSuchAlgorithmException e) {
            cacheKey = String.valueOf(key.hashCode());
        }
        return cacheKey;
    }

    private String bytesToHexString(byte[] bytes) {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < bytes.length; i++) {
            String hex = Integer.toHexString(0xFF & bytes[i]);
            if (hex.length() == 1) {
                sb.append('0');
            }
            sb.append(hex);
        }
        return sb.toString();
    }
}
