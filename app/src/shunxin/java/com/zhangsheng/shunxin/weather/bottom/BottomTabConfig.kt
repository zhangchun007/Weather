package com.zhangsheng.shunxin.weather.bottom

import com.maiya.thirdlibrary.utils.CacheUtil
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.weather.common.EnumType

/**
 * 说明：底部tab配置
 * 作者：刘鹏
 * 添加时间：5/17/21 11:01 AM
 * 修改人：liupe
 * 修改时间：5/17/21 11:01 AM
 */
class BottomTabConfig {

    companion object {
        // 底部tab默认配置
        val bottomTabConfigs = arrayOf(BottomBarItem.CMD_WEATHER, BottomBarItem.CMD_VIDEO, BottomBarItem.CMD_STREAM, BottomBarItem.CMD_CALENDAR, BottomBarItem.CMD_TOOLBOX)

        /**
         * 说明：创建对应的tab项
         * 作者：刘鹏
         * 添加时间：5/17/21 11:02 AM
         * 修改人：liupe
         * 修改时间：5/17/21 11:02 AM
         */
        fun createBottomBarItem(cmd: String) : BottomBarItem? {
            var item : BottomBarItem? = null
            when(cmd) {
                BottomBarItem.CMD_WEATHER -> {
                    item = BottomBarItem()
                    item.setTitle("天气")
                    item.setTopDrawableId(R.drawable.select_tab_bt_weather)
                    item.setEventCMD(cmd)
                }
                BottomBarItem.CMD_VIDEO -> {
                    item = BottomBarItem()
                    item.setTitle("视频")
                    item.setTopDrawableId(R.drawable.select_tab_bt_video)
                    item.setEventCMD(cmd)
                    item.setClickEvent(EnumType.上报埋点.小视频底部tab点击)
                }
                BottomBarItem.CMD_STREAM -> {
                    item = BottomBarItem()
                    item.setTitle("热点")
                    item.setTopDrawableId(R.drawable.select_tab_bt_hot)
                    item.setEventCMD(cmd)
                    item.setClickEvent(EnumType.上报埋点.信息流底部tab点击)
                }
                BottomBarItem.CMD_CALENDAR -> {
                    item = BottomBarItem()
                    item.setTitle("日历")
                    item.setTopDrawableId(R.drawable.select_tab_bt_calendar)
                    item.setEventCMD(cmd)
                    item.setClickEvent(EnumType.上报埋点.日历底部tab点击)
                }
                BottomBarItem.CMD_TOOLBOX -> {
                    item = BottomBarItem()
                    item.setTitle("实用")
                    item.setTopDrawableId(R.drawable.select_tab_bt_setting)
                    item.setEventCMD(cmd)
                    item.setClickEvent(EnumType.上报埋点.实用底部tab点击)
                    if (!CacheUtil.getBoolean(BottomBarItem.KEY_TAB_NEW_TIP_PRE + cmd, false)) {
                        item.setShowNewTabTip()
                    }
                }
            }

            return item
        }
    }
}