package com.zhangsheng.shunxin.calendar.activity


import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.lifecycle.lifecycleScope
import com.maiya.thirdlibrary.base.AacActivity
import com.maiya.thirdlibrary.ext.inflate
import com.maiya.thirdlibrary.ext.isVisible
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.ext.setSingleClickListener
import com.maiya.thirdlibrary.utils.StatusBarUtil
import com.necer.utils.CalendarUtil
import com.zhangsheng.shunxin.ad.AdConstant
import com.zhangsheng.shunxin.calendar.model.AlmanacModel
import com.zhangsheng.shunxin.calendar.util.CalendarDataUtils
import com.zhangsheng.shunxin.calendar.util.CalendarHelper
import com.zhangsheng.shunxin.calendar.wegdit.ButtomTimeSelect
import com.zhangsheng.shunxin.databinding.ActivityAlmanacBinding
import com.zhangsheng.shunxin.weather.ext.getAppModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import org.joda.time.LocalDate
import org.koin.android.ext.android.inject
import java.util.*

/**
 * @Description:
 * @Author:         lhy
 * @CreateDate:     2020/4/22 20:42
 */
class AlmanacActivity : AacActivity<AlmanacModel>() {
    override val vm: AlmanacModel by inject()
    override val binding by inflate<ActivityAlmanacBinding>()
    private val viewModel = vm


    override fun initView(savedInstanceState: Bundle?) {
        binding.calendarStatusBar.let {
            it.layoutParams.height = StatusBarUtil.getStatusBarHeight(this)
        }
        binding.almanacTitle.almanacBack.isVisible(true)
    }

    override fun onResume() {
        super.onResume()
//        XMLogAgent.onPageStart(EnumType.上报埋点.日历底部栏黄历)
    }

    override fun onPause() {
        super.onPause()
//        XMLogAgent.onPageEnd(EnumType.上报埋点.日历底部栏黄历)
    }

