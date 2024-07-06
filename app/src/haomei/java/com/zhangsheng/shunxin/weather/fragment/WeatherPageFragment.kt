package com.zhangsheng.shunxin.weather.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Rect
import android.text.Spannable
import android.text.SpannableString
import android.text.TextUtils
import android.text.style.ForegroundColorSpan
import android.util.TypedValue
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.maiya.thirdlibrary.base.AacFragment
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.utils.CacheUtil
import com.maiya.thirdlibrary.utils.DisplayUtil
import com.maiya.thirdlibrary.widget.shapview.ShapeView
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.ad.AdConstant
import com.zhangsheng.shunxin.databinding.FragmentWeatherPageBinding
import com.zhangsheng.shunxin.information.adapter.InfoVpAdapter
import com.zhangsheng.shunxin.information.constant.Constants
import com.zhangsheng.shunxin.information.dialog.ChannelDialog
import com.zhangsheng.shunxin.information.utils.DensityUtil
import com.zhangsheng.shunxin.information.utils.DeviceUtil
import com.zhangsheng.shunxin.information.widget.tablayout.TabLayout
import com.zhangsheng.shunxin.weather.MainActivity
import com.zhangsheng.shunxin.weather.activity.*
import com.zhangsheng.shunxin.weather.adapter.FifWeatherAdapter
import com.zhangsheng.shunxin.weather.adapter.FifWeatherListAdapter
import com.zhangsheng.shunxin.weather.adapter.HourWeatherAdapter
import com.zhangsheng.shunxin.weather.adapter.LifeAdapter
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.*
import com.zhangsheng.shunxin.weather.model.WeatherPageViewModel
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.utils.AnimationUtil
import com.zhangsheng.shunxin.weather.utils.DataUtil
import com.zhangsheng.shunxin.weather.utils.NewsChannelDataUtils
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import com.zhangsheng.shunxin.weather.widget.TouchScrollView
import org.koin.android.ext.android.inject

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/7/22 17:42
 */
class WeatherPageFragment : AacFragment<WeatherPageViewModel,FragmentWeatherPageBinding>(R.layout.fragment_weather_page) {

    private var cityCode: String = ""
    private var shouldLoadInfo = true
    private var chanDialog: ChannelDialog? = null

    override val vm: WeatherPageViewModel by inject()

