package com.zhangsheng.shunxin.weather.model

import android.app.Activity
import android.content.Context
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import com.maiya.thirdlibrary.net.callback.CallResult
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.ext.xToast
import com.zhangsheng.shunxin.weather.common.EnumType
import com.zhangsheng.shunxin.weather.db.city_db.InnerJoinResult
import com.zhangsheng.shunxin.weather.ext.getAppControl
import com.zhangsheng.shunxin.weather.ext.net
import com.zhangsheng.shunxin.weather.ext.showReport
import com.zhangsheng.shunxin.weather.livedata.SafeMutableLiveData
import com.zhangsheng.shunxin.weather.net.bean.*
import com.zhangsheng.shunxin.weather.utils.WeatherUtils

class CitySelectViewModel : BaseViewModel() {
    var hotCity = ArrayList<ControlBean.HotCityBean>()
    var IpLocation = SafeMutableLiveData<IpLocation>()
    var cityDbSearch: List<InnerJoinResult>? = null
    var provinceDbSearch: List<InnerJoinResult>? = null
    var searchlist: ArrayList<InnerJoinResult> = ArrayList()
    var isEt: Boolean = false


    val data by lazy {
        arrayOf(
            ControlBean.HotCityBean("", "江苏"),
            ControlBean.HotCityBean("", "山东"),
            ControlBean.HotCityBean("", "浙江"),
            ControlBean.HotCityBean("", "广东"),
            ControlBean.HotCityBean("", "湖南"),
            ControlBean.HotCityBean("", "河南"),
            ControlBean.HotCityBean("", "湖北"),
            ControlBean.HotCityBean("", "四川"),
            ControlBean.HotCityBean("", "上海"),
            ControlBean.HotCityBean("", "安徽"),
            ControlBean.HotCityBean("", "北京"),
            ControlBean.HotCityBean("", "河北"),
            ControlBean.HotCityBean("", "辽宁"),
            ControlBean.HotCityBean("", "广西"),
            ControlBean.HotCityBean("", "江西"),
            ControlBean.HotCityBean("", "福建"),
            ControlBean.HotCityBean("", "陕西"),
            ControlBean.HotCityBean("", "黑龙江"),
            ControlBean.HotCityBean("", "贵州"),
            ControlBean.HotCityBean("", "重庆"),
            ControlBean.HotCityBean("", "山西"),
            ControlBean.HotCityBean("", "云南"),
            ControlBean.HotCityBean("", "吉林"),
            ControlBean.HotCityBean("", "新疆"),
            ControlBean.HotCityBean("", "内蒙古"),
            ControlBean.HotCityBean("", "甘肃"),
            ControlBean.HotCityBean("", "天津"),
            ControlBean.HotCityBean("", "海南"),
            ControlBean.HotCityBean("", "宁夏"),
            ControlBean.HotCityBean("", "青海"),
            ControlBean.HotCityBean("", "西藏"),
            ControlBean.HotCityBean("", "香港"),
            ControlBean.HotCityBean("", "台湾"),
            ControlBean.HotCityBean("", "澳门")
        )
    }

    fun initHot() {
        if (getAppControl().hot_city.nN().isNotEmpty()) {
            if (getAppControl().hot_city.nN().size > 6) {
                hotCity.addAll(getAppControl().hot_city.nN().subList(0, 6))
            } else {
                hotCity.addAll(getAppControl().hot_city.nN())
            }

        } else {
            hotCity.add(ControlBean.HotCityBean("310000", "上海"))
            hotCity.add(ControlBean.HotCityBean("110000", "北京"))
            hotCity.add(ControlBean.HotCityBean("440100", "广州"))
            hotCity.add(ControlBean.HotCityBean("440300", "深圳"))
            hotCity.add(ControlBean.HotCityBean("420100", "武汉"))
            hotCity.add(ControlBean.HotCityBean("330100", "杭州"))

//            hotCity.add(ControlBean.HotCityBean().apply {
//                code = "320100"
//                title = "南京"
//            })
//
//
//            hotCity.add(ControlBean.HotCityBean().apply {
//                code = "510100"
//                title = "成都"
//            })
//
//            hotCity.add(ControlBean.HotCityBean().apply {
//                code = "320500"
//                title = "苏州"
//            })
        }
    }


    fun requestIpLocation() {
        callNativeApi({
            net().获取IP定位()
        }, object : CallResult<IpLocation>() {
            override fun ok(result: IpLocation?) {
                super.ok(result)
                IpLocation.value = result
            }

            override fun failed(code: Int, msg: String) {
                super.failed(code, msg)
                showReport(EnumType.上报埋点.城市添加ip接口错误, "$code")
            }
        })
    }


    fun chooseCity(context: Activity, code: String, name_cn: String) {
        var copy = WeatherUtils.getDatas().filter { weathers ->
            weathers.regioncode == code
        }
        when {
            copy.isNotEmpty() -> {
                xToast("当前城市已存在")
            }
            WeatherUtils.getDatas().size >= 9 -> {
                xToast("最多只能添加9个城市")
            }
            else -> {
                WeatherUtils.addWeather(WeatherBean().apply {
                    this.regioncode = code
                    this.regionname = name_cn
                    this.refreshTime = System.currentTimeMillis()
                })
                context.setResult(200, context.intent)
                context.finish()
            }
        }
    }

    fun hideInputTips(et_text: EditText) {
        val imm = et_text.context.getSystemService(Context.INPUT_METHOD_SERVICE)
                as? InputMethodManager ?: return
        imm.hideSoftInputFromWindow(et_text.windowToken, 0)
    }

}