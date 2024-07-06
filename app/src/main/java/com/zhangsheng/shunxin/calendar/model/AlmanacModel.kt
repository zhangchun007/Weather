package com.zhangsheng.shunxin.calendar.model

import com.maiya.thirdlibrary.base.BaseViewModel
import com.zhangsheng.shunxin.calendar.util.CalendarDataUtils
import com.zhangsheng.shunxin.calendar.util.CalendarHelper
import com.zhangsheng.shunxin.calendar.data.bean.AlmanacDataBean
import org.joda.time.LocalDate

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/4/22 20:43
 */
class AlmanacModel() : BaseViewModel() {


    /**
     * 查询
     *
     * @param date
     * @return com.example.demo.calendar.HlObj
     * @author qubianzhong
     * @Date 21:59 2019/4/17
     */
    suspend fun querySAByDay(localDate: LocalDate?): AlmanacDataBean {
        val almanacDataBean =
            AlmanacDataBean()
        almanacDataBean.pzbj = CalendarDataUtils.getPZBJ(localDate)
        val advices = CalendarHelper.getAdvices(localDate)
        almanacDataBean.jsyq = advices?.favonian
        almanacDataBean.xsyj = advices?.terrible
        almanacDataBean.taishen = advices?.fetus
        almanacDataBean.wx = CalendarDataUtils.getWx(localDate)
        almanacDataBean.cs = CalendarDataUtils.getCS(localDate) //冲煞
        almanacDataBean.zhishen = CalendarDataUtils.getZhiShen(localDate)
        almanacDataBean.jianchu = CalendarDataUtils.jianChuOfDate(localDate)
        almanacDataBean.stars28 = CalendarDataUtils.stars28OfDate(localDate)
        return almanacDataBean
    }

}