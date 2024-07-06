package com.maiya.thirdlibrary.base

import android.content.Context
import androidx.multidex.MultiDex
import androidx.multidex.MultiDexApplication
import com.maiya.thirdlibrary.utils.AppContext
import com.maiya.thirdlibrary.utils.WebViewFixUtil

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2021/3/23 16:20
 */
open class BaseApp : MultiDexApplication() {


    override fun onCreate() {
        super.onCreate()
        AppContext.context = this
    }

    override fun attachBaseContext(base: Context?) {
        super.attachBaseContext(base)
        WebViewFixUtil.fixWebviewBug(base)
        MultiDex.install(base)
    }

}