package com.zhangsheng.shunxin.calendar.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.maiya.thirdlibrary.base.AacFragment
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.utils.StatusBarUtil
import com.necer.calendar.MonthCalendar
import com.necer.utils.CalendarUtil
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.ad.AdConstant
import com.zhangsheng.shunxin.calendar.model.CalendarModel
import com.zhangsheng.shunxin.calendar.wegdit.ButtomTimeSelect
import com.zhangsheng.shunxin.databinding.FragmentCalendarBinding
import com.zhangsheng.shunxin.weather.ext.getAppModel
import org.joda.time.LocalDate
import org.koin.android.ext.android.inject
import java.util.*


/**
 * @Description:
 * @Author:         lhy
 * @CreateDate:     2020/4/20 13:05
 */
class CalendarFragment : AacFragment<CalendarModel,FragmentCalendarBinding>(R.layout.fragment_calendar) {


    override val vm: CalendarModel by inject()

    private val viewModel = vm

    private var isBack = false

    override fun initView() {

        arguments?.let {
            isBack = it.getBoolean("isBack", false)
        }

        binding.calendarToolbar.back.isVisible(isBack)

        binding.calendarStatusBar.post {
            Try {
                binding.calendarStatusBar.layoutParams.height =
                    StatusBarUtil.getStatusBarHeight(activity)
            }
        }
        initData()
        getAppModel().localDate.value = LocalDate()
    }

    override fun onResume() {
        super.onResume()
        if (userVisibleHint) {
            loadAd()
            binding.calendarView.post {
                Try {
                    binding.calendarView.findViewById<MonthCalendar>(R.id.mc_calendar)
                        ?.jumpDate(getAppModel().localDate.value)
                }
            }
        }
    }


    override fun initListener() {
        binding.calendarToolbar.flJumpToday.setSingleClickListener {
            getAppModel().localDate.value = LocalDate()
            binding.calendarView.findViewById<MonthCalendar>(R.id.mc_calendar)?.toToday()
        }

        binding.calendarToolbar.tvCalendarTitle.setSingleClickListener {
            val value = getAppModel().localDate.value
            val calendar = Calendar.getInstance()
            calendar.time = value?.toDate()
            showTimeSelect(calendar, listener)
        }

        binding.calendarToolbar.back.setSingleClickListener {
            activity?.finish()
        }

        viewModel.setParseHolidayData { arrayList, arrayList2 ->
            setHolidayData(arrayList, arrayList2)
        }
    }

    private fun initData() {
        getAppModel().localDate.safeObserve(this, androidx.lifecycle.Observer {
            binding.calendarToolbar.tvCalendarTitle.text = it?.toString("yyyy年M月")
            binding.calendarToolbar.flJumpToday.isVisible(!CalendarUtil.isToday(it))
            /**
             * 这里要进行选择年处理 如果选择的两个日期是同一年 就不进行调用
             * 因为这里会同时调用多次
             */
            if ("${it.nN().year}" != getAppModel().selectYear) {
                getAppModel().selectYear = "${it.nN().year}"
                viewModel.requestWithoutWorkDay(it.nN())
            }
        })
    }

    fun loadAd() {
        binding.advCalendar.showFeedAd(activity, AdConstant.SLOT_BIGRLBOTTOM)
    }


    /**
     * 显示时间选择器
     */
    private var mDialogTimeSelect: ButtomTimeSelect? = null

    private fun showTimeSelect(
        calendar: Calendar,
        listener: ButtomTimeSelect.TimeSelectedOnclickListener
    ) {
        if (mDialogTimeSelect == null) {
            mDialogTimeSelect =
                ButtomTimeSelect(context)
        }
        if (mDialogTimeSelect.nN().isShowing) {
            mDialogTimeSelect?.dismiss()
        } else {
            mDialogTimeSelect?.setCancelable(true)
            mDialogTimeSelect?.setCanceledOnTouchOutside(true)
            mDialogTimeSelect?.show()
            mDialogTimeSelect?.initCalendar(calendar)
            mDialogTimeSelect?.setTimeSelectedOnclickListener(listener)
        }
    }


    /**
     * 开始日期现在回调
     */
    private val listener: ButtomTimeSelect.TimeSelectedOnclickListener =
        object : ButtomTimeSelect.TimeSelectedOnclickListener {
            override fun onAffirm(calendarData: Calendar) {
                val time = calendarData.time
                val localDate = LocalDate.fromDateFields(time)
                getAppModel().localDate.value = localDate
                binding.calendarView
                    ?.findViewById<MonthCalendar>(R.id.mc_calendar)
                    ?.jumpDate(getAppModel().localDate.value)
            }

            override fun onFinish() {

            }

            override fun onLunarSelect(isLunar: Boolean) {
            }
        }

    /**
     * 设置假期
     */
    private fun setHolidayData(holidayDay: ArrayList<String>, workDay: ArrayList<String>) {
        binding.calendarView?.findViewById<MonthCalendar>(R.id.mc_calendar)
            ?.calendarPainter?.setLegalHolidayList(holidayDay, workDay)
    }

    override fun injectBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentCalendarBinding=FragmentCalendarBinding.inflate(inflater,viewGroup,false)

    override fun onDestroy() {
        super.onDestroy()
        clearBinding()
    }

}