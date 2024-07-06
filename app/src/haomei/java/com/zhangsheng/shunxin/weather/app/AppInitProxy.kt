package com.zhangsheng.shunxin.weather.app

import android.app.Application
import com.bytedance.applog.AppLog
import com.bytedance.applog.InitConfig
import com.bytedance.applog.picker.Picker
import com.bytedance.applog.util.UriConstants
import com.bytedance.sdk.dp.DPSdk
import com.bytedance.sdk.dp.DPSdkConfig

import com.maiya.thirdlibrary.ext.LogE
import com.meituan.android.walle.WalleChannelReader
import com.zhangsheng.shunxin.weather.common.Configure

class AppInitProxy {

    companion object {

        fun initCustom(application: Application) {
            initNews(application)
        }

        private fun initNews(application: Application) {
            val Log = InitConfig(
                "203316",
                "${WalleChannelReader.getChannel(application, Configure.appQid)}"
            )
            Log.setUriConfig(UriConstants.DEFAULT)
            Log.picker = Picker(application, Log)
            Log.isAbEnable = false
            Log.setAutoStart(true)
            AppLog.init(application, Log)

            val config = DPSdkConfig.Builder()
                .debug(Configure.Debug) //日志调试开关，上线时一定要关闭
                .needInitAppLog(false) //是否由sdk来初始化applog
                .partner("haomeitianqi_sdk")
                .secureKey("a6ab96a57c2f9e233ab09111e8621122")
                .appId("203316")
                .initListener { isSuccess -> //注意：1如果您的初始化没有放到application，请确保使用时初始化已经成功
                    //     2如果您的初始化在application，可以忽略该初始化接口
                    //isSuccess=true表示初始化成功
                    //初始化失败，可以再次调用初始化接口（建议最多不要超过3次)
                    LogE("DPHolder：init result=$isSuccess\"")
                }
                .build()

            DPSdk.init(application, config)
        }
    }
}