package com.zhangsheng.shunxin.weather.utils


import android.graphics.Color
import com.airbnb.lottie.LottieAnimationView
import com.amap.api.maps.model.LatLng
import com.maiya.thirdlibrary.ext.isVisible
import com.maiya.thirdlibrary.ext.listIndex
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.ext.parseDouble
import com.maiya.thirdlibrary.utils.CacheUtil
import com.maiya.thirdlibrary.widget.shapview.ShapeView
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.LocationEllipsis
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.net.bean.cityBean

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2019/12/15 13:03
 */
object WeatherUtils : BaseWeatherUtils() {


    fun weatherHasCache(): Boolean {
        return CacheUtil.getList(Constant.SP_WEATHER_DATA, WeatherBean::class.java).isNotEmpty()
    }

    fun airIndexValueColor(code: String): String {
        var color = when (code) {
            "优" -> "#05C085"
            "良" -> "#FFAE13"
            "轻度" -> "#F08E4A"
            "中度" -> "#E86E6E"
            "重度" -> "#C9619A"
            "严重" -> "#853A85"
            else -> "#05C085"
        }
        return color
    }

    fun hightAlertColor(level: String): Int {
        return Color.parseColor(
            when (level) {
                "蓝色" -> "#3C92F5"
                "黄色" -> "#FFC429"
                "橙色" -> "#FF803B"
                "红色" -> "#FF4B4B"
                else -> "#3C92F5"
            }
        )
    }

    fun hightAlertColorStr(level: String): String {
        return when (level) {
            "蓝色" -> "#3C92F5"
            "黄色" -> "#FFC429"
            "橙色" -> "#FF803B"
            "红色" -> "#FF4B4B"
            else -> "#3C92F5"
        }
    }

    fun airFaceImg(code: String): Int {
        var imgId = when (code) {
            "优" -> R.mipmap.icon_air_face_you
            "良" -> R.mipmap.icon_air_face_liang
            "轻度" -> R.mipmap.icon_air_face_qingdu
            "中度" -> R.mipmap.icon_air_face_medium
            "重度" -> R.mipmap.icon_air_face_sever
            "严重" -> R.mipmap.icon_air_face_yanzhong
            else -> R.mipmap.icon_air_face_you
        }
        return imgId
    }

    fun airTitleColor(code: String): Int {
        var titleColor = when (code) {
            "优" -> R.drawable.air_bg_gradient_green1
            "良" -> R.drawable.air_bg_gradient_liang1
            "轻度" -> R.drawable.air_bg_gradient_qingdu1
            "中度" -> R.drawable.air_bg_gradient_mediun1
            "重度" -> R.drawable.air_bg_gradient_severe1
            "严重" -> R.drawable.air_bg_gradient_yanzhong1
            else -> R.drawable.air_bg_gradient_green1
        }
        return titleColor
    }

    fun airBgColor(code: String): Int {
        var airBgColor = when (code) {
            "优" -> R.drawable.air_bg_gradient_green
            "良" -> R.drawable.air_bg_gradient_liang
            "轻度" -> R.drawable.air_bg_gradient_qingdu
            "中度" -> R.drawable.air_bg_gradient_mediun
            "重度" -> R.drawable.air_bg_gradient_severe
            "严重" -> R.drawable.air_bg_gradient_yanzhong
            else -> R.drawable.air_bg_gradient_green
        }
        return airBgColor
    }

    fun airBgColorFuture(code: String): Int {
        return when (code) {
            "优" -> R.drawable.public_shape_corner_10dp_air_you
            "良" -> R.drawable.public_shape_corner_10dp_air_liang
            "轻度" -> R.drawable.public_shape_corner_10dp_air_qingdu
            "中度" -> R.drawable.public_shape_corner_10dp_air_medium
            "重度" -> R.drawable.public_shape_corner_10dp_air_sever
            "严重" -> R.drawable.public_shape_corner_10dp_air_yanzhong
            else -> R.drawable.public_shape_corner_10dp_air_you
        }
    }

    fun getAnimRainLottie(type: String, level: Float): String {
        var isSnow = type == "1"
        return when {
            level < 11.33f -> if (!isSnow) "rain_large.json" else "snow_large.json"

            level >= 11.33f -> if (!isSnow) "rain_small.json" else "snow_small.json"

            else -> if (!isSnow) "rain_small.json" else "snow_small.json"
        }

    }

