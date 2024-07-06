package com.zhangsheng.shunxin.calendar.data.bean

import com.zhangsheng.shunxin.weather.net.bean.ControlBean

/**
 * @Description:   日历fragment中使用
 * @Author:         lhy
 * @CreateDate:     2020/4/20 15:18
 */
class CalendarDataBean {
    constructor(itemType: Int) {
        this.itemType = itemType
    }

    var itemType: Int = 0

    var cps: List<ControlBean.CPS>? = null

    var AD:String? =null;

    var position:Int = 0;

}