package com.zrm.tumblr.net;


import android.util.Log;

import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.zrm.tumblr.app.AppInfo;
import com.zrm.tumblr.utils.RestClient;


import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.util.HashMap;
import java.util.Map;

public class DataAcquire {

    public static final String ACCESS_TOKEN = "access_token";//请求参数
    public static final String APP_KEY = "app_key";
    public static final String SIG = "sig";
    public static final String PID = "pid";
    public static final String STATUS = "status";
    public static final String CONTENT = "content";
    public static final String NAME = "name";
    public static final String URL = "url";
    public static final String EMAIL = "email";
    public static final String PASSWORD = "password";
    public static final String MSG = "msg";
    public static final String ID = "id";


    /**
     * 服务器时间戳减去本地时间戳的差值, 在onLocationChanged回调中给timeGap赋值
     */
    public static long timeGap = 0;

    /**
     * 获取服务器时间戳
     */
    public static void getServerTime(AsyncHttpResponseHandler asyncHttpResponseHandler) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            getNetData("v1/common/getServerTime", map, asyncHttpResponseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void getPhotoList(JsonHttpResponseHandler jsonHttpResponseHandler) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            getNetData("v1/photo/list", map, jsonHttpResponseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void collectPhoto(Map<String, String> map,JsonHttpResponseHandler jsonHttpResponseHandler) {
        try {
            getNetData("v1/collect/photo", map, jsonHttpResponseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void register(Map<String, String> map,JsonHttpResponseHandler jsonHttpResponseHandler) {
        try {
            getNetData("v1/user/register", map, jsonHttpResponseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void login(Map<String, String> map,JsonHttpResponseHandler jsonHttpResponseHandler) {
        try {
            getNetData("v1/user/login", map, jsonHttpResponseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 获取个人资料
     * @param accessToken
     * @param asyncHttpResponseHandler
     */
    /*public static void getUserInfo(String accessToken, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        try {
            Map<String, String> map = new HashMap<String, String>();
            map.put(DataAcquire.ACCESS_TOKEN, accessToken);
            getNetData("v1/user/getUserInfo", map, asyncHttpResponseHandler);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/



















    /**
     * welcome获取服务器端版本
     * @param asyncHttpResponseHandler
     */
    public static void getWelcomeVersion(String accessToken,Map<String, String> map,AsyncHttpResponseHandler asyncHttpResponseHandler){
        if(asyncHttpResponseHandler == null || map == null){
            Log.e("getNetData", "error handler:"+(asyncHttpResponseHandler != null));
            return;
        }
        map.put(DataAcquire.ACCESS_TOKEN, accessToken);
        getNetData("v1/passport/welcome", map, asyncHttpResponseHandler);
    }

    /**
     * about获取服务器端版本
     * @param asyncHttpResponseHandler
     */
    public static void getAboutVersion(Map<String, String> map, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        if (asyncHttpResponseHandler == null) {
            Log.e("getNetData", "error handler:" + (asyncHttpResponseHandler != null));
            return;
        }
        getNetData("v1/passport/about", map, asyncHttpResponseHandler);
    }

    private static void getNetData(String url, Map<String, String> map, AsyncHttpResponseHandler asyncHttpResponseHandler) {
        if (asyncHttpResponseHandler == null || map == null) {
            Log.e("getNetData", "error map:" + (map != null) + "  handler:" + (asyncHttpResponseHandler != null));
            return;
        }
        RestClient.post(url, map, asyncHttpResponseHandler);
    }

    /* 发送文件 */
    private static void sendRequest(String url, Map<String, String> map, File file, AsyncHttpResponseHandler asyncHttpResponseHandler) {

        if (asyncHttpResponseHandler == null || map == null) {
            Log.e("getNetData", "error map:" + (map != null) + "  handler:" + (asyncHttpResponseHandler != null));
            return;
        }
        RestClient.post(url, map, file, asyncHttpResponseHandler);
    }


}
