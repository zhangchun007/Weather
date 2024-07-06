package com.zhangsheng.shunxin.weather.fragment

import android.graphics.Color
import android.graphics.Rect
import android.util.TypedValue
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.LinearLayout
import androidx.lifecycle.Observer
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.SCROLL_STATE_DRAGGING
import androidx.viewpager.widget.ViewPager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.maiya.thirdlibrary.base.AacFragment
import com.maiya.thirdlibrary.net.bean.None
import com.maiya.thirdlibrary.utils.CacheUtil
import com.maiya.thirdlibrary.utils.DisplayUtil
import com.maiya.weather.information.bean.InfoFragmentSkip
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.calendar.util.CalendarHelper
import com.zhangsheng.shunxin.weather.MainActivity
import com.zhangsheng.shunxin.weather.activity.*
import com.zhangsheng.shunxin.ad.AdConstant
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.widget.shapview.ShapeView
import com.zhangsheng.shunxin.weather.adapter.FifWeatherAdapter
import com.zhangsheng.shunxin.weather.adapter.HourWeatherAdapter
import com.zhangsheng.shunxin.weather.adapter.LifeAdapter
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.*
import com.zhangsheng.shunxin.weather.livedata.LiveDataBus.getChannel
import com.zhangsheng.shunxin.weather.model.WeatherPageViewModel
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.utils.AnimationUtil
import com.zhangsheng.shunxin.weather.utils.DataUtil
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import com.zhangsheng.shunxin.weather.widget.TouchScrollView
import com.necer.utils.CalendarUtil
import com.zhangsheng.shunxin.ad.widget.AdvWeatherIconLayout
import com.zhangsheng.shunxin.calendar.activity.AlmanacActivity
import com.zhangsheng.shunxin.calendar.activity.CalendarActivity
import com.zhangsheng.shunxin.databinding.FragmentWeatherPageBinding
import com.zhangsheng.shunxin.information.adapter.InfoVpAdapter
import com.zhangsheng.shunxin.information.constant.Constants
import com.zhangsheng.shunxin.information.dialog.ChannelDialog
import com.zhangsheng.shunxin.information.utils.DensityUtil
import com.zhangsheng.shunxin.information.utils.DeviceUtil
import com.zhangsheng.shunxin.information.widget.tablayout.TabLayout
import com.zhangsheng.shunxin.weather.utils.NewsChannelDataUtils
import kotlinx.coroutines.async
import org.joda.time.LocalDate
import org.koin.android.ext.android.inject

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/7/22 17:42
 */
