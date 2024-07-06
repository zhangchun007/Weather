package com.zhangsheng.shunxin.information.utils;

import android.annotation.SuppressLint;
import android.annotation.TargetApi;
import android.app.Activity;
import android.content.Context;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.provider.Settings;
import android.telephony.TelephonyManager;
import android.text.TextUtils;
import android.view.Display;
import android.view.WindowManager;

import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.util.Enumeration;

/**
 * @Author:liupengbing
 * @Data: 2020/5/23 10:46 AM
 * @Email:aliupengbing@163.com
 */
public class PhoneInfoUtils {

    /**
     * 系统4.0的时候
     * 获取手机IMEI 或者MEID
     *
     * @return 手机IMEI
     */
    @SuppressLint("MissingPermission")
    public static String getImeiOrMeid(Context ctx) {
        try {
            TelephonyManager manager = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
            if (manager != null) {
                return manager.getDeviceId();
            }
        }catch (RuntimeException e){
            e.printStackTrace();
        }

        return null;
    }



    /**
     *  5.0统一使用这个获取IMEI IMEI2 MEID
     *
     * @param ctx
     * @return
     */
    @SuppressLint("MissingPermission")
    @TargetApi(Build.VERSION_CODES.M)
    public static String getImeiAndMeid(Context ctx) {
        try {
            TelephonyManager mTelephonyManager = (TelephonyManager) ctx.getSystemService(Activity.TELEPHONY_SERVICE);
            mTelephonyManager.getDeviceId(0);
            return  mTelephonyManager.getDeviceId(0);
        }catch (RuntimeException e){
            e.printStackTrace();
        }
        return "";
    }

    @SuppressLint("MissingPermission")
    @TargetApi(Build.VERSION_CODES.O)
    public static String getIMEIforO(Context context) {
        String imei1 = "";
        try {
            TelephonyManager tm = ((TelephonyManager) context.getSystemService(Context.TELEPHONY_SERVICE));
            imei1 = tm.getImei(0);
        }catch (RuntimeException e){
            e.printStackTrace();
        }
        return imei1;
    }



    public static String getIMEI(Context ctx) {
        String imei = "";
        try {

            if (Build.VERSION.SDK_INT < Build.VERSION_CODES.M) {  //4.0以下 直接获取
                imei = getImeiOrMeid(ctx);
            } else if (Build.VERSION.SDK_INT < Build.VERSION_CODES.O) { //5。0，6。0系统
                imei = getImeiAndMeid(ctx);
            } else {
                imei = getIMEIforO(ctx);

            }

        } catch (SecurityException e) {
            e.printStackTrace();
        }
        return imei;
    }


    /**
     * 获取设备Id
     *
     * @param context
     * @return
     */
    public static String getAndroidId(Context context) {
        String androidID = "";
        try {
            androidID = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        } catch (Exception e) {

        }
        return androidID;
    }

    /**
     * 获取设备sn
     * @return
     */
    public static String getSN() {
        String sn = "";
        if (Build.VERSION.SDK_INT < 28) {
            sn = Build.SERIAL;
        }
        return sn;
    }

    /**
     * 获取uuid（和牛数保持一致的UUID）
     * @param context
     * @return
     */
    public static String getUUID(Context context){
        String uuid = getAndroidId(context);
        String sn =  getSN();
        if (!TextUtils.isEmpty(sn) && !Build.UNKNOWN.equalsIgnoreCase(sn)) {
            uuid = uuid + "_" + sn;
        }
        return uuid;
    }


    /**
     * 获取机型
     */
    public static String getPhoneModel() {
        String brand = Build.BRAND;//手机品牌
        String model = Build.MODEL;//手机型号
        return brand + " " + model;
    }

    /**
     * 获取操作系统版本号
     *
     * @return
     */
    public static String getSystemVersion() {
        return "Android" + Build.VERSION.RELEASE;
    }


    /**
     * 获取手机分辨率
     *
     * @param context
     * @return
     */
    public static String getResolution(Context context) {
        // 方法1 Android获得屏幕的宽和高
        WindowManager windowManager = ((Activity) context).getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        int screenWidth = display.getWidth();
        int screenHeight = display.getHeight();
        return screenWidth + "*" + screenHeight;

    }

    /**
     * 获取ip
     */
    public static String getIp(Context context) {
        String ip;
        if (context == null) {
            ip = "0.0.0.0";
        }else {
            if(NetworkUtil.isWifiNetworkUp(context)) {
                //获取wifi服务
                WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
                //判断wifi是否开启
                if (!wifiManager.isWifiEnabled()) {
                    wifiManager.setWifiEnabled(true);
                }
                WifiInfo wifiInfo = wifiManager.getConnectionInfo();
                int ipAddress = wifiInfo.getIpAddress();
                ip = intToIp(ipAddress);

//                Log.w("lpb", "-----》ip:" + ip);
            }else {
                ip = get4GIp();
//                Log.w("lpb", "-----》4gip:" + ip);
            }
        }
        return ip;
    }

    public static String intToIp(int i) {
        return  ( i & 0xFF) + "." +
                ((i >> 8 ) & 0xFF) + "." +
                ((i >> 16 ) & 0xFF) + "." +
                ((i >> 24 ) & 0xFF ) ;
    }

    public static  String get4GIp(){
        //这是获取4G网的ip
        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements();) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf
                        .getInetAddresses(); enumIpAddr.hasMoreElements();) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && inetAddress instanceof Inet4Address) {
//                        Log.w("lpb","4G---->"+inetAddress.getHostAddress());
                        return   inetAddress.getHostAddress();
                    }
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "0.0.0.0";
    }


}
