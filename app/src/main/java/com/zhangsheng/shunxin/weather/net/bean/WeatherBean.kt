package com.zhangsheng.shunxin.weather.net.bean

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2020/7/24 13:40
 */
class WeatherBean {

    var aqi: String=""
    var aqiLevel: String=""
    var falls: Fall? = null
    var homeDetail: HomeDetailBean? = null
    var pressure: String? = null
    var regioncode:String=""
    var regionname: String=""
    var typhoon:String="-1"    //只有1展示
    var tcmax: Int=0
    var tcmin: Int = 0
    var location: Location?=null
    var latitude:String=""
    var longitude:String=""
    var lastRequestStatus=false
    var isLocation:Boolean=false
    var refreshTime:Long=0
    var rh: String? = null
    var tc: String=""
    var uvlv: String=""
    var wdir: String? = null
    var ws: String? = null
    var wt: String=""
    var wtid: String="-1"
    var cctv: CctvBean? = null
    var lifes: List<LifesBean>? = null
    var prediction: List<PredictionBean>? = null
    var warns: List<Warns>? = null
    var wtablesNew: List<WtablesNewBean>? = null
    var ybds: List<YbdsBean>? = null
    var ybhs: List<YbhsBean>? = null
    var time:String=""

    class Fall {
        var desc:String=""
        var isFall="-1"   //0 下雨
        var rss:List<Float>?=null
        var rssPre="0"   //0 降雨 1降雪

    }

    class Warns{
        var desc:String=""
        var level:String=""
        var time:String=""
        var prevention:String=""
        var type:String=""

        override fun toString(): String {
            return "Warns(desc='$desc', level='$level', time='$time', prevention='$prevention', type='$type')"
        }


    }

    class HomeDetailBean {

        var aqi: String? = null
        var aqiLevel: String? = null
        var fct: String? = null
        var regionname: String? = null
        var stc:String=""
        var rh:String=""
        var tc: String=""
        var wt: String=""
        var wtid: String=""
        var zs: String? = null
        var descs: List<DescsBean>? = null

        class DescsBean {

            var desc: String? = null
            var name: String=""

        }
    }

    class CctvBean {

        var cover: String=""
        var title: String=""
        var ptime: String=""
        var video_url: String=""

    }

    class LifesBean {

        var desc: String=""
        var lv: String=""
        var name: String=""
        var type:String=""
        var url:String=""
        var img:String=""

    }

    class PredictionBean {

        var aqi: String? = null
        var aqiLevel: String? = null
        var fct: String? = null
        var regionname: String? = null
        var sunr: String? = null
        var suns: String? = null
        var tcr: String? = null
        var wdir: String? = null
        var ws: String? = null
        var wt: String? = null
        var wtid: String=""
        var descs: List<DescsBeanX>? = null
        var lifes: List<LifesBeanX>? = null

        class DescsBeanX {

            var desc: String? = null
            var name: String=""

        }

        class LifesBeanX {

            var desc: String? = null
            var lv: String? = null
            var name: String? = null

        }
    }

    class WtablesNewBean {

        var aqi: String=""
        var aqiLevel: String=""
        var fct: String=""
        var tcr: String=""
        var wt: String=""
        var wtid: String=""

    }

    class YbdsBean {

        var aqi: String=""
        var aqiLevel: String=""
        var fct: String=""
        var tcd: Int=0
        var tcn: Int=0
        var wdir: String=""
        var ws: String=""
        var wtd: String=""
        var wtdid: String=""
        var wtq:String=""
        var wtn: String=""
        var wtnid: String=""
        var wtqid:String=""

    }

    class YbhsBean {

        var sunrise: String=""
        var sunset: String=""
        var weatherDetail: WeatherDetailBean? = null

        class WeatherDetailBean {

            var aqi: String=""
            var aqiLevel: String=""
            var fct: String=""
            var tc: String=""
            var wdir: String=""
            var ws: String=""
            var wt: String=""
            var wtid: String=""
            var descs: List<DescsBean>? = null

            class DescsBean {

                var desc: String=""
                var name: String=""

            }

        }
    }

    override fun toString(): String {
        return "WeatherBean(aqi=$aqi, aqiLevel=$aqiLevel, falls=$falls, homeDetail=$homeDetail, pressure=$pressure, regioncode='$regioncode', regionname='$regionname', isLocation=$isLocation, refreshTime=$refreshTime, rh=$rh, tc=$tc, wdir=$wdir, ws=$ws, wt=$wt, wtid='$wtid', cctv=$cctv, lifes=$lifes, prediction=$prediction, warns=$warns, wtablesNew=$wtablesNew, ybds=$ybds, ybhs=$ybhs)"
    }


}