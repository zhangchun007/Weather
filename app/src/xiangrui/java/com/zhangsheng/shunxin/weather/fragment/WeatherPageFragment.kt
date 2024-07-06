package com.zhangsheng.shunxin.weather.fragment

import android.annotation.SuppressLint
import android.graphics.Color
import android.graphics.Rect
import android.os.Bundle
import android.text.TextUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.observe
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.maiya.thirdlibrary.base.AacFragment
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.utils.CacheUtil
import com.maiya.thirdlibrary.utils.DisplayUtil.getScreenHeight
import com.necer.utils.CalendarUtil
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.ad.AdConstant
import com.zhangsheng.shunxin.calendar.activity.AlmanacActivity
import com.zhangsheng.shunxin.calendar.activity.CalendarActivity
import com.zhangsheng.shunxin.calendar.util.CalendarHelper
import com.zhangsheng.shunxin.databinding.FragmentWeatherPageBinding
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
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import com.zhangsheng.shunxin.weather.widget.TouchScrollView1
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.withContext
import org.joda.time.LocalDate
import org.koin.android.ext.android.inject

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/7/22 17:42
 */
class WeatherPageFragment :
    AacFragment<WeatherPageViewModel, FragmentWeatherPageBinding>(R.layout.fragment_weather_page) {

    private val bgHalfHeight = getScreenHeight().toFloat() * 0.3f

    override val vm: WeatherPageViewModel by inject()

    override fun initView() {
        changePageHeight()
        Try {
            vm.index = arguments.nN().getInt("index", -1)
            if (vm.index != -1 && WeatherUtils.getDatas().size > vm.index) {
                weatherUpdate(WeatherUtils.getDatas().listIndex(vm.index))
            }
        }

        binding.mainView.background.mutate().alpha = 0
        binding.scrollview.setOnScrollistener(object : TouchScrollView1.OnScrollistener {
            override fun onScroll(scrollY: Int, oldScrollY: Int) {
                if (!isAdded) {
                    return
                }
                getParent()?.scrollChange(vm.index, scrollY)
                val alpha = scrollY.toFloat() / bgHalfHeight
                binding.mainView.background.mutate().alpha =
                    (255 * if (alpha > 1) 1f else alpha).toInt()
                (255 * if (alpha > 1) 1f else alpha).toInt()
                loadAd(scrollY)
            }
        })

        binding.llWeather.ClickReport(EnumType.上报埋点.首页分钟级降雨点击) {
            skipActivity(WeatherMapActivity::class.java)
        }

        binding.rlTemp.ClickReport(EnumType.上报埋点.首页实况天气) {
            skipActivity(WeatherActivity::class.java)
        }

        binding.llCloud.setSingleClickListener {
            skipActivity(WeatherActivity::class.java)
        }

        binding.linChartType.setSingleClickListener {
            changeFifChartState(true)
        }

        binding.linLineType.setSingleClickListener {
            changeFifChartState(false)
        }

        binding.warns.ClickReport(EnumType.上报埋点.天气预警点击) {
            skipActivity(HighAlertActivity::class.java) {
                this.putExtra("index", binding.warns.getCurrentIndex())
            }
        }

        binding.llCalendar.isVisible(isControlShow(getAppControl().swtich_all.listIndex(0).life))

        binding.clAlmanac.ClickReport(EnumType.上报埋点.生活指数模块日历点击宜忌) {
            if (!isControl()) {
                skipActivity(AlmanacActivity::class.java)
            }
        }

        binding.clCalendar.ClickReport(EnumType.上报埋点.生活指数模块日历点击) {
            if (!isControl()) {
                skipActivity(CalendarActivity::class.java) {
                    this.putExtra("from", "home")
                }
            }
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

        binding.tvFortyMore.ClickReport(EnumType.上报埋点.首页40日天气查看更多) {
            skipActivity(FortyWeatherActivity::class.java) {
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
        binding.rlFortyTemp.ClickReport(EnumType.上报埋点.首页40日预报点击温度) {
            skipActivity(FortyWeatherActivity::class.java) {
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
        binding.rlFortyRain.ClickReport(EnumType.上报埋点.首页40日预报点击降雨) {
            skipActivity(FortyWeatherActivity::class.java) {
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

        binding.tvAir.setSingleClickListener {
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

        binding.ctBottom.setSingleClickListener {
            getAppModel().wallpaperCode = "wallpaper_background"
            getParent()?.initScreenLock("wallpaper_background")
        }

        binding.fifListWeather.setSmartListener(FifWeatherListAdapter())
        binding.life.setSmartListener(LifeAdapter(activity.nN()))
        binding.hourWeather.setSmartListener(HourWeatherAdapter())
        binding.fifWeather.setSmartListener(FifWeatherAdapter())
    }

    override fun initListener() {
        super.initListener()
        binding.hourWeather.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_DRAGGING) {
                    clickReport(EnumType.上报埋点.二十四小时天气模块左右滑动)
                }
            }
        })

        binding.fifWeather.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(recyclerView: RecyclerView, newState: Int) {
                super.onScrollStateChanged(recyclerView, newState)
                if (newState == SCROLL_STATE_DRAGGING) {
                    clickReport(EnumType.上报埋点.十五日天气模块左右滑动)
                }
            }
        })
    }

    override fun initObserve() {
        super.initObserve()
        vm.weatherData.observe(this, {
            setErrorState(it.nN().wt)
            rainState(it)
            setWeather(it.nN())
            setWarnsAlert(it.nN())
            loadMoreWeather(it.nN())
        })
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
        if (it.falls != null && it.falls.nN().desc.isNotEmpty() && it.nN().isLocation) {
            binding.rainTv.text = it.nN().falls.nN().desc
        } else {
            binding.rainTv.text = "查看未来2小时降雨预报"
        }
        if (it.isLocation) {
            val drawable = resources.getDrawable(R.mipmap.icon_trend_location)
            binding.rainTv.setCompoundDrawablesWithIntrinsicBounds(drawable, null, null, null)
        } else {
            binding.rainTv.setCompoundDrawablesWithIntrinsicBounds(null, null, null, null)
        }
        if (TextUtils.isEmpty(it.wt)) {
            binding.weather.visibility = View.GONE
        } else {
            binding.weather.text = it.wt
            binding.weather.visibility = View.VISIBLE
        }
        if (TextUtils.isEmpty(it.tc)) {
            binding.temp.visibility = View.GONE
        } else {
            binding.temp.text = it.tc
            binding.temp.visibility = View.VISIBLE
        }
        if (TextUtils.isEmpty(it.wdir)) {
            binding.cloud.visibility = View.GONE
        } else {
            binding.cloud.text = it.wdir
            binding.cloud.visibility = View.VISIBLE
        }
        if (TextUtils.isEmpty(it.ws)) {
            binding.cloudLevel.visibility = View.GONE
        } else {
            binding.cloudLevel.text = it.ws
            binding.cloudLevel.visibility = View.VISIBLE
        }
        if (TextUtils.isEmpty(it.uvlv)) {
            binding.tvSun.visibility = View.GONE
            binding.sun.visibility = View.GONE
        } else {
            binding.tvSun.visibility = View.VISIBLE
            binding.sun.visibility = View.VISIBLE
            binding.tvSun.text = "紫外线"
            binding.sun.text = it.uvlv
        }
        binding.weatherIcon.setImageResource(WeatherUtils.weatherBigIcon(it.wtid))
        if (TextUtils.isEmpty(it.rh)) {
            binding.tvWeight.visibility = View.GONE
            binding.weight.visibility = View.GONE
        } else {
            binding.tvWeight.visibility = View.VISIBLE
            binding.weight.visibility = View.VISIBLE
            binding.tvWeight.text = "湿度"
            binding.weight.text = "${it.rh}%"
        }

        if (it.wtablesNew?.size == 2) {
            val day = it.nN().wtablesNew.nN()
                .sortedBy { wt -> DataUtil.Data2TimeStamp(wt.fct, "yyyy-MM-dd") }

            binding.todayTemp.text = day.listIndex(0).tcr
            binding.todayWeather.text = day.listIndex(0).wt
            binding.todayWeatherIcon.setImageResource(WeatherUtils.IconBig(day.listIndex(0).wtid))

            binding.tomorrowTemp.text = day.listIndex(1).tcr
            binding.tomorrowWeather.text = day.listIndex(1).wt
            binding.tomorrowWeatherIcon.setImageResource(WeatherUtils.IconBig(day.listIndex(1).wtid))
        }

        binding.tvAir.setImageBackGround(WeatherUtils.leafIcon(it.aqiLevel))
        if (null != it?.aqi && !TextUtils.isEmpty(it.aqi) && it.aqi.toFloat() > 500) {
            binding.tvAir.text = "  ${it.aqiLevel}500 "
        } else {
            binding.tvAir.text = "  ${it?.aqiLevel}${it?.aqi} "
        }
        if (it.aqi.isNotEmpty() && it.aqi != "0") {
            showReport(EnumType.上报埋点.空气质量模块展示)
        }

        binding.tvAir.isVisible(it.aqi.isNotEmpty() && it.aqi != "0")
        binding.ll24.isVisible(it.ybhs.nN().isNotEmpty())
    }

    private var isRainNeedAnim = false
    private fun rainState(it: WeatherBean?) {
        val rains = it.nN().falls.nN().rss.nN().filter { rain ->
            rain > 0.08f
        }

        if (rains.isNotEmpty()) {
            binding.rainChart.setData(it.nN().falls.nN().rss.nN())
            binding.rainChart.setWhiteTheme()
            binding.rainLayout.isVisible(true)
            binding.animRainLottie.imageAssetsFolder = "images"
            binding.animRainLottie.setAnimation(
                WeatherUtils.getAnimRainLottie(it.nN().falls.nN().rssPre, rains.listIndex(0))
            )
            binding.animRainLottie.playAnimation()
            isRainNeedAnim = true
            binding.rainChart.animShow()
        } else {
            isRainNeedAnim = false
            binding.rainLayout.isVisible(false)
            binding.animRainLottie.cancelAnimation()
        }
    }

    private fun changePageHeight() {
        binding?.mainView?.post {
            runOnUi {
                val height = binding.mainView.measuredHeight
                val params = binding.ctBottom.layoutParams
                params.height = if (height > 0) height else getScreenHeight()
                binding.ctBottom.layoutParams = params
            }
        }
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

    private fun changeFifChartState(isChart: Boolean) {
        if (isChart && binding.fifWeather.visibility == View.VISIBLE) return
        if (!isChart && binding.frameListFif.visibility == View.VISIBLE) return

        CacheUtil.put(Constant.SP_FIF_IS_CHART, isChart)
        binding.fifWeather.isVisible(isChart)
        binding.frameListFif.isVisible(!isChart)

        if (isChart) {
            binding.chartType.setBackgroundResource(R.drawable.shape_blue_sbg)
            binding.lineType.background = null
            binding.lineType.setTextColor((Color.parseColor("#82878B")))
            binding.chartType.setTextColor((Color.parseColor("#ffffff")))
        } else {
            binding.lineType.setBackgroundResource(R.drawable.shape_blue_sbg)
            binding.chartType.background = null
            binding.lineType.setTextColor((Color.parseColor("#ffffff")))
            binding.chartType.setTextColor((Color.parseColor("#82878B")))
        }
    }

    private fun loadMoreWeather(it: WeatherBean) {
        initCalendar()
        binding.fifWeather.notifyData(it.nN().ybds.nN())
        if (it.nN().ybds.nN().size >= 7 && !vm.isFifLoadMore) {
            binding.fifListWeather.notifyData(it.nN().ybds.nN().subList(0, 7))
        } else {
            binding.fifListWeather.notifyData(it.nN().ybds.nN())
        }

        changeFifChartState(CacheUtil.getBoolean(Constant.SP_FIF_IS_CHART, true))
        if (it.nN().ybhs.nN().isNotEmpty()) {
            binding.hourWeather.notifyData(it.nN().ybhs.nN())
        }
        setLife(it.nN())
        setCCTV(it)
    }

    private fun setLife(it: WeatherBean) {
        vm.lifeData.clear()
        vm.lifeData.addAll(it.nN().lifes.nN())
        binding.life.notifyData(vm.lifeData)
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
                    Glide.with(this).load(getAppControl().cctv.listIndex(0).cover).apply(
                        RequestOptions.bitmapTransform(
                            RoundedCorners(4)
                        ).override(0, 0)
                    ).error(R.mipmap.bg_cctv_img).into(binding.cctvImage)
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

    private fun initCalendar() {
        getAppModel().calendarDate = CalendarUtil.getCalendarDate2(LocalDate())
        lifecycleScope.async(Dispatchers.Main) {
            val bean = withContext(Dispatchers.IO) { CalendarHelper.getYIJI(LocalDate()) }
            binding.tvJi.text = bean.nN().ji.isStr("无")
            binding.tvYi.text = bean.nN().yi.isStr("无")
        }
        binding.tvSolarTerm.isVisible(!getAppModel().calendarDate.nN().solarTerm.isNullOrEmpty())
        binding.tvSolarTerm.text = getAppModel().calendarDate.nN().solarTerm
        binding.tvCalendarLunar.text =
            "${getAppModel().calendarDate.nN().lunar.nN().lunarMonthStr}${getAppModel().calendarDate.nN().lunar.nN().lunarDayStr}"

        binding.dateTime.text = "${
            DataUtil.timeStamp2Date(
                System.currentTimeMillis(),
                "MM月dd日"
            )
        }  |  ${DataUtil.getWeek2(System.currentTimeMillis())}"
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
//            WeatherFragment()
        }
        return null
    }

    private fun setErrorState(it: String) {
        Try {
            if (it.isEmpty()) {
                if (vm.emptyView == null) {
                    vm.emptyView = binding.layoutError.inflate()
                }
                binding.scrollview?.isVisible(false)
                binding.rel?.isVisible(false)
            } else {
                binding.scrollview?.isVisible(true)
                binding.rel?.isVisible(true)
                if (vm.emptyView != null && vm.emptyView.nN().visibility == View.VISIBLE) {
                    AnimationUtil.lightOutAnim(vm.emptyView.nN())
                }
            }
        }
    }

    public override fun onReLoad() {
        if (!isAdded) {
            return
        }
        super.onReLoad()
        loadPageOneAd()
        changeFifChartState(CacheUtil.getBoolean(Constant.SP_FIF_IS_CHART, true))
        if (isRainNeedAnim) {
            binding.animRainLottie.let {
                if (!binding.animRainLottie.isAnimating) {
                    binding.animRainLottie.playAnimation()
                }
            }
        }
        binding.warns.start()
        if (vm.weatherData.value.nN().refreshTime != vm.refreshTime) {
            vm.refreshTime = vm.weatherData.value.nN().refreshTime
            val weatherBean = vm.weatherData.value.nN()
            loadMoreWeather(weatherBean)
        }
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

    override fun reTry() {
        if (!isAdded) {
            return
        }
        super.reTry()
        getAppModel().refreshAction.value = EnumType.刷新类型.开始刷新
    }

    public override fun onHidden() {
        if (!isAdded) {
            return
        }
        super.onHidden()
        binding.warns.stop()
        binding.animRainLottie.let {
            if (binding.animRainLottie.isAnimating) {
                binding.animRainLottie.cancelAnimation()
            }
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
            binding.rel.isVisible(false)
            vm.emptyView?.findViewById<View>(R.id.btn_net_error)?.isVisible(true)
            vm.emptyView?.findViewById<View>(R.id.tv_net_error)?.isVisible(true)
            vm.emptyView?.findViewById<View>(R.id.btn_net_error)?.setSingleClickListener {
                getAppModel().refreshAction.value = EnumType.刷新类型.开始刷新
            }
        }
    }

    private fun loadPageOneAd() {
        binding.advIcon.loadAd()
        binding.advBanner.loadAd()
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
        if (binding.fortyWeatherLayout.getGlobalVisibleRect(Rect())) {
            showReport(EnumType.上报埋点.首页40日预报展示)
        }
    }

    fun upToScroll() {}

    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        if (null != savedInstanceState) {
            val translation = savedInstanceState?.getFloat("header_translationy")
            if (translation != 0f) {
                binding.rel.translationY = translation
            }
        }
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putFloat("header_translationy", binding.rel.translationY)
    }

    override fun injectBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentWeatherPageBinding = FragmentWeatherPageBinding.inflate(inflater, viewGroup, false)

    override fun onDestroy() {
        super.onDestroy()
        clearBinding()
    }

}