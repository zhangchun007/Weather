package com.zhangsheng.shunxin.weather.utils

import android.app.Activity
import android.text.TextUtils
import android.util.Log
import androidx.fragment.app.Fragment
import com.xm.xmlog.XMLogAgent
import java.util.*

class DataLogUtils {
    companion object {
        private var activityPages =
            HashMap<String, String>()
        private var fragmentPages =
            HashMap<String, String>()

        init {
            activityPages["ChooseLuckyDayActivity"] = "auspicious"; // 吉日查询`
            activityPages["LuckyDayActivity"] = "auspicious_list"; // 吉日列表
            activityPages["AlmanacActivity"] = "auspicious_result"; // 吉日结果
            activityPages["SettingsActivity"] = "set_page"; // 设置
            activityPages["CityListManageActivity"] = "city"; // 城市管理
            activityPages["WeatherActivity"] = "weather_instant"; // 实况天气
            activityPages["FifteenWeatherDetailsActivity"] = "weather_forecast"; // 十五日天气
            activityPages["AboutUsActivity"] = "treaty"; // 用户隐私协议
            activityPages["WeatherMapActivity"] = "minute_precipitation"; //分钟级降雨
            activityPages["AirActivity"] = "airquality"; //空气质量
            activityPages["HighAlertActivity"] = "weather_warning"; //天气预警
            activityPages["CCTVWeatherActivity"] = "weather_video_page"; //天气视频
            activityPages["AlmancMainActivity"] = "oldcalendar"; //黄历
            activityPages["ChooseLuckyDayActivity"] = "auspicious"; //择吉日
            activityPages["LuckyDayActivity"] = "auspicious_list"; //择吉日列表页
            activityPages["AlmanacActivity"] = "auspicious_result"; //择吉日结果页
        }

        /**
         * 页面时长统计
         *
         * @param pageId
         * @param startOtStop
         */
        fun showOrStop(pageId: String, startOtStop: Boolean) {
            if (TextUtils.isEmpty(pageId)) return
            if (startOtStop) {
                XMLogAgent.onPageStart(pageId)
            } else {
                XMLogAgent.onPageEnd(pageId)
            }
            Log.d(
                "PageLogUtils-all",
                "ShowOrStop pageId:$pageId startOtStop:$startOtStop"
            )
        }

        fun fragmentShowOrStop(
            fragment: Fragment,
            startOtStop: Boolean
        ) {
            val simpleName = fragment.javaClass.simpleName
            val pageId: String =
                fragmentPages[simpleName] ?: ""
            Log.d(
                "PageLogUtils",
                " fragmentShowOrStop pageId:$pageId simpleName:$simpleName startOtStop:$startOtStop"
            )
            showOrStop(pageId, startOtStop)
        }

        fun activityShowOrStop(activity: Activity, startOtStop: Boolean) {
            val simpleName = activity.javaClass.simpleName
            val pageId: String =
                activityPages[simpleName] ?: ""
            Log.d(
                "PageLogUtils",
                " activityShowOrStop pageId:$pageId simpleName: $simpleName startOtStop:$startOtStop"
            )
            showOrStop(pageId, startOtStop)
        }
    }


}