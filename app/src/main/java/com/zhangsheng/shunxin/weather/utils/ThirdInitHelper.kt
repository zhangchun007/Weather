package com.zhangsheng.shunxin.weather.utils

import android.app.Activity
import android.app.Application
import com.bumptech.glide.Glide
import com.clean.bridge.OnePeaceBridge
import com.clean.widget.CleanView
import com.maiya.adlibrary.ad.KeepLiveNotificationCreator
import com.maiya.adlibrary.ad.listener.ShowFeedListener
import com.maiya.sdk.httplibrary.http.HttpSDK
import com.maiya.thirdlibrary.ext.LogE
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.utils.AppContext
import com.maiya.thirdlibrary.utils.AppProcessUtil
import com.maiya.thirdlibrary.utils.CacheUtil
import com.maiya.thirdlibrary.utils.DeviceUtil
import com.maiya.wallpaper.WallPaperSDK
import com.maiya.wallpaper.WallpaperSDKConfig
import com.meituan.android.walle.WalleChannelReader
import com.my.sdk.stpush.STPushManager
import com.my.sdk.stpush.StPushConfig
import com.my.sp.ISpInitListener
import com.my.sp.SpSDK
import com.my.sp.SpSDKConfig
import com.xinmeng.shadow.common.XMGlobalActivityLifecycleMonitor
import com.xinmeng.shadow.mediation.source.IEmbeddedMaterial
import com.xinmeng.shadow.mediation.source.LoadMaterialError
import com.xm.xmcommon.XMCommonConfig
import com.xm.xmcommon.XMCommonManager
import com.xm.xmlog.XMLogManager
import com.xm.xmlog.bean.XMActivityBean
import com.xyz.sdk.e.keeplive.KeepLive
import com.zhangsheng.shunxin.BuildConfig
import com.zhangsheng.shunxin.ad.AdConstant
import com.zhangsheng.shunxin.ad.AdConstant.SLOT_BIGXWXQ
import com.zhangsheng.shunxin.ad.AdSdkInitializer
import com.zhangsheng.shunxin.ad.widget.B2PictureAdMaterialView
import com.zhangsheng.shunxin.ad.widget.CleanFullAdvPop
import com.zhangsheng.shunxin.ad.widget.CleanPictureAdMaterialView
import com.zhangsheng.shunxin.weather.MainActivity
import com.zhangsheng.shunxin.weather.app.App
import com.zhangsheng.shunxin.weather.app.AppInitProxy
import com.zhangsheng.shunxin.weather.common.*
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.ext.isControl
import com.zhangsheng.shunxin.weather.ext.sdkReport
import com.zhangsheng.shunxin.weather.ext.showReport
import com.zhangsheng.shunxin.weather.net.bean.PushBean
import com.zhangsheng.shunxin.weather.notifycation.WidgetBroadcast
import com.zhangsheng.shunxin.weather.service.PushService
import com.zhangsheng.shunxin.weather.wallpaper.CustomCommonParams
import com.zhangsheng.shunxin.weather.wallpaper.DefaultLogReport
import com.zhangsheng.shunxin.weather.widget.weather.WeatherForecast
import com.zhangsheng.shunxin.weather.widget.weather.WeatherRainSnowForecast

object ThirdInitHelper {

    private var xmConfig: XMCommonConfig? = null

    fun init(app: Application) {
        if (AppProcessUtil.isMainProcess(app)) {
            if (CacheUtil.getBoolean(Constant.SP_AGREE_PRIVACY, false)) {
                backInit(app)
            } else {
                preInit(app)

            }

        }
        KeepLive.init(app, KeepLiveNotificationCreator(WidgetBroadcast::class.java))
    }

    private fun preInit(app: Application) {
        //XMLOG
        XMCommonManager.getInstance().preInit(app, getXmConfig(app))

        //广告
        XMGlobalActivityLifecycleMonitor.register(app)
    }


    //后置完全初始化
    fun backInit(app: Application, secondInt: Boolean = false) {
        //XMLOG
        if (secondInt) {
            XMCommonManager.getInstance().init()
            XMLogManager.getInstance().init()
            getAppModel().requestControl()
        } else {
            xmLog(app)
        }
        AdSdkInitializer.init(app)
        AppInitProxy.initCustom(app)
        push()
        BuglyConfig.initBugly(app)
        App.getAppModel().initWidget()

        initHttpSDK() // http sdk初始化一定要在壁纸和场景功能sdk之前
        initSpSdk(app)
        initCleanSdk()
        if (!isControl()) {
            initWallpaperSDK(app)
        }
    }

    private fun initCleanSdk() {
        OnePeaceBridge.getInstance().connectBridge { activity, iBridgeListener ->
            CleanPictureAdMaterialView(activity).apply {
                this.showFeedAd(
                    activity,
                    AdConstant.SLOT_BIGSET,
                    4f,
                    true,
                    object : ShowFeedListener() {
                        override fun onLoad(p0: IEmbeddedMaterial?): Boolean {
                            iBridgeListener.showAd(root)
                            return super.onLoad(p0)
                        }

                        override fun onError(p0: LoadMaterialError?) {
                            super.onError(p0)
                        }
                    })

            }
        }

    }

