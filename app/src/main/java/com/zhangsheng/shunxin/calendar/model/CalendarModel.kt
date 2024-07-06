package com.zhangsheng.shunxin.calendar.model


import com.google.gson.Gson
import com.google.gson.JsonObject
import com.maiya.thirdlibrary.base.BaseViewModel
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.net.callback.CallResult
import com.maiya.thirdlibrary.utils.CacheUtil
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.ext.encrypt
import com.zhangsheng.shunxin.weather.ext.net
import com.zhangsheng.shunxin.calendar.data.bean.HolidayBean
import org.joda.time.LocalDate
import java.util.*
import kotlin.collections.ArrayList
import kotlin.collections.HashMap

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2020/4/20 13:06
 */
class CalendarModel() : BaseViewModel() {

    fun requestWithoutWorkDay(localDate: LocalDate) {
        var year = localDate.year
        var day = Calendar.getInstance().get(Calendar.DAY_OF_YEAR)

        val holdayforYear =
            CacheUtil.getObj(Constant.SP_CALENDAR_HOLIDAY_DATA + year, Map::class.java)
                ?.toMutableMap()

        if (!holdayforYear.isNullOrEmpty() && holdayforYear["${Constant.SP_CALENDAR_HOLIDAY_DATA + year}"] == "$day") {
            //使用缓存
            parseHolidayData(holdayforYear)
            return
        }
        var map = HashMap<String, String>()
        map.put("year", "$year")
        map.put("service", "App.Tools.Holiday")
        map.toSortedMap(compareByDescending { it })
        var sign = Gson().toJson(map).toString().encrypt()
        var url = Constant.GET_WITHOUT_WORK_DAT_API + "&year=$year&sign=$sign"

        callNativeApi({ net().获取指定年份调班调休日(url) }, object : CallResult<JsonObject>() {
            override fun ok(result: JsonObject?) {
                super.ok(result)
                if (result.nN().toString().isNullOrEmpty()) {
                    var map = hashMapOf<Any, Any>()
                    map["${Constant.SP_CALENDAR_HOLIDAY_DATA + year}"] = "$day"
                    CacheUtil.putObj(Constant.SP_CALENDAR_HOLIDAY_DATA + year, map)
                    parseHolidayData(map)
                } else {
                    Try {
                        val jsonToMap = jsonToMap(result.nN().toString()).nN().toMutableMap()
                        jsonToMap["${Constant.SP_CALENDAR_HOLIDAY_DATA + year}"] = "$day"
                        CacheUtil.putObj(Constant.SP_CALENDAR_HOLIDAY_DATA + year, jsonToMap)
                        parseHolidayData(jsonToMap)
                    }
                }
            }
        })
    }


    fun jsonToMap(str: String): Map<*, *>? {
        return Gson().fromJson(str, Map::class.java)
    }

    private var mListener: ((ArrayList<String>, ArrayList<String>) -> Unit)? = null

    fun setParseHolidayData(mListener: ((ArrayList<String>, ArrayList<String>) -> Unit)) {
        this.mListener = mListener
    }

    fun parseHolidayData(map: MutableMap<*, *>) {
        val holidayDay = ArrayList<String>()
        val workDay = ArrayList<String>()
        val gson = Gson()
        map.forEach() {
            if (!it.key.toString().contains(Constant.SP_CALENDAR_HOLIDAY_DATA)) {
                var fromJson: HolidayBean? = null
                Try {
                    fromJson = gson.fromJson(it.value.toString(), HolidayBean::class.java)
                }
                if (fromJson?.type == 1) {
                    holidayDay.add(it.key.toString())
                } else if (fromJson?.type == 2) {
                    workDay.add(it.key.toString())
                }
            }
        }
        mListener?.invoke(holidayDay, workDay)
    }
}


