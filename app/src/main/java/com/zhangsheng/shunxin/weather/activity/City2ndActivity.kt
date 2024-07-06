package com.zhangsheng.shunxin.weather.activity

import android.content.Intent
import android.os.Bundle
import com.maiya.thirdlibrary.base.AacActivity
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.*
import com.zhangsheng.shunxin.R
import com.zhangsheng.shunxin.databinding.ActivityCity2ndBinding
import com.zhangsheng.shunxin.weather.adapter.CityList2ndAdapter
import com.zhangsheng.shunxin.weather.db.city_db.InnerJoinResult
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean
import com.zhangsheng.shunxin.weather.utils.WeatherUtils
import org.koin.android.ext.android.inject

class City2ndActivity : AacActivity<BaseViewModel>() {
    private var floor = "0"
    private var cityStr = ""
    private var list: List<InnerJoinResult>? = null

    override val vm by inject<BaseViewModel>()
    override val binding by inflate<ActivityCity2ndBinding>()



    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (requestCode == 888) {
            if (resultCode == 200) {
                intent.putExtra("position", WeatherUtils.getDatas().size - 1)
                setResult(200, intent)
                finish()
            }
        }
    }


    override fun initView(savedInstanceState: Bundle?) {
        binding.title.initTitle("添加城市")

        Try {
            floor = intent.getStringExtra("floor").isStr("")
            cityStr = intent.getStringExtra("city").isStr("")
            binding.city.text = cityStr
        }
        if (floor == "2") {
            list = getAppModel().city2ndList.nN().filter { it.district_cn == it.name_cn }
                .distinctBy { it.district_cn }
            if (list.nN().size <= 1) {
                floor = "3"
            }
        }

        if (floor == "3") {
            list = getAppModel().city2ndList.nN().filter { it.district_cn.nN().contains(cityStr) }
        }

        binding.gv.adapter = CityList2ndAdapter(
            this,
            list.nN(),
            R.layout.item_gv_local, floor
        ) { position, bean ->
            if (floor == "3") {
                chooseCity(bean.code, bean.name_cn)
            } else {
                var list =
                    getAppModel().city2ndList.nN().filter { it.district_cn == bean.district_cn }
                        .distinctBy { it.name_cn }.toMutableList()

                if (list.isEmpty()) {
                    chooseCity(bean.code, bean.name_cn)
                } else {
                    var intent = Intent()
                    intent.putExtra("city", bean.district_cn)
                    intent.putExtra("floor", "3")
                    intent.setClass(
                        this@City2ndActivity,
                        City2ndActivity::class.java
                    )
                    startActivityForResult(intent, 888)
                }
            }
        }
    }

    private fun chooseCity(code: String?, name_cn: String?) {
        if (code.isNullOrEmpty() || name_cn.isNullOrEmpty()) return
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
                setResult(200, intent)
                finish()
            }
        }
    }
}