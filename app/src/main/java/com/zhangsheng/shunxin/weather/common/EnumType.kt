package com.zhangsheng.shunxin.weather.common

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2019/10/24 15:47
 */
class EnumType {


    object 推送状态 {
        val 成功 = "0"
        val 未知错误 = "100001"
        val 网络错误 = "100002"
        val context无效 = "100003"
        val 服务初始化 = "100004"
        val 设置频率过快 = "100005"
        val 服务端禁用 = "100006"
    }

    object 天气气象 {

        val 晴天 = "0"
        val 多云 = "1"
        val 阴天 = "2"
        val 雾霾 = "53"
        val 中度雾霾 = "54"
        val 重度雾霾 = "55"
        val 小雨 = "7"
        val 中雨 = "8"
        val 大雨 = "9"
        val 暴雨 = "10"
        val 雾 = "18"
        val 小雪 = "14"
        val 中雪 = "15"
        val 大雪 = "16"
        val 暴雪 = "17"
        val 浮尘 = "29"
        val 沙尘暴 = "30"
        val 大风 = "100"
        val 雷阵雨 = "4"
        val 冰雹 = "5"
        val 雨夹雪 = "6"
    }

    object 刷新类型 {

        val 初始状态 = 0
        val 正在刷新 = 1
        val 刷新成功 = 2
        val 刷新失败 = 3
        val 开始刷新 = 4
        val 网络错误 = 5
        val 定位失败 = 6
        val 重新定位 = 7
        val 定位权限 = 8
        val 系统时间 = 9
    }

    object 上报埋点 {
        val 用户协议 = "user_agreement"
        val 隐私政策 = "privacy_policy"

        val 新用户协议显示 = "new-agree-page"

        val 新用户协议同意并进入 = "new-agree"

        val 新用户协议不同意 = "new-dissent"

        val 拒绝协议抱歉弹窗 = "new-dissent"

        val 拒绝协议抱歉弹窗同意并进入 = "newsorry-agree"

        val 拒绝协议抱歉弹窗再想想 = "newsorry-dissent"

        val 缺少定位权限上报 = "Report_lack_location"

        val 缺少存储权限上报 = "Report_lack_memory"

        val 缺少电话权限上报 = "Report_lack_phone"

        val 新用户协议同意发起电话授权授权成功 = "newAgree_phoneSuc"

        val 新用户协议同意发起电话授权授权失败 = "newAgree_phoneFail"


        val 新用户协议同意发起定位授权授权失败 = "newAgree_LocationFail"

        val 新用户协议同意发起存储授权授权成功 = "newAgree_MemorySuc"

        val 新用户协议同意发起存储授权授权失败 = "newAgree_MemoryFail"

        val 存储权限挽留弹窗 = "memoryPer"

        val 存储权限挽留弹窗去开启 = "memoryPer_agree"

        val 存储权限挽留弹窗放弃服务 = "memoryPer_cancel"

        val 存储权限挽留弹窗再次发起存储授权授权成功 = "memoryPer_againSuc"

        val 存储权限挽留弹窗再次发起存储授权授权失败 = "memoryPer_againFail"

        val app内存储申请弹窗显示 = "appMemory"

        val app内存储申请弹窗去开启 = "appMemory_agree"

        val app内存储申请弹窗放弃服务 = "appMemory_cancel"

        val app内向系统申请存储授权挽留弹窗去开启授权成功 = "appMemory_agree_Suc"

        val app内向系统申请存储授权挽留弹窗去开启授权失败 = "appMemory_agree_Fail"

        val 首次安装定位失败页面 = "first_install_location_fail"

        val 城市添加定位按钮 = "first_city_location"

        val 城市添加ip城市 = "first_city_ipciy"

        val 城市添加ip省份 = "first_city_ipprvc"

        val 城市添加非ip城市 = "first_city_noipciy"

        val 城市添加非ip省份 = "first_city_noipprvc"

        val 城市添加ip接口错误 = "first_city_iperror"

        val 城市添加ip城市匹配失败 = "first_city_ipnomatch"

        val 城市添加页显示 = "first_city"

        val 城市添加页搜索 = "first_city_search"

        val 小视频底部tab点击 = "video_tab"

        val 进入首页并成功获取到天气数据 = "homepage_weather"

        val 定位城市天气数据获取成功子埋点 = "locationcity"

        val 非定位城市天气数据获取成功子埋点 = "unlocationcity"

        val 日历底部tab点击 = "calendar_tab"

        val 信息流底部tab点击 = "news_tab"

        val 设置底部tab点击 = "set_tab"

        val 首页设置点击 = "set"

        val 首页语音播报点击 = "voice"

        val 城市添加页搜索点击搜索结果 = "first_city_searchresult"

        val 城市管理添加按钮 = "citymanage_add"

        val 城市管理搜索添加城市 = "citymanage_search"

        val 城市管理编辑编辑 = "citymanage_edit"

        val 城市管理编辑排序 = "citymanage_sort"

        val 城市管理编辑设为提醒城市 = "citymanage_remind"

        val 天气底部tab展示 = "weather"

        val 实用底部tab点击 = "practical_tab"

