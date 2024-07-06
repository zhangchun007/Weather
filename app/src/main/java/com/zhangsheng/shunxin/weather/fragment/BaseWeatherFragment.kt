package com.zhangsheng.shunxin.weather.fragment

import android.app.Service
import android.content.Intent
import android.graphics.Color
import android.media.AudioManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import android.provider.Settings
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.alibaba.idst.nui.CommonUtils
import com.alibaba.idst.nui.Constants
import com.alibaba.idst.nui.INativeTtsCallback
import com.alibaba.idst.nui.NativeNui
import com.kwai.video.player.KsMediaPlayerInitConfig.packageName
import com.maiya.thirdlibrary.base.AacFragment
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.utils.AppContext
import com.maiya.thirdlibrary.utils.CacheUtil
import com.maiya.thirdlibrary.utils.DisplayUtil
import com.maiya.thirdlibrary.utils.StatusBarUtil.getStatusBarHeight
import com.maiya.thirdlibrary.widget.shapview.ShapeView
import com.maiya.thirdlibrary.widget.smartlayout.adapter.SmartViewHolder
import com.maiya.thirdlibrary.widget.smartlayout.listener.SmartRecycleListener
import com.xm.xmcommon.XMParam
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.databinding.FragmentWeatherBinding
import com.zhangsheng.shunxin.weather.MainActivity
import com.zhangsheng.shunxin.weather.activity.CityListManageActivity
import com.zhangsheng.shunxin.weather.activity.CitySelectActivity
import com.zhangsheng.shunxin.weather.common.Configure
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.dialog.AppStorageDialog
import com.zhangsheng.shunxin.weather.dialog.WeatherSpeakDialog
import com.zhangsheng.shunxin.weather.ext.*
import com.zhangsheng.shunxin.weather.model.WeatherViewModel
import com.zhangsheng.shunxin.weather.net.bean.CurrentWeatherBean
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.utils.LocationUtil
import com.zhangsheng.shunxin.weather.utils.PermissionsUtils
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import com.zhangsheng.shunxin.weather.utils.alispeak.SpeakerUtils
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import org.json.JSONObject
import org.koin.android.ext.android.inject
import java.lang.Math.abs


/**
 * 说明：天气tab对应的baseFragment，所有马甲包天气首页继承该fragment
 * 作者：刘鹏
 * 添加时间：5/14/21 4:52 PM
 * 修改人：liupe
 * 修改时间：5/14/21 4:52 PM
 */
