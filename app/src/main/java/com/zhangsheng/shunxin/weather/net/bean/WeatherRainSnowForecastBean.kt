package com.zhangsheng.shunxin.weather.net.bean


class WeatherRainSnowForecastBean {
    var aqi: String = ""
    var aqiLevel: String = ""
    var fall: Fall? = null
    var rh: String = ""
    var tc: String = ""
    var wdir: String = ""
    var ws: String = ""
    var wt: String = ""
    var ftc:String=""
    var wtid: String = ""

    class Fall {
      var desc: String = ""
      var rss: List<Float>? = null
      var rssPre: String = ""
    }
}