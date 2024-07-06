package com.zhangsheng.shunxin.calendar.wegdit

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import com.maiya.thirdlibrary.ext.*
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.calendar.util.CalendarHelper
import com.zhangsheng.shunxin.calendar.util.FestivalUtils
import com.necer.calendar.BaseCalendar
import com.necer.calendar.MonthCalendar
import com.necer.enumeration.DateChangeBehavior
import com.necer.listener.OnCalendarChangedListener
import com.necer.utils.CalendarUtil
import com.zhangsheng.shunxin.calendar.activity.AlmanacActivity
import com.zhangsheng.shunxin.weather.bottom.BottomBarItem
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.*
import kotlinx.android.synthetic.main.item_calendar.view.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.MainScope
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.joda.time.LocalDate

class CustomCalendarView : LinearLayout, OnCalendarChangedListener {

    constructor(context: Context) : this(context, null)

    constructor(context: Context, attrs: AttributeSet?) : this(context, attrs, 0)

    constructor(context: Context, attrs: AttributeSet?, defStyleAttr: Int)
            : super(context, attrs, defStyleAttr) {
        initView(attrs)
    }

    private fun initView(attrs: AttributeSet?) {
        LayoutInflater.from(context).inflate(R.layout.item_calendar, this)
        initListener();
    }

    private fun initListener() {

        ll_yiji.isVisible(isControlShow(getAppControl().swtich_all.listIndex(0).yelentrance))

        srl_calendar.ClickReport(if (getAppModel().appPageIndex == BottomBarItem.CMD_WEATHER) EnumType.上报埋点.生活指数模块日历点击宜忌 else EnumType.上报埋点.日历底部栏点击宜忌) {
            if (!isControl() && isControlShow(getAppControl().swtich_all.listIndex(0).yelentrance)) {
                skipActivity(AlmanacActivity::class.java)
            }
        }
        mc_calendar.addOnAttachStateChangeListener(onAttachStateChangeListener);
        mc_calendar?.setOnCalendarChangedListener(this)
    }

    override fun onCalendarChange(
        baseCalendar: BaseCalendar?,
        year: Int,
        month: Int,
        localDate: LocalDate?,
        dateChangeBehavior: DateChangeBehavior?
    ) {
        if (DateChangeBehavior.INITIALIZE == dateChangeBehavior) {
            return
        }

        getAppModel().localDate.value = localDate
        var festivals = ""
        Try {
            festivals = FestivalUtils.instance.getFestivals(localDate)
        }
        var calendarDate = CalendarUtil.getCalendarDate2(localDate)
        tv_calendar_lunar.text =
            "${calendarDate.nN().lunar.nN().lunarMonthStr}${calendarDate.nN().lunar.nN().lunarDayStr}"
        if (festivals.isEmpty()) {
            tv_calendar_gz.visibility = View.GONE
        } else {
            tv_calendar_gz.visibility = View.VISIBLE
            tv_calendar_gz.text = festivals
        }
        tv_calendar_date.text = "${calendarDate.nN().date + " " + calendarDate.nN().GZ}"
        tv_solar_term.isVisible(!calendarDate.nN().solarTerm.isNullOrEmpty())
        tv_solar_term.text = calendarDate.nN().solarTerm

        MainScope().async(Dispatchers.Main) {
            var bean = withContext(Dispatchers.IO) {
                CalendarHelper.getYIJI(localDate)
            }
            ll_yiji.isVisible(isControlShow(getAppControl().swtich_all.listIndex(0).yelentrance))

            tv_ji.text = bean.nN().ji.isStr("无")
            tv_yi.text = bean.nN().yi.isStr("无")
        }

    }


    private var onAttachStateChangeListener: View.OnAttachStateChangeListener =
        object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(view: View) {
                if (view is MonthCalendar) { //以反射的形式处理 recyclerview + viewepager时 viewpager 被销毁重新进入window时 mFirstLayout 为true 导致的
//viewpager第一次滑动失效的问题
                    try {
                        val superclass: Class<*>? =
                            view.javaClass.superclass!!.superclass
                        val field =
                            superclass!!.getDeclaredField("mFirstLayout")
                        field.isAccessible = true
                        field.setBoolean(view, false)
                        view.post { view.jumpDate(getAppModel().localDate.value) }

                    } catch (e: Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onViewDetachedFromWindow(view: View) {}
        }
}