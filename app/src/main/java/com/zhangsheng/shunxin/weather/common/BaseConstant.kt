package com.zhangsheng.shunxin.weather.common

import android.Manifest
import android.content.Context
import android.content.ContextWrapper
import android.os.Build
import androidx.fragment.app.FragmentActivity
import com.maiya.thirdlibrary.utils.CacheUtil

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2019/12/3 10:13
 */
abstract class BaseConstant {

    open val SP_AGREE_PRIVACY = "sp_agree_privacy"
    open val SP_PASS_PERMISSION = "sp_pass_permission"
    open val SP_LOCATION_DATA = "sp_location_data"
    open val SP_STT_REFRESH_TOKEN = "sp_stt_refresh_token"
    open val SP_WEATHER_DATA = "sp_weather_data"
    open val SP_CONTROL_STATE = "sp_control_state"
    open val SP_KEEP_ALIVE_STATE = "sp_keep_alive_state"
    open val SP_POLLING_CONTROL = "sp_polling_control"
    open val SP_SPLASH_SHOW_TIME = "sp_splash_show_time"
    open val SP_AD_POP = "sp_ad_pop"
    open val SP_AIR_AD_TIMES = "sp_air_ad_times"
    open val SP_REBOOT_TIME = "sp_reboot_time"
    open val SP_CCTV = "sp_cctv"
    open val SP_PUSH_CODE = "sp_push_code"
    open val SP_LAST_PUSH_TIME = "sp_last_push_time"
    open val SP_AIR_RANK = "sp_air_rank"
    open val SP_ACTIVEUSER_KEY = "sp_activeuser_key"
    open val SP_WINDOW_SHOW_TIME = "window.show.time"
    open val SP_ISLOCATION_USER = "sp_islocation_user"
    open val SP_INSTALL_TIME = "sp_install_time"
    open val SP_UPDATE_TIME = "sp_update_time"
    open val SP_WIDGET_DATA = "sp_widget_data"
    open val SP_CHECK_ALL_PERMISSION = "sp_check_all_permission"
    open val SP_STROGE_WINDOW_SHOW_TIME = "sp_stroge_window_show_time"
    open val SP_STROGE_WINDOW_SHOW_NUMBERS = "storage.numberOfPopups"
    open val SP_CCTV_VIDEO_WIFI = "sp_cctv_video_wifi"
    open val SP_PUSH_ALERT = "sp_push_alert"
    open val SP_IS_LOCATION_PERMISSION = "sp_is_location_permission"
    open val SP_IS_WALLPAPER_FIRST = "sp_is_wallpaper_first"

    // 是否获取到了定位权限
    open val SP_LOCATION_PERMISSION_GET = "sp_location_permission_get"

    // 是否完成了定位流程
    open val SP_FIRST_LOCATION_COMPLETE = "sp_first_location_complete"
    open val SP_FIF_IS_CHART = "sp_fif_is_chart"

    //进入首页，每人每天只报一次 缺少通知权限上报（无法显示推送和常驻通知栏）
    open val SP_REPORT_LACK_NOTIFICATION = "Report_lack_notification"

    //壁纸保活存储权限弹框 每天最多弹出1次，生命周期内最多弹出3次。
    open val SP_WALL_PAPER_SDK_VOICE = "wallpaper_voice"
    open val SP_WALL_PAPER_SDK_BACKGROUND = "wallpaper_background"

    open val WALLPAPER_START = "wallpaper_start"


    fun isFirstInstall(): Boolean {
        val installTime = CacheUtil.getLong(SP_INSTALL_TIME)
        if (installTime == 0L) {
            return true
        }
        return false
    }


    var CHECK_MUST_PERMISIIONS = if (Build.VERSION.SDK_INT > Build.VERSION_CODES.O) {
        arrayOf(
            Manifest.permission.READ_PHONE_STATE,
            Manifest.permission.READ_EXTERNAL_STORAGE,
            Manifest.permission.WRITE_EXTERNAL_STORAGE
        )
    } else {
        arrayOf(Manifest.permission.READ_PHONE_STATE)
    }

    var CHECK_MUST_PERMISIIONS_AGAIN = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )


    var CHECK_LOCATION_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_COARSE_LOCATION,
        Manifest.permission.ACCESS_FINE_LOCATION
    )

    var CHECK_SAVE_PERMISSIONS = arrayOf(
        Manifest.permission.READ_EXTERNAL_STORAGE,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    var CHECK_PHONE_STATE_PERMISSIONS = arrayOf(
        Manifest.permission.READ_PHONE_STATE
    )


    open var INFO_STREAM_ORDER_CHANNEL_CHANGE = false
    open var INFO_STREAM_ALL_CHANNEL_CHANGE = false

    //================================日历===========================================//
    open val APP_H5_ACTIVE = "https://event.jiandantianqi.com/"

    open val SP_CALENDAR_HOLIDAY_DATA = "sp_calendar_holiday_data_"
    open val GET_WITHOUT_WORK_DAT_API = "${APP_H5_ACTIVE}?service=App.Tools.Holiday"


    fun context2Activity(context: Context): FragmentActivity? {
        if (context is ContextWrapper) {
            if (context is FragmentActivity) {
                return context
            }
        }
        return null
    }

    //标记当前tab 为天气
    open var flag_is_weather = true
}