package com.zhangsheng.shunxin.weather.utils


import android.graphics.Color
import com.airbnb.lottie.LottieAnimationView
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.widget.shapview.ShapeView
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.weather.common.EnumType

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2019/12/15 13:03
 */
object WeatherUtils : BaseWeatherUtils() {

    var appHomeHigh = 0

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

    fun getAnimRainLottie(type: String, level: Float): String {
        val isSnow = type == "1"
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
            else -> R.mipmap.icon_weather_ziwaixian
        }
    }

    fun hightAlertColors(level: String): Array<String> {
        return when (level) {
            "蓝色" -> arrayOf("#2483FF", "#189BFF")
            "黄色" -> arrayOf("#FF9100", "#FCC100")
            "橙色" -> arrayOf("#FF5F1C", "#FF8A03")
            "红色" -> arrayOf("#FF3737", "#FF5624")
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

    fun getTopBarColor(code: String): String {
        val res = when (code) {
            EnumType.天气气象.晴天 -> if (!isNight) "#25B7E0" else "#001862"
            EnumType.天气气象.阴天 -> if (!isNight) "#305474" else "#232C3E"
            EnumType.天气气象.小雨,
            EnumType.天气气象.中雨,
            EnumType.天气气象.大雨,
            EnumType.天气气象.暴雨,
            EnumType.天气气象.雷阵雨,
            EnumType.天气气象.冰雹,
            EnumType.天气气象.雨夹雪 -> if (!isNight) "#558C9E" else "#144870"
            EnumType.天气气象.多云 -> if (!isNight) "#2C92D8" else "#2A4887"
            EnumType.天气气象.雾,
            EnumType.天气气象.雾霾,
            EnumType.天气气象.中度雾霾,
            EnumType.天气气象.重度雾霾 -> "#39658A"
            EnumType.天气气象.小雪,
            EnumType.天气气象.中雪,
            EnumType.天气气象.大雪,
            EnumType.天气气象.暴雪 -> "#56B1F1"
            EnumType.天气气象.大风 -> "#5382B4"
            EnumType.天气气象.沙尘暴,
            EnumType.天气气象.浮尘 -> "#5882A2"
            else -> "#25B7E0"
        }
        return res
    }

    fun getWeatherAnimBg(code: String): String {
        var res = "anim_null_bg.json"
        when (code) {
            EnumType.天气气象.晴天 -> res = if (isNight) "anim_sun_night.json" else "anim_sun_day.json"
            EnumType.天气气象.阴天 -> res = if (isNight) "anim_yin_night.json" else "anim_yin_day.json"
            EnumType.天气气象.小雨,
            EnumType.天气气象.中雨,
            EnumType.天气气象.大雨,
            EnumType.天气气象.暴雨,
            EnumType.天气气象.雷阵雨,
            EnumType.天气气象.冰雹,
            EnumType.天气气象.雨夹雪 -> res =
                if (isNight) "anim_rain_night.json" else "anim_rain_day.json"
            EnumType.天气气象.多云 -> res =
                if (isNight) "anim_duoyun_night.json" else "anim_duoyun_day.json"
            EnumType.天气气象.雾,
            EnumType.天气气象.雾霾,
            EnumType.天气气象.中度雾霾,
            EnumType.天气气象.重度雾霾 -> res = "anim_forg.json"
            EnumType.天气气象.小雪,
            EnumType.天气气象.中雪,
            EnumType.天气气象.大雪,
            EnumType.天气气象.暴雪 -> res = "anim_snow.json"
            EnumType.天气气象.大风 -> res = "anim_wind.json"
            EnumType.天气气象.沙尘暴,
            EnumType.天气气象.浮尘 -> res = "anim_shachen.json"
        }
        return res
    }

    private var weatherIconBig =
        arrayOf(
            R.mipmap.icon_weather_qing_day,  //0
            R.mipmap.icon_weather_duoyun_day,  //1
            R.mipmap.icon_weather_yin, // 2
            R.mipmap.icon_weather_mai, //53
            R.mipmap.icon_weather_mai,  //54
            R.mipmap.icon_weather_mai,//55
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
        val index = iconIndex(code)
        return if (isNight) {
            when (code) {
                "0" -> R.mipmap.icon_weather_qing_night
                "1" -> R.mipmap.icon_weather_duoyun_night
                else -> {
                    if (index > weatherIconBig.size - 1) {
                        R.mipmap.icon_weather_defulat
                    } else {
                        weatherIconBig[index]
                    }
                }
            }
        } else {
            if (index > weatherIconBig.size - 1) {
                R.mipmap.icon_weather_defulat
            } else {
                weatherIconBig[index]
            }
        }
    }

    fun IconBig(code: String, night: Boolean): Int {
        val index = iconIndex(code)
        return if (night) {
            when (code) {
                "0" -> R.mipmap.icon_weather_qing_night
                "1" -> R.mipmap.icon_weather_duoyun_night
                else -> {
                    if (index > weatherIconBig.size - 1) {
                        R.mipmap.icon_weather_defulat
                    } else {
                        weatherIconBig[index]
                    }
                }
            }
        } else {
            if (index > weatherIconBig.size - 1) {
                R.mipmap.icon_weather_defulat
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
        val color = when (code) {
            "优" -> "#17D267"
            "良" -> "#FFC700"
            "轻度" -> "#FF892F"
            "中度" -> "#FD7053"
            "重度" -> "#D24264"
            "严重" -> "#5F479A"
            else -> "#80CCCCCC"
        }

        view.exeConfig(view.getConfig().apply { bgColor = Color.parseColor(color) })
    }

    fun airColorStr(code: String): String {
        return when (code) {
            "优" -> "#17D267"
            "良" -> "#FFC700"
            "轻度" -> "#FF892F"
            "中度" -> "#FD7053"
            "重度" -> "#D24264"
            "严重" -> "#5F479A"
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
            "53", "54", "55" -> "icon_weather_mai"
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

    fun weatherBigIcon(view: LottieAnimationView?, code: String, night: Boolean = isNight) {
        view?.cancelAnimation()
        view?.progress = 0f
        view?.loop(true)
        view?.imageAssetsFolder = "images"
        Try {
            when (code) {
                EnumType.天气气象.晴天 -> {
                    if (night) {
                        view?.setAnimation("晴天晚.json")
                    } else {
                        view?.setAnimation("晴天白.json")
                    }
                    view?.playAnimation()
                }
                EnumType.天气气象.阴天 -> {
                    if (night) {
                        view?.setAnimation("阴天晚.json")
                    } else {
                        view?.setAnimation("阴天白.json")
                    }
                    view?.playAnimation()
                }
                EnumType.天气气象.小雨 -> {
                    if (night) {
                        view?.setAnimation("小雨晚.json")
                    } else {
                        view?.setAnimation("小雨白.json")
                    }
                    view?.playAnimation()
                }
                EnumType.天气气象.中雨 -> {
                    if (night) {
                        view?.setAnimation("中雨晚.json")
                    } else {
                        view?.setAnimation("中雨白.json")
                    }
                    view?.playAnimation()
                }
                EnumType.天气气象.暴雨 -> {
                    if (night) {
                        view?.setAnimation("暴雨晚.json")
                    } else {
                        view?.setAnimation("暴雨白.json")
                    }
                    view?.playAnimation()
                }
                EnumType.天气气象.大雨 -> {
                    if (night) {
                        view?.setAnimation("大雨晚.json")
                    } else {
                        view?.setAnimation("大雨白.json")
                    }
                    view?.playAnimation()
                }
                EnumType.天气气象.雷阵雨 -> {
                    if (night) {
                        view?.setAnimation("雷阵雨晚.json")
                    } else {
                        view?.setAnimation("雷阵雨白.json")
                    }
                    view?.playAnimation()
                }
                EnumType.天气气象.冰雹 -> {
                    if (night) {
                        view?.setAnimation("冰雹晚.json")
                    } else {
                        view?.setAnimation("冰雹白.json")
                    }
                    view?.playAnimation()
                }
                EnumType.天气气象.雨夹雪 -> {
                    if (night) {
                        view?.setAnimation("雨夹雪晚.json")
                    } else {
                        view?.setAnimation("雨夹雪白.json")
                    }
                    view?.playAnimation()
                }
                EnumType.天气气象.多云 -> {
                    if (night) {
                        view?.setAnimation("多云晚.json")
                    } else {
                        view?.setAnimation("多云白.json")
                    }
                    view?.playAnimation()
                }
                EnumType.天气气象.雾 -> {
                    if (night) {
                        view?.setAnimation("雾晚.json")
                    } else {
                        view?.setAnimation("雾白.json")
                    }
                    view?.playAnimation()
                }
                EnumType.天气气象.沙尘暴,
                EnumType.天气气象.浮尘 -> {
                    if (night) {
                        view?.setImageResource(R.mipmap.ic_big_icon_shachen_night)
                    } else {
                        view?.setImageResource(R.mipmap.ic_big_icon_shachen_day)
                    }
                }
                EnumType.天气气象.雾霾,
                EnumType.天气气象.中度雾霾,
                EnumType.天气气象.重度雾霾 -> {
                    if (night) {
                        view?.setImageResource(R.mipmap.ic_big_icon_wumai_night)
                    } else {
                        view?.setImageResource(R.mipmap.ic_big_icon_wumai_day)
                    }
                }
                EnumType.天气气象.小雪 -> {
                    if (night) {
                        view?.setAnimation("小雪晚.json")
                    } else {
                        view?.setAnimation("小雪白.json")
                    }
                    view?.playAnimation()
                }
                EnumType.天气气象.中雪 -> {
                    if (night) {
                        view?.setAnimation("中雪晚.json")
                    } else {
                        view?.setAnimation("中雪白.json")
                    }
                    view?.playAnimation()
                }
                EnumType.天气气象.大雪 -> {
                    if (night) {
                        view?.setAnimation("大雪晚.json")
                    } else {
                        view?.setAnimation("大雪白.json")
                    }
                    view?.playAnimation()
                }
                EnumType.天气气象.暴雪 -> {
                    if (night) {
                        view?.setAnimation("暴雪晚.json")
                    } else {
                        view?.setAnimation("暴雪白.json")
                    }
                    view?.playAnimation()
                }
                EnumType.天气气象.大风 -> {
                    if (night) {
                        view?.setImageResource(R.mipmap.ic_big_icon_feng_night)
                    } else {
                        view?.setImageResource(R.mipmap.ic_big_icon_feng_day)
                    }
                }
                else -> {
                    if (night) {
                        view?.setAnimation("晴天晚.json")
                    } else {
                        view?.setAnimation("晴天白.json")
                    }
                    view?.playAnimation()
                }
            }
        }
    }

    fun setHomeWeatherBg(code: String): Int = when (code) {
        EnumType.天气气象.小雪,
        EnumType.天气气象.中雪,
        EnumType.天气气象.大雪,
        EnumType.天气气象.暴雪,
        EnumType.天气气象.多云,
        EnumType.天气气象.晴天 ->
            if (isNight) R.mipmap.ic_home_weatner_night_bg else R.mipmap.ic_home_weatner_day_bg
        EnumType.天气气象.雾,
        EnumType.天气气象.大风,
        EnumType.天气气象.阴天,
        EnumType.天气气象.小雨,
        EnumType.天气气象.中雨,
        EnumType.天气气象.大雨,
        EnumType.天气气象.暴雨,
        EnumType.天气气象.雷阵雨,
        EnumType.天气气象.冰雹,
        EnumType.天气气象.雨夹雪 ->
            if (isNight) R.mipmap.ic_home_weatner_yin_night_bg else R.mipmap.ic_home_weatner_yin_day_bg
        EnumType.天气气象.雾霾,
        EnumType.天气气象.中度雾霾,
        EnumType.天气气象.重度雾霾,
        EnumType.天气气象.沙尘暴,
        EnumType.天气气象.浮尘 -> if (isNight) R.mipmap.ic_home_weatner_shachen_night_bg else R.mipmap.ic_home_weatner_shachen_day_bg
        else -> if (isNight) R.mipmap.ic_home_weatner_night_bg else R.mipmap.ic_home_weatner_day_bg
    }

    fun setHomeWeatherBgColor(code: String): Int = when (code) {
        EnumType.天气气象.小雪,
        EnumType.天气气象.中雪,
        EnumType.天气气象.大雪,
        EnumType.天气气象.暴雪,
        EnumType.天气气象.多云,
        EnumType.天气气象.晴天 ->
            if (isNight) Color.parseColor("#FF6147D3") else Color.parseColor("#FF98E6FF")
        EnumType.天气气象.雾,
        EnumType.天气气象.大风,
        EnumType.天气气象.阴天,
        EnumType.天气气象.小雨,
        EnumType.天气气象.中雨,
        EnumType.天气气象.大雨,
        EnumType.天气气象.暴雨,
        EnumType.天气气象.雷阵雨,
        EnumType.天气气象.冰雹,
        EnumType.天气气象.雨夹雪 ->
            if (isNight) Color.parseColor("#FF544591") else Color.parseColor("#FF84AEFF")
        EnumType.天气气象.雾霾,
        EnumType.天气气象.中度雾霾,
        EnumType.天气气象.重度雾霾,
        EnumType.天气气象.沙尘暴,
        EnumType.天气气象.浮尘 -> if (isNight) Color.parseColor("#FF614123") else Color.parseColor("#FFD69B51")
        else -> if (isNight) Color.parseColor("#FF6147D3") else Color.parseColor("#FF98E6FF")
    }

}