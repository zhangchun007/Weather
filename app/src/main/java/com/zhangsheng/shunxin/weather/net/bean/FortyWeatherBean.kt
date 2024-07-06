package com.zhangsheng.shunxin.weather.net.bean

/**
 * @Description:
 * @Author:         zhangchun
 * @CreateDate:     2021/7/22
 * @Version:        1.0
 */
class FortyWeatherBean {
    var fallTrend = "" //降雨趋势描述“0 天降水”
    var tcFullDesc = "" //温度趋势-详细描述
    var tcDesc = "" //温度趋势-简介
    var ybds: ArrayList<YbdsBean>? = null //40日预报数据
    var falls: ArrayList<FallsBean>? = null //40日降雨数据
    var tcTrend: TcTreadBean? = null //温度趋势


    class YbdsBean {
        var fct = "" //时间 格式："2021-04-15"
        var tcd = 0 //最高温度
        var tcn = 0 //最低温度
        var wdir = "" //西风
        var wslv = "" //风速等级
        var wt = "" //全天天气现象
        var wtid = "" //全天天气id
        var wt1 = "" //白天天气现象
        var wt1id = "" //白天天气现象id
        var wt2 = "" //	晚上天气现象
        var wt2id = "" //晚上天气现象id
    }

    class FallsBean {
        var fct = "" //时间 格式："2021-04-15"
        var rss = "" //是否降雨 0-不降雨、1-降雨
        var wt = "" //当天天气现象
    }

    class TcTreadBean {
        var up = "" //温度上升天数
        var down = "" //	温度下降天数
        var avgTc = "" //	平均最高温度
    }

}


