package com.zhangsheng.shunxin.information.utils;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

/**
 * @Author:liupengbing
 * @Data: 2020/5/23 10:53 AM
 * @Email:aliupengbing@163.com
 */
public class AppTimeUtils {
    public static final SimpleDateFormat YYYYMMDDHHMMSS_FORMAT_SIMPLE = new SimpleDateFormat("yyyyMMddHHmmss", Locale.getDefault());

    /**
     * 获取"yyyyMMddHHmmss"格式的14位时间字符串
     * @return
     */
    public static String getCurrentSimpleYYYYMMDDHHMM() {
        Date date = new Date();
        return YYYYMMDDHHMMSS_FORMAT_SIMPLE.format(date);
    }

    public static String getInfoPublicTime(Long publicTime){
        String title="";
        if(publicTime==null){
            return title;
        }
        long  currTime=System.currentTimeMillis()/1000;
//        Log.w("lpb","publicTime;"+publicTime+",,currTime:"+currTime);
        if(currTime-publicTime>=0&&currTime-publicTime<5*60){
            title ="刚刚";
        }else if(currTime-publicTime>=5*60&&currTime-publicTime<6*60){
            title ="5分钟之前";
        }else if(currTime-publicTime>=6*60&&currTime-publicTime<24*60*60){
            title ="1小时之前";
        }else if(currTime-publicTime<0){
            title ="刚刚";
        }else{
            title ="1天之前";
        }
        return title;
    }

    public static String getHHmmssTime(long second) {
        long h = 0;
        long d = 0;
        long s = 0;
        long temp = second % 3600;
        if (second > 3600) {
            h = second / 3600;
            if (temp != 0) {
                if (temp > 60) {
                    d = temp / 60;
                    if (temp % 60 != 0) {
                        s = temp % 60;
                    }
                } else {
                    s = temp;
                }
            }
        } else {
            d = second / 60;
            if (second % 60 != 0) {
                s = second % 60;
            }
        }
        if(h==0){
            if(d<10){
                if(s<10){
                    return  "0"+d + ":0" + s ;
                }else{
                    return  "0"+d + ":" + s ;
                }

            }else{
                if(s<10){
                    return  d + ":0" + s ;
                }else{
                    return  d + ":" + s ;
                }

            }
        }else{
            if(d<10){
                if(s<10){
                    return  "0"+h + ":0" + d + ":0" + s ;
                }else{
                    return  "0"+h + ":0" + d + ":" + s ;
                }

            }else{
                if(s<10){
                    return h + ":" + d + ":0" + s ;
                }else{
                    return h + ":" + d + ":" + s ;
                }

            }

        }

    }

}
