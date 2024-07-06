package com.zhangsheng.shunxin.weather.net.bean

import java.io.Serializable

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2019/12/11 10:10
 */
class Location:Serializable {
    var state = 0    //1 成功  //2失败
    var address = ""
    var time = 0L
    var province = ""
    var city = ""
    var country = ""
    var district = ""
    var street = ""
    var lat = ""
    var lng = ""
    var errorCode: String? = null
    var errorMessage: String? = null
    var locationType: Int = 0
    override fun toString(): String {
        return "Location(state=$state, address='$address', time=$time, province='$province', city='$city', country='$country', district='$district', street='$street', lat='$lat', lng='$lng', errorCode=$errorCode, errorMessage=$errorMessage)"
    }


}