package com.zhangsheng.shunxin.weather.net.bean

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2020/3/2 17:36
 */
class PushCityBean {
    var isLocation:Boolean=false
    var code=""
    var city=""
    var dayTime=""
    var nightTime=""
    var isChoose=false


    override fun toString(): String {
        return "PushCityBean(isLocation=$isLocation, code='$code', dayTime='$dayTime', nightTime='$nightTime')"
    }


}