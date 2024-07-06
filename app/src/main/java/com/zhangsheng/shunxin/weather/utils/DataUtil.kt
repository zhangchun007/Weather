package com.zhangsheng.shunxin.weather.utils

import android.text.format.Time
import android.util.Log
import com.maiya.thirdlibrary.ext.isStr
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.jvm.Throws
import kotlin.math.abs


/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2019/12/6 16:39
 */
object DataUtil {
    fun isSameDay(
        millis1: Long,
        millis2: Long,
        timeZone: TimeZone = TimeZone.getTimeZone("Asia/Shanghai")
    ): Boolean {
        val interval = millis1 - millis2
        return interval < 86400000 && interval > -86400000 && millis2Days(
            millis1,
            timeZone
        ) == millis2Days(millis2, timeZone)
    }

    fun isSameYear(
        millis1: Long,
        millis2: Long, timeZone: TimeZone = TimeZone.getTimeZone("Asia/Shanghai")
    ): Boolean {

        return timeStamp2Date(millis1, "yyyy") == timeStamp2Date(millis2, "yyyy")
    }

    private fun millis2Days(
        millis: Long,
        timeZone: TimeZone = TimeZone.getTimeZone("Asia/Shanghai")
    ): Long {
        return (timeZone.getOffset(millis) + millis) / 86400000
    }


    fun timeStamp2Date(millis: Long, format: String = ""): String {
        var format = format
        if (format.isEmpty()) format = "yyyy-MM-dd HH:mm:ss"
        val sdf = SimpleDateFormat(format)
        return sdf.format(millis)
    }

    fun Data2TimeStamp(data: String, format: String = ""): Date? {
        var simple = SimpleDateFormat(format.isStr("yyyy-MM-dd HH:mm:ss"))

        return simple.parse(data)
    }

    fun Date2TimeCalendarHourCN(time: Long): String {
        val date = Date()
        date.time = time
        var calendar = Calendar.getInstance();
        calendar.time = date;
        return "${calendar.get(Calendar.HOUR_OF_DAY)}点";
    }



    fun Date2TimeCalendarHour(time: Long): Int {
        var hour = Calendar.getInstance().apply {
            setTime(Date(time))
        }.get(Calendar.HOUR_OF_DAY)

        return hour
    }