    override fun initView() {
        changePageHeight()

        Try {
            vm.index = arguments.nN().getInt("index", -1)
            if (vm.index != -1 && WeatherUtils.getDatas().size > vm.index) {
                weatherUpdate(WeatherUtils.getDatas().listIndex(vm.index))
                cityCode = WeatherUtils.getDatas().listIndex(vm.index).regioncode
            }
        }

        binding.llPageTow.background.mutate().alpha = 0

        binding.scrollview.setOnScrollistener(object : TouchScrollView.OnScrollistener {
            override fun onScroll(scrollY: Int, oldScrollY: Int) {
                if (!isAdded) {
                    return
                }
                getParent()?.scrollChange(vm.index, scrollY)
                loadAd(scrollY)
                if (shouldLoadInfo() && isControlShow(getAppModel().control.value.nN().swtich_all.listIndex(0).infostream)) {
                    initSmartTabLayout(0)
                    shouldLoadInfo = false
                }
            }

            override fun isViewPageVisible(): Boolean {
                return binding.infoStream.llInfoStream.visibility == View.VISIBLE
            }

            override fun stick(flag: Boolean) {
                super.stick(flag)
                if (!isAdded) {
                    return
                }
                getParent()?.showInfoBar(flag)
            }
        })

        initTabLayout()
        //点击信息流频道
        binding.infoStream.ivChannel.setSingleClickListener {
            chanDialog = activity?.let { ChannelDialog(it) }
            if (chanDialog != null) {
                chanDialog!!.setData(curPos)
                val isFinish = activity?.isFinishing
                if (isFinish != null && !isFinish) {
                    chanDialog!!.show()
                }
            }

        }

        binding.rainTv.ClickReport(EnumType.上报埋点.首页分钟级降雨点击) {
            skipActivity(WeatherMapActivity::class.java)
        }

        binding.clickWeather.ClickReport(EnumType.上报埋点.首页实况天气) {
            skipActivity(WeatherActivity::class.java)
        }

        binding.warns.ClickReport(EnumType.上报埋点.天气预警点击) {
            skipActivity(HighAlertActivity::class.java) {
                this.putExtra("index", binding.warns.getCurrentIndex())
            }
        }

        binding.weatherLl.setSingleClickListener {
            skipActivity(WeatherActivity::class.java)
        }

        binding.linChartType.setSingleClickListener {
            changeFifChartState(true)
        }

        binding.linLineType.setSingleClickListener {
            changeFifChartState(false)
        }

        binding.typhoon.ClickReport(EnumType.上报埋点.首页台风点击) {
            skipActivity(TyphoonActivity::class.java)
        }

        binding.layoutToday.ClickReport(EnumType.上报埋点.首页今天明天模块今天) {
            skipActivity(FifWeatherActivity::class.java) {
                putExtra("position", 1)
                putExtra("source", "hometoday-15xiangqin")
            }
        }
        binding.layoutTomorrow.ClickReport(EnumType.上报埋点.首页今天明天模块明天) {
            skipActivity(FifWeatherActivity::class.java) {
                putExtra("position", 2)
                putExtra("source", "hometoday-15xiangqin")
            }
        }

        binding.linAir.ClickReport(EnumType.上报埋点.空气质量模块点击) {
            skipActivity(AirActivity::class.java) {
                putExtra("code", vm.weatherData.value.nN().regioncode)
                putExtra(
                    "name", LocationEllipsis(
                        getAppModel().currentWeather.value.nN().weather.nN().regionname,
                        getAppModel().currentWeather.value.nN().weather.nN().isLocation
                    )
                )
                putExtra("location", vm.weatherData.value.nN().isLocation)
            }
        }

        binding.cctvImage.ClickReport(EnumType.上报埋点.CCTV视频模块点击) {
            skipActivity(WeatherVideoActivity::class.java) {
                if (getAppModel().currentWeather.value.nN().weather.nN().cctv.nN().video_url.isNotEmpty()) {
                    putExtra(
                        "url",
                        getAppModel().currentWeather.value.nN().weather.nN().cctv.nN().video_url
                    )
                    putExtra(
                        "CCTV_TIME",
                        getAppModel().currentWeather.value.nN().weather.nN().cctv.nN().ptime
                    )
                } else {
                    val cctv = CacheUtil.getObj(Constant.SP_CCTV, WeatherBean.CctvBean::class.java)
                    putExtra("url", cctv.nN().video_url)
                    putExtra("CCTV_TIME", cctv.nN().ptime)
                }
            }
        }

        binding.fifLoadMore.setSingleClickListener {
            if (!vm.isFifLoadMore) {
                vm.isFifLoadMore = true
                binding.fifListWeather.notifyData(vm.weatherData.value.nN().ybds.nN())
                binding.fifLoadMore.text = "点击收起"
                binding.fifLoadMore.setImageBackGround(
                    R.mipmap.icon_arrow_up,
                    binding.fifLoadMore.RIGHT_DRAWABLE
                )
            } else {
                if (vm.weatherData.value.nN().ybds.nN().size <= 7) return@setSingleClickListener
                vm.isFifLoadMore = false
                binding.fifListWeather.notifyData(vm.weatherData.value.nN().ybds.nN().subList(0, 7))
                binding.fifLoadMore.text = "点击查看15天天气"
                binding.fifLoadMore.setImageBackGround(
                    R.mipmap.icon_arrow_down,
                    binding.fifLoadMore.RIGHT_DRAWABLE
                )
            }
        }

        binding.rlPageOne.setSingleClickListener {
            getAppModel().wallpaperCode = "wallpaper_background"
            getParent()?.initScreenLock("wallpaper_background")
        }

        binding.hourWeather.setSmartListener(HourWeatherAdapter())
        binding.fifWeather.setSmartListener(FifWeatherAdapter())
        binding.life.setSmartListener(LifeAdapter(activity.nN()))
        binding.fifListWeather.setSmartListener(FifWeatherListAdapter())
    }

