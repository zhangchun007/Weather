package com.zhangsheng.shunxin.weather.net.bean

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/8/6 21:09
 */
class AdPopBean {
    var showMainPopTimes=0  //每日展示次数
    var showMainPopShowTime=0L  //上次展示
    override fun toString(): String {
        return "AdPopBean(showMainPopTimes=$showMainPopTimes, showMainPopShowTime=$showMainPopShowTime)"
    }


}