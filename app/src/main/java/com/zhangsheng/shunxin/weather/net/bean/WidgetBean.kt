package com.zhangsheng.shunxin.weather.net.bean

import com.maiya.thirdlibrary.utils.AppContext
import java.io.Serializable

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2020/4/23 11:29
 */
class WidgetBean:Serializable {
    /**
     * aqi : 51
     * aqiLevel : 良
     * days : [{"fct":"2020-04-23","tcr":"18°/12°","wt":"多云","wtid":"1"},{"fct":"2020-04-24","tcr":"22°/12°","wt":"晴转多云","wtid":"1"},{"fct":"2020-04-25","tcr":"26°/12°","wt":"多云转阴","wtid":"2"}]
     * fallDesc : 未来两小时无降雨
     * fct : 2020-04-23 11:27:42
     * tc : 17
     * wt : 晴
     * wtid : 0
     */
    var aqi: String=""
    var aqiLevel: String=""
    var fallDesc: String=""
    var reqType:String="2"
    var location:Location?=null
    var fct: String=""
    var tc: String=""
    var wt: String=""
    var wtid: String=""
    var tcr:String=""
    var reqTimestamp:Long=0L
    var regioncode:String=""
    var regionname:String=""
    var sunr:String=""
    var suns:String=""
    var refreshTime=0L
    var errorTimes=0
    var days: List<DaysBean>? = null
    var aqiColor:String="#44C690"
    var aqiPic:String="${AppContext.getContext().packageName}:mipmap/icon_leaf_you"
    var wtStatusPic:String="${AppContext.getContext().packageName}:drawable/icon_weather_qing_day_small"
    var wtPic:String="${AppContext.getContext().packageName}:mipmap/icon_weather_qing_day"

    class DaysBean :Serializable{
        /**
         * fct : 2020-04-23
         * tcr : 18°/12°
         * wt : 多云
         * wtid : 1
         */
        var fct: String=""
        var tcr: String=""
        var wt: String=""
        var wtid: String=""

        override fun toString(): String {
            return "DaysBean(fct='$fct', tcr='$tcr', wt='$wt', wtid='$wtid')"
        }


    }

    override fun toString(): String {
        return "WidgetBean(aqi='$aqi', aqiLevel='$aqiLevel', fallDesc='$fallDesc', location=$location, fct='$fct', tc='$tc', wt='$wt', wtid='$wtid', reqTimestamp=$reqTimestamp, regionCode='$regioncode', regionName='$regionname', sunr='$sunr', suns='$suns', refreshTime=$refreshTime, days=$days)"
    }


}