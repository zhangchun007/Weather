package com.zhangsheng.shunxin.weather.net.bean

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2019/12/17 21:45
 */
class AirBean {
    var refreshTime:Long=0L
    var aqi: String = ""
    var aqiLevel: String = ""
    var aqiDesc:String=""
    var co: String = ""
    var coLevel: String = ""
    var no2: String = ""
    var no2Level: String = ""
    var o3: String = ""
    var o3Level: String = ""
    var pm10: String = ""
    var pm10Level: String = ""
    var pm25: String = ""
    var pm25Level: String = ""
    var so2: String = ""
    var so2Level: String = ""
    var time: String = ""
    var aqd: List<AqdBean>? = null

    class AqdBean {

        var aqi: String =""
        var aqiLevel: String = ""
        var time: String = ""

    }

}