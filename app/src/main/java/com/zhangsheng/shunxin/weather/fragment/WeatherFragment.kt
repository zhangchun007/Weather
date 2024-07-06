//package com.zhangsheng.shunxin.weather.fragment
//
//import android.content.Intent
//import android.graphics.Color
//import android.os.Bundle
//import android.os.Parcelable
//import android.util.Log
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.core.view.isVisible
//
//import androidx.fragment.app.Fragment
//import androidx.fragment.app.FragmentStatePagerAdapter
//import androidx.lifecycle.Observer
//import androidx.viewpager.widget.PagerAdapter
//import androidx.viewpager.widget.ViewPager
//import com.alibaba.idst.util.NlsClient
//import com.alibaba.idst.util.SpeechSynthesizer
//import com.alibaba.idst.util.SpeechSynthesizerCallback
//import com.maiya.thirdlibrary.base.AacFragment
//import com.maiya.thirdlibrary.utils.CacheUtil
//import com.maiya.thirdlibrary.utils.DisplayUtil
//import com.maiya.thirdlibrary.ext.*
//import com.maiya.thirdlibrary.utils.StatusBarUtil.getStatusBarHeight
//import com.maiya.thirdlibrary.widget.smartlayout.listener.SmartRecycleListener
//import com.maiya.thirdlibrary.widget.shapview.ShapeView
//import com.maiya.thirdlibrary.widget.smartlayout.adapter.SmartViewHolder
//import com.zhangsheng.shunxin.R
//import com.zhangsheng.shunxin.weather.MainActivity
//import com.zhangsheng.shunxin.weather.activity.CityListManageActivity
//import com.zhangsheng.shunxin.weather.activity.CitySelectActivity
//import com.zhangsheng.shunxin.weather.activity.SettingsActivity
//import com.zhangsheng.shunxin.weather.common.Constant
//import com.zhangsheng.shunxin.weather.common.EnumType
//import com.zhangsheng.shunxin.weather.model.WeatherViewModel
//import com.zhangsheng.shunxin.weather.net.bean.CurrentWeatherBean
//import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
//import com.zhangsheng.shunxin.weather.utils.AnimationUtil
//import com.zhangsheng.shunxin.weather.utils.LocationUtil
//import com.zhangsheng.shunxin.weather.utils.WeatherUtils
//import com.zhangsheng.shunxin.weather.utils.alispeak.SpeakerUtils
//import com.xm.xmlog.XMLogAgent
//import com.zhangsheng.shunxin.databinding.FragmentWeatherBinding
//import com.zhangsheng.shunxin.information.InformationFragment
//import com.zhangsheng.shunxin.information.utils.JrttPostBackUtils
//import com.zhangsheng.shunxin.weather.dialog.WeatherSpeakDialog
//import com.zhangsheng.shunxin.weather.ext.*
//import com.zhangsheng.shunxin.weather.widget.transformer.ZoomOutPageTransformer
//import org.koin.android.ext.android.inject
//import java.lang.Math.abs
//import java.lang.ref.WeakReference
//
///**
// * @Description:
// * @Author:         Qrh
// * @CreateDate:     2020/7/15 14:43
// */
//class WeatherFragment : AacFragment<WeatherViewModel>(R.layout.fragment_weather),
//    SpeechSynthesizerCallback {
//    private var viewSize = 0
//    private var index = 0
//    private var client: NlsClient? = null
//    private var pageScroll = HashMap<Int, Int>(9)
//    private var speechSynthesizer: SpeechSynthesizer? = null
//    private var synthesizerWeakReference: WeakReference<SpeechSynthesizer>? = null
//    private var speak_error = ""
//    private var tabColor = "#00000000"
//    private var speak_loading: WeatherSpeakDialog? = null
//    private var fragmentAdapter: FragmentStatePagerAdapter? = null
//    private var weatherRefreshTime = 5 * 60 * 1000
//    private var weatherBg: Int = -1
//    private var initScreenLock = true
//
//    override val binding by bindView<FragmentWeatherBinding>()
//    override val vm: WeatherViewModel by inject()
//    override fun initView() {
//        initData()
//        initSpeak()
//        binding.topBar.setPadding(0, getStatusBarHeight(activity), 0, 0)
//
//        binding.tabView.setSmartListener(object : SmartRecycleListener() {
//            override fun AutoAdapter(
//                holder: SmartViewHolder,
//                item: Any,
//                tabPosition: Int
//            ) {
//                super.AutoAdapter(holder, item, tabPosition)
//                var tab = holder.findView<ShapeView>(R.id.tab)
//                tab.isVisible(viewSize > 1)
//                if (index == tabPosition) {
//                    tab.apply {
//                        exeConfig(getConfig().apply { bgColor = Color.parseColor("#ffffff") })
//                    }
//                } else {
//                    tab.apply {
//                        exeConfig(getConfig().apply { bgColor = Color.parseColor("#33ffffff") })
//                    }
//                }
//            }
//        })
//
//        binding.vp.offscreenPageLimit = 8
//        binding.vp.adapter = getAdapter()
//        binding.vp.setPageTransformer(true, ZoomOutPageTransformer())
//
//        binding.vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
//            override fun onPageScrollStateChanged(state: Int) {
//
//            }
//
//            override fun onPageScrolled(
//                position: Int,
//                positionOffset: Float,
//                positionOffsetPixels: Int
//            ) {
//
//            }
//
//            override fun onPageSelected(position: Int) {
//                if (position == 0 && WeatherUtils.getDatas()
//                        .listIndex(0).isLocation && WeatherUtils.getDatas()
//                        .listIndex(0).location.nN().state != LocationUtil.定位成功
//                ) {
//                    if (WeatherUtils.getDatas()
//                            .listIndex(0).location.nN().state == LocationUtil.定位权限
//                    ) {
//                        getAppModel().refreshAction.value = EnumType.刷新类型.定位权限
//                    } else {
//                        getAppModel().refreshAction.value = EnumType.刷新类型.定位失败
//                    }
//                } else {
//                    if (getAppModel().refreshAction.value == EnumType.刷新类型.定位失败 || getAppModel().refreshAction.value == EnumType.刷新类型.定位权限) {
//                        getAppModel().refreshAction.value = EnumType.刷新类型.初始状态
//                    }
//                }
//                if (index != position) {
//                    stopSpeak()
//                    index = position
//
//                    if (abs(System.currentTimeMillis() - WeatherUtils.getWeatherBean(index).refreshTime) > weatherRefreshTime || WeatherUtils.getWeatherBean(
//                            index
//                        ).wt.nN().isEmpty()
//                    ) {
//                        getAppModel().requestWeather(WeatherUtils.getWeatherBean(index), index)
//                    } else {
//                        getAppModel().currentWeather.value = CurrentWeatherBean().apply {
//                            this.position = index
//                            this.weather = WeatherUtils.getWeatherBean(index)
//                        }
//                    }
//                    binding.tabView.notifyData(WeatherUtils.getDatas())
//                    binding.iconLocation.isVisible(
//                        WeatherUtils.getDatas().listIndex(index).isLocation
//                    )
//                    binding.tvLocation.LocationEllipsis(
//                        WeatherUtils.getDatas().listIndex(index).nN().regionname,
//                        WeatherUtils.getDatas().listIndex(index).nN().isLocation
//                    )
//                }
//            }
//
//        })
//
//        binding.infoBar.llInfoBack.setSingleClickListener {
//            getCurFragment(index)?.upToScroll()
//        }
//    }
//
//    override fun onResume() {
//        super.onResume()
//        refreshMainData()
//        if (binding.infoBar.rlInfoTopBar.isVisible) {
//            XMLogAgent.onPageStart(EnumType.上报埋点.信息流主页)
//        }
//    }
//
//    override fun onPause() {
//        super.onPause()
//        stopSpeak()
//        if (binding.infoBar.rlInfoTopBar.isVisible) {
//            XMLogAgent.onPageEnd(EnumType.上报埋点.信息流主页)
//        }
//    }
//
//
//    override fun onHiddenChanged(hidden: Boolean) {
//        super.onHiddenChanged(hidden)
//        if (!hidden) {
//            getCurFragment(index)?.onReLoad()
//            refreshMainData()
//        } else {
//            stopSpeak()
//            getCurFragment(index)?.onHidden()
//        }
//
//
//    }
//
//    private fun refreshMainData() {
//        if (WeatherUtils.getDatas().isNotEmpty()) {
//            var time = System.currentTimeMillis() - WeatherUtils.getWeatherBean(index).refreshTime
//            if (abs(time) > weatherRefreshTime) {
//                if (WeatherUtils.getWeatherBean(index).isLocation) {
//                    getAppModel().location()
//                } else {
//                    getAppModel().requestWeather(
//                        WeatherUtils.getWeatherBean(index),
//                        index
//                    )
//                }
//            } else {
//                vm.checkStatus()
//            }
//        }
//        vm.startWidgetService(activity.nN())
//    }
//
//    private fun initData() {
//        var datas = WeatherUtils.initData()
//        if (datas.isNotEmpty()) {
//            binding.weatherBg.setBackgroundResource(WeatherUtils.getWeatherBg(datas.listIndex(0).wtid))
//            viewSize = datas.size
//            if (datas.listIndex(0).isLocation) {
//                binding.tvLocation.LocationEllipsis(LocationUtil.getLocation().district, true)
//            } else {
//                binding.tvLocation.text = datas.listIndex(0).regionname
//            }
//            binding.tabView.notifyData(datas)
//            getAdapter().notifyDataSetChanged()
//
//            if (!datas.listIndex(0).isLocation) {
//                getAppModel().requestWeather(datas.listIndex(0), 0)
//            }
//        }
//
//        getAppModel().currentWeather.observe(this, Observer { current ->
//            if (current.position == index) {
//                if (index == 0) {
//                    viewSize = WeatherUtils.getDatas().size
//                    binding.tabView.notifyData(WeatherUtils.getDatas())
//                    getAdapter().notifyDataSetChanged()
//                }
//                checkDataError(current.weather.nN()) {
//                    getCurFragment(current.position)?.weatherUpdate(it)
//
//                    binding.iconLocation.isVisible(current.weather.nN().isLocation)
//                    if (current.weather.nN().isLocation) {
//                        binding.tvLocation.LocationEllipsis(
//                            LocationUtil.getLocation().district,
//                            true
//                        )
//                    } else {
//                        binding.tvLocation.text = current.weather.nN().regionname
//                    }
//
//                    if (current.weather.nN().nN().ybhs.nN().isNotEmpty()) {
//                        WeatherUtils.setSunTime(
//                            current.weather.nN().nN().ybhs.listIndex(0).sunrise,
//                            current.weather.nN().nN().ybhs.listIndex(0).sunset
//                        )
//                    }
//                    var scroll = pageScroll[index].nN(0)
//                    changeBarColor(scroll)
//                    binding.weatherBg.removeCallbacks(changeBg)
//                    binding.weatherBg.postDelayed(changeBg, 300)
//                    binding.infoBar.infoTemp.text = it.nN().tc
//                    binding.infoBar.tvInfoCity.text = binding.tvLocation.text.toString()
//                    binding.infoBar.ivInfoLocation.isVisible(
//                        WeatherUtils.getDatas().listIndex(index).isLocation
//                    )
//                    binding.infoBar.ivInfoWeather.setImageResource(WeatherUtils.IconBig(it.nN().wtid))
//                }
//            }
//        })
//        getAppModel().refreshAction.safeObserve(this, Observer {
//            binding.refreshStatusView.setStatus(it, index)
//        })
//
//    }
//
//
//    fun showInfoBar(show: Boolean) {
//        binding.vp.setScroll(!show)
//        binding.infoBar.rlInfoTopBar.isVisible(show)
//        if (activity != null && activity is MainActivity) {
//            (activity as MainActivity).hideBottomBar(show)
//        }
//        if (show) {
//            XMLogAgent.onPageStart(EnumType.上报埋点.信息流主页)
//            binding.weatherBgCurrent.let {
//                if (it.isAnimating) {
//                    binding.weatherBgCurrent.pauseAnimation()
//                }
//            }
//        } else {
//            XMLogAgent.onPageEnd(EnumType.上报埋点.信息流主页)
//            binding.weatherBgCurrent.let {
//                if (!it.isAnimating) {
//                    binding.weatherBgCurrent.playAnimation()
//                }
//            }
//            refreshMainData()
//            JrttPostBackUtils.getInstance().infoSahowPostBack(
//                InformationFragment.lastVisibleItem
//            )
//        }
//    }
//
//    private var changeBg = Runnable {
//        Try {
//            var wtid = getAppModel().currentWeather.value.nN().weather.nN().wtid
//            if (wtid == "-1") WeatherUtils.getDatas().listIndex(index)
//            if (weatherBg != WeatherUtils.getWeatherBg(wtid)) {
//                weatherBg = WeatherUtils.getWeatherBg(wtid)
//                AnimationUtil.animChange(
//                    binding.weatherBgCurrent,
//                    binding.weatherBg,
//                    WeatherUtils.getWeatherAnimBg(wtid)
//                )
//                tabColor = WeatherUtils.getTopBarColor(wtid)
//            }
//
//            getCurFragment(index)?.UpdateCardColor(WeatherUtils.weatherCardColor(wtid), true)
//            if (index > 0) {
//                getCurFragment(index - 1)?.UpdateCardColor(WeatherUtils.weatherCardColor(wtid))
//            }
//            if (index < WeatherUtils.getDatas().size - 1) {
//                getCurFragment(index + 1)?.UpdateCardColor(WeatherUtils.weatherCardColor(wtid))
//            }
//        }
//    }
//
//    var alpha = 0f
//    private var bgHalfHeight = DisplayUtil.getScreenHeight().toFloat() / 4
//    private var bgAlphaHeight = DisplayUtil.getScreenHeight().toFloat() * 0.75f
//    private fun changeBarColor(scrollY: Int) {
//        Try {
//            binding.topBar.setBackgroundColor(Color.parseColor(tabColor))
//            binding.infoBar.rlInfoTopBar.setBackgroundColor(Color.parseColor(tabColor))
//            binding.refreshLayout.setEnableRefresh(scrollY <= 0)
//            when {
//                scrollY <= bgHalfHeight -> {
//                    alpha = scrollY.toFloat() / bgHalfHeight
//                    binding.topBar.background.mutate().alpha = 0
//                }
//                scrollY.toFloat() <= bgAlphaHeight -> {
//                    alpha = (scrollY.toFloat() - bgHalfHeight) / (bgAlphaHeight - bgHalfHeight)
//                    binding.topBar.background.mutate().alpha = (alpha * 255).toInt()
//                }
//                else -> {
//                    binding.topBar.background.mutate().alpha = 255
//                }
//            }
//        }
//    }
//
//    private fun getCurFragment(pageIndex: Int): WeatherPageFragment? {
//        try {
//            val fragment = getAdapter().instantiateItem(binding.vp, pageIndex) as WeatherPageFragment
//            if (fragment.isAdded) {
//                return fragment
//            }
//        } catch (e: Exception) {
////            WeatherPageFragment().apply {
////                arguments = Bundle().apply {
////                    putInt("index", pageIndex)
////                }
////            }
//        }
//        return null
//    }
//
//    fun getRootView(): ViewPager {
//
//        return binding.vp
//    }
//
//    fun getCurrentIndex(): Int {
//        return index
//    }
//
//    private fun checkDataError(it: WeatherBean?, func: (weather: WeatherBean) -> Unit) {
//        if (WeatherUtils.getDatas().listIndex(index).wt.nN().isEmpty()) {
//            getCurFragment(index)?.UpDateErrorPage()
//        } else {
//            func(
//                if (it != null && it.wt.isNotEmpty()) it.nN() else WeatherUtils.getDatas()
//                    .listIndex(index)
//            )
//        }
//    }
//
//
//    override fun initListener() {
//        binding.refreshLayout.setOnRefreshListener {
//            getAppModel().refreshAction.value = EnumType.刷新类型.开始刷新
//            binding.refreshLayout.finishRefresh(0)
//        }
//
//        binding.setting.ClickReport(EnumType.上报埋点.首页设置) {
//            skipActivity(SettingsActivity::class.java)
//        }
//
//        binding.adCity.setSingleClickListener {
//            stopSpeak()
//            startActivityResult.launch(Intent().apply {
//                putExtra("position", index)
//                setClass(mContext, CityListManageActivity::class.java)
//            })
//        }
//
//        binding.speak.ClickReport(EnumType.上报埋点.首页语音播报) {
//            when {
//                SpeakerUtils.isPlaying() -> {
//                    stopSpeak()
//                }
//                binding.speak.isAnimating -> {
//                    stopSpeak()
//                }
//                SpeakerUtils.checkSpeaker(WeatherUtils.getWeatherBean(binding.vp.currentItem).regioncode) -> {
//                    SpeakerUtils.autoPlay(
//                        activity.nN(),
//                        WeatherUtils.getWeatherBean(binding.vp.currentItem).regioncode
//                    )
//                }
//                else -> {
//                    startSpeakSynthesizer(vm.speakText())
//                }
//            }
//        }
//    }
//
//    override fun onDestroy() {
//        stopSpeak()
//        startActivityResult.unregister()
//        client?.release()
//        super.onDestroy()
//    }
//
//    private fun getAdapter(): FragmentStatePagerAdapter {
//        if (fragmentAdapter == null) {
//            fragmentAdapter = object : FragmentStatePagerAdapter(
//                childFragmentManager,
//                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
//            ) {
//                override fun getItem(position: Int): Fragment {
//                    return WeatherPageFragment().apply {
//                        arguments = Bundle().apply {
//                            putInt("index", position)
//                        }
//                    }
//                }
//
//                override fun getCount(): Int = viewSize
//                override fun getItemPosition(fragment: Any): Int {
//                    return try {
//                        if ((fragment as WeatherPageFragment).UpDateDiff()) {
//                            PagerAdapter.POSITION_UNCHANGED
//                        } else {
//                            PagerAdapter.POSITION_NONE
//                        }
//                    } catch (e: Exception) {
//                        PagerAdapter.POSITION_NONE
//                    }
//                }
//                override fun saveState(): Parcelable? {
//                    return null
//                }
//            }
//        }
//        return fragmentAdapter!!
//    }
//
//
//    private val startActivityResult =
//        registerForActivityResult(ActivityResultContracts.StartActivityForResult()) {
//            var position = 0
//            (activity as MainActivity).hiddenLoading()
//            if (!initScreenLock) {
//                initScreenLock = true
//                getAppModel().initScreenLock()
//            }
//            Try {
//                if (WeatherUtils.getDatas().size > 0) {
//                    viewSize = WeatherUtils.getDatas().size
//                    if (it.data != null) {
//                        position = it.data!!.getIntExtra("position", 0)
//                    }
//                    if (position > viewSize) {
//                        position = viewSize
//                    }
//
//                    if (WeatherUtils.getWeatherBean(position).wt.isEmpty() && (position == 0 || index == position)) {
//                        getAppModel().requestWeather(
//                            WeatherUtils.getWeatherBean(position),
//                            position
//                        )
//                    }
//                    changeBarColor(pageScroll[position].nN(0))
//                    binding.vp.adapter.nN().notifyDataSetChanged()
//                    binding.vp.currentItem = position
//                    binding.tabView.notifyData(WeatherUtils.getDatas())
//                    binding.iconLocation.isVisible(
//                        WeatherUtils.getDatas().listIndex(position).isLocation
//                    )
//                    binding.tvLocation.LocationEllipsis(
//                        WeatherUtils.getDatas().listIndex(position).nN().regionname,
//                        WeatherUtils.getDatas().listIndex(position).nN().isLocation
//                    )
//                }
//            }
//        }
//
//
//    fun startCitySelect() {
//        initScreenLock = false
//        startActivityResult.launch(Intent().apply {
//            putExtra("back", false)
//            putExtra("source", "index-tjcity")
//            setClass(activity.nN(), CitySelectActivity::class.java)
//        })
//    }
//
//    //region ->语音播报
//    override fun onBinaryReceived(data: ByteArray?, p1: Int) {
//        binding.vp.let {
//            SpeakerUtils.prepare(
//                data,
//                WeatherUtils.getWeatherBean(binding.vp.currentItem).regioncode
//            )
//        }
//    }
//
//    override fun onSynthesisCompleted(p0: String?, p1: Int) {
//        LogE("speaker->onSynthesisCompleted")
//        binding.vp.let {
//            SpeakerUtils.addVoice(WeatherUtils.getWeatherBean(binding.vp.currentItem).regioncode)
//            synthesizerWeakReference?.get()?.stop()
//        }
//    }
//
//    override fun onSynthesisStarted(p0: String?, p1: Int) {
//        LogE("speaker->onSynthesisStarted")
//    }
//
//    override fun onMetaInfo(message: String?, p1: Int) {
//        LogE("speaker->onMetaInfo:$message")
//    }
//
//    override fun onTaskFailed(msg: String?, code: Int) {
//        LogE("speaker->onTaskFailed  msg->$msg   code->$code")
//        speak_error = "onTaskFailed->msg:$msg code:$code"
//        synthesizerWeakReference?.get()?.stop()
//        if (code == 10000002) {
//            CacheUtil.remove(Constant.SP_STT_REFRESH_TOKEN)
//        }
//    }
//
//    override fun onChannelClosed(msg: String?, code: Int) {
//        LogE("speaker->onChannelClosed  msg:$msg  code:$code")
//    }
//
//    fun setSynthesizer(speechSynthesizer: SpeechSynthesizer) {
//        synthesizerWeakReference = WeakReference(speechSynthesizer)
//    }
//
//    private fun startSpeakSynthesizer(speaktv: String) {
//
//        loadSpeakAnim()
//        speechSynthesizer = client?.createSynthesizerRequest(this)
//        setSynthesizer(speechSynthesizer.nN())
//        vm.requestTtsToken {
//            if (it.nN().token.isNotEmpty()) {
//                speechSynthesizer?.setToken(it.nN().token)
//                speechSynthesizer?.setAppkey(it.nN().appkey)
//                // 设置要转为语音的文本
//                speechSynthesizer?.setText(speaktv)
//                speechSynthesizer?.setSampleRate(SpeechSynthesizer.SAMPLE_RATE_16K)
//                speechSynthesizer?.setFormat(SpeechSynthesizer.FORMAT_PCM)
//                speechSynthesizer?.setVolume(100)
//                speechSynthesizer.nN().start()
//            } else {
//                speak_error = "Token接口获取失败"
//                speak_loading?.changeSpeak(false)
//            }
//        }
//    }
//
//    private fun loadSpeakAnim() {
//        SpeakerUtils.requestPrepareListener(3 * 1000) {
//            speak_error = "3秒超时"
//            stopSpeak(true)
//        }
//        speak_loading = WeatherSpeakDialog(activity.nN())
//        speak_loading?.show()
//    }
//
//    fun scrollChange(pageIndex: Int, scrollY: Int) {
//        pageScroll[pageIndex] = scrollY.nN(0)
//        changeBarColor(scrollY.nN())
//    }
//
//
//    fun stopSpeak(isTimer: Boolean = false) {
//        Try {
//            if (!isTimer) {
//                SpeakerUtils.timerCancel()
//            }
//            if (speechSynthesizer != null) {
//                speechSynthesizer.nN().cancel()
//                SpeakerUtils.stop()
//            }
//            binding.speak.setAnimation("icon_voice.json")
//            binding.speak.cancelAnimation()
//        }
//    }
//
//    private fun initSpeak() {
//        binding.speak.setAnimation("icon_voice.json")
//        SpeakerUtils.setOnSpeakListener {
//            runOnUi {
//                Try {
//                    speak_loading?.changeSpeak(it)
//                    if (it) {
//                        startSpeakAnim()
//                    } else {
//                        if (speechSynthesizer != null) {
//                            speechSynthesizer!!.cancel()
//                            binding.speak.setAnimation("icon_voice.json")
//                            binding.speak.cancelAnimation()
//                        }
//                    }
//                }
//            }
//        }
//        client = NlsClient()
//    }
//
//    private fun startSpeakAnim() {
//        Try {
//            runOnUi {
//                binding.speak.setAnimation("speak_anim.json")
//                binding.speak.loop(true)
//                binding.speak.imageAssetsFolder = "images"
//                binding.speak.playAnimation()
//            }
//        }
//    }
//
//
//    //endregion
//}