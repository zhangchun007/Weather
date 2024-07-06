package com.maiya.thirdlibrary.utils

import android.os.Parcelable
import com.google.gson.Gson
import com.google.gson.JsonObject
import com.google.gson.reflect.TypeToken
import com.maiya.thirdlibrary.ext.isStr
import com.maiya.thirdlibrary.ext.nN
import com.tencent.mmkv.MMKV
import java.util.*

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/6/12 15:06
 */
object CacheUtil {
    var mmkv: MMKV? = null

    init {
        mmkv = MMKV.defaultMMKV()
    }

    fun put(key: String, value: Any?) {
        when (value) {
            is String -> getMMKV().encode(key, value)
            is Float -> getMMKV().encode(key, value)
            is Boolean -> getMMKV().encode(key, value)
            is Int -> getMMKV().encode(key, value)
            is Long -> getMMKV().encode(key, value)
            is Double -> getMMKV().encode(key, value)
            is ByteArray -> getMMKV().encode(key, value)
            else-> putObj(key,value)
        }
    }

    fun <T> put(name: String, data: ArrayList<T>) {
        val json = Gson().toJson(data)
        put(name, json)
    }


    fun <T : Parcelable> putObj(key: String, obj: T?) {
        if (obj == null) {
            return
        }
        getMMKV().encode(key, obj)
    }

    fun <T> putObj(key: String, obj: T?) {
        if (obj != null) {
            val json = Gson().toJson(obj)
            put(key, json)
        }
    }



    fun <T> putArray(name: String, data: ArrayList<T>) {
        val regularEx = "#"
        var str = StringBuffer()
        if (data.size > 0) {
            for (value in data) {
                str.append(value)
                str.append(regularEx)
            }
            put(name, str.toString())
        }
    }

    fun getStringArray(key: String): ArrayList<String> {
        val regularEx = "#"
        var values = getString(key, "")
        var list = ArrayList<String>()
        list.addAll(values.split(regularEx.toRegex()))
        return list
    }

    fun <T : Parcelable> getObj(key: String, clz: Class<T>): T? {
        return getMMKV().decodeParcelable(key, clz)
    }


    fun getInt(key: String, default: Int = -1): Int {
        return getMMKV().decodeInt(key, default)
    }

    fun getString(key: String, default: String = ""): String {
        return getMMKV().decodeString(key, default).isStr(default)
    }

    fun getDouble(key: String, default: Double = 0.0): Double {
        return getMMKV().decodeDouble(key, default)
    }

    fun getFloat(key: String, default: Float = 0.0f): Float {
        return getMMKV().decodeFloat(key, default)
    }

    fun getLong(key: String, default: Long = 0): Long {
        return getMMKV().decodeLong(key, default)
    }

    fun getBoolean(key: String, default: Boolean = false): Boolean {
        return getMMKV().decodeBool(key, default)
    }

    fun <T> getObj(name: String, clazz: Class<T>): T? {
        return if (getString(name).isNotEmpty()) {
            Gson().fromJson(getString(name), clazz)
        } else null
    }
    fun <T> getList(key: String, clazz: Class<T>): ArrayList<T> {
        var sp = getString(key,"")
        return if (sp.isEmpty()) {
            ArrayList()
        } else {
            val type = object : TypeToken<ArrayList<JsonObject>>() {}.type
            val jsonObjects = Gson().fromJson<java.util.ArrayList<JsonObject>>(sp, type)
            val arrayList = ArrayList<T>()
            for (jsonObject in jsonObjects) {
                arrayList.add(Gson().fromJson(jsonObject, clazz))
            }
            arrayList
        }
    }


    fun remove(key: String) {
        getMMKV().removeValueForKey(key)
    }

    fun clearAll() {
        getMMKV().clearAll()
    }

    private fun getMMKV(): MMKV {
        if (mmkv == null) {
            mmkv = MMKV.defaultMMKV()
        }

        return mmkv.nN()
    }
}