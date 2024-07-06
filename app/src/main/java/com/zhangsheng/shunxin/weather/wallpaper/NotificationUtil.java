package com.zhangsheng.shunxin.weather.wallpaper;

import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.os.Build;

import androidx.core.app.NotificationCompat;

import com.maiya.thirdlibrary.utils.AppUtils;

/**
 * @Description:
 * @Author: zhangchun
 * @CreateDate: 2021/6/17
 * @Version: 1.0
 */
public class NotificationUtil {
    public static String RESIDENT_CHANNEL_ID = AppUtils.INSTANCE.getAppPackageName().replaceAll("\\.", "");
    public static String RESIDENT_CHANNEL_NAME = "天气通知";

    public static void createNotificationChannel(Context context) {
        if (context != null && Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel residentChannel = new NotificationChannel(RESIDENT_CHANNEL_ID, RESIDENT_CHANNEL_NAME, NotificationManager.IMPORTANCE_LOW);

            NotificationManager notificationManager = (NotificationManager) context.getSystemService(Context.NOTIFICATION_SERVICE);
            if (notificationManager != null) {
                notificationManager.createNotificationChannel(residentChannel);
            }
        }
    }

    public static NotificationCompat.Builder createNotificationBuilder(Context context, String channelId) {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            try {
                return new NotificationCompat.Builder(context, channelId);
            } catch (Throwable e) {
                e.printStackTrace();
            }
        }
        return
    }
}
