package com.zhangsheng.shunxin.weather.model

import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.net.callback.CallResult
import com.maiya.thirdlibrary.utils.CacheUtil
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.ext.net
import com.zhangsheng.shunxin.weather.livedata.SafeMutableLiveData
import com.zhangsheng.shunxin.weather.net.bean.AirBean
import com.zhangsheng.shunxin.weather.net.bean.FortyWeatherBean
import kotlin.math.abs

/**
 * @Description:
 * @Author:         zhangchun
 * @CreateDate:     2021/7/20
 * @Version:        1.0
 */
class ForthWeatherModel : BaseViewModel() {

    /**
     * 获取40日天气数据
     * @param regionCode 地区编码
     * @param regionName 地区名称
     */
    fun requestFortyDaysWeatherInfo(regionCode: String, regionName: String) {
        callNativeApi({ net().四十日天气预报(regionCode, regionName) },
            object : CallResult<FortyWeatherBean>() {
                override fun failed(code: Int, msg: String) {
                    super.failed(code, msg)
                    loadFail(code, msg)
                }

                override fun ok(result: FortyWeatherBean?) {
                    super.ok(result)
                    getAppModel().fortyWeatherBean.value = result
                }
            }
        )
    }
}