package com.maiya.thirdlibrary.utils;

import android.app.ActivityManager;
import android.app.Application;
import android.content.Context;
import android.os.Build;
import android.webkit.WebView;

/**
 * @Author:liupengbing
 * @Data: 2020/7/6 13:21
 * @Email:aliupengbing@163.com
 */
public class WebViewFixUtil {
    //Caused by: java.lang.RuntimeException: Using WebView from more than one process at once with the same data
    public static void fixWebviewBug(Context context) {
        try {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.P) {
                String processName = Application.getProcessName();
                String packageName = context.getPackageName();
                if (!packageName.equals(processName)) {
                    WebView.setDataDirectorySuffix(processName);
                }
            }
        }catch (Exception e){
        }
    }

    public static String getProcessName(Context context) {
        if (context == null) return null;
        try {
            ActivityManager manager = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
            for (ActivityManager.RunningAppProcessInfo processInfo : manager.getRunningAppProcesses()) {
                if (processInfo.pid == android.os.Process.myPid()) {
                    return processInfo.processName;
                }
            }
        } catch (Exception e) {
            return null;
        }
        return null;
    }

}
