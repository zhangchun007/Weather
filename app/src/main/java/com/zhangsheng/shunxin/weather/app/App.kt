package com.zhangsheng.shunxin.weather.app

import android.content.Context
import android.content.Intent
import androidx.lifecycle.ViewModelProvider
import com.maiya.thirdlibrary.base.BaseApp
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.utils.AppContext
import com.maiya.thirdlibrary.utils.AppProcessUtil
import com.my.sp.SpSDK
import com.tencent.mmkv.MMKV
import com.xm.xmcommon.business.mdid.XMJLibraryHelper
import com.zhangsheng.shunxin.weather.koin.viewModelModule
import com.zhangsheng.shunxin.weather.model.AppViewModel
import com.zhangsheng.shunxin.weather.utils.CrashInterceptor
import com.zhangsheng.shunxin.weather.utils.ProcessLifecycleObserver
import com.zhangsheng.shunxin.weather.utils.ThirdInitHelper
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.context.startKoin
import java.lang.reflect.Method


/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/7/3 15:43
 */
class App : BaseApp() {

    companion object {
        private lateinit var appModel: AppViewModel

        fun getAppModel(): AppViewModel {
            return appModel
        }
    }

    override fun onCreate() {
        super.onCreate()
        if (AppProcessUtil.isMainProcess(this)) {
            appModel = ViewModelProvider.AndroidViewModelFactory.getInstance(this)
                .create(AppViewModel::class.java)
            appModel.initUrlDomain()
            MMKV.initialize(this)
            registerActivityLifecycleCallbacks(ProcessLifecycleObserver)
            initKoin()
            CrashInterceptor.init(applicationContext)
        }
        ThirdInitHelper.init(this)

    }


    private fun initKoin() {
        startKoin {
            androidLogger()
            androidContext(AppContext.getContext())
            modules(listOf(viewModelModule))
        }
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        XMJLibraryHelper.initEntry(base)
    }


    override fun startActivity(intent: Intent?) {
        var handled = false
        Try {
            val clazz = Class.forName("com.xinmeng.xm.XMMarker")
            val methods: Array<Method> = clazz.declaredMethods
            for (method in methods) {
                val parameterTypes = method.parameterTypes
                if (parameterTypes != null && parameterTypes.size == 1 && parameterTypes[0] == Intent::class.java) {
                    handled = method.invoke(null, intent) as Boolean
                    if (handled) return@Try
                    break
                }
            }
            val packIntent: Intent? = SpSDK.packPlugIntent(intent)
            if (packIntent != null) {
                super.startActivity(packIntent)
                return@Try
            }
        }
        Try {
            if (!handled) super.startActivity(intent)
        }
    }
}
