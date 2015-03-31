package com.zrm.tumblr.app;


import java.util.HashMap;

/**
 * Created by zhangrm on 14-5-21.
 */
public class AppEnv {

    public static AppEnv.Env APP_ENV = AppEnv.Env.REA;

    public static HashMap<String, String> CHANEL_MAP;

    public static String getChanelName(String chanelNO) {
        if (CHANEL_MAP == null) {
            Chanel[] chanel = Chanel.values();
            CHANEL_MAP = new HashMap<String, String>(chanel.length);
            for (int i = 0; i < chanel.length; i++) {
                CHANEL_MAP.put(chanel[i].CHANEL_NO, chanel[i].CHANEL_NAME);
            }
        }
        return CHANEL_MAP.get(chanelNO);
    }

    public enum Env {
        REA("http://115.28.57.215:8080/api/", "Version"),
        PRE("http://localhost:8080/api/", "PreVersion");

        public String BASE_URL;
        public String VERSION_NAME;

        private Env(String BASE_URL, String VERSION_NAME) {
            this.BASE_URL = BASE_URL;
            this.VERSION_NAME = VERSION_NAME;
        }
    }


    public enum Chanel {
        CHANEL_0_DEV("0", "开发版本"),
        CHANEL_2_MARKET_OFFICE("2", "官网"),
        CHANEL_301_MAKET_91("301", "91助手"),
        CHANEL_302_MARKET_360("302", "360助手"),
        CHANEL_303_MARKET_MYAPP("303", "应用宝"),
        CHANEL_304_MARKET_WDJ("304", "豌豆荚"),
        CHANEL_305_MARKET_YYH("305", "应用汇"),
        CHANEL_306_MARKET_UMENG("306", "友盟"),
        CHANEL_307_MARKET_DMA("307", "多盟A"),
        CHANEL_308_MARKET_DMB("308", "多盟B"),
        CHANEL_309_MARKET_DMC("309", "多盟C"),
        CHANEL_310_MARKET_ANZHI("310", "安智"),
        CHANEL_311_MARKET_XIAOMI("311", "小米"),
        CHANEL_312_MARKET_BAIDU("312", "百度助手"),
        CHANEL_313_MARKET_ANDROID("313", "安卓市场"),
        CHANEL_314_MARKET_WOSTORE("314", "联通沃商店"),
        CHANEL_315_MARKET_LENOVO("315", "联想乐商店"),
        CHANEL_316_MARKET_AMAZON("316", "亚马逊"),
        CHANEL_316_MARKET_AHOME("317", "安卓之家"),
        CHANEL_316_MARKET_KUCHUAN("318", "酷传"),
        CHANEL_316_MARKET_OPPO("319", "oppo"),
        CHANEL_316_MARKET_JIFENG("320", "机锋"),
        CHANEL_316_MARKET_SOGOU("321", "sogou"),
        CHANEL_316_MARKET_HUAWEI("322", "华为"),
        CHANEL_316_MARKET_MEIZU("323", "魅族"),
        CHANEL_316_MARKET_TAOBAO("324", "淘宝商店");

        public String CHANEL_NO;
        public String CHANEL_NAME;

        private Chanel(String CHANEL_NO, String CHANEL_NAME) {
            this.CHANEL_NO = CHANEL_NO;
            this.CHANEL_NAME = CHANEL_NAME;
        }
    }
}
