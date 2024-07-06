package com.zhangsheng.shunxin.weather.wallpaper

import com.maiya.wallpaper.http.IWallpaperCommonParams
import com.zhangsheng.shunxin.weather.common.Configure
import com.zhangsheng.shunxin.weather.common.UrlConstant

/**
 * @Description:
 * @Author:         zhangchun
 * @CreateDate:     2021/6/7
 * @Version:        1.0
 */
class CustomCommonParams : IWallpaperCommonParams {
    override fun getConfigUrl(): String? {
        return (if (Configure.Debug) UrlConstant.DEBUG_APP_CONTROL else UrlConstant.APP_CONTROL) + "/app-control/polling.config"
    }
}