    fun date2Long(date: String?, format: String = ""): Long {
        try {
            val sdf = SimpleDateFormat(format.isStr("yyyy-MM-dd HH:mm:ss"))
            return (sdf.parse(date).time)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return 0L
    }

    fun date2date(date: String, from: String, to: String): String {
        try {
            val sdf = SimpleDateFormat(from.isStr("yyyy-MM-dd HH:mm:ss"))
            var time = (sdf.parse(date).time)
            return timeStamp2Date(time, to)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return ""
    }


    fun judgeIsSetBiddingTime(begin: String, end: String): Boolean { //设置日期格式
        val df = SimpleDateFormat("HH:mm")
        var now: Date? = null
        var beginTime: Date? = null
        var endTime: Date? = null
        return try {
            now = df.parse(df.format(Date()))
            beginTime = df.parse(begin)
            endTime = df.parse(end)
            val date = Calendar.getInstance()
            date.time = now
            val begin = Calendar.getInstance()
            begin.time = beginTime
            val end = Calendar.getInstance()
            end.time = endTime
            date.after(begin) && date.before(end)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            false
        }
    }

    fun judgeIsSetBiddingTime(nowTime: String, begin: String, end: String): Boolean { //设置日期格式
        val df = SimpleDateFormat("HH:mm")
        var now: Date? = null
        var beginTime: Date? = null
        var endTime: Date? = null
        try {
            now = df.parse(nowTime)
            beginTime = df.parse(begin)
            endTime = df.parse(end)
            val date = Calendar.getInstance()
            date.time = now
            val begin = Calendar.getInstance()
            begin.time = beginTime
            val end = Calendar.getInstance()
            end.time = endTime
            return date.after(begin) && date.before(end)

        } catch (e: java.lang.Exception) {
            e.printStackTrace()
            return false
        }
    }


    fun getWeek(time: Long): String {
        val cd = Calendar.getInstance()
        cd.time = Date(time)
        val week = cd[Calendar.DAY_OF_WEEK] //获取星期
        val weekString: String
        weekString = when (week) {
            Calendar.SUNDAY -> "周日"
            Calendar.MONDAY -> "周一"
            Calendar.TUESDAY -> "周二"
            Calendar.WEDNESDAY -> "周三"
            Calendar.THURSDAY -> "周四"
            Calendar.FRIDAY -> "周五"
            else -> "周六"
        }
        return weekString
    }

    fun getWeek2(time: Long): String {
        val cd = Calendar.getInstance()
        cd.time = Date(time)
        val week = cd[Calendar.DAY_OF_WEEK] //获取星期
        val weekString: String
        weekString = when (week) {
            Calendar.SUNDAY -> "星期日"
            Calendar.MONDAY -> "星期一"
            Calendar.TUESDAY -> "星期二"
            Calendar.WEDNESDAY -> "星期三"
            Calendar.THURSDAY -> "星期四"
            Calendar.FRIDAY -> "星期五"
            else -> "星期六"
        }
        return weekString
    }

    fun isTodayBoolean(time: Long): Boolean {
        val now = Time()
        now.setToNow()
        val then = Time()
        then.set(time)
        return then.year === now.year && then.month === now.month && then.monthDay === now.monthDay
    }

    fun isToday(timeStamp: Long): String {
        val todayCalendar = Calendar.getInstance()
        val calendar = Calendar.getInstance()
        calendar.timeInMillis = timeStamp
        return when {
            todayCalendar[Calendar.DAY_OF_YEAR] - calendar[Calendar.DAY_OF_YEAR] == 0 -> "今天"
            todayCalendar.get(Calendar.DAY_OF_YEAR) - calendar.get(Calendar.DAY_OF_YEAR) == 1 -> "昨天"
            else -> getWeek(timeStamp)
        }

    }

    fun checkTodayOrBefore(time: Long): String {

        val today = System.currentTimeMillis() //当前时间的毫秒数

        val l = today - time
        val days = l / (24 * 60 * 60 * 1000)

        return when {
            days > 1 -> "前天"
            days > 0 -> "昨天"
            else -> "今天"
        }
    }

    fun millisToStringShort(millis: Long): String {
        var strBuilder = StringBuilder()
        var temp = millis
        val hper = 60 * 60 * 1000.toLong()
        val mper = 60 * 1000.toLong()
        val sper: Long = 1000
        when {
            temp / hper >= 10 -> {
                strBuilder.append(temp / hper).append(":")
            }
            temp / hper > 0 -> {
                strBuilder.append("0${temp / hper}").append(":")
            }
            else -> {
                strBuilder.append("00").append(":")
            }
        }
        temp %= hper

        when {
            temp / mper >= 10 -> {
                strBuilder.append(temp / mper).append(":")
            }
            temp / mper > 0 -> {
                strBuilder.append("0${temp / mper}").append(":")
            }
            else -> {
                strBuilder.append("00").append(":")
            }
        }
        temp %= mper
        when {
            temp / sper >= 10 -> {
                strBuilder.append(temp / sper)
            }
            temp / sper > 0 -> {
                strBuilder.append("0${temp / sper}")
            }
            else -> {
                strBuilder.append("00")
            }
        }

        return strBuilder.toString()
    }

    /**
     * 判断当前时间是否在[startTime, endTime]区间，注意时间格式要一致
     *
     * @param nowTime 当前时间
     * @param startTime 开始时间
     * @param endTime 结束时间
     * @return
     * @author jqlin
     */
    fun isEffectiveDate(nowTime: Date, startTime: Date, endTime: Date): Boolean {
        if (nowTime.time === startTime.time
            || nowTime.time === endTime.time
        ) {
            return true
        }
        var date = Calendar.getInstance()
        date.time = nowTime
        var begin = Calendar.getInstance()
        begin.time = startTime
        var end = Calendar.getInstance()
        end.time = endTime
        return date.after(begin) && date.before(end)
    }

    fun millisToStringDown(millis: Long): String {
        var strBuilder = StringBuilder()
        var temp = millis
        val hper = 60 * 60 * 1000.toLong()
        val mper = 60 * 1000.toLong()
        val sper: Long = 1000
        when {
            temp / hper >= 10 -> {
                strBuilder.append(temp / hper).append("小时 ")
            }
            temp / hper > 0 -> {
                strBuilder.append("0${temp / hper}").append("小时 ")
            }
            else -> {
                strBuilder.append("")
            }
        }
        temp %= hper

        when {
            temp / mper >= 10 -> {
                strBuilder.append(temp / mper).append("分钟 ")
            }
            temp / mper > 0 -> {
                strBuilder.append("0${temp / mper}").append("分钟 ")
            }
            else -> {
                strBuilder.append("00").append("分钟 ")
            }
        }
        temp %= mper
        when {
            temp / sper >= 10 -> {
                strBuilder.append(temp / sper).append("秒")
            }
            temp / sper > 0 -> {
                strBuilder.append("0${temp / sper}").append("秒")
            }
            else -> {
                strBuilder.append("00").append("秒")
            }
        }

        return strBuilder.toString()
    }

    fun getTargetTime(hour: Int, minute: Int): Long {
        var curTimeZone = TimeZone.getTimeZone("GMT+8")
        var calendar = Calendar.getInstance(curTimeZone)
        calendar.timeInMillis = System.currentTimeMillis()
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.timeInMillis
    }

    fun getTargetTime(currentTime: Long, hour: Int, minute: Int): Long {
        var curTimeZone = TimeZone.getTimeZone("GMT+8")
        var calendar = Calendar.getInstance(curTimeZone)
        calendar.timeInMillis = currentTime
        calendar.set(Calendar.HOUR_OF_DAY, hour)
        calendar.set(Calendar.MINUTE, minute)
        calendar.set(Calendar.SECOND, 0)
        calendar.set(Calendar.MILLISECOND, 0)

        return calendar.timeInMillis
    }

    /**
     * 相差天数(绝对值)
     */
    fun daysBetween(day1: Long, day2: Long): Int {
        var sdf = SimpleDateFormat("yyyy-MM-dd")
        var date1 = sdf.parse(timeStamp2Date(day1, "yyyy-MM-dd"))
        var date2 = sdf.parse(timeStamp2Date(day2, "yyyy-MM-dd"))
        return abs(daysBetweenDate(date1, date2))
    }

    fun daysBetweenNomal(day1: Long, day2: Long): Int {
        var sdf = SimpleDateFormat("yyyy-MM-dd")
        var date1 = sdf.parse(timeStamp2Date(day1, "yyyy-MM-dd"))
        var date2 = sdf.parse(timeStamp2Date(day2, "yyyy-MM-dd"))
        return daysBetweenDate(date1, date2)
    }

    @Throws(ParseException::class)
    fun daysBetweenDate(smdate: Date, bdate: Date): Int {
        var smdate = smdate
        var bdate = bdate

        val sdf = SimpleDateFormat("yyyy-MM-dd")

        smdate = sdf.parse(sdf.format(smdate))

        bdate = sdf.parse(sdf.format(bdate))

        val cal = Calendar.getInstance()

        cal.time = smdate

        val time1 = cal.timeInMillis

        cal.time = bdate

        val time2 = cal.timeInMillis

        val between_days = (time2 - time1) / (1000 * 3600 * 24)

        return Integer.parseInt(between_days.toString())

    }


    /**
     * 获取日
     * @return
     */
    fun getDay(): Int {
        val cd = Calendar.getInstance()
        return cd[Calendar.DATE]
    }


    fun getDayHour(): Int {
        val cd = Calendar.getInstance()
        return cd[Calendar.HOUR_OF_DAY]
    }

    /**
     * 获取月
     * @return
     */
    private var nums: Array<String> =
        arrayOf<String>("零", "一", "二", "三", "四", "五", "六", "七", "八", "九", "十", "十一", "十二")

    fun getMonth(): String {
        val cd = Calendar.getInstance()
        return nums[cd[Calendar.MONTH] + 1]
    }

    /**
     * 获取当前时间
     */
    fun curFormat(format: String = "yyyy-MM-dd HH:mm:ss"): String {
        var format = SimpleDateFormat(format)

        return format.format(Date(System.currentTimeMillis()))
    }

    /**
     * 获取当前时间
     */
    fun getHHmmTimeFormat(str: Long,format: String = "yyyy-MM-dd HH:mm:ss"): String {
        var format = SimpleDateFormat(format)

        return format.format(Date(str))
    }

    fun getHHmm(str: String): String? {
        var string = ""
        try {
            string = str.substring(11, 16)
        } catch (e: java.lang.Exception) {
            e.printStackTrace()
        }
        return string
    }


}