    fun getWeatherIcon(name: String): Int {
        return when (name) {
            "湿度" -> R.mipmap.icon_weather_shidu
            "紫外线" -> R.mipmap.icon_weather_ziwaixian
            "能见度" -> R.mipmap.icon_weather_nengjiandu
            "风向风力" -> R.mipmap.icon_weather_fengxiang
            "空气质量" -> R.mipmap.icon_weather_kongqizhiliang
            "日出日落" -> R.mipmap.icon_weather_riluorichu
            "气压" -> R.mipmap.icon_weather_qiya
            else -> R.mipmap.icon_weather_shidu
        }
    }

    fun hightAlertBg(name: String): Int {
        return when (name) {
            "蓝色" -> R.drawable.waringbgbule
            "红色" -> R.drawable.waringbgred
            "黄色" -> R.drawable.waringbgyellow
            "橙色" -> R.drawable.waringbgcheng
            else -> R.drawable.waringbgbule
        }
    }


    fun hightAlertColors(level: String): Array<String> {
        return when (level) {
            "蓝色" -> arrayOf("#347EFF", "#0450E9")
            "黄色" -> arrayOf("#FEE047", "#FFB82A")
            "橙色" -> arrayOf("#F9A11F", "#FF6E00")
            "红色" -> arrayOf("#FF4E4E", "#EB190B")
            else -> arrayOf("#347EFF", "#0450E9")
        }
    }

    fun hightAlertIcon(name: String): Int {
        return when (name) {
            "森林火灾" -> R.mipmap.icon_warns_cenglinhuozai
            "冰雹" -> R.mipmap.icon_warns_bingbao
            "雷电" -> R.mipmap.icon_warns_leidian
            "台风" -> R.mipmap.icon_warns_taifeng
            "暴雨" -> R.mipmap.icon_warns_baoyu
            "暴雪" -> R.mipmap.icon_warns_baoxue
            "高温" -> R.mipmap.icon_warns_gaowen
            "寒潮" -> R.mipmap.icon_warns_hanchao
            "沙尘暴" -> R.mipmap.icon_warns_shachenbao
            "大风" -> R.mipmap.icon_warns_dafeng
            "干旱" -> R.mipmap.icon_warns_ganhan
            "霜冻" -> R.mipmap.icon_warns_shuangdong
            "大雾" -> R.mipmap.icon_warns_dawu
            "霾" -> R.mipmap.icon_warns_mai
            "道路结冰" -> R.mipmap.icon_warns_daolujiebing
            "雷雨大风" -> R.mipmap.icon_warns_leiyudafeng
            else -> R.mipmap.icon_warns_mai
        }
    }

    fun getWeatherAnimBg(code: String): String {
        var res = "anim_null_bg.json"
        when (code) {
            EnumType.天气气象.晴天 -> res = if (isNight) "anim_sun_night.json" else "anim_sun_day.json"
            EnumType.天气气象.阴天 -> res = if (isNight) "anim_yin_night.json" else "anim_yin_day.json"
            EnumType.天气气象.小雨 -> res =
                if (isNight) "anim_rain_night_s.json" else "anim_rain_day_s.json"
            EnumType.天气气象.中雨 -> res =
                if (isNight) "anim_rain_night_m.json" else "anim_rain_day_m.json"
            EnumType.天气气象.大雨,
            EnumType.天气气象.暴雨,
            EnumType.天气气象.雷阵雨,
            EnumType.天气气象.冰雹,
            EnumType.天气气象.雨夹雪 -> res =
                if (isNight) "anim_rain_night_b.json" else "anim_rain_day_b.json"
            EnumType.天气气象.多云 -> res =
                if (isNight) "anim_duoyun_night.json" else "anim_duoyun_day.json"
            EnumType.天气气象.雾,
            EnumType.天气气象.雾霾,
            EnumType.天气气象.中度雾霾,
            EnumType.天气气象.重度雾霾 -> res = "anim_wumai.json"
            EnumType.天气气象.小雪 -> res = "anim_snow_s.json"
            EnumType.天气气象.中雪 -> res = "anim_snow_m.json"
            EnumType.天气气象.大雪,
            EnumType.天气气象.暴雪 -> res = "anim_snow_b.json"
            EnumType.天气气象.大风 -> res = "anim_wind.json"
            EnumType.天气气象.沙尘暴,
            EnumType.天气气象.浮尘 -> res = "anim_shachen.json"
        }
        return res
    }

