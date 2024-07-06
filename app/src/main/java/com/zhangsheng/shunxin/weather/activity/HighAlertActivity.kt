package com.zhangsheng.shunxin.weather.activity

import android.graphics.Color
import android.os.Bundle
import android.text.method.ScrollingMovementMethod
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.viewpager.widget.PagerAdapter
import androidx.viewpager.widget.ViewPager
import com.maiya.thirdlibrary.utils.PicUtils
import com.maiya.thirdlibrary.widget.smartlayout.listener.SmartRecycleListener
import com.maiya.thirdlibrary.widget.shapview.ShapeView
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.ad.AdConstant
import com.maiya.thirdlibrary.base.AacActivity
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.widget.smartlayout.adapter.SmartViewHolder
import com.zhangsheng.shunxin.weather.ext.LocationEllipsis
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import com.xm.xmlog.XMLogAgent
import com.zhangsheng.shunxin.databinding.ActivityHighAlertBinding
import com.zhangsheng.shunxin.weather.common.EnumType
import org.koin.android.ext.android.inject

class HighAlertActivity : AacActivity<BaseViewModel>() {
    override val vm: BaseViewModel by inject()
    override val binding by inflate<ActivityHighAlertBinding>()

    private var views = ArrayList<View>()
    private var index = 0
    private var warnsData: List<WeatherBean.Warns>? = null


    override fun initView(savedInstanceState: Bundle?) {
        binding.title.initTitle(
            LocationEllipsis(
                getAppModel().currentWeather.value.nN().weather.nN().regionname,
                getAppModel().currentWeather.value.nN().weather.nN().isLocation
            ), "#ffffff"
        )

        if (getAppModel().currentWeather.value.nN().weather.nN().isLocation) {
            binding.title.setIcon(R.mipmap.icon_location)
        }
        binding.title.setTitleBgColor("#00000000")
        Try {
            warnsData = getAppModel().currentWeather.value.nN().weather.nN().warns.nN()
            if (warnsData.nN().size > 4) {
                warnsData = warnsData.nN().subList(0, 4)
            }
            index = intent.getIntExtra("index", 0)
            binding.tab.notifyData(warnsData.nN())

        }

        binding.tab.setSmartListener(object : SmartRecycleListener() {
            override fun AutoAdapter(holder: SmartViewHolder, item: Any, position: Int) {
                super.AutoAdapter(holder, item, position)
                var data = item as WeatherBean.Warns


                holder.findView<FrameLayout>(R.id.root).apply {
                    this.layoutParams = this.layoutParams.apply {
                        this.width = binding.tab.measuredWidth / warnsData.nN().size
                    }
                }
                var tv_tab = holder.findView<ShapeView>(R.id.tv_tab)
                tv_tab.setTextColor(WeatherUtils.hightAlertColor(data.level))
                tv_tab.text = data.type
                var color = holder.findView<ImageView>(R.id.color_view)
                PicUtils.tintColor(color, WeatherUtils.hightAlertColorStr(data.level))

                if (index == position) {
                    tv_tab.exeConfig(tv_tab.getConfig().apply {
                        bgColor = Color.parseColor("#FFFFFF")
                    })
                    color.visibility = View.VISIBLE
                } else {
                    tv_tab.exeConfig(tv_tab.getConfig().apply {
                        bgColor = Color.parseColor("#00000000")
                    })
                    color.visibility = View.INVISIBLE
                }
                tv_tab.setSingleClickListener {
                    if (index != position) {
                        index = position
                        binding.vp.currentItem = index
                        binding.tab.notifyData(warnsData.nN())
                    }
                }
            }
        })


        warnsData.nN().forEachIndexed { index, warns ->
            var layout = LayoutInflater.from(this).inflate(R.layout.item_high_alert, null)
            views.add(layout)

        }

        binding.vp.adapter = object : PagerAdapter() {
            override fun getCount(): Int = views.size
            override fun isViewFromObject(p0: View, p1: Any): Boolean = p0 == p1
            override fun instantiateItem(container: ViewGroup, position: Int): Any {
                container.addView(views[position])

                initVpView(position)
                return views[position]
            }

            override fun destroyItem(container: ViewGroup, position: Int, `object`: Any) {
                container.removeView(views[position])
            }
        }
        binding.vp.currentItem = index
        binding.vp.addOnPageChangeListener(object : ViewPager.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {}
            override fun onPageScrolled(
                position: Int,
                positionOffset: Float,
                positionOffsetPixels: Int
            ) {
            }

            override fun onPageSelected(position: Int) {
                index = position
                binding.tab.notifyData(warnsData.nN())
                loadAd()
            }

        })
    }

    private fun initVpView(position: Int) {
        var data = warnsData.nN().listIndex(position)
        var view = views[position]

        view.findViewById<LinearLayout>(R.id.ll_protect).isVisible(data.prevention.isNotEmpty())
        view.findViewById<TextView>(R.id.tv_protect).text = data.prevention
        view.findViewById<ShapeView>(R.id.warns_color).apply {
            this.exeConfig(this.getConfig().apply {
                this.bgColor = WeatherUtils.hightAlertColor(data.level)
            })
        }
        view.findViewById<TextView>(R.id.hint).apply {
            this.text = "${data.type}${data.level}预警"
        }
        view.findViewById<TextView>(R.id.time).apply {
            this.text = "${data.time}发布"
        }

        view.findViewById<TextView>(R.id.content).apply {
            this.text = data.desc
            this.movementMethod = ScrollingMovementMethod.getInstance()
        }
        view.findViewById<ImageView>(R.id.img)
            .setBackgroundResource(WeatherUtils.hightAlertIcon(data.type))

        var process = when (data.level) {
            "蓝色" -> view.findViewById<View>(R.id.tag1)
            "黄色" -> view.findViewById<View>(R.id.tag2)
            "橙色" -> view.findViewById<View>(R.id.tag3)
            "红色" -> view.findViewById<View>(R.id.tag4)
            else -> view.findViewById<View>(R.id.tag1)
        }

        var tag = view.findViewById<ImageView>(R.id.tag)
        tag.post {
            tag.layoutParams = (tag.layoutParams as LinearLayout.LayoutParams).apply {
                this.leftMargin = process.left + process.width / 2 - tag.width / 2
            }
        }

    }

    private fun loadAd() {
        binding.advBig.showFeedAd(this,AdConstant.SLOT_BIGYJEJYM)
    }

    override fun onResume() {
        super.onResume()
        loadAd()
        XMLogAgent.onPageStart(EnumType.上报埋点.天气预警详情页)
    }

    override fun onDestroy() {
        super.onDestroy()
        XMLogAgent.onPageEnd(EnumType.上报埋点.天气预警详情页)
    }

}