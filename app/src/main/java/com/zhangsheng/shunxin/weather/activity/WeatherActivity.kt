package com.zhangsheng.shunxin.weather.activity

import android.os.Bundle
import android.view.View
import androidx.lifecycle.Observer
import com.maiya.thirdlibrary.widget.smartlayout.listener.SmartRecycleListener
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.ad.AdConstant
import com.maiya.thirdlibrary.base.AacActivity
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.*
import com.maiya.thirdlibrary.widget.smartlayout.adapter.SmartViewHolder
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.ext.LocationEllipsis
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.utils.DataUtil
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import com.zhangsheng.shunxin.databinding.ActivityWeatherBinding
import org.koin.android.ext.android.inject

class WeatherActivity : AacActivity<BaseViewModel>() {

    override val vm: BaseViewModel by inject()
    override val binding by inflate<ActivityWeatherBinding>()

    private var desc: ArrayList<WeatherBean.HomeDetailBean.DescsBean> = ArrayList()


    private fun dealData() {
        getAppModel().currentWeather.safeObserve(this, Observer {
            binding.temp.text = it.nN().weather.nN().homeDetail.nN().tc.isStr("--")
            binding.time.text = "${DataUtil.timeStamp2Date(
                DataUtil.date2Long(it.nN().weather.nN().homeDetail.nN().fct),
             
                "HH:mm"
            )}发布"
            binding.des.text="体感: ${it.nN().weather.nN().homeDetail.nN().stc.isStr("--")}°  |  湿度: ${it.nN().weather.nN().homeDetail.nN().rh.isStr("--")}"
            if (it.nN().weather.nN().homeDetail.nN().zs.nN().isNotEmpty()){
                binding.content.text ="\"${it.nN().weather.nN().homeDetail.nN().zs}\""
            }
            desc.clear()
            desc.addAll(it.nN().weather.nN().homeDetail.nN().descs.nN())
            binding.gvWeather.notifyData(desc)
            binding.icon.setImageResource(WeatherUtils.IconBig(it.nN().weather.nN().homeDetail.nN().wtid))
            binding.weather.text = it.nN().weather.nN().homeDetail.nN().wt
        })
    }
    override fun initView(savedInstanceState: Bundle?) {
        if ( getAppModel().refreshAction.value==EnumType.刷新类型.网络错误){
            xToast("网络异常，请检查网络")
        }

        binding.title.setTitleBgColor("#379BFF")
        dealData()
        if (getAppModel().currentWeather.value.nN().weather.nN().isLocation) {
            binding.title.setIcon(R.mipmap.icon_location)
        }

        binding.title.initTitle(LocationEllipsis(getAppModel().currentWeather.value.nN().weather.nN().regionname,getAppModel().currentWeather.value.nN().weather.nN().isLocation),"#ffffff")

        binding.gvWeather.setSmartListener(object : SmartRecycleListener(){
            override fun AutoAdapter(holder: SmartViewHolder, item: Any, position: Int) {
                super.AutoAdapter(holder, item, position)
                var bean =item as WeatherBean.HomeDetailBean.DescsBean
                holder.findView<View>(R.id.divider)
                    .isVisible(!(position == 1 || position == 3 || position == 5))
                holder.setTextViewText(R.id.name,bean.name )
                holder.setTextViewText(R.id.detail,bean.desc.isStr("--"))
                    .setImageRes(R.id.icon,WeatherUtils.getWeatherIcon(bean.name))
            }
        })
    }

    override fun onResume() {
        super.onResume()
        loadAd()
    }
    private fun loadAd() {
        binding.advMaterialView.showFeedAd(this,AdConstant.SLOT_BIGDRAWDRTQ)
    }


}