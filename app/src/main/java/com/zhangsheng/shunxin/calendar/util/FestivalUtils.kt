package com.zhangsheng.shunxin.calendar.util

import android.text.TextUtils
import com.google.gson.Gson
import com.maiya.thirdlibrary.ext.parseInt
import com.maiya.thirdlibrary.utils.AppContext
import com.necer.utils.CalendarUtil
import com.necer.utils.HolidayUtil
import com.necer.utils.LunarUtil
import com.zhangsheng.shunxin.calendar.data.bean.Festivals
import org.joda.time.LocalDate
import java.io.BufferedReader
import java.io.IOException
import java.io.InputStream
import java.io.InputStreamReader
import java.lang.ref.SoftReference

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2020/4/24 10:42
 */
class FestivalUtils private constructor() {
    companion object {
        var instance: FestivalUtils = FestivalUtils()
        var softReference: SoftReference<Festivals>? = null
    }

    fun getFestivals(localDate: LocalDate?): String {
        var stringBuilder = StringBuffer()
        if (localDate == null) {
            return ""
        }
        if (softReference == null || softReference?.get() == null) {
            var festivals =
                Gson().fromJson(getAssetsJsonForName("festivals.json"), Festivals::class.java)
            softReference = SoftReference(festivals)
        }
        var festivals = softReference?.get() ?: return ""

        var monthOfYear = if (localDate.monthOfYear < 10) {
            "0${localDate.monthOfYear}"
        } else {
            "${localDate.monthOfYear}"
        }
        var dayOfMonth = if (localDate.dayOfMonth < 10) {
            "0${localDate.dayOfMonth}"
        } else {
            "${localDate.dayOfMonth}"
        }

        var s = festivals.s

        val GregorianCalendarFestivals = s[monthOfYear + dayOfMonth]?.filter {
            !TextUtils.isEmpty(it.y) && it.y.parseInt() < localDate.year
        }


        var lunar = LunarUtil.getLunar(localDate.year, localDate.monthOfYear, localDate.dayOfMonth)

        monthOfYear = if (lunar.lunarMonth < 10) {
            "0${lunar.lunarMonth}"
        } else {
            "${lunar.lunarMonth}"
        }
        dayOfMonth = if (lunar.lunarDay < 10) {
            "0${lunar.lunarDay}"
        } else {
            "${lunar.lunarDay}"
        }
        var l = festivals.l

        var isLeepMonth =
            LunarUtil.isLeepMonth(localDate.year, localDate.monthOfYear, localDate.dayOfMonth);
        val LunarFestivals = l[monthOfYear + dayOfMonth]?.filter {
            !TextUtils.isEmpty(it.y) && it.y.parseInt() < localDate.year && !isLeepMonth
        }



        if (monthOfYear == "12" && dayOfMonth == "29" && LunarUtil.daysInMonth(
                lunar.lunarYear,
                lunar.lunarMonth
            ) == 29
        ) {
            stringBuilder.append("除夕夜  ")
        }

        monthOfYear = if (localDate.monthOfYear < 10) {
            "0${localDate.monthOfYear}"
        } else {
            "${localDate.monthOfYear}"
        }

        var weekOfMonth = CalendarUtil.countDate(localDate.toDate(), localDate.dayOfWeek)

        var dayOfWeek = if (localDate.dayOfWeek == 7) {
            0
        } else {
            localDate.dayOfWeek
        }

        var w = festivals.w
        val wFestivals = w[monthOfYear + weekOfMonth + dayOfWeek]?.filter {
            !TextUtils.isEmpty(it.y) && it.y.parseInt() < localDate.year
        }

        GregorianCalendarFestivals?.forEach {
            if (it.isS) {
                stringBuilder.insert(0, it.v + "  ")
            } else {
                stringBuilder.append(it.v + "  ")
            }

        }
        LunarFestivals?.forEach {
            if (it.isS) {
                stringBuilder.insert(0, it.v + "  ")
            } else {
                stringBuilder.append(it.v + "  ")
            }

        }
        wFestivals?.forEach {
            if (it.isS) {
                stringBuilder.insert(0, it.v + "  ")
            } else {
                stringBuilder.append(it.v + "  ")
            }

        }

        var festivalData: java.lang.StringBuilder = java.lang.StringBuilder()
        var i = 0
        if (HolidayUtil.EasterDay.contains("${localDate.year}-${localDate.monthOfYear}-${localDate.dayOfMonth}")) {
            stringBuilder.append("复活节  ")
        }

        var showAllFestivals = stringBuilder.split("  ")

        showAllFestivals.forEach() {
            festivalData.append(it + "  ")
            i++
            if (i > 2) {
                return festivalData.toString()
            }
        }

        return stringBuilder.toString()

    }

    private fun getAssetsJsonForName(name: String): String {
        val newstringBuilder = StringBuilder()
        var inputStream: InputStream? = null
        try {
            inputStream = AppContext.getContext().resources.assets.open(name)
            val isr = InputStreamReader(inputStream)
            val reader = BufferedReader(isr)
            var jsonLine: String?
            while (reader.readLine().also { jsonLine = it } != null) {
                newstringBuilder.append(jsonLine)
            }
            reader.close()
            isr.close()
            inputStream.close()
        } catch (var6: IOException) {
            var6.printStackTrace()
        }
        val result = newstringBuilder.toString()
        return result
    }

}