    fun getWeatherPagAnimBg(code: String): String {
        var res = "weizhi.pag"
        when (code) {
            EnumType.天气气象.晴天 -> res = if (isNight) "晴天晚上.pag" else "晴天.pag"
            EnumType.天气气象.阴天 -> res = if (isNight) "阴天晚上.pag" else "阴天.pag"
            EnumType.天气气象.小雨 -> res =
                if (isNight) "小雨晚上.pag" else "小雨.pag"
            EnumType.天气气象.中雨 -> res =
                if (isNight) "中雨晚上.pag" else "中雨.pag"
            EnumType.天气气象.大雨,
            EnumType.天气气象.暴雨,
            EnumType.天气气象.雷阵雨,
            EnumType.天气气象.冰雹,
            EnumType.天气气象.雨夹雪 -> res =
                if (isNight) "大雨晚上.pag" else "大雨.pag"
            EnumType.天气气象.多云 -> res =
                if (isNight) "多云晚上.pag" else "多云.pag"
            EnumType.天气气象.雾 -> res = "大雾.pag"
            EnumType.天气气象.雾霾,
            EnumType.天气气象.沙尘暴,
            EnumType.天气气象.浮尘,
            EnumType.天气气象.中度雾霾,
            EnumType.天气气象.重度雾霾 -> res = "雾霾.pag"
            EnumType.天气气象.小雪 -> res = "小雪.pag"
            EnumType.天气气象.中雪 -> res = "中雪.pag"
            EnumType.天气气象.大雪,
            EnumType.天气气象.暴雪 -> res = "大雪.pag"
            EnumType.天气气象.大风 -> res = "大风.pag"
        }
        return res
    }

    fun getWeatherBg(code: String): Int {
        var res = R.mipmap.bg_weather_sun_day
        when (code) {
            EnumType.天气气象.晴天 -> res =
                if (isNight) R.mipmap.bg_weather_sun_night else R.mipmap.bg_weather_sun_day
            EnumType.天气气象.阴天 -> res =
                if (isNight) R.mipmap.bg_weather_yin_night else R.mipmap.bg_weather_yin_day
            EnumType.天气气象.小雨,
            EnumType.天气气象.中雨,
            EnumType.天气气象.大雨,
            EnumType.天气气象.暴雨,
            EnumType.天气气象.雷阵雨,
            EnumType.天气气象.冰雹,
            EnumType.天气气象.雨夹雪 -> res =
                if (isNight) R.mipmap.bg_weather_rain_day else R.mipmap.bg_weather_rain_night
            EnumType.天气气象.多云 -> res =
                if (isNight) R.mipmap.bg_weather_duoyun_night else R.mipmap.bg_weather_duoyun_day
            EnumType.天气气象.雾 -> res = R.mipmap.bg_weather_wumai
            EnumType.天气气象.小雪,
            EnumType.天气气象.中雪,
            EnumType.天气气象.大雪,
            EnumType.天气气象.暴雪 -> res = R.mipmap.bg_weather_snow
            EnumType.天气气象.大风 -> res = R.mipmap.bg_weather_wind
            EnumType.天气气象.雾霾,
            EnumType.天气气象.中度雾霾,
            EnumType.天气气象.重度雾霾,
            EnumType.天气气象.沙尘暴,
            EnumType.天气气象.浮尘 -> res = R.mipmap.bg_weather_shachen
        }
        return res
    }


    fun getPagWeatherBg(code: String): Int {
        var res = R.mipmap.bg_xinqing_weath
        when (code) {
            EnumType.天气气象.晴天 -> res =
                if (isNight) R.mipmap.bg_xinqing_weather_sun_night else R.mipmap.bg_xinqing_weather_sun_day
            EnumType.天气气象.阴天 -> res =
                if (isNight) R.mipmap.bg_xinqing_weather_yin_night else R.mipmap.bg_xinqing_weather_yin_night
            EnumType.天气气象.小雨,
            EnumType.天气气象.中雨,
            EnumType.天气气象.大雨,
            EnumType.天气气象.暴雨,
            EnumType.天气气象.雷阵雨,
            EnumType.天气气象.冰雹,
            EnumType.天气气象.雨夹雪 -> res =
                if (isNight) R.mipmap.bg_xinqing_weather_rain_night else R.mipmap.bg_xinqing_weather_rain_day
            EnumType.天气气象.多云 -> res =
                if (isNight) R.mipmap.bg_xinqing_weather_duoyun_night else R.mipmap.bg_xinqing_weather_duoyun_day
            EnumType.天气气象.雾 -> res = R.mipmap.bg_xinqing_weather_wu
            EnumType.天气气象.小雪,
            EnumType.天气气象.中雪,
            EnumType.天气气象.大雪,
            EnumType.天气气象.暴雪 -> res = R.mipmap.bg_xinqing_weather_snow
            EnumType.天气气象.大风 -> res = R.mipmap.bg_xinqing_weather_wu
            EnumType.天气气象.雾霾,
            EnumType.天气气象.中度雾霾,
            EnumType.天气气象.重度雾霾,
            EnumType.天气气象.沙尘暴,
            EnumType.天气气象.浮尘 -> res = R.mipmap.bg_xinqing_weather_wumai
        }
        return res
    }

