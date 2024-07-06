package com.zhangsheng.shunxin.weather.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import com.maiya.thirdlibrary.base.AacFragment
import com.maiya.thirdlibrary.ext.bindView
import com.maiya.thirdlibrary.ext.isVisible
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.ext.setSingleClickListener
import com.maiya.thirdlibrary.utils.PicUtils
import com.maiya.thirdlibrary.utils.StatusBarUtil.getStatusBarHeight
import com.xm.xmlog.XMLogAgent
import com.zhangsheng.shunxin.BuildConfig
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.ad.AdConstant
import com.zhangsheng.shunxin.databinding.FragmentSettingBinding
import com.zhangsheng.shunxin.weather.activity.AboutUsActivity
import com.zhangsheng.shunxin.weather.activity.AdSetActivity
import com.zhangsheng.shunxin.weather.activity.PushSettingActivity
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.dialog.ContactUsDialog
import com.zhangsheng.shunxin.weather.dialog.FeedBackInfoDialog
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.ext.skipActivity
import com.zhangsheng.shunxin.weather.model.SettingViewModel
import kotlinx.android.synthetic.main.title_bar.view.*
import org.koin.android.ext.android.inject

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/8/28 16:07
 */
class SettingFragment : AacFragment<SettingViewModel,FragmentSettingBinding>(R.layout.fragment_setting) {
    override val vm: SettingViewModel by inject()
    private var isBack = false
    private var isShow = false
    override fun initView() {
        initTitle()
        binding.tvVersionName.text = "V${BuildConfig.VERSION_NAME}"
    }

    private fun initTitle() {
        binding.title.ll_topbar.setPadding(0, getStatusBarHeight(activity), 0, 0)
        arguments?.let {
            isBack = it.getBoolean("isBack", false)
        }

        binding.title.back.isVisible(isBack)
        PicUtils.tintColor(binding.title.back, "#222222")
        binding.title.back.setSingleClickListener {
            activity?.finish()
        }
        binding.title.tv_title.text = "设置"

    }

    private fun loadAd() {
        binding.advSetting.showFeedAd(activity, AdConstant.SLOT_BIGSET)
    }

    override fun initObserve() {
        super.initObserve()
        getAppModel().pushClientId.observe(this, Observer {
            binding.relWeatherNotice.isVisible(it.isNotEmpty())
        })
    }


    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            loadAd()
            XMLogAgent.onPageStart(EnumType.上报埋点.设置详情页)
        } else {
            XMLogAgent.onPageEnd(EnumType.上报埋点.设置详情页)
        }
    }

    override fun initListener() {
        super.initListener()
        binding.relWeatherNotice.setSingleClickListener {
            skipActivity(PushSettingActivity::class.java)
        }

        binding.relVersionCheck.setSingleClickListener {
            vm.checkUpDate(activity.nN())
        }

        binding.relReportInfo.setSingleClickListener {
            FeedBackInfoDialog(activity.nN()) {
                vm.日志上报接口("1", it)
            }.show()
        }

        binding.relContactUs.setSingleClickListener {
            ContactUsDialog(activity.nN()).show()
        }

        binding.relAboutUs.setSingleClickListener {
            skipActivity(AboutUsActivity::class.java)
        }

        binding.relAdSet.setSingleClickListener {
            skipActivity(AdSetActivity::class.java)
        }
    }

    override fun onResume() {
        if (isVisible) {
            XMLogAgent.onPageStart(EnumType.上报埋点.设置详情页)
            isShow = true
        }
        super.onResume()
        loadAd()
    }

    override fun onPause() {
        if (isShow) {
            XMLogAgent.onPageEnd(EnumType.上报埋点.设置详情页)
            isShow = false
        }

        super.onPause()
    }

    override fun injectBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentSettingBinding =FragmentSettingBinding.inflate(inflater,viewGroup,false)

    override fun onDestroy() {
        super.onDestroy()
        clearBinding()
    }

}