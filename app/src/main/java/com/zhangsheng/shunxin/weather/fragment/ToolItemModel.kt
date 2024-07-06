package com.zhangsheng.shunxin.weather.fragment

class ToolItemModel {

    companion object {
        // 空气实况
        const val TYPE_AIR_LIVE = 0
        // 台风路径
        const val TYPE_TYPHOON = 1
        // 降雨趋势
        const val TYPE_RAIN = 2
        // 空气质量排行
        const val TYPE_AIR_RANK = 3
        // 15日天气
        const val TYPE_FIFDAY_WEATHER = 4
        // 早晚天气提醒
        const val TYPE_WEATHER_NOTICE = 5
    }

    var toolTitle: String? = ""
    var toolIcon: Int = 0
    var showRed: Boolean = false
    var type: Int = -1
}