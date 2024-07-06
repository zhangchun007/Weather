package com.zhangsheng.shunxin.weather.activity

import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.text.TextUtils
import android.util.Log
import android.view.View
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.maiya.thirdlibrary.base.AacActivity
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.inflate
import com.maiya.thirdlibrary.ext.isStr
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.ad.AdConstant
import com.zhangsheng.shunxin.databinding.ActivityFortyWeatherBinding
import com.zhangsheng.shunxin.information.bean.FortyCalendarInfo
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.clickReport
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.ext.showReport
import com.zhangsheng.shunxin.weather.fragment.FortyCalendarFragment
import com.zhangsheng.shunxin.weather.model.ForthWeatherModel
import com.zhangsheng.shunxin.weather.utils.ForthCalendarUtils
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import org.koin.android.ext.android.inject

/**
 * @Description:    40日天气activity
 * @Author:         zhangchun
 * @CreateDate:     2021/7/20
 * @Version:        1.0
 */
class FortyWeatherActivity : AacActivity<ForthWeatherModel>() {
    override val vm: ForthWeatherModel by inject()
    override val binding by inflate<ActivityFortyWeatherBinding>()
    private var fragmentAdapter: FragmentStatePagerAdapter? = null
    private var dataList: ArrayList<String>? = null
    private var isBackTodaySelect = false//返回按钮是否选中

    private var isLocation = false
    private var regionname = ""
    private var regioncode = ""
    private val TAG = "FortyWeatherActivity"

    private var currentMonthData: MutableList<FortyCalendarInfo>? = null
    private var nextMonthData: MutableList<FortyCalendarInfo>? = null
    private var thirdMonthData: MutableList<FortyCalendarInfo>? = null

    //今日日历信息
    private var toDataItemInfo: FortyCalendarInfo? = null

    private fun initTitle() {
        Try {
            regioncode = intent.getStringExtra("code").isStr("")
            isLocation = intent.getBooleanExtra("location", false)
            regionname = intent.getStringExtra("name").isStr("")
            Log.e(TAG, "regioncode==$regioncode--regionname=-$regionname")
        }
        if (isLocation) binding.title.setIcon(R.mipmap.icon_location)
        binding.title.initTitle(regionname, "#ffffff")
        binding.title.setTitleBgColor("#00ffffff")

        showReport(EnumType.上报埋点._40日天气二级页展示)
    }

