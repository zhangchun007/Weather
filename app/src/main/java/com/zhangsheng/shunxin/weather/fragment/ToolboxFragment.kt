package com.zhangsheng.shunxin.weather.fragment

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.GridLayoutManager
import com.maiya.thirdlibrary.base.AacFragment
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.utils.CacheUtil
import com.maiya.thirdlibrary.utils.PicUtils
import com.maiya.thirdlibrary.utils.StatusBarUtil.getStatusBarHeight
import com.xm.xmlog.XMLogAgent
import com.zhangsheng.shunxin.BuildConfig
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.ad.AdConstant
import com.zhangsheng.shunxin.databinding.FragmentToolboxBinding
import com.zhangsheng.shunxin.weather.activity.*
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.dialog.ContactUsDialog
import com.zhangsheng.shunxin.weather.dialog.FeedBackInfoDialog
import com.zhangsheng.shunxin.weather.ext.*
import com.zhangsheng.shunxin.weather.model.SettingViewModel
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import kotlinx.android.synthetic.main.title_bar.view.*
import org.koin.android.ext.android.inject

/**
 * 说明：实用工具页
 * 作者：刘鹏
 * 添加时间：5/8/21 2:14 PM
 * 修改人：liupe
 * 修改时间：5/8/21 2:14 PM
 */
class ToolboxFragment :
    AacFragment<SettingViewModel, FragmentToolboxBinding>(R.layout.fragment_toolbox) {

    override fun injectBinding(inflater: LayoutInflater, viewGroup: ViewGroup?): FragmentToolboxBinding {
        return FragmentToolboxBinding.inflate(inflater,viewGroup,false)
    }

    override val vm: SettingViewModel by inject()
    private var isBack = false
    private var isAdLoaded = false
    private var isShow = false
    private val adapter by lazy { ToolBoxAdapter() }
    override fun initView() {
        initTitle()
        binding.tvVersionName.text = "V${BuildConfig.VERSION_NAME}"

        binding.rvToolbox.layoutManager = GridLayoutManager(context, 3)
        adapter.setData(vm.initToolBox())
        adapter.setOnItemClickListener(object : ToolBoxAdapter.OnItemClickListener {
            override fun onItemClick(item: ToolItemModel) {
                when (item.type) {
                    ToolItemModel.TYPE_AIR_LIVE -> {
                        skipActivity(AirMapActivity::class.java)
                        CacheUtil.put(vm.KEY_AIR_REDPOINT_CLICKED, true)
                        item.showRed = false
                        adapter.notifyDataSetChanged()
                        clickReport(EnumType.上报埋点.实用tab空气实况)
                    }
                    ToolItemModel.TYPE_TYPHOON -> {
                        skipActivity(TyphoonActivity::class.java)
                        CacheUtil.put(vm.KEY_TYPHOON_REDPOINT_CLICKED, true)
                        item.showRed = false
                        adapter.notifyDataSetChanged()
                        clickReport(EnumType.上报埋点.实用tab台风)
                    }
                    ToolItemModel.TYPE_RAIN -> {
                        skipActivity(WeatherMapActivity::class.java) {
                            this.putExtra("isFromTab", true)
                        }
                        clickReport(EnumType.上报埋点.实用tab分钟级降雨地图)
                    }
                    ToolItemModel.TYPE_AIR_RANK -> {
                        skipActivity(AirRankActivity::class.java) {
                            putExtra("code", WeatherUtils.getCity().regionCode)
                        }
                        clickReport(EnumType.上报埋点.实用tab空气质量城市排行)
                    }
                    ToolItemModel.TYPE_FIFDAY_WEATHER -> {
                        skipActivity(FifWeatherActivity::class.java) {
                            putExtra("position", 1)
                        }
                        clickReport(EnumType.上报埋点.实用tab15日天气)
                    }
                    ToolItemModel.TYPE_WEATHER_NOTICE -> {
                        skipActivity(PushSettingActivity::class.java)
                        clickReport(EnumType.上报埋点.实用tab早晚天气提醒)
                    }
                }
            }
        })
        binding.rvToolbox.adapter = adapter

    }


    private fun initTitle() {
        binding.title.ll_topbar.setPadding(0, getStatusBarHeight(activity), 0, 0)
        arguments?.let {
            isBack = it.getBoolean("isBack", false)
        }
        if (isBack) {
            loadAd()
        }
        binding.title.back.isVisible(isBack)
        PicUtils.tintColor(binding.title.back, "#222222")
        binding.title.back.setSingleClickListener {
            activity?.finish()
        }
        binding.title.tv_title.text = "实用"

    }

    private fun loadAd() {
        isAdLoaded = true
        binding.advSetting.showFeedAd(activity, AdConstant.SLOT_SMALLSYXT)
    }

    override fun initObserve() {
        super.initObserve()
        getAppModel().pushClientId.safeObserve(this, Observer {
            adapter.setData(vm.initToolBox())
            adapter.notifyDataSetChanged()
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
    }

    override fun onPause() {
        if (isShow) {
            XMLogAgent.onPageEnd(EnumType.上报埋点.设置详情页)
            isShow = false
        }
        super.onPause()
    }


    override fun onDestroy() {
        super.onDestroy()
        clearBinding()

    }


}