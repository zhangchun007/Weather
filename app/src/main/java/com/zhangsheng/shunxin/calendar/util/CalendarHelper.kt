package com.zhangsheng.shunxin.calendar.util

import android.text.TextUtils
import com.maiya.thirdlibrary.ext.nN
import com.zhangsheng.shunxin.calendar.db.CalendarAdvices
import com.zhangsheng.shunxin.calendar.db.CalendarDatabase
import com.zhangsheng.shunxin.calendar.db.CalendarIndexTable
import com.zhangsheng.shunxin.calendar.db.CalendarYJData
import org.joda.time.LocalDate
import java.text.SimpleDateFormat

object CalendarHelper {

    suspend fun getAdvices(localDate: LocalDate?): CalendarAdvices? {
        try {
            val dayGz: String = CalendarDataUtils.getStemsBranchDayAsString(localDate) //日的干支
            val code: Int = (CalendarDataUtils.getStemsBranchMonth(localDate) + 10) % 12 + 1
            val list = CalendarDatabase.getCalendarAdvicesDao().query(code.toString(), dayGz)

            if (!list.isNullOrEmpty()) {
                return list[0]
            }
        } catch (e: Exception) {
            return null
        }
        return null
    }

    suspend fun getYIJI(localDate: LocalDate?): CalendarYJData {
        try {
            if (localDate.nN().year in 2010..2049) {
                val SimpleDateFormat = SimpleDateFormat("yyyy-MM-dd")
                val format = SimpleDateFormat.format(localDate.nN().toDate())
                val list = CalendarDatabase.getCalendarIndexTableDao().query(format)
                return if (list.isNullOrEmpty()) {
                    CalendarYJData()
                } else {
                    getYIJI(list[0])
                }
            } else {
                val array = CalendarDataUtils.daysBetween(localDate)
                val calendarIndexTable = CalendarIndexTable()
                calendarIndexTable.jx = array[0].toString()
                calendarIndexTable.gz = array[1].toString()
                return getYIJI(calendarIndexTable)
            }
        } catch (e: Exception) {
            return CalendarYJData()
        }
    }

    private suspend fun getYIJI(calendarIndexTable: CalendarIndexTable): CalendarYJData {
        try {
            if (TextUtils.isEmpty(calendarIndexTable.jx) || TextUtils.isEmpty(calendarIndexTable.gz)) {
                return CalendarYJData()
            }
            val list = CalendarDatabase.getCalendarYJDao()
                .query(calendarIndexTable.jx!!, calendarIndexTable.gz!!)
            return if (list.isNullOrEmpty()) {
                CalendarYJData()
            } else {
                list[0]
            }
        } catch (e: Exception) {
            return CalendarYJData()
        }
    }
}