    /**
     * bg_xinqing_weather_rain_night
     * bg_xinqing_weather_sun_night
     * bg_xinqing_weather_duoyun_night
     * 这三张背景图片需要更改颜色
     */
    fun isNeedChangeColor(code: String): Boolean {
        if (code != null && isNight && (code == "0" || code == "7" || code == "8" || code == "9" || code == "10" || code == "4" || code == "5" || code == "6" || code == "1" || code == "2")) {
            return true
        }
        return false
    }

    fun getWeatherBottomBg(code: String): Int {
        var res = R.mipmap.bg_weather_sun_day_bottom
        when (code) {
            EnumType.天气气象.晴天 -> res =
                if (isNight) R.mipmap.bg_weather_sun_night_bottom else R.mipmap.bg_weather_sun_day_bottom
            EnumType.天气气象.阴天 -> res =
                if (isNight) R.mipmap.bg_weather_yin_night_bottom else R.mipmap.bg_weather_yin_day_bottom
            EnumType.天气气象.小雨,
            EnumType.天气气象.中雨,
            EnumType.天气气象.大雨,
            EnumType.天气气象.暴雨,
            EnumType.天气气象.雷阵雨,
            EnumType.天气气象.冰雹,
            EnumType.天气气象.雨夹雪 -> res =
                if (isNight) R.mipmap.bg_weather_rain_night_bottom else R.mipmap.bg_weather_rain_day_bottom
            EnumType.天气气象.多云 -> res =
                if (isNight) R.mipmap.bg_weather_duoyun_night_bottom else R.mipmap.bg_weather_duoyun_day_bottom
            EnumType.天气气象.雾 -> res = R.mipmap.bg_weather_wumai_bottom
            EnumType.天气气象.小雪,
            EnumType.天气气象.中雪,
            EnumType.天气气象.大雪,
            EnumType.天气气象.暴雪 -> res = R.mipmap.bg_weather_snow_bottom
            EnumType.天气气象.大风 -> res = R.mipmap.bg_weather_wind_bottom
            EnumType.天气气象.雾霾,
            EnumType.天气气象.中度雾霾,
            EnumType.天气气象.重度雾霾,
            EnumType.天气气象.沙尘暴,
            EnumType.天气气象.浮尘 -> res = R.mipmap.bg_weather_shachen_bottom
        }
        return res
    }

