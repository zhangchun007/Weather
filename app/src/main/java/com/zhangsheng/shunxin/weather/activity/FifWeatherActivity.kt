package com.zhangsheng.shunxin.weather.activity

import android.graphics.Color
import android.os.Bundle
import android.os.Parcelable
import android.util.TypedValue
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentStatePagerAdapter
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.maiya.thirdlibrary.base.AacActivity
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.widget.smartlayout.adapter.SmartViewHolder
import com.maiya.thirdlibrary.widget.smartlayout.layoutmanager.CenterLayoutManager
import com.maiya.thirdlibrary.widget.smartlayout.listener.SmartRecycleListener
import com.xm.xmlog.XMLogAgent
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.databinding.ActivityFifWeatherBinding
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.LocationEllipsis
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.fragment.FifWeatherItemFragment
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.utils.DataUtil
import kotlinx.android.synthetic.main.activity_fif_weather.*
import org.koin.android.ext.android.inject

class FifWeatherActivity : AacActivity<BaseViewModel>() {

    override val vm: BaseViewModel by inject()
    override val binding by inflate<ActivityFifWeatherBinding>()

    private var index: Int = 0
    private var viewSize=0


    override fun initView(savedInstanceState: Bundle?) {
        if (getAppModel().refreshAction.value == EnumType.刷新类型.网络错误) {
            xToast("网络异常，请检查网络")
        }
        Try {
            index = intent.getIntExtra("position", 0)
        }
        binding.title.initTitle(
            LocationEllipsis(
                getAppModel().currentWeather.value.nN().weather.nN().regionname,
                getAppModel().currentWeather.value.nN().weather.nN().isLocation
            ), "#ffffff"
        )
        binding.title.setTitleBgColor("#00000000")
        if (getAppModel().currentWeather.value.nN().weather.nN().isLocation) {
            binding.title.setIcon(R.mipmap.icon_location)
        }

        tab_view.setSmartListener(object : SmartRecycleListener() {
            override fun AutoAdapter(holder: SmartViewHolder, item: Any, position: Int) {
                super.AutoAdapter(holder, item, position)
                var bean = item as WeatherBean.PredictionBean
                var bg = holder.findView<LinearLayout>(R.id.ll_bg)
                var time = holder.findView<TextView>(R.id.time)
                var date = holder.findView<TextView>(R.id.date)

                time.text = DataUtil.isToday(DataUtil.date2Long(bean.nN().fct, "yyyy-MM-dd"))
                date.text = DataUtil.timeStamp2Date(
                    DataUtil.date2Long(bean.nN().fct, "yyyy-MM-dd"),
                    "MM-dd"
                ).replace("-", "/")

                if (index == position) {
                    holder.setVisibility(R.id.tag, View.VISIBLE)
                    time.setTextColor(Color.parseColor("#ffffff"))
                    date.setTextColor(Color.parseColor("#ffffff"))
                    time.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
                    date.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 16f)
                } else {
                    holder.setVisibility(R.id.tag, View.GONE)
                    time.setTextColor(Color.parseColor("#80ffffff"))
                    date.setTextColor(Color.parseColor("#80ffffff"))
                    time.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
                    date.setTextSize(TypedValue.COMPLEX_UNIT_DIP, 14f)
                }
                bg.setSingleClickListener {

                    if (index != position) {
                        index = position
                        tab_view.notifyData(getAppModel().currentWeather.value.nN().weather.nN().prediction.nN())
                        if (getAppModel().currentWeather.value.nN().weather.nN().prediction.nN().size > 2) {
                            (tab_view.layoutManager as CenterLayoutManager).smoothScrollToPosition(tab_view,
                                RecyclerView.State(),index)
                        }
                        vp.currentItem = index
                    }
                }
            }
        })


        binding.vp.adapter =object : FragmentStatePagerAdapter(supportFragmentManager,
        FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT){
            override fun getCount(): Int =viewSize

            override fun getItem(position: Int): Fragment =FifWeatherItemFragment().apply {
                arguments = Bundle().apply {
                    putInt("position", position)
                }
            }

            override fun getItemPosition(fragment: Any): Int= PagerAdapter.POSITION_UNCHANGED

            override fun saveState(): Parcelable? {
                return null
            }
        }
        binding.vp.addOnPageChangeListener(object:ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                if (index != position) {
                    index = position
                    tab_view.notifyData(getAppModel().currentWeather.value.nN().weather.nN().prediction.nN())
                    if (getAppModel().currentWeather.value.nN().weather.nN().prediction.nN().size > 2) {
                        (tab_view.layoutManager as CenterLayoutManager).smoothScrollToPosition(tab_view,
                            RecyclerView.State(),index)
                    }
                }
            }

        })
    }

    override fun initObserve() {
        super.initObserve()
        getAppModel().currentWeather.safeObserve(this, Observer {
            viewSize= it.weather.nN().prediction.nN().size
            vp.adapter?.notifyDataSetChanged()
            vp.nN().setCurrentItem(index, false)
            tab_view.notifyData(getAppModel().currentWeather.value.nN().weather.nN().prediction.nN())
            if (getAppModel().currentWeather.value.nN().weather.nN().prediction.nN().size > 2) {
                tab_view.scrollToPosition(index - 2)
            }
        })
    }

    override fun onResume() {
        super.onResume()
        XMLogAgent.onPageStart(EnumType.上报埋点.十五日天气详情页)
    }

    override fun onPause() {
        super.onPause()
        XMLogAgent.onPageEnd(EnumType.上报埋点.十五日天气详情页)
    }
}