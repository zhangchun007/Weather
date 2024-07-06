package com.zhangsheng.shunxin.weather.net.bean

import com.amap.api.maps.model.Marker



data class CustomMarker(var index:Int=0,  var marker: Marker?=null,var type:Int=0)

//type :0 正常  1 typhoon