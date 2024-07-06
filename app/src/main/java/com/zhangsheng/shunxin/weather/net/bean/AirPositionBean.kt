package com.zhangsheng.shunxin.weather.net.bean

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2020/3/16 21:49
 */
class AirPositionBean {
    var refreshTime:Long=0
    var data:List<Postion>?=null
    /**
     * aqi : 59
     * aqiLevel : 良
     * lat : 39.942500000000002
     * lng : 116.361000000000004
     * name : 官园
     */
    class Postion{
        var aqi: String = ""
        var aqiLevel: String = ""
        var lat: Double = 0.0
        var lng: Double = 0.0
        var name: String = ""
    }

}