    fun setCloudAnimJson(code: String, view1: LottieAnimationView?, view2: LottieAnimationView?) {
        when (code) {
            EnumType.天气气象.晴天 -> {
                view1?.isVisible(true)
                view2?.isVisible(false)
                if (isNight) {
                    view1?.setAnimation("晴夜.json")
                } else {
                    view1?.setAnimation("晴白.json")
                }
            }
            EnumType.天气气象.阴天 -> {
                view1?.isVisible(true)
                view2?.isVisible(true)
                if (isNight) {
                    view1?.setAnimation("阴天夜前.json")
                    view2?.setAnimation("阴天夜后.json")
                } else {
                    view1?.setAnimation("阴天白前.json")
                    view2?.setAnimation("阴天白后.json")
                }
            }
            EnumType.天气气象.小雨,
            EnumType.天气气象.中雨,
            EnumType.天气气象.大雨,
            EnumType.天气气象.暴雨,
            EnumType.天气气象.雷阵雨,
            EnumType.天气气象.冰雹,
            EnumType.天气气象.雨夹雪 -> {
                view1?.isVisible(true)
                view2?.isVisible(true)
                if (isNight) {
                    view1?.setAnimation("雨夜前.json")
                    view2?.setAnimation("雨夜后.json")
                } else {
                    view1?.setAnimation("雨白前.json")
                    view2?.setAnimation("雨白后.json")
                }
            }

            EnumType.天气气象.多云 -> {
                view1?.isVisible(true)
                view2?.isVisible(true)
                if (isNight) {
                    view1?.setAnimation("多云夜前.json")
                    view2?.setAnimation("多云夜后.json")
                } else {
                    view1?.setAnimation("多云白前.json")
                    view2?.setAnimation("多云白后.json")
                }
            }
            EnumType.天气气象.雾 -> {
                view1?.isVisible(true)
                view2?.isVisible(false)
                view1?.setAnimation("大雾.json")
            }
            EnumType.天气气象.小雪,
            EnumType.天气气象.中雪,
            EnumType.天气气象.大雪,
            EnumType.天气气象.暴雪 -> {
                view2?.isVisible(false)
                view1?.isVisible(false)
            }
            EnumType.天气气象.大风 -> {
                view1?.isVisible(true)
                view2?.isVisible(true)
                view1?.setAnimation("大风前.json")
                view2?.setAnimation("大风后.json")
            }
            EnumType.天气气象.雾霾,
            EnumType.天气气象.中度雾霾,
            EnumType.天气气象.重度雾霾,
            EnumType.天气气象.沙尘暴,
            EnumType.天气气象.浮尘 -> {
                view1?.isVisible(true)
                view2?.isVisible(false)
                view1?.setAnimation("沙尘.json")
            }
        }
    }

    fun setFullAnimJson(code: String, view1: LottieAnimationView?) {
        when (code) {
            EnumType.天气气象.小雨 -> {
                view1?.isVisible(true)
                view1?.setAnimation("小雨.json")
            }
            EnumType.天气气象.中雨 -> {
                view1?.isVisible(true)
                view1?.setAnimation("中雨.json")
            }
            EnumType.天气气象.大雨,
            EnumType.天气气象.暴雨,
            EnumType.天气气象.雷阵雨,
            EnumType.天气气象.冰雹 -> {
                view1?.isVisible(true)
                view1?.setAnimation("大雨+暴雨雷阵雨冰雹.json")
            }
            EnumType.天气气象.小雪 -> {
                view1?.isVisible(true)
                view1?.setAnimation("小雪.json")
            }
            EnumType.天气气象.中雪 -> {
                view1?.isVisible(true)
                view1?.setAnimation("中雪.json")
            }
            EnumType.天气气象.大雪,
            EnumType.天气气象.雨夹雪,
            EnumType.天气气象.暴雪 -> {
                view1?.isVisible(true)
                view1?.setAnimation("大雪+暴雪+雨夹雪.json")
            }
            else -> {
                view1?.isVisible(false)
            }
        }
    }

    fun WeatherBigIcon(code: String): Int {
        var id = 0
        when (code) {
            "0" -> id = if (isNight) {
                R.mipmap.icon_qin_night
            } else {
                R.mipmap.icon_qin_day
            }
            "1" -> id = if (isNight) {
                R.mipmap.icon_duoyu_night
            } else {
                R.mipmap.icon_duoyun_day
            }
            "2" -> id = R.mipmap.icon_yin
            "18" -> id = R.mipmap.icon_dawu
            "4", "7", "8", "9", "10" -> id = R.mipmap.icon_yu
            "5", "6", "14", "15", "16", "17" -> id = R.mipmap.icon_xue
            "53", "54", "55", "29", "30" -> id = R.mipmap.icon_shashen_fushen_wumai
            "100" -> id = R.mipmap.icon_dafen
        }

        return id
    }

    private var weatherIconSlinBig =
        arrayOf(
            R.mipmap.icon_weather_slin_qing_day,  //0
            R.mipmap.icon_weather_slin_duoyun_day,  //1
            R.mipmap.icon_weather_slin_yin, // 2
            R.mipmap.icon_weather_slin_mai_all, //53
            R.mipmap.icon_weather_slin_mai_all,  //54
            R.mipmap.icon_weather_slin_mai_all,//55
            R.mipmap.icon_weather_slin_xiaoyu,  //7
            R.mipmap.icon_weather_slin_zhongyu,//8
            R.mipmap.icon_weather_slin_dayu,//9
            R.mipmap.icon_weather_slin_baoyu,//10
            R.mipmap.icon_weather_slin_wu,//18
            R.mipmap.icon_weather_slin_xiaoxue,//14
            R.mipmap.icon_weather_slin_zhongxue,//15
            R.mipmap.icon_weather_slin_daxue,//16
            R.mipmap.icon_weather_slin_baoxue,//17
            R.mipmap.icon_weather_slin_shachen,//29
            R.mipmap.icon_weather_slin_shachen,//30
            R.mipmap.icon_weather_slin_dafeng,//100
            R.mipmap.icon_weather_slin_leizhenyu,//4
            R.mipmap.icon_weather_slin_bingbao,//5
            R.mipmap.icon_weather_slin_yujiaxue//6
        )

