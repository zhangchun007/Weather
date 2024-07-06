package com.zhangsheng.shunxin.weather.utils

import android.content.Context
import android.text.TextUtils
import com.maiya.thirdlibrary.utils.AppUtils
import com.zhangsheng.shunxin.weather.common.Configure
import com.meituan.android.walle.WalleChannelReader
import com.tencent.bugly.crashreport.CrashReport
import com.zhangsheng.shunxin.BuildConfig
import java.io.BufferedReader
import java.io.FileReader
import java.io.IOException

object BuglyConfig {

//    private val APP_ID: String
//
//    init {
//        APP_ID = "0b3a95f092"
//    }

    fun initBugly(cxt: Context) {
        val strategy = CrashReport.UserStrategy(cxt)
        strategy.appVersion = BuildConfig.BUGLY_VERSTION  // 版本号
        strategy.appPackageName = AppUtils.appPackageName // 包名
        strategy.appChannel = WalleChannelReader.getChannel(cxt, Configure.appQid)      // 渠道号
        CrashReport.initCrashReport(cxt, Configure.buglyAppId, false, strategy)// 初始化
    }

    fun testJavaCrash() {
        CrashReport.testJavaCrash()
    }

    //单独设置页面的错误的tag
    fun setUserSceneTag(cxt: Context, tag: Int) {
        CrashReport.setUserSceneTag(cxt, tag)
    }

    /**
     * 获取进程号对应的进程名
     *
     * @param pid 进程号
     * @return 进程名
     */
    private fun getProcessName(pid: Int): String? {
        var reader: BufferedReader? = null
        try {
            reader = BufferedReader(FileReader("/proc/$pid/cmdline"))
            var processName: String = reader.readLine()
            if (!TextUtils.isEmpty(processName)) {
                processName = processName.trim { it <= ' ' }
            }
            return processName
        } catch (throwable: Throwable) {
            throwable.printStackTrace()
        } finally {
            try {
                reader?.close()
            } catch (exception: IOException) {
                exception.printStackTrace()
            }
        }
        return null
    }

}