package com.zhangsheng.shunxin.weather.livedata

import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer


/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/3/9 23:41
 */

class SafeMutableLiveData<T> : MutableLiveData<T>() {

    fun safeObserve(owner: LifecycleOwner, observer: Observer<T>) {

        removeObservers(owner)

        removeObserver(observer)

        observe(owner, observer)
    }

}