    override fun initView(savedInstanceState: Bundle?) {
        //设置标题
        initTitle()
        //请求数据
        vm.requestFortyDaysWeatherInfo(regioncode, regionname)
        //设置日期
        var dateTime = ForthCalendarUtils.getCurrentYear()
            .toString() + "年" + ForthCalendarUtils.getCurrentMonth() + "月"
        binding.fortyCalendar.tvDate.text = dateTime

        //判断当前月份从今天开始+下个月份是否超过40天，如果超过只有两页数据，如果没有超过就有三页数据
        if (ForthCalendarUtils.needThreeFragment()) {//超过三页面
            dataList = arrayListOf("当月", "下个月", "下下个月")
            binding.fortyCalendar.imgDotThree.visibility = View.VISIBLE
        } else {
            dataList = arrayListOf("当月", "下个月")
            binding.fortyCalendar.imgDotThree.visibility = View.GONE
        }
        binding.fortyCalendar.vpCalender.adapter = getAdapter()

        binding.fortyCalendar.vpCalender.addOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                // 切换到当前页面，重置高度
                setSelectState(position)
                binding.fortyCalendar.vpCalender.requestLayout()
            }

        })

        binding.fortyCalendar.tvDate.text = dateTime

        binding.fortyCalendar.tvBackToday.setOnClickListener {
            if (isBackTodaySelect) {
                dataList?.forEachIndexed { index, s ->
                    getCurFragment(index)?.resetState()
                }
                binding.fortyCalendar.vpCalender.setCurrentItem(0, false)
                getCurFragment(0)?.setTodaySelect()
                changeBackTodayButtonState(false)
                setCurrentWeatherInfo(toDataItemInfo)
            }
        }

    }

    /**
     * 设置选中状态
     */
    private fun setSelectState(position: Int) {
        var dateTime = ""
        if (position == 0) {
            //设置日期
            dateTime = ForthCalendarUtils.getCurrentYear()
                .toString() + "年" + ForthCalendarUtils.getCurrentMonth() + "月"
            //设置图片选中状态
            binding.fortyCalendar.imgDotOne.setImageResource(R.mipmap.img_calendar_dot_select)
            binding.fortyCalendar.imgDotTwo.setImageResource(R.mipmap.img_calendar_dot_unselect)
            if (ForthCalendarUtils.needThreeFragment()) {
                binding.fortyCalendar.imgDotThree.visibility = View.VISIBLE
                binding.fortyCalendar.imgDotThree.setImageResource(R.mipmap.img_calendar_dot_unselect)
            }
            binding.fortyCalendar.imgArrowLeft.visibility = View.GONE
            binding.fortyCalendar.imgArrowRight.visibility = View.VISIBLE

            getCurFragment(position)?.setData(currentMonthData!!)
        } else if (position == 1) {
            //设置日期
            dateTime = ForthCalendarUtils.getNextMonthYear()
                .toString() + "年" + ForthCalendarUtils.getNextMonth() + "月"
            //设置图片选中状态
            binding.fortyCalendar.imgDotOne.setImageResource(R.mipmap.img_calendar_dot_unselect)
            binding.fortyCalendar.imgDotTwo.setImageResource(R.mipmap.img_calendar_dot_select)
            if (ForthCalendarUtils.needThreeFragment()) {
                binding.fortyCalendar.imgDotThree.visibility = View.VISIBLE
                binding.fortyCalendar.imgArrowRight.visibility = View.VISIBLE
                binding.fortyCalendar.imgDotThree.setImageResource(R.mipmap.img_calendar_dot_unselect)
            } else {
                binding.fortyCalendar.imgArrowRight.visibility = View.GONE
            }
            binding.fortyCalendar.imgArrowLeft.visibility = View.VISIBLE

            getCurFragment(position)?.setData(nextMonthData!!)
        } else if (position == 2) {
            //设置日期
            dateTime = ForthCalendarUtils.getThirdMonthYear()
                .toString() + "年" + ForthCalendarUtils.getThirdMonth() + "月"
            //设置图片选中状态
            binding.fortyCalendar.imgDotOne.setImageResource(R.mipmap.img_calendar_dot_unselect)
            binding.fortyCalendar.imgDotTwo.setImageResource(R.mipmap.img_calendar_dot_unselect)
            binding.fortyCalendar.imgDotThree.visibility = View.VISIBLE
            binding.fortyCalendar.imgDotThree.setImageResource(R.mipmap.img_calendar_dot_select)

            binding.fortyCalendar.imgArrowLeft.visibility = View.VISIBLE
            binding.fortyCalendar.imgArrowRight.visibility = View.GONE
            getCurFragment(position)?.setData(thirdMonthData!!)
        }
        binding.fortyCalendar.tvDate.text = dateTime
    }

    override fun initObserve() {
        super.initObserve()
        getAppModel().fortyWeatherBean.safeObserve(this, Observer {
            if (it != null) {
                binding.rainfallTrendView.setData(it)
                binding.temperatureTrendView.setData(it)
                //获取今天的开始位置
                var startCurrentMonthIndex = getCurrentMothWeatherStartIndex()
                //获取下个月第一个有内容的index
                var startNextMonthIndex = getNextMonthWeatherStartIndex()
                var startThirdMonthIndex = getThirdMonthWeatherStartIndex()
                //当月数据
                currentMonthData = ForthCalendarUtils.getCurrentMonthData()
                //次月数据
                nextMonthData = ForthCalendarUtils.getNextMonthData()
                //第三个月
                thirdMonthData = ForthCalendarUtils.getThirdMonthData()
                if (currentMonthData == null || nextMonthData == null || thirdMonthData == null) return@Observer
                it.ybds?.forEachIndexed { index, ybdsBean ->
                    if (startCurrentMonthIndex != -1 && startCurrentMonthIndex < currentMonthData?.size!!) {
                        currentMonthData!![startCurrentMonthIndex].apply { //组装当月数据
                            this.fct = ybdsBean.fct
                            this.tcd = ybdsBean.tcd.toString()
                            this.tcn = ybdsBean.tcn.toString()
                            this.wdir = ybdsBean.wdir
                            this.wslv = ybdsBean.wslv
                            this.wt = ybdsBean.wt
                            this.wtid = ybdsBean.wtid
                            this.wt1 = ybdsBean.wt1
                            this.wt1id = ybdsBean.wt1id
                            this.wt2 = ybdsBean.wt2
                            this.wt2id = ybdsBean.wt2id
                        }
                        startCurrentMonthIndex++
                    } else if (startNextMonthIndex != -1 && startNextMonthIndex < nextMonthData?.size!!) {
                        nextMonthData!![startNextMonthIndex].apply { //组装次月数据
                            this.fct = ybdsBean.fct
                            this.tcd = ybdsBean.tcd.toString()
                            this.tcn = ybdsBean.tcn.toString()
                            this.wdir = ybdsBean.wdir
                            this.wslv = ybdsBean.wslv
                            this.wt = ybdsBean.wt
                            this.wtid = ybdsBean.wtid
                            this.wt1 = ybdsBean.wt1
                            this.wt1id = ybdsBean.wt1id
                            this.wt2 = ybdsBean.wt2
                            this.wt2id = ybdsBean.wt2id
                        }
                        startNextMonthIndex++
                    } else if (startThirdMonthIndex != -1 && startThirdMonthIndex < thirdMonthData?.size!!) {
                        thirdMonthData!![startThirdMonthIndex].apply { //组装第三个月数据
                            this.fct = ybdsBean.fct
                            this.tcd = ybdsBean.tcd.toString()
                            this.tcn = ybdsBean.tcn.toString()
                            this.wdir = ybdsBean.wdir
                            this.wslv = ybdsBean.wslv
                            this.wt = ybdsBean.wt
                            this.wtid = ybdsBean.wtid
                            this.wt1 = ybdsBean.wt1
                            this.wt1id = ybdsBean.wt1id
                            this.wt2 = ybdsBean.wt2
                            this.wt2id = ybdsBean.wt2id
                        }
                        startThirdMonthIndex++
                    }

                }

                getCurFragment(0)?.setData(currentMonthData!!)
                getCurFragment(1)?.setData(nextMonthData!!)
//                if (ForthCalendarUtils.needThreeFragment()) {
//                    getCurFragment(2)?.setData(thirdMonthData!!)
//                }

                //服务端数据返回后，设置今天天气
                setCurrentWeatherInfo(currentMonthData!![getCurrentMothWeatherStartIndex()])
                toDataItemInfo = currentMonthData!![getCurrentMothWeatherStartIndex()]
            }
        })
    }

    /**
     * 当前月份表示今天position
     */
    private fun getCurrentMothWeatherStartIndex(): Int {
        var startIndex = -1
        run outside@{
            ForthCalendarUtils.getCurrentMonthData().forEachIndexed { index, fortyCalendarInfo ->
                if (fortyCalendarInfo.isToday) {
                    startIndex = index
                    return@outside
                }
            }
        }
        return startIndex
    }

    /**
     * 下个月份第一天有内容的position
     */
    private fun getNextMonthWeatherStartIndex(): Int {
        var startIndex = -1
        run outside@{
            ForthCalendarUtils.getNextMonthData().forEachIndexed { index, fortyCalendarInfo ->
                if (!TextUtils.isEmpty(fortyCalendarInfo.yinli)) {
                    startIndex = index
                    return@outside
                }
            }
        }
        return startIndex
    }


    /**
     * 第三个月份第一天有内容的position
     */
    private fun getThirdMonthWeatherStartIndex(): Int {
        var startIndex = -1
        run outside@{
            ForthCalendarUtils.getThirdMonthData().forEachIndexed { index, fortyCalendarInfo ->
                if (!TextUtils.isEmpty(fortyCalendarInfo.yinli)) {
                    startIndex = index
                    return@outside
                }
            }
        }
        return startIndex
    }

    /**
     * 将另外一个fragment选中状态取消
     * @param position 当前fragment日历点击的position
     * @param bean 当前日历item数据
     */
    fun clearOtherFragmentSelectState(position: Int, bean: FortyCalendarInfo) {
        if (binding.fortyCalendar.vpCalender.currentItem == 0) {
            getCurFragment(1)?.resetState()
            if (ForthCalendarUtils.needThreeFragment()) {
                getCurFragment(2)?.resetState()
            }
            if (ForthCalendarUtils.getCurrentMonthData()[position].isToday) {
                changeBackTodayButtonState(false)
            } else {
                changeBackTodayButtonState(true)
            }
        } else if (binding.fortyCalendar.vpCalender.currentItem == 1) {
            getCurFragment(0)?.resetState()
            if (ForthCalendarUtils.needThreeFragment()) {
                getCurFragment(2)?.resetState()
            }
            changeBackTodayButtonState(true)
        } else if (binding.fortyCalendar.vpCalender.currentItem == 2) {
            getCurFragment(0)?.resetState()
            getCurFragment(1)?.resetState()
            changeBackTodayButtonState(true)
        }

        //设置当前天气信息
        setCurrentWeatherInfo(bean)

    }

    /**
     * 设置返回到今天按钮的颜色
     * @param select
     */
    private fun changeBackTodayButtonState(select: Boolean) {
        if (select) {
            binding.fortyCalendar.tvBackToday.setBackgroundResource(R.drawable.back_today_select_bg)
            binding.fortyCalendar.tvBackToday.setTextColor(Color.parseColor("#ffffff"))
        } else {
            binding.fortyCalendar.tvBackToday.setBackgroundResource(R.drawable.back_today_unselect_bg)
            binding.fortyCalendar.tvBackToday.setTextColor(Color.parseColor("#AAAAAA"))
        }
        isBackTodaySelect = select

    }

    /**
     * 设置天气数据
     */
    private fun setCurrentWeatherInfo(info: FortyCalendarInfo?) {
        if (info != null) {
            binding.tvDates.text = "${info.month}" + "月" + info.yinli + "日"
            binding.tvWeek.text = ForthCalendarUtils.getWeek(info.fct)
            binding.imgWeather.setImageResource(WeatherUtils.IconBig(info.wtid))
            binding.tvWeatherNum.text = info.tcd + "°/ " + info.tcn + "°"
            binding.tvWeather.text = info.wt
            if (!TextUtils.isEmpty(info.wdir)) {
                binding.tvWoond.visibility = View.VISIBLE
                binding.tvWoond.text = info.wdir
                binding.tvWoondNum.text = info.wslv
            } else {
                binding.tvWoond.visibility = View.GONE
            }
            clickReport(EnumType.上报埋点.日历天气切换)
        }
    }

    /**
     * 获取当前fragment
     */
    private fun getCurFragment(pageIndex: Int): FortyCalendarFragment? {
        try {
            val fragment =
                getAdapter().instantiateItem(
                    binding.fortyCalendar.vpCalender,
                    pageIndex
                ) as FortyCalendarFragment
            if (fragment.isAdded) {
                return fragment
            }
        } catch (e: Exception) {
        }
        return null
    }

    private fun getAdapter(): FragmentStatePagerAdapter {
        if (fragmentAdapter == null) {
            fragmentAdapter = object : FragmentStatePagerAdapter(
                supportFragmentManager,
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            ) {
                override fun getCount(): Int = dataList?.size!!

                override fun getItem(position: Int): Fragment = FortyCalendarFragment().apply {
                    arguments = Bundle().apply {
                        putInt("position", position)
                    }
                }

                override fun getItemPosition(fragment: Any): Int = PagerAdapter.POSITION_UNCHANGED

                override fun saveState(): Parcelable? {
                    return null
                }
            }
        }
        return fragmentAdapter!!
    }

    override fun onDestroy() {
        getAppModel().fortyWeatherBean.value = null
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()
        loadAd()
    }

    private fun loadAd() {
        binding.advMaterialView.showFeedAd(this, AdConstant.SLOT_PREDICT40DAYS)
    }

}