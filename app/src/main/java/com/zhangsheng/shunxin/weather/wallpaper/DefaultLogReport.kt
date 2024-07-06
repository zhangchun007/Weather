package com.zhangsheng.shunxin.weather.wallpaper

import com.maiya.wallpaper.log.ILogReporter
import com.xm.xmlog.bean.XMActivityBean
import com.zhangsheng.shunxin.weather.ext.clickReport
import com.zhangsheng.shunxin.weather.ext.uploadAppActive

/**
 * @Description:
 * @Author:         zhangchun
 * @CreateDate:     2021/6/7
 * @Version:        1.0
 */
class DefaultLogReport : ILogReporter {
    /**
     * * 项目方调用中台日志上报方法进行上报
     * * @param actentryid
     * * @param entrytype
     * * @param actid
     * * @param subactid
     * * @param materialid
     * * @param type     */
    override fun onReport(
        actentryid: String?,
        entrytype: String?,
        actid: String?,
        subactid: String?,
        materialid: String?,
        type: String?
    ) {
        //上报
        uploadAppActive("$actentryid","$entrytype", "$actid", "$subactid", "$materialid", "$type")
    }
}