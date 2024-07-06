package com.zhangsheng.shunxin.weather.model

import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.net.callback.CallResult
import com.maiya.thirdlibrary.utils.CacheUtil
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.ext.getAppModel
import com.zhangsheng.shunxin.weather.ext.net
import com.zhangsheng.shunxin.weather.livedata.SafeMutableLiveData
import com.zhangsheng.shunxin.weather.net.bean.Air
import com.zhangsheng.shunxin.weather.net.bean.AirBean
import com.zhangsheng.shunxin.weather.net.bean.AirPositionBean
import com.zhangsheng.shunxin.weather.net.bean.AirRankBean
import kotlin.math.abs

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2019/12/6 21:01
 */
class AirModel : BaseViewModel() {
    var airBean: SafeMutableLiveData<AirBean> =
        SafeMutableLiveData()
    var air = ArrayList<Air>()
    var airPositions = ArrayList<AirPositionBean.Postion>()

    fun requestAir(isLocation: Boolean, regionCode: String) {
        var data = CacheUtil.getObj("air$regionCode", AirBean::class.java)
        if (abs(System.currentTimeMillis() - data.nN().refreshTime) > 5 * 60 * 1000) {
            if (isLocation) {
                requestAirByLocation(regionCode)
            } else {
                requestAir(regionCode)
            }
        } else {
            airBean.value = data.nN()
            dealAirDate(data)
        }
    }

    private fun requestAirByLocation(regioncode: String) {
        showLoading()
        callNativeApi({
            net().按地理位置获取空气质量数据(
                getAppModel().currentWeather.value.nN().weather.nN().location.nN().lat,
                getAppModel().currentWeather.value.nN().weather.nN().location.nN().lng,
                getAppModel().currentWeather.value.nN().weather.nN().regionname
            )
        },
            object : CallResult<AirBean>() {

                override fun ok(result: AirBean?) {
                    super.ok(result)
                    dealAirDate(result)
                    airBean.value = result.nN()
                    if (regioncode.isNotEmpty()) {
                        CacheUtil.putObj("air$regioncode", result.nN().apply {
                            this.refreshTime = System.currentTimeMillis()
                        })
                    }
                }

                override fun failed(code: Int, msg: String) {
                    super.failed(code, msg)
                    requestAir(getAppModel().currentWeather.value.nN().weather.nN().regionname)
                }
            }
        )
    }

   private fun requestAir(regioncode: String) {
        showLoading()
        callNativeApi({ net().空气质量数据(regioncode) },
            object : CallResult<AirBean>() {
                override fun failed(code: Int, msg: String) {
                    super.failed(code, msg)
                    loadFail(code, msg)
                }

                override fun ok(result: AirBean?) {
                    super.ok(result)
                    dealAirDate(result)
                    airBean.value = result.nN()
                    CacheUtil.putObj("air$regioncode", result.nN().apply {
                        this.refreshTime = System.currentTimeMillis()
                    })
                }
            }
        )
    }

    private fun dealAirDate(airBean:AirBean?){
        airBean?.let {
            loadSuccess()
            air.clear()
            air.add(Air().apply {
                name = "PM2.5"
                content = "细颗粒物"
                num = it.pm25
                air_color = it.pm25Level
            })
            air.add(Air().apply {
                name = "PM10"
                content = "粗颗粒物"
                num = it.pm10
                air_color = it.pm10Level
            })
            air.add(Air().apply {
                name = "SO"
                content = "二氧化硫"
                num = it.so2
                air_color = it.so2Level
            })
            air.add(Air().apply {
                name = "NO"
                content = "二氧化氮"
                num = it.no2
                air_color = it.no2Level
            })
            air.add(Air().apply {
                name = "CO"
                content = "一氧化碳"
                num = it.co
                air_color = it.coLevel
            })
            air.add(Air().apply {
                name = "O"
                content = "臭氧"
                num = it.o3
                air_color = it.o3Level
            })
        }
    }

    fun loadMapAir(regioncode: String, callback: (List<AirPositionBean.Postion>?) -> Unit) {
        var data= CacheUtil.getObj("airMap$regioncode",AirPositionBean::class.java)

        if (abs(System.currentTimeMillis()-data.nN().refreshTime)>5*60*1000){
            callNativeApi(
                { net().空气质量检测站点地图数据(regioncode) },
                object : CallResult<List<AirPositionBean.Postion>>() {
                    override fun ok(result: List<AirPositionBean.Postion>?) {
                        super.ok(result)
                        CacheUtil.putObj("airMap$regioncode",AirPositionBean().apply {
                            this.refreshTime=System.currentTimeMillis()
                            this.data=result
                        })
                        callback(result)
                    }
                }
            )
        }else{
            callback(data.nN().data.nN().toMutableList())
        }
    }

    val getAirRankStatar = SafeMutableLiveData<Boolean>()

    fun rankRequst(){
        showLoading()
        requestAirRank(true)
    }

    fun requestAirRank(shouldShowEmpty: Boolean) {
        var data= CacheUtil.getObj(Constant.SP_AIR_RANK,AirRankBean::class.java)
        if (abs(System.currentTimeMillis()-data.nN().refreshTime)> 5 * 60 * 1000){
            callNativeApi(
                { net().空气质量排名数据() },
                object : CallResult<AirRankBean>() {
                    override fun ok(result: AirRankBean?) {
                        super.ok(result)
                        loadSuccess()
                        if (result.nN().rankings.nN().isNotEmpty()) {
                            getAppModel().airRank.value = result.nN()
                            getAirRankStatar.value = true
                            CacheUtil.put(Constant.SP_AIR_RANK, result.nN().apply {
                                this.refreshTime=System.currentTimeMillis()
                            })
                        } else {
                            getAirRankStatar.value = false
                        }
                    }

                    override fun failed(code: Int, msg: String) {
                        super.failed(code, msg)
                        if (shouldShowEmpty){
                            loadFail(code,msg)
                        }
                        getAirRankStatar.value = false
                    }
                }
            )
        }else{
            loadSuccess()
            getAppModel().airRank.value=data.nN()
        }
    }

}