    override fun initObserve() {
        super.initObserve()
        vm.weatherData.safeObserve(this, Observer{
            if (it.typhoon == "1") {
                showReport(EnumType.上报埋点.首页台风展示)
                binding.typhoon.isVisible(true)
            } else {
                binding.typhoon.isVisible(false)
            }

            setErrorState(it.nN())
            setWeather(it.nN())
            setWarnsAlert(it.nN())
            loadMoreWeather(it.nN())
        })
    }

    override fun initListener() {
        super.initListener()
        binding.hourWeather.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    clickReport(EnumType.上报埋点.二十四小时天气模块左右滑动)
                }
            }
        })

        binding.fifWeather.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == RecyclerView.SCROLL_STATE_DRAGGING) {
                    clickReport(EnumType.上报埋点.十五日天气模块左右滑动)
                }
            }
        })
    }

    private fun loadMoreWeather(it: WeatherBean) {
        binding.fifWeather.notifyData(it.nN().ybds.nN())
        binding.fifWeather.notifyData(it.nN().ybds.nN())
        if (it.nN().ybds.nN().size >= 7 && !vm.isFifLoadMore) {
            binding.fifListWeather.notifyData(it.nN().ybds.nN().subList(0, 7))
        } else {
            binding.fifListWeather.notifyData(it.nN().ybds.nN())
        }
        changeFifChartState(CacheUtil.getBoolean(Constant.SP_FIF_IS_CHART, true))
        if (it.nN().ybhs.nN().isNotEmpty()) {
            binding.hourWeather.notifyData(it.nN().ybhs.nN())
            binding.timeSunUp.text = it.nN().ybhs.listIndex(0).sunrise
            binding.timeSunDown.text = it.nN().ybhs.listIndex(0).sunset
        }
        setLife(it.nN())
        setCCTV(it)
    }

    private fun changeFifChartState(isChart: Boolean) {
        if (isChart && binding.fifWeather.visibility == View.VISIBLE) return
        if (!isChart && binding.frameListFif.visibility == View.VISIBLE) return

        CacheUtil.put(Constant.SP_FIF_IS_CHART, isChart)
        binding.fifWeather.isVisible(isChart)
        binding.frameListFif.isVisible(!isChart)

        if (isChart) {
            binding.lineType.setTextColor((Color.parseColor("#BABFCC")))
            binding.chartType.setTextColor((Color.parseColor("#44A0FF")))
        } else {
            binding.lineType.setTextColor((Color.parseColor("#44A0FF")))
            binding.chartType.setTextColor((Color.parseColor("#BABFCC")))
        }
    }

    private fun setCCTV(it: WeatherBean) {
        if (isControlShow(getAppControl().swtich_all.listIndex(0).report)) {
            val cctv = if (it.cctv.nN().video_url.isNotEmpty()) it.cctv.nN() else CacheUtil.getObj(
                Constant.SP_CCTV, WeatherBean.CctvBean::class.java
            ).nN()
            binding.llCctv.isVisible(cctv.video_url.isNotEmpty())
            if (cctv.video_url.isNotEmpty()) {
                CacheUtil.putObj(Constant.SP_CCTV, cctv)
                if (cctv.cover.isNotEmpty()) {
                    Glide.with(this).load(getAppControl().cctv.listIndex(0).cover)
                        .error(R.mipmap.bg_cctv_img).into(binding.cctvImage)
                    //  Glide.with(this).load(getAppControl().cctv.listIndex(0).cover).into(cctv_image)
                }
                if (cctv.ptime.isNotEmpty()) {
                    binding.cctvTime.text =
                        "${getAppControl().cctv.listIndex(0).sub_title.isStr("中国气象局")} ${
                            DataUtil.date2date(
                                cctv.ptime,
                                "yyyy-MM-dd HH:mm:ss",
                                "HH:mm"
                            )
                        }发布"
                }
                binding.cctvMtitle.text = getAppControl().cctv.listIndex(0).title.isStr("天气预报")
            }
        } else {
            binding.llCctv.isVisible(false)
        }
    }

    private fun setLife(it: WeatherBean) {
        vm.lifeData.clear()
        vm.lifeData.addAll(it.nN().lifes.nN())
        binding.life?.notifyData(vm.lifeData)
    }

    private fun setWarnsAlert(data: WeatherBean) {
        Try {
            binding.warns.let {
                var warnsData = data.nN().warns.nN()
                if (warnsData.size > 4) {
                    warnsData = warnsData.subList(0, 4)
                }
                binding.warns.isVisible(warnsData.isNotEmpty())
                if (warnsData.isNotEmpty()) {
                    showReport(EnumType.上报埋点.天气预警展示)
                    binding.warns.initData(warnsData)
                }
            }
        }
    }

    @SuppressLint("SetTextI18n")
    private fun setWeather(it: WeatherBean) {
        binding.rainTv.isSelected = true
        binding.rainTv.isVisible(true)
        if (it.nN().falls != null && it.nN().falls.nN().desc.isNotEmpty()) {
            binding.rainTv.text = it.nN().falls.nN().desc
        } else {
            binding.rainTv.text = "查看未来2小时降雨预报"
        }
        binding.weatherIcon.setImageResource(WeatherUtils.WeatherBigIcon(it.nN().wtid))
        Constants.temp = it.nN().tc
        Constants.wtid = it.nN().wtid
        binding.temp.text = it.nN().tc
        binding.weather.text = it.nN().wt
        binding.cloud.text = it.nN().wdir
        binding.weatherTvPressure.text = it?.pressure?.replace(" ", "")

        binding.cloudLevel.text = it.nN().ws
        binding.weight.text = "${it.nN().rh}%"
        binding.weight.isVisible(!TextUtils.isEmpty(it.nN().rh))
        binding.weightTv.isVisible(!TextUtils.isEmpty(it.nN().rh))
        binding.weatherTvPressure.isVisible(!TextUtils.isEmpty(it.nN().pressure))
        binding.weatherPressureTv.isVisible(!TextUtils.isEmpty(it.nN().pressure))
        binding.weatherTvDot.isVisible(!TextUtils.isEmpty(it.nN().tc))
        if (it.nN().wtablesNew.nN().size == 2) {
            var day = it.nN().wtablesNew.nN()
                .sortedBy { wt -> DataUtil.Data2TimeStamp(wt.fct, "yyyy-MM-dd") }
            if (!TextUtils.isEmpty(day.listIndex(0).tcr)) {
                var weatherTextView = day.listIndex(0).tcr.nN("")
                val indexOf = weatherTextView.indexOf("/");
                val spannableString = SpannableString(weatherTextView)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor("#4d333333")),
                    indexOf,
                    indexOf + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )

                binding.todayTemp.text = spannableString
            }

            binding.todayWeather.text = day.listIndex(0).wt

            binding.todayWeatherIcon.setImageResource(WeatherUtils.IconBig(day.listIndex(0).wtid))

            if (!TextUtils.isEmpty(day.listIndex(1).tcr)) {
                var weatherTextView = day.listIndex(1).tcr.nN("")
                val indexOf = weatherTextView.indexOf("/");
                val spannableString = SpannableString(weatherTextView)
                spannableString.setSpan(
                    ForegroundColorSpan(Color.parseColor("#4d333333")),
                    indexOf,
                    indexOf + 1, Spannable.SPAN_EXCLUSIVE_INCLUSIVE
                )
                binding.tomorrowTemp.text = spannableString
            }

            binding.tomorrowWeather.text = day.listIndex(1).wt

            binding.tomorrowWeatherIcon.setImageResource(WeatherUtils.IconBig(day.listIndex(1).wtid))
        }

        showAirInfo(it.nN()?.aqi, it.nN().aqiLevel)
        binding.ll24.isVisible(it.nN().ybhs.nN().isNotEmpty())
    }

    fun showAirInfo(aqi: String, aqiLevel: String) {
        if (TextUtils.isEmpty(aqi) || TextUtils.isEmpty(aqiLevel)) {
            return
        }
        setAirContent(aqi)
        binding.linAir.isVisible(aqi.isNotEmpty() && aqi != "0")
        if (aqi.isNotEmpty() && aqi != "0") {
            showReport(EnumType.上报埋点.空气质量模块展示)
        }
    }

    private fun setAirContent(aqi: String?) {
        if (TextUtils.isEmpty(aqi)) {
            binding.tvAir.text = "    无"
        } else {
            var aqiLv = WeatherUtils.getAirairQualityString(aqi?.toInt() ?: 0)
            if (aqiLv?.length == 1) {
                aqiLv = " $aqiLv"
            }
            binding.tvAir.text = aqi + aqiLv
        }
        val drawableId = WeatherUtils.airQualityIconID(aqi?.toInt() ?: 0)
        binding.imAir.setImageResource(drawableId)
    }

    private fun setErrorState(weatherBean: WeatherBean) {
        Try {
            if (weatherBean.nN().wt.nN().isEmpty()) {
                if (vm.emptyView == null) {
                    vm.emptyView = binding.layoutError.inflate()
                }
                binding.scrollview?.isVisible(false)
            } else {
                binding.scrollview?.isVisible(true)
                if (vm.emptyView != null && vm.emptyView.nN().visibility == View.VISIBLE) {
                    AnimationUtil.lightOutAnim(vm.emptyView.nN())
                }
            }
        }
    }

    private fun changePageHeight() {
        getParent()?.getRootView()?.post {
            runOnUi {
                try {
                    if (binding.rlPageOne == null) {
                        return@runOnUi
                    }
                    val params = binding.rlPageOne.layoutParams as LinearLayout.LayoutParams
                    params.width = DisplayUtil.getScreenWidth()
                    params.height = getParent()?.getRootView().nN().measuredHeight
                    binding.rlPageOne.layoutParams = params
                } catch (e: Exception) {
                }
            }
        }
    }

    private fun getParent(): WeatherFragment? {
        try {
            if (activity != null && !(requireActivity().isFinishing)) {
                val fragment = (activity.nN() as MainActivity).getFragment()
                if (fragment.isAdded) {
                    return fragment
                }
            }
        } catch (e: Exception) {
            WeatherFragment()
        }
        return null
    }

    fun UpDateDiff(): Boolean {
        return vm.weatherData.value.nN().regioncode == WeatherUtils.getDatas()
            .listIndex(vm.index).regioncode
    }

    fun weatherUpdate(data: WeatherBean) {
        if (isAdded) {
            vm.weatherData.postValue(data)
        }
    }

    fun showErrorWeather() {
        if (!isAdded) {
            return
        }
        Try {
            if (vm.emptyView == null) {
                vm.emptyView = binding.layoutError.inflate()
            }
            binding.scrollview.isVisible(false)
            vm.emptyView?.findViewById<ShapeView>(R.id.btn_net_error).apply {
                this?.isVisible(true)
                this?.setSingleClickListener {
                    getAppModel().refreshAction.value = EnumType.刷新类型.开始刷新
                }
            }
        }
    }

    public override fun onReLoad() {
        if (!isAdded) {
            return
        }
        super.onReLoad()
        Try {
            loadPageOneAd()
            changeFifChartState(CacheUtil.getBoolean(Constant.SP_FIF_IS_CHART, true))
            binding.warns.start()

            if (vm.weatherData.value.nN().refreshTime != vm.refreshTime) {
                vm.refreshTime = vm.weatherData.value.nN().refreshTime
                val weatherBean = vm.weatherData.value.nN()
                loadMoreWeather(weatherBean)
            }
        }
    }


    public override fun onHidden() {
        if (!isAdded) {
            return
        }
        super.onHidden()
        binding.warns.stop()
    }

    fun upToScroll() {
        if (isAdded) {
            if (!binding.scrollview.isTop())
                this.binding.scrollview?.upToScroll()
        }
    }

    override fun reTry() {
        if (!isAdded) {
            return
        }
        super.reTry()
        getAppModel().refreshAction.value = EnumType.刷新类型.开始刷新
    }

    override fun onLazyLoad() {
        if (!isAdded) {
            return
        }
        super.onLazyLoad()
        Try {
            if (getParent()?.getCurrentIndex() == vm.index) {
                loadPageOneAd()
            }
        }
        if (!vm.isAddRefresh) {
            if (WeatherUtils.getDatas().listIndex(vm.index).wtid.isNotEmpty()) {
                vm.weatherData.value = WeatherUtils.getDatas().listIndex(vm.index)
                vm.isAddRefresh = true
            }
        }

        if (vm.weatherData.value.nN().refreshTime != vm.refreshTime) {
            vm.refreshTime = vm.weatherData.value.nN().refreshTime
            val weatherBean = vm.weatherData.value.nN()
            loadMoreWeather(weatherBean)
        }
    }

    //region ->广告
    private fun loadPageOneAd() {
        binding.advIcon.loadAd()
        binding.advBanner.loadAd()
//        binding.advFloat.showFeedAd(activity, AdConstant.SLOT_FLOATSMALL)
        binding.advWeather24.setCanLoadable(true)
        binding.advWeatherFif.setCanLoadable(true)
        binding.advWeatherLife.setCanLoadable(true)
    }

    private fun loadAd(scrollY: Int) {
        if (scrollY > 80) {
            binding.advWeather24.loadAd(AdConstant.SLOT_MIXBT24PRE15, AdConstant.SLOT_BIGBTPRE15)
        }

        if (binding.ll24.getGlobalVisibleRect(Rect())) {
            showReport(EnumType.上报埋点.二十四小时天气模块展示)
        }

        if (binding.fifteenWeatherContainer.getGlobalVisibleRect(Rect())) {
            binding.advWeatherFif.loadAd(
                AdConstant.SLOT_BIGDRAWBTSHZS,
                AdConstant.SLOT_BIGDRAWBTSHZS2
            )
            showReport(EnumType.上报埋点.十五日天气模块展示)
        }
        if (binding.llLife.getGlobalVisibleRect(Rect())) {
            binding.advWeatherLife.loadAd(AdConstant.SLOT_BIGDRAWSHZS, AdConstant.SLOT_BIGDRAWSHZS2)
            showReport(EnumType.上报埋点.生活指数模块展示)
        }

        if (binding.llCctv.getGlobalVisibleRect(Rect())) {
            showReport(EnumType.上报埋点.CCTV视频模块展示)
        }
    }

    private fun shouldLoadInfo(): Boolean {
        return shouldLoadInfo && (isInfoViewVisible() || isLifeViewVisible())
    }

    private fun isInfoViewVisible(): Boolean {
        return try {
            if (binding.infoStream.llInfoStream.visibility == View.GONE) {
                binding.llCctv.getGlobalVisibleRect(Rect())
            } else {
                binding.infoStream.llInfoStream.getGlobalVisibleRect(Rect())
            }
        } catch (e: Exception) {
            false
        }
    }

    private fun isLifeViewVisible(): Boolean {
        return try {
            if (binding.infoStream.llInfoStream.visibility == View.GONE) {
                binding.llLife.getGlobalVisibleRect(Rect())
            } else {
                binding.infoStream.llInfoStream.getGlobalVisibleRect(Rect())
            }
        } catch (e: Exception) {
            false
        }
    }

    private var curPos: Int = 0

    private fun initSmartTabLayout(pos: Int) {
        curPos = pos
        val informAdapter = InfoVpAdapter(childFragmentManager)
        binding.infoStream.weatherNewsViewpager.adapter = informAdapter
        //处理数据
        if (informAdapter.count > 0) {
            binding.infoStream.weatherNewsViewpager.currentItem = 0
            return
        }
        val tabList = context?.let { NewsChannelDataUtils.getNewTabList(it) }
        if (tabList == null || tabList?.size == 0) {
            return
        }
        informAdapter.replace(tabList)
        if (pos == -1) {
            binding.infoStream.weatherNewsViewpager.currentItem = tabList.size - 1
        } else {
            binding.infoStream.weatherNewsViewpager.currentItem = pos
        }
        binding.infoStream.weatherNewsViewpager.addOnAttachStateChangeListener(
            onAttachStateChangeListener
        )
        binding.infoStream.weatherNewsViewpager.setOnPageChangeListener(object :
            ViewPager.OnPageChangeListener {
            override fun onPageScrolled(i: Int, v: Float, i1: Int) {
            }

            override fun onPageSelected(i: Int) {
                curPos = i
                if (tabList.size > i) {
                    Constants.curTabCode = tabList.nN().get(i).code
                    Constants.curTabTitle = tabList.nN().get(i).title
                }
            }

            override fun onPageScrollStateChanged(i: Int) {

            }
        })
        binding.infoStream.weatherNewsIndicator.addOnTabSelectedListener(object :
            TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {
                val tabView: TabLayout.TabView = tab.mView
                if (tabView.mTextView != null) {
                    tabView.mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 18F)
                    tabView.mTextView.setTextColor(Color.parseColor("#379BFF"))
                    tabView.mTextView.paint.isFakeBoldText = true
                }
            }

            override fun onTabUnselected(tab: TabLayout.Tab) {
                val tabView: TabLayout.TabView = tab.mView
                if (tabView.mTextView != null) {
                    tabView.mTextView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16F)
                    tabView.mTextView.setTextColor(Color.parseColor("#999999"))
                    tabView.mTextView.getPaint().setFakeBoldText(false)
                }
            }

            override fun onTabReselected(tab: TabLayout.Tab?) {}
        })

        binding.infoStream.weatherNewsIndicator.setupWithViewPager(binding.infoStream.weatherNewsViewpager)
    }

    var onAttachStateChangeListener: View.OnAttachStateChangeListener =
        object : View.OnAttachStateChangeListener {
            override fun onViewAttachedToWindow(view: View) {
                if (view is ViewPager) {
                    //以反射的形式处理 recyclerview + viewepager时 viewpager 被销毁重新进入window时 mFirstLayout 为true 导致的
                    //viewpager第一次滑动失效的问题
                    try {
                        val superclass: Class<*> = view.javaClass
                        val field =
                            superclass.getDeclaredField("mFirstLayout")
                        field.isAccessible = true
                        field.setBoolean(view, false)
                    } catch (e: java.lang.Exception) {
                        e.printStackTrace()
                    }
                }
            }

            override fun onViewDetachedFromWindow(view: View) {}
        }

    //region ->信息流
    private fun initTabLayout() {
        if (isControlShow(getAppModel().control.value.nN().swtich_all.listIndex(0).infostream) && !isControl()) {
            dealWithViewPager()
            binding.infoStream.llInfoStream.isVisible(true)
//            binding.view50.isVisible(false)
            Constants.curTabCode = "__all__"
        } else {
            binding.infoStream.llInfoStream.isVisible(false)
//            binding.view50.isVisible(true)
        }
    }

    private fun dealWithViewPager() {
        // 导航栏高度
        var navigationBarHeight = 0
        if (DeviceUtil.hasNavBar(context) && DeviceUtil.hasNavigationBar(context)) {
            navigationBarHeight = DeviceUtil.getNavigationBarHeight(context)
        }
        // 新判断导航栏高度
        if (!DeviceUtil.hasNavigationBarByHeight(context)) {
            navigationBarHeight = 0
        }
        //判断android10 导航栏是否显示
        if (DeviceUtil.isNavigationBarExist(activity.nN())) {
            navigationBarHeight = DeviceUtil.getNavigationBarHeight(context)
        }
        val titleBar: Int = DeviceUtil.getStatusBarHeight(context)

        val params1: ViewGroup.LayoutParams = binding.infoStream.llInfoStream.layoutParams
        params1.height =
            (DeviceUtil.getScreenHeightPx(activity.nN())
                    - titleBar.nN()
                    - DensityUtil.dip2px(activity.nN(), 52F)//bar
                    - navigationBarHeight)
        binding.infoStream.llInfoStream.layoutParams = params1
    }

    override fun injectBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentWeatherPageBinding =FragmentWeatherPageBinding.inflate(inflater,viewGroup,false)

}