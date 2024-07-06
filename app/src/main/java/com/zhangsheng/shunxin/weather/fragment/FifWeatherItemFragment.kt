package com.zhangsheng.shunxin.weather.fragment

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.maiya.thirdlibrary.widget.smartlayout.listener.SmartRecycleListener
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.ad.AdConstant
import com.maiya.thirdlibrary.base.AacFragment
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.widget.smartlayout.adapter.SmartViewHolder
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import com.zhangsheng.shunxin.databinding.ItemFifWeatherVpBinding
import org.koin.android.ext.android.inject


/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/5/19 21:59
 */
class FifWeatherItemFragment : AacFragment<BaseViewModel,ItemFifWeatherVpBinding>(R.layout.item_fif_weather_vp) {
    override val vm: BaseViewModel by inject()
    private var position = 0
    override fun initView() {

        Try {
            position = arguments.nN().getInt("position")
        }

        binding.gvWeather.setSmartListener(object : SmartRecycleListener() {
            override fun AutoAdapter(
                holder: SmartViewHolder,
                item: Any,
                position: Int
            ) {
                super.AutoAdapter(holder, item, position)
                val bean = item as WeatherBean.PredictionBean.DescsBeanX
                holder.findView<View>(R.id.divider)
                    .isVisible(!(position == 1 || position == 3 || position == 5))
                holder.setTextViewText(R.id.name, bean.name)
                holder.setTextViewText(R.id.detail, bean.desc)
                    .setImageRes(R.id.icon, WeatherUtils.getWeatherIcon(bean.name))
            }
        })
        val bean =
            getAppModel().currentWeather.value.nN().weather.nN().prediction.listIndex(position)
        binding.weatherIcon.setImageResource(WeatherUtils.IconBig(bean.wtid))
        binding.gvWeather.notifyData(bean.descs.nN())
        binding.temp.text = bean.tcr.nN().replace("°", "")
        binding.weather.text = "${bean.wt}"
    }

    override fun onResume() {
        super.onResume()
        if (userVisibleHint){
            requestAd()
        }
    }



    private fun requestAd() {
        binding.advMaterialView.showFeedAd(activity, AdConstant.SLOT_BIGDRAWJMXQ)
    }

    override fun injectBinding(
        inflater: LayoutInflater,
        viewGroup: ViewGroup?
    ): ItemFifWeatherVpBinding =ItemFifWeatherVpBinding.inflate(inflater,viewGroup,false)

    override fun onDestroy() {
        super.onDestroy()
        clearBinding()
    }
}