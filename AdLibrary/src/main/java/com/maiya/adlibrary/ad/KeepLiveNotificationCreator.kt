package com.maiya.adlibrary.ad

import android.app.Notification
import android.content.Context
import android.content.Intent
import com.xyz.sdk.e.keeplive.notification.ICustomNotificationCreator

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2021/2/3 19:41
 */
class KeepLiveNotificationCreator(val clzz:Class<*>) :ICustomNotificationCreator{
    override fun getSmallIconResId(): Int = 0

    override fun getLargeIconResId(): Int =0

    override fun notificationMode(): Int =2

    override fun createNotification(p0: Context?, p1: Intent?): Notification? =null

    override fun getReceiverClass(): Class<*> {
      return clzz
    }
}