    private fun initHttpSDK() {
        HttpSDK.init(SDKCommonParamsProvider())
    }

    private fun initWallpaperSDK(app: Application) {
        //壁纸
        var config = WallpaperSDKConfig.Builder()
            .setDebug(Configure.Debug)
            .setCommonParams(CustomCommonParams())
            .setLogReporter(DefaultLogReport()).build()
        WallPaperSDK.Companion.init(app, config)
    }

    private fun initSpSdk(app: Application) {
        val config: SpSDKConfig = SpSDKConfig.Builder()
            .setMainPageName(MainActivity::class.java.name)
            .setILogReporter { p0, p1, p2, p3, p4, p5 ->
                sdkReport("$p0", "$p1", "$p2", "$p3", "$p4", "$p5")
            }
            .setISpCommonParams(SpCommonParams())
            .setIViewRepoter { jsonObject, iPluViewListener ->
                if (jsonObject == null || iPluViewListener == null) return@setIViewRepoter
                if (!DeviceUtil.isScreenPortrait()) {
                    iPluViewListener.onViewRenderFailed("screen ORIENTATION_LANDSCAPE")
                    return@setIViewRepoter
                }

                Try {
                    val code = jsonObject.optString("code", "")

                    when (code) {
                        "RainSnow_reminder" -> {
                            WeatherRainSnowForecast(app).apply {
                                this.setIPluViewListener(iPluViewListener)
                                requestDate(jsonObject)
                            }
                        }

                        "morning_reminder", "night_reminder" -> {
                            WeatherForecast(app).apply {
                                this.setIPluViewListener(iPluViewListener)
                                requestDate(jsonObject)
                            }
                        }
                        "clean_reminder" -> {
                            Try {
                                when (jsonObject.optString("style", "")) {
                                    "2" -> {
                                        CleanFullAdvPop(app).apply {
                                            iPluViewListener?.onPassVerification()
                                            iPluViewListener?.onViewRenderSuccess(
                                                this,
                                                getAdvLayout(),
                                                getCloseView()
                                            )
                                        }
                                    }
                                    else -> {
                                        CleanView(app).apply {
                                            try {
                                                iPluViewListener?.onPassVerification()
                                                setType(jsonObject.optString("style", ""))
                                                val iconUrl = jsonObject.optString("iconurl", "")
                                                val iconName = jsonObject.optString("iconname", "")
                                                if (iconUrl.isNotEmpty()) {
                                                    Glide.with(iconView).load(iconUrl)
                                                        .into(iconView)
                                                }
                                                if (iconName.isNotEmpty()) {
                                                    iconTitleView.text = iconName
                                                }
                                                iPluViewListener?.onViewRenderSuccess(
                                                    this,
                                                    this.ex_layout,
                                                    this.closeView
                                                )

                                            } catch (e: Exception) {
                                                iPluViewListener?.onViewRenderFailed("$e")
                                            }
                                        }
                                    }
                                }
                            }

                        }

                        else -> {
                            showReport(EnumType.上报埋点.雨雪天气提醒上报, subactid = "code_NullOrMismatching")
                            iPluViewListener.onViewRenderFailed("code NullOrMismatching")
                        }

                    }
                }

            }
            .setDebug(Configure.Debug)
            .setChannel(BuildConfig.SP_CHANNEL)
            .build()

        SpSDK.init(app, config, object : ISpInitListener {
            override fun onSuccess() {
                if (Configure.spBuglyTag.isNotEmpty()) {
                    try {
                        SpSDK.getInstance().setPluginExceptionTag(Configure.spBuglyTag.toInt())
                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onFailed(p0: String?) {
                sdkReport(EnumType.上报埋点.初始化失败, "", "", "", "", XMActivityBean.TYPE_SHOW)
            }

        })

    }


    private fun getXmConfig(app: Application): XMCommonConfig {
        if (xmConfig == null) {
            xmConfig = XMCommonConfig.Builder()
                .setAppTypeId(Configure.appTypeId)
                .setAppQid(WalleChannelReader.getChannel(app, Configure.appQid))
                .setTest(Configure.Debug)
                .build()
        }

        return xmConfig.nN()
    }

    private fun xmLog(app: Application) {
        XMCommonManager.getInstance().preInit(app, getXmConfig(app))
        XMCommonManager.getInstance().init()
        XMLogManager.getInstance().init()

    }

    private fun push() {
        getAppModel().queryTags.postValue(PushBean())
        NotificationsUtils.setNotificationChannel(AppContext.getContext())
        StPushConfig.getInstance().enablePushLog(Configure.Debug)
        STPushManager.getInstance().init(AppContext.getContext())
        STPushManager.getInstance()
            .registerPushIntentService(AppContext.getContext(), PushService::class.java)
    }

}