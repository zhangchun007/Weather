package com.zhangsheng.shunxin.weather.common

import com.maiya.thirdlibrary.utils.AppContext
import com.maiya.thirdlibrary.utils.AppUtils
import com.meituan.android.walle.WalleChannelReader

object CommonUrlConstant {

    private const val URL_USER_NOTIFY = "http://rule.songmeng888.com/rule.html";
    var URL_USER_NOTIFY_PARAM = "?v=${System.currentTimeMillis()}&pkgName=${AppUtils.appPackageName}&channel=${ WalleChannelReader.getChannel(AppContext.getContext())}"
    var URL_USER_POLICY = "$URL_USER_NOTIFY$URL_USER_NOTIFY_PARAM&notify=policy"
    var URL_USER_AGREE = "$URL_USER_NOTIFY$URL_USER_NOTIFY_PARAM&notify=agree"
}