class WeatherPageFragment : AacFragment<WeatherPageViewModel,FragmentWeatherPageBinding>(R.layout.fragment_weather_page),
    TouchScrollView.OnScrollistener {
    override val vm: WeatherPageViewModel by inject()
    private var curPos: Int = 0
    private var shouldLoadInfo = true
    private var chanDialog: ChannelDialog? = null

    override fun initView() {
        changePageHeight()

        Try {
            vm.index = arguments.nN().getInt("index", -1)
            if (vm.index != -1 && WeatherUtils.getDatas().size > vm.index) {
                weatherUpdate(WeatherUtils.getDatas().listIndex(vm.index))
            }
        }

        initTabLayout()

        binding.scrollview.setOnScrollistener(this)

        binding.typhoon.ClickReport(EnumType.上报埋点.首页台风点击) {
            skipActivity(TyphoonActivity::class.java)
        }

        binding.warns.setClickListener {
            skipActivity(HighAlertActivity::class.java) {
                this.putExtra("index", binding.warns.getCurrentIndex())
            }
        }

        binding.clCalendar.ClickReport(EnumType.上报埋点.生活指数模块日历点击) {
            if (!isControl()) {
                skipActivity(CalendarActivity::class.java)
            }
        }
        binding.clAlmanac.ClickReport(EnumType.上报埋点.生活指数模块日历点击宜忌) {
            if (!isControl()) {
                skipActivity(AlmanacActivity::class.java)
            }
        }
        binding.today.ClickReport(EnumType.上报埋点.首页今天明天模块今天) {
            skipActivity(FifWeatherActivity::class.java) {
                putExtra("position", 1)
                putExtra("source", "hometoday-15xiangqin")
            }
        }
        binding.tomorrow.ClickReport(EnumType.上报埋点.首页今天明天模块明天) {
            skipActivity(FifWeatherActivity::class.java) {
                putExtra("position", 2)
                putExtra("source", "hometoday-15xiangqin")
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
                    var cctv = CacheUtil.getObj(Constant.SP_CCTV, WeatherBean.CctvBean::class.java)
                    putExtra("url", cctv.nN().video_url)
                    putExtra(
                        "CCTV_TIME",
                        cctv.nN().ptime
                    )
                }
            }
        }


        binding.hourWeather.setSmartListener(HourWeatherAdapter())
        binding.fifWeather.setSmartListener(FifWeatherAdapter())
        binding.life.setSmartListener(LifeAdapter(activity.nN()))
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

        //点击信息流频道
        binding.infoStream.ivChannel.setSingleClickListener {

            chanDialog = activity?.let {
                ChannelDialog(
                    it
                )
            }

            if (chanDialog != null) {
                chanDialog!!.setData(curPos)
                var isFinish = activity?.isFinishing
                if (isFinish != null && !isFinish) {
                    chanDialog!!.show()
                }
            }

        }

        binding.rlPageOne.setSingleClickListener {
            getAppModel().wallpaperCode = "wallpaper_background"
            getParent()?.initScreenLock("wallpaper_background")
        }

    }

    fun UpdateCardColor(colors: IntArray, isAnim: Boolean = false) {
        if (isAdded) {
            binding.weatherCard.UpdateCardColor(colors, isAnim)
        }
    }


    override fun initObserve() {
        super.initObserve()

        getAppModel().control.safeObserve(this, Observer {
            if (!isControlShow(getAppModel().control.value.nN().swtich_all.listIndex(0).infostream)) {
                binding.infoStream.llInfoStream.isVisible(false)
            }
        })
        vm.weatherData.safeObserve(this, Observer {
            if (it.typhoon == "1"){
                showReport(EnumType.上报埋点.首页台风展示)
                binding.typhoon.isVisible(true)
            }else{
                binding.typhoon.isVisible(false)
            }
            setErrorState(it.nN().wt)
            setWeather(it.nN())
            setWarnsAlert(it.nN())
            loadMoreWeather(it.nN())
        })
        getChannel<InfoFragmentSkip>("InfoFragmentSkip").safeObserve(this, Observer {
            initSmartTabLayout(it.nN().pos)
        })

        getChannel<None>("ScreenBean").safeObserve(this, Observer {
            if (chanDialog != null) {
                if (chanDialog!!.isShowing) {
                    var isFinish = activity?.isFinishing
                    if (isFinish != null && !isFinish) {
                        chanDialog?.dismiss()
                    }
                }
            }
            upToScroll()
        })
    }

    fun upToScroll() {
        if (isAdded) {
            if (!binding.scrollview.isTop())
                this.binding.scrollview?.upToScroll()
        }
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
                    binding.warns.initData(warnsData)
                }
            }
        }
    }

    private fun setWeather(it: WeatherBean) {
        binding.weatherCard.upDateWeather(it)

        if (it.nN().wtablesNew.nN().size == 2) {
            var day = it.nN().wtablesNew.nN()
                .sortedBy { wt -> DataUtil.Data2TimeStamp(wt.fct, "yyyy-MM-dd") }
            binding.todayTemp.text = day.listIndex(0).tcr
            binding.todayWeather.text = day.listIndex(0).wt
            binding.todayWeatherIcon.setImageResource(WeatherUtils.IconBig(day.listIndex(0).wtid))
            binding.llToday.gravity =
                if (day.listIndex(0).wt.nN().length > 2) Gravity.RIGHT else Gravity.CENTER

            binding.tomorrowTemp.text = day.listIndex(1).tcr
            binding.tomorrowWeather.text = day.listIndex(1).wt
            binding.tomorrowWeatherIcon.setImageResource(WeatherUtils.IconBig(day.listIndex(1).wtid))

            binding.llTomorrow.gravity =
                if (day.listIndex(1).wt.nN().length > 2) Gravity.RIGHT else Gravity.CENTER

        }
        binding.ll24.isVisible(it.nN().ybhs.nN().isNotEmpty())

    }


    private fun changePageHeight() {
        getParent()?.getRootView()?.post {
            runOnUi {
                var height = getParent()?.getRootView().nN().measuredHeight
                var params = binding.rlPageOne.layoutParams as LinearLayout.LayoutParams
                params.width = DisplayUtil.getScreenWidth()
                params.height = if (height > 0) height else DisplayUtil.getScreenHeight()
                binding.rlPageOne.layoutParams = params
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


    private fun loadMoreWeather(it: WeatherBean) {
        initCalendar()
        binding.fifWeather.notifyData(it.nN().ybds.nN())
        if (it.nN().ybhs.nN().isNotEmpty()) {
            binding.hourWeather.notifyData(it.nN().ybhs.nN())
            binding.timeSunUp.text = it.nN().ybhs.listIndex(0).sunrise
            binding.timeSunDown.text = it.nN().ybhs.listIndex(0).sunset
        }
        setLife(it.nN())
        setCCTV(it)
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

    private fun setErrorState(it: String) {
        Try {
            if (it.isEmpty()) {
                if (vm.emptyView == null) {
                    vm.emptyView = binding.layoutError.inflate()
                }
                binding.scrollview.isVisible(false)
            } else {
                binding.scrollview.isVisible(true)
                if (vm.emptyView != null && vm.emptyView.nN().visibility == View.VISIBLE) {
                    AnimationUtil.lightOutAnim(vm.emptyView.nN())
                }
            }
        }
    }

    private fun setLife(it: WeatherBean) {
        vm.lifeData.clear()
        vm.lifeData.addAll(it.nN().lifes.nN())
        if (vm.lifeData.size % 3 > 0) {
            vm.lifeData.add(WeatherBean.LifesBean())
        }
        binding.life.notifyData(vm.lifeData)
    }

    private fun setCCTV(it: WeatherBean) {
        if (isControlShow(getAppControl().swtich_all.listIndex(0).report) && !isControl()) {
            var cctv = if (it.cctv.nN().video_url.isNotEmpty()) it.cctv.nN() else CacheUtil.getObj(
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
        getAppModel().calendarDate.init {
            getAppModel().calendarDate = CalendarUtil.getCalendarDate2(LocalDate())
        }
        getAppModel().calendarYJdata.init {
            lifecycleScope.async {
                getAppModel().calendarYJdata = CalendarHelper.getYIJI(LocalDate())
            }
        }
        binding.tvSolarTerm.isVisible(!getAppModel().calendarDate.nN().solarTerm.isNullOrEmpty())
        binding.tvSolarTerm.text = getAppModel().calendarDate.nN().solarTerm
        binding.tvCalendarLunar.text =
            "${getAppModel().calendarDate.nN().lunar.nN().lunarMonthStr}${getAppModel().calendarDate.nN().lunar.nN().lunarDayStr}"

        binding.clAlmanac.isVisible(isControlShow(getAppControl().swtich_all.listIndex(0).life))
        binding.tvJi.text = getAppModel().calendarYJdata.nN().ji.isStr("无")
        binding.tvYi.text = getAppModel().calendarYJdata.nN().yi.isStr("无")
        binding.dateTime.text = "${
            DataUtil.timeStamp2Date(
                System.currentTimeMillis(),
                "MM月dd日"
            )
        }  |  ${DataUtil.getWeek2(System.currentTimeMillis())}"
    }


    public override fun onHidden() {
        if (!isAdded) {
            return
        }
        super.onHidden()
        binding.warns.stop()
        binding.weatherCard.stopRainAnim()
    }

    override fun reTry() {
        if (!isAdded) {
            return
        }
        super.reTry()
        getAppModel().refreshAction.value = EnumType.刷新类型.开始刷新
    }

    public override fun onReLoad() {
        if (!isAdded) {
            return
        }
        super.onReLoad()
        Try {
            loadPageOneAd()
            binding.weatherCard.startRainAnim()
            binding.warns.start()
            if (vm.weatherData.value.nN().refreshTime != vm.refreshTime) {
                vm.refreshTime = vm.weatherData.value.nN().refreshTime
                var weatherBean = vm.weatherData.value.nN()
                loadMoreWeather(weatherBean)
            }
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
            var weatherBean = vm.weatherData.value.nN()
            loadMoreWeather(weatherBean)
        }
    }


    //region ->广告
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
    }

    //endregion

    //region ->信息流
    private fun initTabLayout() {
        if (isControlShow(getAppModel().control.value.nN().swtich_all.listIndex(0).infostream) && !isControl()) {
            dealWithViewPager()
            binding.infoStream.llInfoStream.isVisible(true)
            Constants.curTabCode = "__all__"
        } else {
            binding.infoStream.llInfoStream.isVisible(false)
        }
    }

    private fun initSmartTabLayout(pos: Int) {
        curPos = pos
        var informAdapter = InfoVpAdapter(childFragmentManager)
        binding.infoStream.weatherNewsViewpager.adapter = informAdapter
        //处理数据
        if (informAdapter.count > 0) {
            binding.infoStream.weatherNewsViewpager.currentItem = 0
            return
        }
        var tabList = context?.let { NewsChannelDataUtils.getNewTabList(it) }
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

    override fun stick(isStick: Boolean) {
        if (!isAdded) {
            return
        }
        if (isStick) {
            showReport(EnumType.上报埋点.首页信息流频道展示)
        }

        getParent()?.showInfoBar(isStick)
    }

    override fun onScrollState(state: Int) {
        super.onScrollState(state)
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

    override fun injectBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): FragmentWeatherPageBinding =FragmentWeatherPageBinding.inflate(inflater,viewGroup,false)

    override fun onDestroy() {
        super.onDestroy()
        clearBinding()
    }

    //endregion


}