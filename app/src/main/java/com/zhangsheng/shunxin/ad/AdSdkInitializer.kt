package com.zhangsheng.shunxin.ad

import android.app.Application
import com.maiya.adlibrary.ad.bd.BDAdSdkInitHelper
import com.maiya.adlibrary.ad.csj.CSJAdManagerHolder
import com.maiya.adlibrary.ad.csj.CSJManagerProvider
import com.maiya.adlibrary.ad.image.ClientImageLoader
import com.maiya.adlibrary.ad.juhe.JuHeAdSDKInitHelper
import com.maiya.adlibrary.ad.ks.KSAdSDKInitHelper
import com.maiya.adlibrary.ad.providers.ClientXMVideoViewSupplier
import com.qq.e.comm.managers.GDTADManager
import com.xinmeng.shadow.base.ICustomParams
import com.xinmeng.shadow.base.IHostLinks
import com.xinmeng.shadow.base.IHttpStack
import com.xinmeng.shadow.base.IImageLoader
import com.xinmeng.shadow.branch.source.bd.BDInjector
import com.xinmeng.shadow.branch.source.csj.CSJInjector
import com.xinmeng.shadow.branch.source.csj.ICSJManagerProvider
import com.xinmeng.shadow.branch.source.gdt.GDTInjector
import com.xinmeng.shadow.branch.source.juhe.JuHeInjector
import com.xinmeng.shadow.branch.source.ks.KSInjector
import com.xinmeng.shadow.branch.source.xm.XMInjector
import com.xinmeng.shadow.mediation.MediationConfig
import com.xinmeng.shadow.mediation.MediationManager
import com.xinmeng.shadow.mediation.api.IDefaultSlotConfigProvider
import com.xinmeng.xm.XMConfig
import com.zhangsheng.shunxin.ad.configs.ClientDefaultConfigProvider
import com.zhangsheng.shunxin.ad.configs.ClientHostLinks
import com.zhangsheng.shunxin.ad.configs.ClientOk3Stack
import com.zhangsheng.shunxin.ad.params.ClientCustomParams
import com.zhangsheng.shunxin.weather.common.Configure

object AdSdkInitializer {

    fun init(app: Application?) {
        //三方sdk初始化 start
        GDTADManager.getInstance().initWith(
            app,
            Configure.gdtAppId
        )
        CSJAdManagerHolder.init(
            app,
            Configure.csjAppId,
            Configure.appName
        )
        KSAdSDKInitHelper.initSDK(
            app,
            Configure.appName,
            Configure.ksAppId
        )

        BDAdSdkInitHelper.init(app, Configure.bdAppId)
        JuHeAdSDKInitHelper.initSDK(app, Configure.appName)
//        FSAdSdkInitHelper.init(app)
        //三方sdk初始化  end

        //初始化广告中台 start
        val customParams: ICustomParams =
            ClientCustomParams()
        val httpStack: IHttpStack =
            ClientOk3Stack()
        val clientImageLoader: IImageLoader =
            ClientImageLoader()
        val configProvider: IDefaultSlotConfigProvider =
            ClientDefaultConfigProvider()
        val hostLinks: IHostLinks =
            ClientHostLinks()
        val config = MediationConfig.Builder()
            .setApplication(app)
            .setImageLoader(clientImageLoader)
            .setCustomParams(customParams)
            .setDefaultConfigProvider(configProvider)
            .setHttpStack(httpStack)
            .setHostLinks(hostLinks)
            .setTestServer(Configure.Debug)
            .build()
        MediationManager.init(config)
        //初始化广告中台 end

        //添加广告源 start
        val mm = MediationManager.getInstance()
        //广点通
        GDTInjector.inject(mm)
        //穿山甲
        val provider: ICSJManagerProvider =
            CSJManagerProvider()
        CSJInjector.inject(mm, provider)
        //快手
        KSInjector.inject(mm)


        //聚合
        JuHeInjector.inject(mm)

        //百度
        BDInjector.inject(mm)

        //风行
//        FSInjector.inject(mm)

        //新萌  请使用demo中的ijk版本，demo中的ijk支持了https
        val videoviewSupplier =
            ClientXMVideoViewSupplier()
        val xmConfig = XMConfig.Builder()
            .setXMVideoViewSupplier(videoviewSupplier)
            .build()
        XMInjector.inject(mm, xmConfig)
        //添加广告源 end

    }
}