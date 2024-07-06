package com.zhangsheng.shunxin.weather.activity

import android.os.Bundle
import com.maiya.thirdlibrary.base.AacActivity
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.inflate
import com.maiya.thirdlibrary.utils.FragmentUtils
import com.xm.xmlog.XMLogAgent
import com.zhangsheng.shunxin.databinding.SettingsActivityBinding
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.fragment.SettingFragment
import org.koin.android.ext.android.inject

class SettingsActivity : AacActivity<BaseViewModel>() {
    override val vm: BaseViewModel by inject()
    override val binding by inflate<SettingsActivityBinding>()

    override fun initView(savedInstanceState: Bundle?) {
        FragmentUtils.add(supportFragmentManager, SettingFragment().apply {
            arguments = Bundle().apply {
                putBoolean("isBack", true)
            }
        }, binding.frameSetting.id)
    }

    override fun onResume() {
        super.onResume()
        XMLogAgent.onPageStart(EnumType.上报埋点.设置详情页)
    }

    override fun onDestroy() {
        super.onDestroy()
        XMLogAgent.onPageEnd(EnumType.上报埋点.设置详情页)
    }


}