    override fun initListener() {
        binding.buttonDateUp.setOnClickListener {
            var localDate = getAppModel().localDate.value
            val minusDays = localDate.nN().minusDays(1)
            if (minusDays.year < 1901) {
                return@setOnClickListener
            }
            getAppModel().localDate.value = minusDays
        }
        binding.buttonDateDown.setOnClickListener {
            var localDate = getAppModel().localDate.value
            val plusDays = localDate.nN().plusDays(1)
            if (plusDays.year > 2099) {
                return@setOnClickListener
            }
            getAppModel().localDate.value = plusDays
        }

        binding.almanacTitle.flAlmanacJumpToday.setSingleClickListener {
            getAppModel().localDate.value = LocalDate()
        }
        binding.almanacTitle.almanacBack.setSingleClickListener {
            finish()
        }

        binding.almanacTitle.tvAlmanacTitle.setSingleClickListener {
            val value = getAppModel().localDate.value
            var calendar = Calendar.getInstance()
            calendar.time = value?.toDate()
            showInDialog(calendar, listener)
        }
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun initObserve() {
        if (null == getAppModel().localDate.value) getAppModel().localDate.value = LocalDate()
        getAppModel().localDate.safeObserve(this, {
            Log.w("lpb", "AlmancActivity  initObserve---" + it?.toString("yyyy年M月d日"))
            binding.almanacTitle.tvAlmanacTitle.text = it?.toString("yyyy年M月d日")
            binding.almanacTitle.flAlmanacJumpToday.isVisible(!CalendarUtil.isToday(it))
            setDate(it.nN())
            var solarTerm = CalendarUtil.getSolarTerm(it)
            binding.tvAlmanacSolarTerm.isVisible(!solarTerm.nN().isNullOrEmpty())
            binding.tvAlmanacSolarTerm.text = solarTerm.nN()
        })

    }

    private fun setDate(localDate: LocalDate) {
        setTitleAndYIJI(localDate)
        setAlmanacData(localDate)
        setJIXIONG(localDate)
        setHS(localDate)
        binding.advMaterialView.showFeedAd(mActivity, AdConstant.SLOT_BIGHUANGLI)
    }

    private fun setTitleAndYIJI(localDate: LocalDate) {
        var calendarDate = CalendarUtil.getCalendarDate2(localDate)
        binding.tvAlmanacLunar.text =
            calendarDate.nN().lunar.nN().lunarMonthStr + calendarDate.nN().lunar.nN().lunarDayStr
        binding.tvAlmanacLunarCn.text = calendarDate.GZ

        lifecycleScope.launch(Dispatchers.Main) {
            val yiji = withContext(Dispatchers.IO) {
                CalendarHelper.getYIJI(localDate)
            }
            setYIJI(binding.tvAlmanacYi, yiji.yi)
            setYIJI(binding.tvAlmanacJi, yiji.ji)
        }
    }

    private fun setYIJI(textView: TextView, data: String?) {
        textView.text = if (TextUtils.isEmpty(data)) {
            "无"
        } else {
            data?.replace(" ", "  ")
        }
    }

    /**
     * 设置伏九天数据
     */
    private fun setHS(localDate: LocalDate) {
        val hanshu = CalendarDataUtils.getHS(localDate.toDate())
        if (TextUtils.isEmpty(hanshu)) {
            isVisible(false, binding.vAlmanacHs, binding.tvAlmanacHs)
        } else {
            isVisible(true, binding.vAlmanacHs, binding.tvAlmanacHs)
            binding.tvAlmanacHs.text = hanshu
        }

    }


    private fun setAlmanacData(localDate: LocalDate) {
        lifecycleScope.async(Dispatchers.Main) {
            val almanacDate = withContext(Dispatchers.IO) {
                viewModel.querySAByDay(localDate)
            }
            binding.almanacItem.tvAlmanacWx.text = almanacDate.wx
            binding.almanacItem.tvAlmanacCs.text = almanacDate.cs
            binding.almanacItem.tvAlmanacZs.text = almanacDate.zhishen
            binding.almanacItem.tvAlmanac12shen.text = almanacDate.jianchu

            binding.almanacItem.tvAlmanacJsyq.text = almanacDate.jsyq
            binding.almanacItem.tvAlmanacJrts.text = almanacDate.taishen
            binding.almanacItem.tvAlmanacXsyj.text = almanacDate.xsyj
            binding.almanacItem.tvAlmanac28star.text = almanacDate.stars28
            binding.almanacItem.tvAlmanacPzbj.text = almanacDate.pzbj
        }
    }

    var hourIndex = -1;
    private fun setJIXIONG(localDate: LocalDate) {
        getNowCNhourIndex(localDate)
        val auspiciousOfChineseHour =
            CalendarDataUtils.get12hourJX(localDate).toMutableList()
        auspiciousOfChineseHour.nN().forEachIndexed { index, str ->
            if (index < 0 || index > 12) {
                return@forEachIndexed
            }
            val lunarHour: Int = CalendarDataUtils.getLumarHourIndex(2 * index)
            val huorStr: String =
                CalendarDataUtils.getStemsBranchHourAsString(localDate.nN().toDate(), lunarHour)
            var hourTextView = binding.almanacItem.llHourJxCn.getChildAt(index + 1) as TextView;
            hourTextView.text = huorStr + str
            setTextColor(index, hourTextView)
        }
    }

    private fun getNowCNhourIndex(localDate: LocalDate) {
        hourIndex = -1;
        if (CalendarUtil.isToday(localDate)) {
            //如果是今天 设置 当前时辰文字为红色 否则 全部颜色设置为黑色
            var calendar = Calendar.getInstance()
            var hour = calendar.get(Calendar.HOUR_OF_DAY)
            if (hour % 2 == 1 && hour < 23) {
                hour++
            }
            hourIndex = if (hour == 24) {
                0
            } else if (hour == 23) {
                -1
            } else {
                hour / 2
            }
        }
    }

    fun setTextColor(index: Int, view: TextView) {
        if (index == hourIndex) {
            view.setTextColor(Color.parseColor("#D13F3F"))
        } else {
            view.setTextColor(Color.parseColor("#333333"))
        }
    }

    var mDialogTimeSelect: ButtomTimeSelect? = null
    private fun showInDialog(
        calendar: Calendar,
        listener: ButtomTimeSelect.TimeSelectedOnclickListener
    ) {
        if (mDialogTimeSelect == null) {
            mDialogTimeSelect =
                ButtomTimeSelect(this)
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
                var localDate = LocalDate.fromDateFields(time)
                getAppModel().localDate.value = localDate
            }

            override fun onFinish() {

            }

            override fun onLunarSelect(isLunar: Boolean) {

            }
        }
}