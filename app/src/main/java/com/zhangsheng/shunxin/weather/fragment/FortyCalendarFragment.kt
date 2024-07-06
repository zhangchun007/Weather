package com.zhangsheng.shunxin.weather.fragment

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.GridLayoutManager
import com.maiya.thirdlibrary.base.AacFragment
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.nN
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.databinding.FragmentFortyCalendarBinding
import com.zhangsheng.shunxin.information.bean.FortyCalendarInfo
import com.zhangsheng.shunxin.weather.activity.FortyWeatherActivity
import com.zhangsheng.shunxin.weather.adapter.FortyCalendarAdapter
import org.koin.android.ext.android.inject

/**
 * @Description:
 * @Author:         zhangchun
 * @CreateDate:     2021/7/20
 * @Version:        1.0
 */
class FortyCalendarFragment :
    AacFragment<BaseViewModel, FragmentFortyCalendarBinding>(R.layout.fragment_forty_calendar) {
    override val vm: BaseViewModel by inject()
    private var type = 0//0表示当月 1表示次月 2表示第三个月分
    private var mAdapter: FortyCalendarAdapter? = null
    private var lastPosition: Int = -1


    override fun initView() {
        Try {
            type = arguments.nN().getInt("position")
        }
        mAdapter = FortyCalendarAdapter()
        binding.rvCalendar.layoutManager = GridLayoutManager(activity, 7)
        binding.rvCalendar.adapter = mAdapter

        //点击时间
        mAdapter?.setOnItemClickListener(object : FortyCalendarAdapter.onItemClickListener {
            override fun onItemSelected(
                position: Int,
                bean: FortyCalendarInfo
            ) {
                Log.e("setOnItemClickListener", "pos==" + position)

                if (lastPosition != position) {
                    if (lastPosition != -1) {
                        mAdapter?.mData?.get(lastPosition)?.isSelect = false
                    }
                    lastPosition = position
                }
                mAdapter?.notifyDataSetChanged()
                getParentActivity()?.clearOtherFragmentSelectState(position, bean)
            }
        })
    }

    /**
     * 设置list数据
     */

    fun setData(list: List<FortyCalendarInfo>) {
        if (list != null) {
            Log.e("setData", "list大小" + list?.size + "--type=" + type)
            mAdapter?.setData(list)
        }
    }


    override fun injectBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentFortyCalendarBinding =
        FragmentFortyCalendarBinding.inflate(inflater, viewGroup, false)

    /**
     * 获取宿主activity
     */
    private fun getParentActivity(): FortyWeatherActivity? {
        if (activity == null || activity !is FortyWeatherActivity) {
            return null
        }
        return activity as FortyWeatherActivity
    }

    /**
     * 重置状态
     */
    fun resetState() {
        if (lastPosition != -1) {
            mAdapter?.mData?.get(lastPosition)?.isSelect = false
            mAdapter?.notifyDataSetChanged()
            lastPosition = -1
        }
    }

    /**
     * 让今天选中
     */
    fun setTodaySelect() {
        mAdapter?.mData?.forEachIndexed { index, fortyCalendarInfo ->
            if (fortyCalendarInfo.isToday) {
                mAdapter?.mData?.get(index)?.isSelect = true
                lastPosition = index
                return@forEachIndexed
            }
        }
        mAdapter?.notifyDataSetChanged()
    }


}