abstract class BaseWeatherFragment :
    AacFragment<WeatherViewModel, FragmentWeatherBinding>(R.layout.fragment_weather) {
    protected var index = 0
    protected var pageScroll = HashMap<Int, Int>(9)
    protected var speak_error = ""
    protected var speak_loading: WeatherSpeakDialog? = null
    protected var fragmentAdapter: FragmentStatePagerAdapter? = null
    protected var weatherRefreshTime = 5 * 60 * 1000
//    protected var initScreenLock = true

    private var nui_tts_instance: NativeNui? = null

    // 是否已经初始化语音合成sdk
    private var nuittsInitialized = false

    //    protected var mediaPlayer: MediaPlayer? = null
    protected var alpha = 0f
    protected var bgHalfHeight = DisplayUtil.getScreenHeight().toFloat() / 4
    protected var bgAlphaHeight = DisplayUtil.getScreenHeight().toFloat() * 0.75f


    override val vm: WeatherViewModel by inject()


    override fun initView() {
        initData()
        initSpeak()
        binding.topBar.setPadding(0, getStatusBarHeight(activity), 0, 0)

        initTabView()

        binding.vp.offscreenPageLimit = 8
        binding.vp.adapter = getAdapter()
        getAdapter().notifyDataSetChanged()
        binding.vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {
                //心晴重写
                pageScrollStateChanged(state)
            }

            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
                //心晴重写
                pageScrolled(position, positionOffset, positionOffsetPixels)
            }

            override fun onPageSelected(position: Int) {
                //心晴重写
                pageSelected(position)

                if (position == 0 && WeatherUtils.getDatas()
                        .listIndex(0).isLocation && WeatherUtils.getDatas()
                        .listIndex(0).location.nN().state != LocationUtil.定位成功
                ) {
                    if (WeatherUtils.getDatas()
                            .listIndex(0).location.nN().state == LocationUtil.定位权限
                    ) {
                        getAppModel().refreshAction.value = EnumType.刷新类型.定位权限
                    } else {
                        getAppModel().refreshAction.value = EnumType.刷新类型.定位失败
                    }
                } else {
                    if (getAppModel().refreshAction.value == EnumType.刷新类型.定位失败 || getAppModel().refreshAction.value == EnumType.刷新类型.定位权限) {
                        getAppModel().refreshAction.value = EnumType.刷新类型.初始状态
                    }
                }
                if (index != position) {
                    stopSpeak()
                    index = position

                    if (abs(System.currentTimeMillis() - WeatherUtils.getWeatherBean(index).refreshTime) > weatherRefreshTime || WeatherUtils.getWeatherBean(
                            index
                        ).wt.nN().isEmpty()
                    ) {
                        getAppModel().requestWeather(
                            WeatherUtils.getWeatherBean(index),
                            index,
                            mustRefresh = true
                        )
                    } else {
                        getAppModel().currentWeather.value = CurrentWeatherBean().apply {
                            this.position = index
                            this.weather = WeatherUtils.getWeatherBean(index)
                        }
                    }
                    binding.tabView?.notifyData(WeatherUtils.getDatas())
                    binding.iconLocation.isVisible(
                        WeatherUtils.getDatas().listIndex(index).isLocation
                    )
                    binding.tvLocation.LocationEllipsis(
                        WeatherUtils.getDatas().listIndex(index).nN().regionname,
                        WeatherUtils.getDatas().listIndex(index).nN().isLocation
                    )
                }

            }
        })
    }

    protected open fun pageScrollStateChanged(state: Int) {}
    protected open fun pageScrolled(
        position: Int,
        positionOffset: Float,
        positionOffsetPixels: Int
    ) {
    }

    protected open fun pageSelected(position: Int) {}

    open fun initTabView() {
        binding.tabView.setSmartListener(object : SmartRecycleListener() {
            override fun AutoAdapter(
                holder: SmartViewHolder,
                item: Any,
                tabPosition: Int
            ) {
                super.AutoAdapter(holder, item, tabPosition)
                var tab = holder.findView<ShapeView>(R.id.tab)
                tab.isVisible(WeatherUtils.getDatas().size > 1)
                if (index == tabPosition) {
                    tab.apply {
                        exeConfig(getConfig().apply {
                            if (getAppModel().isRefreshBlackModel) {
                                bgColor = Color.parseColor("#ffffff")
                            } else
                                bgColor = resources.getColor(R.color.color_tab_indicator_selected)
                        })
                    }
                } else {
                    tab.apply {
                        exeConfig(getConfig().apply {
                            if (getAppModel().isRefreshBlackModel)
                                bgColor = Color.parseColor("#33ffffff")
                            else
                                bgColor = resources.getColor(R.color.color_tab_indicator_unselected)
                        })
                    }
                }
            }
        })
    }

    override fun onResume() {
        super.onResume()
        refreshMainData()
        LogD("WeatherFragment onResume refreshMainData", "refreshAction")
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onHiddenChanged(hidden: Boolean) {
        super.onHiddenChanged(hidden)
        if (!hidden) {
            getCurFragment(index)?.onReLoad()
            refreshMainData()
            LogD("WeatherFragment onHiddenChanged refreshMainData", "refreshAction")
        } else {
            getCurFragment(index)?.onHidden()
        }
    }

    protected fun refreshMainData() {
        if (WeatherUtils.getDatas().isNotEmpty()) {
            val time = System.currentTimeMillis() - WeatherUtils.getWeatherBean(index).refreshTime
            if (abs(time) > weatherRefreshTime) {
                if (WeatherUtils.getWeatherBean(index).isLocation) {
                    getAppModel().location()
                } else {
                    getAppModel().requestWeather(
                        WeatherUtils.getWeatherBean(index),
                        index
                    )
                }
            } else {
                vm.checkStatus()
            }
        }
        vm.startWidgetService(AppContext.getContext())
    }

    private fun initData() {
        val datas = WeatherUtils.initData()
        if (datas.isNotEmpty()) {
            initWeatherBg(datas.listIndex(0))
            updateCityTitle(datas.listIndex(0))
            binding.tabView?.notifyData(datas)
            if (!datas.listIndex(0).isLocation) {
                getAppModel().requestWeather(datas.listIndex(0), 0)
            }
        } else {
            WeatherUtils.addWeather(WeatherBean())
        }

        getAppModel().currentWeather.safeObserve(this, Observer { current ->
            if (current.position == index) {
                if (index == 0) {
                    binding.tabView?.notifyData(WeatherUtils.getDatas())
                    getAdapter().notifyDataSetChanged()
                }
                checkDataError(current.weather.nN()) {
                    getCurFragment(current.position)?.weatherUpdate(it)

                    binding.iconLocation.isVisible(current.weather.nN().isLocation)
                    if (current.weather.nN().isLocation) {
                        binding.tvLocation.LocationEllipsis(
                            LocationUtil.getLocation().district,
                            true
                        )
                    } else {
                        binding.tvLocation.text = current.weather.nN().regionname
                    }

                    if (current.weather.nN().nN().ybhs.nN().isNotEmpty()) {
                        WeatherUtils.setSunTime(
                            current.weather.nN().nN().ybhs.listIndex(0).sunrise,
                            current.weather.nN().nN().ybhs.listIndex(0).sunset
                        )
                    }

                    scrollChange(
                        index,
                        if (pageScroll.size > index) pageScroll[index].nN(0) else 0,
                        "currentWeather:$index"
                    )
                    weatherBgCutAnim()
                    updateInfoBarData(it)
                }
            }
        })

        getAppModel().refreshAction.safeObserve(this, Observer {
            binding.refreshStatusView.setStatus(it, index)
        })

    }

    /**
     * 统一设置城市title
     */
    private fun updateCityTitle(weather: WeatherBean) {
        binding.iconLocation.isVisible(weather.nN().isLocation)
        binding.tvLocation.LocationEllipsis(weather.nN().regionname, weather.nN().isLocation)
    }


    /**
     * 说明：更新信息流tab吸顶时，顶部tab数据
     * 作者：刘鹏
     * 添加时间：5/17/21 10:14 AM
     * 修改人：liupe
     * 修改时间：5/17/21 10:14 AM
     */
    abstract fun updateInfoBarData(data: WeatherBean)

    /**
     * 说明：初始化天气背景
     * 作者：刘鹏
     * 添加时间：5/14/21 4:45 PM
     * 修改人：liupe
     * 修改时间：5/14/21 4:45 PM
     */
    abstract fun initWeatherBg(data: WeatherBean)

    /**
     * 说明：切换城市后，天气背景切换
     * 作者：刘鹏
     * 添加时间：5/14/21 4:45 PM
     * 修改人：liupe
     * 修改时间：5/14/21 4:45 PM
     */
    abstract fun weatherBgCutAnim()

    /**
     * 说明：更改titlebar的背景颜色
     * 作者：刘鹏
     * 添加时间：5/14/21 4:46 PM
     * 修改人：liupe
     * 修改时间：5/14/21 4:46 PM
     */
    abstract fun changeBarColor(scrollY: Int)

    protected fun getCurFragment(pageIndex: Int): WeatherPageFragment? {
        try {
            val fragment =
                getAdapter().instantiateItem(binding.vp, pageIndex) as WeatherPageFragment
            if (fragment.isAdded) {
                return fragment
            }
        } catch (e: Exception) {
        }
        return null
    }

    fun getRootView(): ViewPager? {
        if (!isAdded) {
            return null
        }
        return binding.vp
    }

    fun getCurrentIndex(): Int {
        return index
    }

    protected fun checkDataError(it: WeatherBean?, func: (weather: WeatherBean) -> Unit) {
        if (WeatherUtils.getDatas().listIndex(index).wt.nN().isEmpty()) {
            getCurFragment(index)?.showErrorWeather()
        } else {
            func(it.nN())
        }
    }


    override fun initListener() {
        binding.refreshLayout.setOnRefreshListener {
            getAppModel().refreshAction.value = EnumType.刷新类型.开始刷新
            binding.refreshLayout.finishRefresh(0)
        }

        binding.adCity.setSingleClickListener {
            stopSpeak()
            startActivityForResult(Intent().apply {
                putExtra("position", index)
                setClass(mContext, CityListManageActivity::class.java)
            }, 888)
        }

        binding.speak.ClickReport(EnumType.上报埋点.首页语音播报点击) {
            when {
                SpeakerUtils.isPlaying() -> {
                    stopSpeak()
                }
                binding.speak.isAnimating -> {
                    stopSpeak()
                }
                SpeakerUtils.checkSpeaker(WeatherUtils.getWeatherBean(binding.vp.currentItem).regioncode) -> {
                    SpeakerUtils.autoPlay(
                        activity.nN(),
                        WeatherUtils.getWeatherBean(binding.vp.currentItem).regioncode
                    )
                }
                else -> {
                    startSpeakSynthesizer(vm.speakText())
                }
            }
        }
    }


    protected fun getAdapter(): FragmentStatePagerAdapter {
        if (fragmentAdapter == null) {
            fragmentAdapter = object : FragmentStatePagerAdapter(
                childFragmentManager,
                BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT
            ) {
                override fun getItem(position: Int): Fragment {
                    return WeatherPageFragment().apply {
                        arguments = Bundle().apply {
                            putInt("index", position)
                        }
                    }
                }

                override fun getCount(): Int = WeatherUtils.initData().size

                override fun getItemPosition(fragment: Any): Int {
                    return try {
                        if ((fragment as WeatherPageFragment).UpDateDiff()) {
                            PagerAdapter.POSITION_UNCHANGED
                        } else {
                            PagerAdapter.POSITION_NONE
                        }
                    } catch (e: Exception) {
                        PagerAdapter.POSITION_NONE
                    }
                }

                //Fragment no longer exists for key f0: index 0
                override fun saveState(): Parcelable? {
                    return null
                }
            }
        }
        return fragmentAdapter!!
    }

    /**
     * 心晴UI需要动态更改状态栏颜色
     * 设置状态栏颜色
     */
    open fun setStatusBarDarkTheme() {
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == 0x03) {
            activity?.let {
                PermissionsUtils.onlycheck(it, Constant.CHECK_SAVE_PERMISSIONS) { with ->
                    if (with) {
                        getAppModel().initScreenLock(getAppModel().wallpaperCode)
                    }
                }
            }
        }

        if (requestCode != 888) return
        var position = 0
        (activity as MainActivity).hiddenLoading()
//        if (!initScreenLock) {
//            initScreenLock = true
//            getAppModel().initScreenLock(Constant.WALLPAPER_START)
//        }
        Try {
            if (WeatherUtils.getDatas().size > 0) {
                if (data != null) {
                    position = data.getIntExtra("position", 0)
                }
                if (position > WeatherUtils.getDatas().lastIndex) {
                    position = WeatherUtils.getDatas().lastIndex
                }

                if (WeatherUtils.getWeatherBean(position).wt.isEmpty() && (position == 0 || index == position)) {
                    getAppModel().requestWeather(
                        WeatherUtils.getWeatherBean(position),
                        position
                    )
                } else if (getCurFragment(position)?.UpDateDiff() == false) {
                    getAppModel().currentWeather.value = CurrentWeatherBean().apply {
                        this.position = position
                        this.weather = WeatherUtils.getWeatherBean(0)
                    }
                }
                scrollChange(position, pageScroll[position].nN(0), "onActivityResult")
                getAdapter().notifyDataSetChanged()
                binding.vp.currentItem = position
                binding.tabView.notifyData(WeatherUtils.getDatas())
                binding.iconLocation.isVisible(
                    WeatherUtils.getDatas().listIndex(position).isLocation
                )
                binding.tvLocation.LocationEllipsis(
                    WeatherUtils.getDatas().listIndex(position).nN().regionname,
                    WeatherUtils.getDatas().listIndex(position).nN().isLocation
                )
            }
        }
    }

    var isJump = false
    fun startCitySelect() {
        if (isAdded && !isJump) {
            isJump = true
//            initScreenLock = false
            startActivityForResult(Intent().apply {
                putExtra("back", false)
                putExtra("source", "index-tjcity")
                setClass(activity.nN(), CitySelectActivity::class.java)
            }, 888)
        }
    }

    private fun startSpeakSynthesizer(speaktv: String) {
        loadSpeakAnim()
        vm.requestTtsToken {
            if (it.nN().token.isNotEmpty()) {
                lifecycleScope.async(Dispatchers.IO) {
                    if (context != null) {
                        var ticker: String? = null
                        Try {
                            //获取token方式一般有两种：

                            //方法1：
                            //参考Auth类的实现在端上访问阿里云Token服务获取SDK进行获取
                            //JSONObject object = Auth.getAliYunTicket();

                            //方法2：（推荐做法）
                            //在您的服务端进行token管理，此处获取服务端的token进行语音服务访问


                            //请输入您申请的id与token，否则无法使用语音服务，获取方式请参考阿里云官网文档：
                            //https://help.aliyun.com/document_detail/72153.html?spm=a2c4g.11186623.6.555.59bd69bb6tkTSc
                            if (!nuittsInitialized) {
                                CommonUtils.copyAssetsData(context)
                                val jsonobj = JSONObject()
                                jsonobj.put("app_key", it.nN().appkey)
                                jsonobj.put("token", it.nN().token)
                                // 设备id，必须
                                jsonobj.put("device_id", XMParam.getDeviceId())
                                jsonobj.put(
                                    "url",
                                    "wss://nls-gateway.cn-shanghai.aliyuncs.com/ws/v1"
                                )
                                jsonobj.put("workspace", CommonUtils.getModelPath(context))
                                // 设置为在线合成
                                jsonobj.put("mode_type", "2")
                                ticker = jsonobj.toString()
                                initialize(ticker.nN())
                                nuittsInitialized = true
                            }
                            nui_tts_instance?.startTts("1", "", speaktv).nN()
                        }
                    }
                }

            } else {
                speak_error = "Token接口获取失败"
                speak_loading?.changeSpeak(false)
            }
        }
    }

    fun initScreenLock(wallpaperCode: String) {
        activity?.let { it1 ->
            if (Build.VERSION.SDK_INT <= Build.VERSION_CODES.O || getAppModel().hasExternalPermission() || !isTargetSDKAbove26(context)) {
                getAppModel().initScreenLock(wallpaperCode)
            } else if (getAppModel().checkScreenLock(wallpaperCode)) {
                AppStorageDialog(it1, false) { isAgree ->
                    if (isAgree) {
                        if (it1.isPermissionDenied()) {
                            PermissionsUtils.checkPermissions(
                                it1,
                                Constant.CHECK_SAVE_PERMISSIONS,
                                object : PermissionsUtils.IPermissionsResult {
                                    override fun passPermissons(
                                        isRequst: Boolean,
                                        permissions: Array<String>
                                    ) {
                                        getAppModel().initScreenLock(wallpaperCode)
                                    }

                                    override fun forbitPermissons(permissions: List<String>) {
                                    }
                                }
                            )
                        } else {
                            startActivityForResult(
                                Intent(
                                    Settings.ACTION_APPLICATION_DETAILS_SETTINGS,
                                    Uri.parse("package:${packageName}")
                                ), 0x03
                            )
                        }
                    }
                }.show()
            }
        }
    }

    private fun loadSpeakAnim() {
        SpeakerUtils.requestPrepareListener(3 * 1000) {
            speak_error = "3秒超时"
            stopSpeak(true)
        }
        speak_loading = WeatherSpeakDialog(activity.nN())
        speak_loading?.show()
    }

    open fun scrollChange(pageIndex: Int, scrollY: Int, from: String = "") {
        pageScroll[pageIndex] = scrollY.nN(0)
        changeBarColor(scrollY.nN())
    }

    fun stopSpeak(isTimer: Boolean = false) {
        if (!isAdded) {
            return
        }
        Try {
            if (!isTimer) {
                SpeakerUtils.timerCancel()
            }
            if (nuittsInitialized) {
                nui_tts_instance?.cancelTts("")
                SpeakerUtils.stop()
            }
            if (getAppModel().isRefreshBlackModel) {//心晴UI黯黑背景时变白色，默认黑色
                binding.speak.setAnimation("icon_voice_white.json")
            } else {
                binding.speak.setAnimation("icon_voice.json")
            }
            binding.speak.cancelAnimation()
        }
    }

    private fun initSpeak() {
        if (getAppModel().isRefreshBlackModel) {//心晴UI黯黑背景时变白色，默认黑色
            binding.speak.setAnimation("icon_voice_white.json")
        } else {
            binding.speak.setAnimation("icon_voice.json")
        }
        SpeakerUtils.setOnSpeakListener {
            runOnUi {
                if (!isAdded) {
                    return@runOnUi
                }
                Try {
                    speak_loading?.changeSpeak(it)
                    if (it) {
                        startSpeakAnim()
                        if (checkVolume()) {
                            xToast(getString(R.string.weather_voice_too_low))
                        }
//                        mediaPlayer = MediaPlayer.create(context, R.raw.voice)
//                        mediaPlayer?.start()
                    } else {
                        if (nuittsInitialized) {
                            nui_tts_instance?.cancelTts("")
                            if (getAppModel().isRefreshBlackModel) {//心晴UI黯黑背景时变白色，默认黑色
                                binding.speak.setAnimation("icon_voice_white.json")
                            } else {
                                binding.speak.setAnimation("icon_voice.json")
                            }
                            binding.speak.cancelAnimation()
                            binding.speak.progress = 1f
                        }
//                        mediaPlayer?.apply {
//                            stop()
//                            release()
//                        }
                    }
                }
            }
        }
        nui_tts_instance = NativeNui(Constants.ModeType.MODE_TTS)
    }

    private fun checkVolume(): Boolean {
        val audioManager: AudioManager =
            requireActivity().getSystemService(Service.AUDIO_SERVICE) as AudioManager
        val max = audioManager.getStreamMaxVolume(AudioManager.STREAM_MUSIC)
        val current = audioManager.getStreamVolume(AudioManager.STREAM_MUSIC)
        return current < max * 0.2
    }


    private fun startSpeakAnim() {
        Try {
            runOnUi {
                if (getAppModel().isRefreshBlackModel) {//心晴UI黯黑背景时变白色，默认黑色
                    binding.speak.setAnimation("speak_anim_white.json")
                } else {
                    binding.speak.setAnimation("speak_anim.json")
                }
                binding.speak.loop(true)
                binding.speak.imageAssetsFolder = "images"
                binding.speak.playAnimation()
            }
        }
    }

    private fun initialize(ticker: String): Int {
        val ret = nui_tts_instance?.tts_initialize(object : INativeTtsCallback {
            override fun onTtsEventCallback(
                event: INativeTtsCallback.TtsEvent?,
                task_id: String?,
                ret_code: Int
            ) {
                when (event) {
                    INativeTtsCallback.TtsEvent.TTS_EVENT_START -> {
                    }
                    INativeTtsCallback.TtsEvent.TTS_EVENT_END -> {
                        SpeakerUtils.addVoice(WeatherUtils.getWeatherBean(binding.vp.currentItem).regioncode)
                    }
                    INativeTtsCallback.TtsEvent.TTS_EVENT_PAUSE -> {
                    }
                    INativeTtsCallback.TtsEvent.TTS_EVENT_RESUME -> {
                    }
                    INativeTtsCallback.TtsEvent.TTS_EVENT_ERROR -> {
                        if (ret_code == 144003) {
                            nuittsInitialized = false
                            CacheUtil.remove(Constant.SP_STT_REFRESH_TOKEN)
                        }
                    }
                }
            }

            override fun onTtsDataCallback(
                info: String?,
                info_len: Int,
                data: ByteArray?
            ) {
                if (data != null && data.isNotEmpty()) {
                    SpeakerUtils.prepare(
                        data,
                        WeatherUtils.getWeatherBean(binding.vp.currentItem).regioncode
                    )
                }
            }

            override fun onTtsVolCallback(vol: Int) {
            }
        }, ticker, Constants.LogLevel.LOG_LEVEL_VERBOSE, Configure.Debug)
        nui_tts_instance?.setparamTts("sample_rate", "16000")
        // 在线语音合成发音人可以参考阿里云官网
        nui_tts_instance?.setparamTts("font_name", "Aiyue")
        nui_tts_instance?.setparamTts("enable_subtitle", "1")
        //        nui_tts_instance.setparamTts("speed_level", "1");
//        nui_tts_instance.setparamTts("pitch_level", "0");
        nui_tts_instance?.setparamTts("volume", "2.0");
        return ret.nN()
    }

    override fun onDestroy() {
        stopSpeak()
        nui_tts_instance?.tts_release()
        binding.vp.adapter?.notifyDataSetChanged()
        super.onDestroy()
        clearBinding()
    }

    override fun injectBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentWeatherBinding = FragmentWeatherBinding.inflate(inflater, viewGroup, false)

}