    fun IconSlinBig(code: String): Int {
        var index = iconIndex(code)
        return if (isNight) {
            when (code) {
                "0" -> R.mipmap.icon_weather_slin_qing_night
                "1" -> R.mipmap.icon_weather_slin_duoyun_night
                "10" -> R.mipmap.icon_weather_slin_wu_night
                else -> {
                    if (index > weatherIconSlinBig.size - 1) {
                        R.mipmap.icon_weather_slin_qing_day
                    } else {
                        weatherIconSlinBig[index]
                    }
                }
            }
        } else {
            if (index > weatherIconSlinBig.size - 1) {
                R.mipmap.icon_weather_slin_qing_day
            } else {
                weatherIconSlinBig[index]
            }
        }
    }

    fun todayAirQualityBgID(aqi: Int): Int {
        var id = R.drawable.tv_today_bg
        if (0 <= aqi && aqi <= 50) {
            id = R.drawable.tv_today_bg
        } else if (50 < aqi && aqi <= 100) {
            id = R.drawable.tv_today_liang_bg
        } else if (100 < aqi && aqi <= 150) {
            id = R.drawable.tv_today_qingdu_bg
        } else if (150 < aqi && aqi <= 200) {
            id = R.drawable.tv_today_zhongdu_bg
        } else if (200 < aqi && aqi <= 300) {
            id = R.drawable.tv_today_zhong_bg
        } else if (300 < aqi && aqi <= 500) {
            id = R.drawable.tv_today_yanzhong_bg
        }
        return id
    }

    private var weatherIconBig =
        arrayOf(
            R.mipmap.icon_weather_qing_day,  //0
            R.mipmap.icon_weather_duoyun_day,  //1
            R.mipmap.icon_weather_yin, // 2
            R.mipmap.icon_weather_mai_light, //53
            R.mipmap.icon_weather_mai_light,  //54
            R.mipmap.icon_weather_mai_light,//55
            R.mipmap.icon_weather_xiaoyu,  //7
            R.mipmap.icon_weather_zhongyu,//8
            R.mipmap.icon_weather_dayu,//9
            R.mipmap.icon_weather_baoyu,//10
            R.mipmap.icon_weather_wu,//18
            R.mipmap.icon_weather_xiaoxue,//14
            R.mipmap.icon_weather_zhongxue,//15
            R.mipmap.icon_weather_daxue,//16
            R.mipmap.icon_weather_baoxue,//17
            R.mipmap.icon_weather_fuchen,//29
            R.mipmap.icon_weather_fuchen,//30
            R.mipmap.icon_weather_dafeng,//100
            R.mipmap.icon_weather_leizhenyu,//4
            R.mipmap.icon_weather_bingbao,//5
            R.mipmap.icon_weather_yujiaxue//6
        )

    fun IconBig(code: String): Int {
        var index = iconIndex(code)
        return if (isNight) {
            when (code) {
                "0" -> R.mipmap.icon_weather_qing_night
                "1" -> R.mipmap.icon_weather_duoyun_night
                else -> {
                    if (index > weatherIconBig.size - 1) {
                        R.mipmap.icon_weather_qing_day
                    } else {
                        weatherIconBig[index]
                    }
                }
            }
        } else {
            if (index > weatherIconBig.size - 1) {
                R.mipmap.icon_weather_qing_day
            } else {
                weatherIconBig[index]
            }
        }
    }

    fun IconBig(code: String, night: Boolean): Int {
        var index = iconIndex(code)
        return if (night) {
            when (code) {
                "0" -> R.mipmap.icon_weather_qing_night
                "1" -> R.mipmap.icon_weather_duoyun_night
                else -> {
                    if (index > weatherIconBig.size - 1) {
                        R.mipmap.icon_weather_qing_day
                    } else {
                        weatherIconBig[index]
                    }
                }
            }
        } else {
            if (index > weatherIconBig.size - 1) {
                R.mipmap.icon_weather_qing_day
            } else {
                weatherIconBig[index]
            }
        }
    }


