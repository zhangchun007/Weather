package com.zhangsheng.shunxin.calendar.activity

import android.os.Bundle
import com.maiya.thirdlibrary.base.AacActivity
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.inflate
import com.maiya.thirdlibrary.utils.FragmentUtils
import com.zhangsheng.shunxin.calendar.fragment.CalendarFragment
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.databinding.ActivityCalendarBinding
import org.koin.android.ext.android.inject

class CalendarActivity : AacActivity<BaseViewModel>() {

    override val vm: BaseViewModel by inject()
    override val binding by inflate<ActivityCalendarBinding>()

    override fun initView(savedInstanceState: Bundle?) {
        FragmentUtils.add(
            supportFragmentManager,
            CalendarFragment().apply {
                arguments = Bundle().apply {
                    this.putBoolean("isBack", true)
                }
            },
            binding.fragmentContainer.id
        )
    }

}