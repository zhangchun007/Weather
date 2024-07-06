package com.zhangsheng.shunxin.weather.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import com.maiya.thirdlibrary.base.AacFragment
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.bindView
import com.xm.xmlog.XMLogAgent
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.calendar.fragment.CalendarFragment
import com.zhangsheng.shunxin.databinding.FragmentCalendarWeatherBinding
import com.zhangsheng.shunxin.weather.common.EnumType
import org.koin.android.ext.android.inject

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/8/28 16:07
 */
class CalendarFragment : AacFragment<BaseViewModel,FragmentCalendarWeatherBinding>(R.layout.fragment_calendar_weather) {
    override val vm: BaseViewModel by inject()
    private var isInit = false
    private var calendar = CalendarFragment()

    override fun initView() {

    }

    override fun onShow(action: Int) {
        super.onShow(action)
//        XMLogAgent.onPageStart(EnumType.上报埋点.日历底部栏万年历)
    }

    override fun onHidden() {
        super.onHidden()
//        XMLogAgent.onPageEnd(EnumType.上报埋点.日历底部栏万年历)
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            if (!isInit) {
                isInit = true
                activity?.supportFragmentManager?.beginTransaction()
                    ?.replace(binding.calendarFrame.id, calendar)
                    ?.commitAllowingStateLoss()
            }else{
                calendar.loadAd()
            }
        }

    }

    override fun injectBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentCalendarWeatherBinding =FragmentCalendarWeatherBinding.inflate(inflater,viewGroup,false)

    override fun onDestroy() {
        super.onDestroy()
        clearBinding()
    }
}