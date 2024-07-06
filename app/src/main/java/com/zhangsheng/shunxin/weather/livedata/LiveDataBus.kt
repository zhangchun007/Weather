package com.zhangsheng.shunxin.weather.livedata

import com.maiya.thirdlibrary.ext.Try


object LiveDataBus {
    private var bus: HashMap<String, SafeMutableLiveData<*>> = HashMap()

    fun <T> getChannel(key: String): SafeMutableLiveData<T> {

        if (!bus.containsKey(key)) {
            bus[key] = SafeMutableLiveData<T>()
        }

        return bus[key] as SafeMutableLiveData<T>
    }

    fun unRegist(key:String){
        Try {
            if (bus.containsKey(key)){
                bus.remove(key)
            }
        }
    }

}