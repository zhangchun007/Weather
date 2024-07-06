package com.maiya.thirdlibrary.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * des ：
 * created by ：wuchangbin
 * created on：2019/6/28
 */
public class AppProcessUtil {

    /**
     * 获取进程名称
     *
     * @param context
     * @return
     */
    public static String getCurrentProcessName(Context context) {
        try{
            ActivityManager activityManager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            if (activityManager != null) {
                List<ActivityManager.RunningAppProcessInfo> appProcesses = activityManager.getRunningAppProcesses();
                if (appProcesses != null) {
                    for (ActivityManager.RunningAppProcessInfo appProcess : appProcesses) {
                        if (appProcess.pid == android.os.Process.myPid()) {
                            return appProcess.processName;
                        }
                    }
                }
            }
            return context.getPackageName();
        }catch (Exception e){
            return "";
        }
    }
    static  String currentProcessName;
    public static boolean isMainProcess(Context context) {
        try {
            if(currentProcessName==null){
                currentProcessName=getCurrentProcessName(context);
            }
            return currentProcessName != null && currentProcessName.equals(context.getPackageName());
        }catch (Exception e){
            return false;
        }
    }
}
