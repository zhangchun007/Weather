package com.zhangsheng.shunxin.weather.widget.weather

import android.content.Context
import android.util.AttributeSet
import android.view.LayoutInflater
import android.widget.RelativeLayout
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.bumptech.glide.request.RequestOptions
import com.maiya.thirdlibrary.ext.isStr
import com.maiya.thirdlibrary.ext.isVisible
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.ext.parseDouble
import com.maiya.thirdlibrary.utils.CacheUtil
import com.maiya.thirdlibrary.utils.DisplayUtil
import com.my.business.imp.plu.IPluViewListener
import com.zhangsheng.shunxin.databinding.LayoutWeatherForecastBinding
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.livedata.LiveDataBus
import com.zhangsheng.shunxin.weather.net.bean.Location
import com.zhangsheng.shunxin.weather.net.bean.PushCityBean
import com.zhangsheng.shunxin.weather.net.bean.WeatherForecastBean
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import org.json.JSONObject

class WeatherForecast @JvmOverloads constructor(
    context: Context, attrs: AttributeSet? = null, defStyleAttr: Int = 0
) : RelativeLayout(context, attrs, defStyleAttr) {
    private val binding =
        LayoutWeatherForecastBinding.inflate(LayoutInflater.from(context), this, true)
    private val CHANNEL = "WeatherForecast"
    private var listener: IPluViewListener? = null
    private var code: String = "1"
    private var iconUrl:String=""
    private var iconName:String=""


    fun requestDate(param: JSONObject) {
        var code = param.optString("code", "")
        iconUrl = param.optString("iconurl", "")
        iconName = param.optString("iconname", "")
        requestDate(code) { data ->
            initView(data)
        }
    }

    fun setIPluViewListener(listener: IPluViewListener) {
        this.listener = listener
    }

    private fun initView(bean: WeatherForecastBean) {
        listener?.onPassVerification()
        if (iconUrl.isNotEmpty()){
            Glide.with(binding.iconView).load(iconUrl).into(binding.iconView)
        }

        if (iconName.isNotEmpty()){
            binding.iconTitle.text=iconName
        }
        binding.title.text = if (code == "1") "今天白天天气" else "今夜到明天天气"
        binding.tvLocation.text = bean.region
        binding.tvLocation.isSelected=true
        Glide.with(this).load(WeatherUtils.getWeatherForecastBg(bean.wtid,code != "1")).centerCrop()
            .apply(RequestOptions.bitmapTransform(RoundedCorners(DisplayUtil.dip2px(16f))))
            .into(binding.bgWeather)
        binding.tvWeather.text = bean.wt
        if (bean.wtid.isNotEmpty()){
            binding.iconWeather.setImageResource(WeatherUtils.getWeatherForecastIcon(bean.wtid,code != "1"))
        }

        binding.tvTemp.text = "${bean.wt1tc.isStr("-")}/${bean.wt2tc.isStr("-")}°"

        binding.tvWind.isVisible(!(bean.wdir.isEmpty() || bean.ws.isEmpty()))

        binding.tvWind.text = "${bean.wdir}${bean.ws}"
        binding.tvAir.isVisible(bean.aqidesc.isNotEmpty())
        binding.tvAir.text = "空气质量${bean.aqidesc}"

        listener?.onViewRenderSuccess(this, binding.exLayout, binding.close)
    }

    private fun requestDate(codeStr: String, initView: (WeatherForecastBean) -> Unit) {
        LiveDataBus.unRegist(CHANNEL)
        code = if (codeStr == "morning_reminder") "1" else "2"
        var location: Location = Location()
        var region=""
        LiveDataBus.getChannel<Location>(CHANNEL).observeForever { local ->
            if (local != null && local.state == 1 && local.lat.isNotEmpty() && local.lng.isNotEmpty()) {
                location = local
                region = local.district.isStr(local.city)
            }
            getAppModel().requestWeatherForecast(region, location, initView, code)
        }
        getAppModel().location(CHANNEL)
    }


    override fun onDetachedFromWindow() {
        super.onDetachedFromWindow()
        LiveDataBus.unRegist(CHANNEL)
    }


}