    fun leafIcon(code: String): Int {

        return when (code) {
            "优" -> R.mipmap.icon_leaf_you
            "良" -> R.mipmap.icon_leaf_liang
            "轻度" -> R.mipmap.icon_leaf_qingdu
            "中度" -> R.mipmap.icon_leaf_middle
            "重度" -> R.mipmap.icon_leaf_weight
            "严重" -> R.mipmap.icon_leaf_yanzhong
            else -> R.mipmap.icon_leaf_you
        }
    }

    fun lifeIcon(code: String): Int {
        return when (code) {
            "穿衣" -> R.mipmap.icon_life_chuanyi
            "感冒指数" -> R.mipmap.icon_life_ganmao
            "紫外线" -> R.mipmap.icon_life_ziwaixian
            "洗车" -> R.mipmap.icon_life_xiche
            "舒适度" -> R.mipmap.icon_life_shushidu
            else -> 0
        }
    }

    fun headerIcom(code: String): Int {
        return when (code) {
            "优" -> R.mipmap.im_water1
            "良" -> R.mipmap.im_water2
            "轻度" -> R.mipmap.im_water3
            "中度" -> R.mipmap.im_water4
            "重度" -> R.mipmap.im_water5
            "严重" -> R.mipmap.im_water6
            else -> R.mipmap.im_water1
        }
    }

    fun airColor(code: String, view: ShapeView) {
        var color = when (code) {
            "优" -> "#0FD692"
            "良" -> "#FFC216"
            "轻度" -> "#FC8822"
            "中度" -> "#E53222"
            "重度" -> "#761B7C"
            "严重" -> "#74130C"
            else -> "#80CCCCCC"
        }
        view.exeConfig(view.getConfig().apply { bgColor = Color.parseColor(color) })
    }

    fun airColorStr(code: String): String {
        return when (code) {
            "优" -> "#0FD692"
            "良" -> "#FFC216"
            "轻度" -> "#FC8822"
            "中度" -> "#E53222"
            "重度" -> "#761B7C"
            "严重" -> "#74130C"
            else -> "#80CCCCCC"
        }
    }

    fun airColor(code: String): ArrayList<String> {
        return when (code) {
            "优" -> arrayListOf("#40E1AA", "#0FD692")
            "良" -> arrayListOf("#FCCB49", "#FFC216")
            "轻度" -> arrayListOf("#FFAB3E", "#FC8822")
            "中度" -> arrayListOf("#F55347", "#E53222")
            "重度" -> arrayListOf("#A355A8", "#761B7C")
            "严重" -> arrayListOf("#8B4944", "#74130C")
            else -> arrayListOf("#2FB999", "#0FD692")
        }
    }

    fun airColors(code: String): IntArray {
        return when (code) {
            "优" -> intArrayOf(
                Color.parseColor("#0040E1AA"), Color.parseColor("#FF0FD692")
            )
            "良" -> intArrayOf(
                Color.parseColor("#00FCCB49"), Color.parseColor("#FFFFC216")
            )
            "轻度" -> intArrayOf(
                Color.parseColor("#00FFAB3E"), Color.parseColor("#FFFC8822")
            )
            "中度" -> intArrayOf(
                Color.parseColor("#00F55347"), Color.parseColor("#FFE53222")
            )
            "重度" -> intArrayOf(
                Color.parseColor("#00A355A8"), Color.parseColor("#FF761B7C")
            )
            "严重" -> intArrayOf(
                Color.parseColor("#008B4944"), Color.parseColor("#FF74130C")
            )
            else -> intArrayOf(
                Color.parseColor("#0040E1AA"), Color.parseColor("#FF0FD692")
            )
        }
    }


    fun airCircle(code: String): Int {
        return when (code) {
            "优" -> R.mipmap.icon_air_you
            "良" -> R.mipmap.icon_air_liang
            "轻度" -> R.mipmap.icon_air_qingdu
            "中度" -> R.mipmap.icon_air_middle
            "重度" -> R.mipmap.icon_air_zhongdu
            "严重" -> R.mipmap.icon_air_weight
            else -> R.mipmap.icon_air_you
        }
    }

