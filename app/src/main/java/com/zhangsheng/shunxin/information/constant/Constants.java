package com.zhangsheng.shunxin.information.constant;


import com.zhangsheng.shunxin.information.bean.InfoBean;
import com.zhangsheng.shunxin.information.bean.InfoCommendBean;
import com.zhangsheng.shunxin.information.bean.InfoBean;
import com.zhangsheng.shunxin.information.bean.InfoCommendBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:liupengbing
 * @Data: 2020/5/21 7:07 PM
 * @Email:aliupengbing@163.com
 */
public class Constants {

    public interface NewsType {
        /**
         * 信息流
         */
        String Type_Info = "info";

        /**
         * 视频流
         */
        String Type_Video = "video";
    }

    /**
     * 信息流 天气 secure_key
     */
     public static String  secure_key="51f6af710c1c828542d924578854be6d";
    /**
     * 信息流 天气 partner：zx_rywnl_api
     */
     public static String  partner="zx_jdtq_api";
    /**
     * 信息流注册获取的Token
     */
    public static String ACCESS_TOKEN="ACCESS_TOKEN";

     public static final String UuidKey = "UuidKey";

    public static final int infoStreamCorner = 5;


    public static final String SP_INFO_ORDER_CHANNEL="sp_info_order_channel";

    public static final String SP_INFO_ALL_CHANNEL="sp_info_all_channel";

    public static final String SP_INFO_LOCAL_ORDER_CHANNEL="sp_info_local_order_channel";

    public static final String SP_AD_WEATHER_TOP = "ad_weather_top";

    public static  String city = "";

    public static String curTabCode="__all__";
    public static String curTabTitle="推荐";

    public static String locationCity="";


    //在信息流详情页面停留时间久 点击返回按钮莫名其妙的返回到第一屏
    public static Boolean topDownTop=false;

    public static String category;
    public static List<InfoBean.DataBean> mList = new ArrayList<>();
    public static List<InfoCommendBean.DataBean> mCommenList = new ArrayList<>();

    public static String temp = "";
    public static String wtid = "";



}
