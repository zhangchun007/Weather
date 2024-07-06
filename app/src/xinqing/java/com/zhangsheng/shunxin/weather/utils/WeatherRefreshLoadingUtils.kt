package com.zhangsheng.shunxin.weather.utils

class WeatherRefreshLoadingUtils {
    companion object {
        var lastShowLoadingWeather: Long = 0L
        var needShowLoading = false
        fun dropFastRefresh(showLoadingWeather: () -> Unit) {
            val duration = System.currentTimeMillis() - lastShowLoadingWeather
            if (duration > 1500 && needShowLoading) {
                lastShowLoadingWeather = System.currentTimeMillis()
                showLoadingWeather()
                needShowLoading = false
            }
        }
    }

}