        val 天气预警展示 = "warning"

        val 天气预警点击 = "warning"

        val 首页分钟级降雨点击 = "precipitation"

        val 首页台风展示 = "typhoon"

        val 首页台风点击 = "typhoon"

        val 实用tab台风 = "practical_typhoon"

        val 实用tab空气实况 = "practical_airlive"

        val 实用tab分钟级降雨地图 = "practical_precipitation"

        val 实用tab空气质量城市排行 = "practical_airrank"

        val 实用tab15日天气 = "practical_daily15"

        val 实用tab早晚天气提醒 = "practical_setpush"

        val 首页实况天气 = "weatherlive"

        val 首页今天明天模块今天 = "daily15_today"

        val 首页今天明天模块明天 = "daily15_tomorrow"

        val 二十四小时天气模块展示 = "hourly"

        val 二十四小时天气模块左右滑动 = "hourly_slide"

        val 十五日天气模块展示 = "daily"

        val 十五日天气模块点击 = "daily"

        val 十五日天气模块左右滑动 = "daily_slide"

        val 空气质量模块展示 = "airquality"

        val 空气质量模块点击 = "airquality"

        val CCTV视频模块展示 = "cctv"

        val CCTV视频模块点击 = "cctv"

        val 生活指数模块展示 = "lifeindex"

        val 生活指数模块日历点击 = "lifeindex_calendar"

        val 生活指数模块日历点击宜忌 = "lifeindex_calendar_taboo"

        val 日历底部栏点击宜忌 = "calendar_tab_taboo"

        val 首页信息流频道展示 = "news"

        val 首页信息流频道详情页展示 = "news_detail"

        val 底部信息流tab详情页展示 = "news_tab_detail"


        val 缺少通知权限上报 = "Report_lack_notification"

        val 有定位权限但定位失败 = "first_gps_fail"

        val 早间天气推送首页点击 = "morningpush_home"

        val 晚间天气推送首页点击 = "nightpush_home"

        val 常驻通知栏首页点击 = "notification_home"

        val 设置广告设置个性化广告推送开 = "personalizedAD_on"

        val 设置广告设置个性化广告推送关 = "personalizedAD_off"

        val 非强制升级弹窗_展示 = "upgrade_pop"
        val 非强制升级弹窗_立即升级 = "upgrade_agree"
        val 非强制升级弹窗_关闭 = "upgrade_close"

        val 强制升级弹窗_展示 = "force_upgrade_pop"
        val 强制升级弹窗_立即升级 = "force_upgrade_agree"


        val 新用户流程_定位授权弹窗_点放弃 = "NewLocation"

        val 新用户流程_定位授权弹窗_点同意 = "NewLocation"
        val 新用户流程_定位授权弹窗_展示 = "NewLocation"

        val 新用户流程_定位授权弹窗_授权成功 = "NewLoction_Suc"

        val 新用户流程_定位授权弹窗_授权失败 = "NewLoction_Fail"

        val 新用户首次启动场景调起设置壁纸show_click = "wallpaper_FirstStart"

        val 非新用户首次启动场景调起设置壁纸show_click = "wallpaper_NextStart"

        val 天气背景场景成功调起设置壁纸界面show_click = "wallpaper_background"

        val 雨雪天气提醒上报 = "RainSnow_request_fail"

        val 首页40日预报展示 = "40day_show"
        val 首页40日预报点击温度 = "40day_temperature"
        val 首页40日预报点击降雨 = "40day_rainfall"
        val 首页40日天气查看更多 = "40day_more"
        val _40日天气二级页展示 = "40day_DetailPages"
        val 温度趋势左右滑动 = "40day_temperature_LRclick"
        val 降水日期点击 = "40day_rainfallDays"
        val 日历天气切换 = "DaysweatherCat"


        //------------子埋点-----------------//
        val 系统GPS位置服务关闭 = "gpsoff"
        val 无网络 = "netoff"
        val 超10秒未返回定位结果 = "timeout"
        val 未获取到GPS信息 = "noresult"


        //页面时常
        val 设置详情页 = "set_page"

        val 天气预警详情页 = "weather_warning_page"

        val 分钟级降雨详情页 = "minute_precipitation_page"

        val 十五日天气详情页 = "weather_forecast_page"

        val 空气质量详情页 = "airquality_page"

        val CCTV视频详情页 = "weather_video_page"

        val 信息流主页 = "newslist"

        val 新闻详情页 = "news_details"

        val 新用户协议弹窗 = "new-agree-page"

        val 初始化失败 = "spsdk_initFail"

        val 常驻通知栏_点击 = "resident"
        val 常驻通知栏_数据过期 = "resident_overdue"
    }

    object 广告样式 {
        val 信息流 = "feed"
        val 激励视频 = "video"
        val 开屏 = "splash"
        val 信息流_模板 = "feed_template"
    }


    object 弹窗优先级 {
        val 强制升级 = 0
        val 首次定位权限 = 1
        val 存储挽留 = 2
        val 广告弹窗 = 3
        val 升级弹窗 = 4
        val 定位权限 = 5
        val 消息通知 = 6

    }


}