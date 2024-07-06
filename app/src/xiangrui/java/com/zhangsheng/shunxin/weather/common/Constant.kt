package com.zhangsheng.shunxin.weather.common

import com.zhangsheng.shunxin.BuildConfig

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2019/12/3 10:13
 */
object Constant : BaseConstant() {

    override val SP_AGREE_PRIVACY: String
        get() = "${BuildConfig.APPLICATION_ID}.sp_agree_privacy"

    override val SP_PASS_PERMISSION: String
        get() = "${BuildConfig.APPLICATION_ID}.sp_pass_permission"

    override val SP_LOCATION_DATA: String
        get() = "${BuildConfig.APPLICATION_ID}.sp_location_data"

    override val SP_STT_REFRESH_TOKEN: String
        get() = "${BuildConfig.APPLICATION_ID}.sp_stt_refresh_token"

    override val SP_WEATHER_DATA: String
        get() = "${BuildConfig.APPLICATION_ID}.sp_weather_data"

    override val SP_CONTROL_STATE: String
        get() = "${BuildConfig.APPLICATION_ID}.sp_control_state"

    override val SP_POLLING_CONTROL: String
        get() = "${BuildConfig.APPLICATION_ID}.sp_polling_control"

    override val SP_SPLASH_SHOW_TIME: String
        get() = "${BuildConfig.APPLICATION_ID}.sp_splash_show_time"

    override val SP_AD_POP: String
        get() = "${BuildConfig.APPLICATION_ID}.sp_ad_pop"

    override val SP_AIR_AD_TIMES: String
        get() = "${BuildConfig.APPLICATION_ID}.sp_air_ad_times"

    override val SP_REBOOT_TIME: String
        get() = "${BuildConfig.APPLICATION_ID}.sp_reboot_time"

    override val SP_CCTV: String
        get() = "${BuildConfig.APPLICATION_ID}.sp_cctv"

    override val SP_PUSH_CODE: String
        get() = "${BuildConfig.APPLICATION_ID}.sp_push_code"

    override val SP_LAST_PUSH_TIME: String
        get() = "${BuildConfig.APPLICATION_ID}.sp_last_push_time"

    override val SP_AIR_RANK: String
        get() = "${BuildConfig.APPLICATION_ID}.sp_air_rank"

    override val SP_ACTIVEUSER_KEY: String
        get() = "${BuildConfig.APPLICATION_ID}.sp_activeuser_key"

    override val SP_WINDOW_SHOW_TIME: String
        get() = "${BuildConfig.APPLICATION_ID}.window.show.time"

    override val SP_ISLOCATION_USER: String
        get() = "${BuildConfig.APPLICATION_ID}.sp_islocation_user"

    override val SP_INSTALL_TIME: String
        get() = "${BuildConfig.APPLICATION_ID}.sp_install_time"

    override val SP_UPDATE_TIME: String
        get() = "${BuildConfig.APPLICATION_ID}.sp_update_time"

    override val SP_WIDGET_DATA: String
        get() = "${BuildConfig.APPLICATION_ID}.sp_widget_data"

}