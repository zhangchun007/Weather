package com.zhangsheng.shunxin.weather.notifycation

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.Build
import com.zhangsheng.shunxin.weather.MainActivity
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.ext.skipActivity
import java.lang.reflect.Method

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/12/15 20:04
 */
class WidgetBroadcast : BroadcastReceiver() {
    companion object {
        const val ACTION_KEY = "action"
        const val REFRESH_VALUE = "refresh"
        const val STATUS_BAR_CONST = "statusbar"
        const val COLLAPSE_CONST = "collapse"
        const val COLLAPSE_PANELS_CONST = "collapsePanels"
        const val PENDING_REQUEST_CODE = 8888
        var DURATION_8_MIN_FOR_TEST = 8 * 60 * 1000L // 8分钟间隔
        var DURATION_30_MIN = 30 * 60 * 1000L // 30分钟间隔
    }

    override fun onReceive(context: Context?, intent: Intent?) {
        if (intent == null) return
        val action = intent.action

        if (context != null) {
            collapseStatusBar(context)
        }
        if (action == "refresh") {
            getAppModel().initWidget()
        } else {
            skipActivity(MainActivity::class.java) {
                this.putExtra("from", "refresh")
            }
        }
    }

    /**
     *
     * 收起通知栏
     * @param context
     */
    fun collapseStatusBar(context: Context) {
        try {
            val statusBarManager = context.getSystemService("statusbar")
            val collapse: Method
            collapse = if (Build.VERSION.SDK_INT <= 16) {
                statusBarManager.javaClass.getMethod("collapse")
            } else {
                statusBarManager.javaClass.getMethod("collapsePanels")
            }
            collapse.invoke(statusBarManager)
        } catch (localException: Exception) {
            localException.printStackTrace()
        }
    }

}