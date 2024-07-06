package com.zhangsheng.shunxin.weather.net.bean

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2021/4/19 16:34
 */
class TyphoonBean {
    var desc:String=""
    var forecast:List<TyphoonDetail>?=null
    var landInfo:LandInfo?=null
    var location:TyphoonLocation?=null
    var pastPath:List<TyphoonDetail>?=null
    var name:String=""
    var power:String=""
    var strong:String=""

    class LandInfo{
        var landAddress:String=""
        var landLocation:TyphoonLocation?=null
        var landTime:String=""
    }

    class TyphoonLocation{
        var lat:String=""
        var lng:String=""
    }

    class TyphoonDetail{
        var lat:String=""
        var lng:String=""
        var name:String=""
        var power:String=""
        var pressuer:String=""
        var speed:String=""
        var speedDirection:String=""
        var strong:String=""
        var time:String=""
    }
}