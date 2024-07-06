package com.zhangsheng.shunxin.weather.model

import android.view.View
import com.maiya.thirdlibrary.base.BaseViewModel
import com.zhangsheng.shunxin.weather.livedata.SafeMutableLiveData
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/7/15 15:07
 */
class WeatherPageViewModel : BaseViewModel() {
    var index = 0
    var isAddRefresh = false
    var refreshTime = 0L
    var weatherData: SafeMutableLiveData<WeatherBean> = SafeMutableLiveData()
    var lifeData = ArrayList<WeatherBean.LifesBean>()
    var isFifLoadMore = false
    var emptyView: View? = null


    override fun onCleared() {
        super.onCleared()
        lifeData.clear()
    }

}