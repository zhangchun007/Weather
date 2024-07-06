package com.zhangsheng.shunxin.weather.utils

object CollectionsUtil {

    fun isEmpty(collection: Collection<*>?): Boolean {
        return collection == null || collection.isEmpty()
    }

    fun <T> nonEmptyList(list: List<T>?): List<T>? {
        return list ?: emptyList()
    }
}