    fun getAirairQualityColorDrawable(i: Int): Int {
        var color = 0
        when (i) {
            in 0..50 -> {
                color = R.drawable.shape_rectangle_you
            }
            in 51..100 -> {
                color = R.drawable.shape_rectangle_liang
            }
            in 101..150 -> {
                color = R.drawable.shape_rectangle_qd
            }
            in 151..200 -> {
                color = R.drawable.shape_rectangle_zhongdu
            }
            in 201..300 -> {
                color = R.drawable.shape_rectangle_zd
            }
            in 301..500 -> {
                color = R.drawable.shape_rectangle_yanzhong
            }
        }
        return color
    }

    fun getAirairQualityString(i: Int): String? {
        var string = ""
        when (i) {
            in 0..50 -> {
                string = "优"
            }
            in 51..100 -> {
                string = "良"
            }
            in 101..150 -> {
                string = "轻度"
            }
            in 151..200 -> {
                string = "中度"
            }
            in 201..300 -> {
                string = "重度"
            }
            in 301..500 -> {
                string = "严重"
            }
        }
        return string
    }


    fun airQualityIconID(aqi: Int): Int {
        var id = 0
        if (0 <= aqi && aqi <= 50) {
            id = R.mipmap.weather_icon_air_you
        } else if (50 < aqi && aqi <= 100) {
            id = R.mipmap.weather_icon_air_liang
        } else if (100 < aqi && aqi <= 150) {
            id = R.mipmap.weather_icon_air_qingdu
        } else if (150 < aqi && aqi <= 200) {
            id = R.mipmap.weather_icon_air_zhongdu
        } else if (200 < aqi && aqi <= 300) {
            id = R.mipmap.weather_icon_air_zd
        } else if (300 < aqi && aqi <= 500) {
            id = R.mipmap.weather_icon_air_yanzhong
        }
        return id
    }

    fun leafIconResStr(code: String): String {

        return when (code) {
            "优" -> "icon_leaf_you"
            "良" -> "icon_leaf_liang"
            "轻度" -> "icon_leaf_qingdu"
            "中度" -> "icon_leaf_middle"
            "重度" -> "icon_leaf_weight"
            "严重" -> "icon_leaf_yanzhong"
            else -> "icon_leaf_you"
        }
    }

    fun iconSmallStr(code: String, night: Boolean): String {
        return when (code) {
            "0" -> if (night) "icon_weather_qing_night_small" else "icon_weather_qing_day_small"
            "1" -> if (night) "icon_weather_duoyun_night_small" else "icon_weather_duoyun_day_small"
            "2" -> "icon_weather_yin_small"
            "53", "54", "55" -> "icon_weather_mai_small"
            "7" -> "icon_weather_xiaoyu_small"
            "8" -> "icon_weather_zhongyu_small"
            "9" -> "icon_weather_dayu_small"
            "10" -> "icon_weather_baoyu_small"
            "18" -> "icon_weather_wu_small"
            "14" -> "icon_weather_xiaoxue_small"
            "15" -> "icon_weather_zhongxue_small"
            "16" -> "icon_weather_daxue_small"
            "17" -> "icon_weather_baoxue_small"
            "29", "30" -> "icon_weather_fuchen_small"
            "100" -> "icon_weather_dafeng_small"
            "4" -> "icon_weather_leizhenyu_small"
            "5" -> "icon_weather_bingbao_small"
            "6" -> "icon_weather_yujiaxue_small"
            else -> "icon_weather_qing_day_small"
        }
    }

    fun iconBigStr(code: String, night: Boolean): String {
        return when (code) {
            "0" -> if (night) "icon_weather_qing_night" else "icon_weather_qing_day"
            "1" -> if (night) "icon_weather_duoyun_night" else "icon_weather_duoyun_day"
            "2" -> "icon_weather_yin"
            "53", "54", "55" -> "icon_weather_mai_middle"
            "7" -> "icon_weather_xiaoyu"
            "8" -> "icon_weather_zhongyu"
            "9" -> "icon_weather_dayu"
            "10" -> "icon_weather_baoyu"
            "18" -> "icon_weather_wu"
            "14" -> "icon_weather_xiaoxue"
            "15" -> "icon_weather_zhongxue"
            "16" -> "icon_weather_daxue"
            "17" -> "icon_weather_baoxue"
            "29", "30" -> "icon_weather_fuchen"
            "100" -> "icon_weather_dafeng"
            "4" -> "icon_weather_leizhenyu"
            "5" -> "icon_weather_bingbao"
            "6" -> "icon_weather_yujiaxue"
            else -> "icon_weather_qing_day"
        }
    }

}