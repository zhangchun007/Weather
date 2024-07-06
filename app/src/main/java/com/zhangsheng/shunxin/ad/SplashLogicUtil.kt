package com.zhangsheng.shunxin.ad

import android.app.Activity
import android.app.KeyguardManager
import android.content.Context
import android.content.Intent
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.ext.parseInt
import com.maiya.thirdlibrary.ext.parseLong
import com.maiya.thirdlibrary.utils.DeviceUtil
import com.zhangsheng.shunxin.weather.activity.SplashActivity
import com.zhangsheng.shunxin.weather.ext.getOpenTimes
import com.zhangsheng.shunxin.weather.ext.getSplashControl
import com.zhangsheng.shunxin.weather.ext.getSplashShowTimes
import com.zhangsheng.shunxin.weather.ext.isControl
import com.zhangsheng.shunxin.weather.utils.DataUtil
import java.lang.Exception
import kotlin.math.abs

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/6/19 17:36
 */
object SplashLogicUtil {

    fun openSplash(activity: Activity): Boolean {
        try {
            if (isControl()) {
                return false
            }

            if (!checkSp()) {
                return false
            }

            if (activity is WarmSplashProhibition) {
                return false
            }

            if (inKeyguardRestrictedInputMode(activity.applicationContext)) {
                return false
            }
            var intent = Intent(activity, SplashActivity::class.java)
            intent.putExtra("HotSplash", true)
            activity.startActivity(intent)
            activity.overridePendingTransition(0, 0)

            return true
        } catch (e: Exception) {
            e.printStackTrace()
            return false
        }
    }

    fun checkSp(): Boolean {
        var splash = getSplashControl()
        if (!splash.open_inerval.isNullOrEmpty()
            && DataUtil.daysBetween(
                DeviceUtil.installed(),
                System.currentTimeMillis()
            ) >= splash.nN().make_start_days.nN().parseInt(0)
            && getOpenTimes() >= splash.launch_times.nN().parseInt()
        ) {
            var showTime = getSplashShowTimes()

            return showTime == 0L || abs(showTime - System.currentTimeMillis()) >= splash.open_inerval.parseLong() * 1000

        }
        return false
    }

    private fun inKeyguardRestrictedInputMode(context: Context): Boolean {
        val manager = context.getSystemService(Context.KEYGUARD_SERVICE) as KeyguardManager
        return manager.isKeyguardLocked
    }

    /**
     * 实现该接口来禁止在activity到前台的时候显示热启动开屏
     */
    interface WarmSplashProhibition
}