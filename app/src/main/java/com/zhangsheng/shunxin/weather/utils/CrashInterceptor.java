package com.zhangsheng.shunxin.weather.utils;

import android.content.Context;
import android.os.Handler;
import android.os.Looper;
import android.view.InflateException;

import com.tencent.bugly.crashreport.CrashReport;
import com.zhangsheng.shunxin.weather.common.Configure;
import com.zhangsheng.shunxin.weather.exception.CapturedException;

/**
 * @Description:
 * @Author: zhangchun
 * @CreateDate: 2021/6/17
 * @Version: 1.0
 */
public class CrashInterceptor {

    private static Context sContext;

    public static void init(Context context) {
        if (context != null) {
            sContext = context.getApplicationContext();
        }
        new Handler(Looper.myLooper()).post(() -> {
            while (true) {
                try {
                    Looper.loop();
                } catch (Throwable exception) {
                    if (exception == null) {
                        return;
                    }
                    exception.printStackTrace();
                    if (excludeBadException(exception)) {
                        throw exception;
                    } else {
                        try {
                            int oldTag = CrashReport.getUserSceneTagId(sContext);
                            CrashReport.setUserSceneTag(sContext, 197048);
                            CrashReport.postCatchedException(new CapturedException(exception));
                            CrashReport.setUserSceneTag(sContext, oldTag);
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }
                }
            }
        });
    }

    public static boolean excludeBadException(Throwable throwable) {
        boolean killProcess = Configure.Debug; // 开发模式下不拦截，
        if (throwable == null) {
            return killProcess;
        }
        if (throwable instanceof InflateException) {
            killProcess = true;
        } else if (throwable instanceof OutOfMemoryError) {
            killProcess = true;
        } else if (throwable.getMessage() != null && throwable.getMessage().startsWith("Unable to start activity")) {
            killProcess = true;
        } else if (throwable.getMessage() != null && throwable.getMessage().contains("Only the original thread that created a view hierarchy can touch its views")) {
            killProcess = true;
        }
        // 直接结束进程
        return killProcess;
    }
}
