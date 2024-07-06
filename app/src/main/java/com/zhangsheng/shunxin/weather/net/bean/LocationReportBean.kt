package com.zhangsheng.shunxin.weather.net.bean

import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.utils.DataUtil
import com.xm.xmcommon.XMParam

class LocationReportBean {

    val appver: String? = XMParam.getAppVer()
    val device: String? = XMParam.getDevice()
    val device_type: String = if (getAppModel().isActiveUser()) "2" else "1"
    var errorCode: String? = "null"
    var errorMessage: String? = "null"
    var feedbackCity: String? = "null"
    var imei: String? = XMParam.getIme()
    var lat: String? = "null"
    var lng: String? = "null"
    var locationType: String? = "null"
    val network: String? = XMParam.getNetwork()
    val os: String = "Android"
    val osversion: String? = XMParam.getOsVersion()
    var reportType: String? = "null"
    var timelog: String? = "null"
    val time: String = DataUtil.timeStamp2Date(System.currentTimeMillis())

    var province: String? = "null"
    var city: String? = "null"
    var district: String? = "null"
    var address: String? = "null"
    var street: String? = "null"
    var country: String? = "null"

    var Livetemperature: String? = "null"//实况温度
    var Liveweather: String? = "null"//实况天气
    var Todaytemperature: String? = "null"//今日温度
    var Todayweather: String? = "null"//今日天气
}