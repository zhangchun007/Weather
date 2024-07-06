package com.zhangsheng.shunxin.weather.utils

import android.app.Activity
import android.app.Application
import android.os.Bundle
import com.maiya.thirdlibrary.base.BaseActivity
import com.maiya.thirdlibrary.ext.LogE
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.ext.runOnTime
import com.maiya.thirdlibrary.utils.ActivityManageTools
import com.maiya.thirdlibrary.utils.CacheUtil
import com.maiya.wallpaper.activity.WallpaperSetActivity
import com.zhangsheng.shunxin.weather.MainActivity
import com.zhangsheng.shunxin.weather.activity.SplashActivity
import com.zhangsheng.shunxin.ad.AdUtils
import com.zhangsheng.shunxin.ad.SplashLogicUtil
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.ext.skipActivity
import com.zhangsheng.shunxin.weather.net.bean.AdPopBean
import com.zhangsheng.shunxin.weather.net.bean.ReBootTimesBean

object ProcessLifecycleObserver : Application.ActivityLifecycleCallbacks {
    private var startCount = 0
    private var curBackTime = 0L
    private var backTime = 0L
    var isFirstOpen = true
    private var isLockPage = false
    var isUserFirstOpen = false


    override fun onActivityStarted(activity: Activity) {
        if (activity is WallpaperSetActivity) {
            isLockPage = true
        }

        if (startCount == 0) {
            AdUtils.clearPopStatus()
            if (activity is BaseActivity) {
                if (curBackTime != 0L) {
                    backTime = System.currentTimeMillis() - curBackTime
                    curBackTime = 0
                } else {
                    backTime = 0
                }
                if (isLockPage) {
                    isLockPage = false
                } else {
                    deal(activity)
                }
            }
        }
        startCount++
    }

    private fun deal(activity: Activity) {
        if (CacheUtil.getBoolean(Constant.SP_AGREE_PRIVACY,false)){
            getAppModel().requestControl()
            runOnTime(200) {
                AdUtils.loadPreAd()
            }
        }
        if (isFirstOpen) return
        AdConfig()
        if (!SplashLogicUtil.openSplash(activity)) {
            splashNext(activity)
        }
    }

    fun splashNext(activity: Activity) {
        Try {
            checkBackTime(MainActivity::class.java) {
                if (!activity.isFinishing && activity.javaClass == SplashActivity::class.java) {
                    activity.finish()
                    ActivityManageTools.removeActivity(activity)
                }
            }
        }
    }

    fun AdConfig() {
        AdUtils.popAdShouldLoad=true
        var reBoot = CacheUtil.getObj(Constant.SP_REBOOT_TIME, ReBootTimesBean::class.java).nN()
        if (!DataUtil.isSameDay(reBoot.recordTime, System.currentTimeMillis())) {
            CacheUtil.putObj(Constant.SP_REBOOT_TIME, reBoot.apply {
                this.recordTime = System.currentTimeMillis()
                this.times = 1
            })
            CacheUtil.put(Constant.SP_SPLASH_SHOW_TIME, 0L)
            CacheUtil.putObj(Constant.SP_AD_POP, AdPopBean().apply {
                this.nN().showMainPopTimes = 0
                this.nN().showMainPopShowTime = 0
            })

        } else {
            CacheUtil.putObj(Constant.SP_REBOOT_TIME, reBoot.apply {
                this.times = reBoot.times + 1
            })
        }
    }

    private fun checkBackTime(
        activity: Class<MainActivity>,
        function: () -> Unit
    ) {
        if (ActivityManageTools.topActivity() != MainActivity::class.java && backTime != 0L && backTime > 30 * 60 * 1000) {
            ActivityManageTools.removeAboveActivities(activity) {
                Try {
                    skipActivity(MainActivity::class.java) {
                        putExtra("from", "main")
                    }
                }
            }
        } else {
            function()
        }

    }

    override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {
        if (activity is BaseActivity) {
            ActivityManageTools.addActivity(activity)
        }
    }

    override fun onActivityResumed(activity: Activity) {
        Try {
            if (activity is SplashActivity || activity !is BaseActivity) return@Try
            if (AdUtils.checkAdPop()) {
                AdUtils.showPopAd(activity)
            }

        }

    }

    override fun onActivityPaused(activity: Activity) {

    }


    override fun onActivityStopped(activity: Activity) {
        startCount--
        if (startCount == 0) {
            isUserFirstOpen = false
            curBackTime = System.currentTimeMillis()
        }

    }

    override fun onActivityDestroyed(activity: Activity) {
        if (activity is BaseActivity) {
            ActivityManageTools.removeActivity(activity)
        }
    }

    override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {

    }
}