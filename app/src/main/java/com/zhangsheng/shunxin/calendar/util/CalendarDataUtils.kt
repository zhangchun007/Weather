package com.zhangsheng.shunxin.calendar.util

import com.maiya.thirdlibrary.ext.nN
import com.necer.utils.CalendarUtil
import org.joda.time.Days
import org.joda.time.LocalDate
import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.*
import kotlin.math.abs
import kotlin.math.ceil
import kotlin.math.floor

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2020/4/26 16:52
 */
object CalendarDataUtils {
    private val MIN = 1900
    private val BASE_STEMS_DATE = LocalDate.parse("1899-2-4")
    private var defaultLocalDate = LocalDate.parse("2049-12-25")
    private var defaultUPLocalDate = LocalDate.parse("1900-12-27")
    private val yyyy_MM_dd_Format = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())


    @Synchronized
    fun jianChuOfDate(localDate: LocalDate?): String {
        var baseDate: Date? = null
        val date = localDate.nN().toDate()
        try {
            baseDate = yyyy_MM_dd_Format.parse("1901-01-01")
        } catch (var14: ParseException) {
            var14.printStackTrace()
        }
        var thisDate: Date? = null
        val thisDateStr = yyyy_MM_dd_Format.format(date)
        try {
            thisDate = yyyy_MM_dd_Format.parse(thisDateStr)
        } catch (var13: ParseException) {
            var13.printStackTrace()
        }
        val arr = twentyFourTermdaysOf(thisDate)
        var jx = -1
        var jianchuIndex: Int
        if (arr.size == 2) {
            jianchuIndex = arr[0]
            val b = arr[arr.size - 1]
            var offsetDayCount =
                if (jianchuIndex % 2 == 0) jianchuIndex / 2 else jianchuIndex / 2 + 1
            if (b != 0 && jianchuIndex % 2 == 0) {
                ++offsetDayCount
            }
            val interval =
                abs((thisDate!!.time - baseDate!!.time) / 1000L)
            val day = interval / 86400L
            jx =
                ceil(((5L + day - offsetDayCount.toLong()) % 12L).toDouble()).toInt()
        }
        jianchuIndex = if (jx >= 2) {
            jx - 2
        } else {
            jx + 10
        }
        return CalendarConstant.jianchuArray[jianchuIndex]
    }

    fun getZhiShen(localDate: LocalDate?): String {
        val monthgz = getStemsBranchMonth(localDate)
        val daygz = getStemsBranchDay(localDate)
        return zhiShenOfMonth(monthgz % 12, daygz % 12)
    }

    fun getStemsBranchMonth(localDate: LocalDate?): Int {
        var date = localDate.nN().toDate()
        val year = getYMDForDate(date, Calendar.YEAR)
        val dayInYear = getYMDForDate(date, Calendar.DAY_OF_YEAR) - 1
        val term = findPreTerm(year, dayInYear)
        val monthOffset =
            floor(((year - 1899) * 12 + (term + 2) / 2 - 2).toDouble()).toInt()
        val t = (monthOffset + 2) % 10
        val b = (monthOffset + 2) % 12
        return (6 * t - 5 * b + 60) % 60
    }

    private fun findPreTerm(year: Int, dayInYear: Int): Int {
        val index = year - MIN
        return if (index > 0 && index < CalendarConstant.TermTable.size / 24) {
            val begin = index * 24
            findPreTerm1(CalendarConstant.TermTable, dayInYear, begin, 24)
        } else {
            -1
        }
    }

    private fun findPreTerm1(
        termTable: IntArray,
        dayInYear: Int,
        begin: Int,
        len: Int
    ): Int {
        val value = IntArray(24)
        var index: Int
        index = begin
        while (index <= begin + 23) {
            value[index - begin] = termTable[index]
            ++index
        }
        index = -1
        var i = 0
        while (i < value.size) {
            if (dayInYear == value[i]) {
                index = i
                break
            }
            if (dayInYear < value[i]) {
                index = i - 1
                break
            }
            ++i
        }
        if (i == value.size && index == -1) {
            index = i - 1
        }
        return index
    }


    private fun getIntervalDays(baseDate: Date?, date: Date): Int {
        val baseDateL = baseDate!!.time
        val dateL = date.time
        return floor((abs(baseDateL - dateL) / 86400000L).toDouble()).toInt()
    }

    private fun zhiShenOfMonth(monthDz: Int, dayDz: Int): String {
        var qinglongBeginIndex = 0

        if (monthDz == 0 || monthDz == 6) {
            qinglongBeginIndex = 8
        } else if (monthDz == 1 || monthDz == 7) {
            qinglongBeginIndex = 10
        } else if (monthDz == 2 || monthDz == 8) {
            qinglongBeginIndex = 0
        } else if (monthDz == 3 || monthDz == 9) {
            qinglongBeginIndex = 2
        } else if (monthDz == 4 || monthDz == 10) {
            qinglongBeginIndex = 4
        } else if (monthDz == 5 || monthDz == 11) {
            qinglongBeginIndex = 6
        }

        var ishen_12 = dayDz - qinglongBeginIndex
        if (ishen_12 < 0) {
            ishen_12 += 12
        }

        return CalendarConstant.shiershenArr[ishen_12]
    }

    @Synchronized
    fun twentyFourTermdaysOf(date: Date?): IntArray {
        return try {
            val calendar = getCalendarForDate(date)
            var year = calendar[Calendar.YEAR]
            if (year in 2099..1900) {
                year = getYMDForDate(Date(), Calendar.YEAR)
            }
            val yearOffset = year - 1900
            val offset = calendar[Calendar.DAY_OF_YEAR] - 1
            var index = 0
            var st = 0
            var i = 0
            while (i < 24) {
                val num = CalendarConstant.TermTable[yearOffset * 24 + i]
                if (num > offset) {
                    index = i
                    st = 0
                    break
                } else if (num == offset) {
                    index = i
                    st = 1
                    break
                }
                ++i
            }
            i = index + yearOffset * 24 - 24
            intArrayOf(i, st)
        } catch (var9: Exception) {
            var9.printStackTrace()
            intArrayOf(0, 0)
        }
    }

    fun getStemsBranchDayAsString(localDate: LocalDate?): String {
        return formatStemsBranchString(getStemsBranchDay(localDate))
    }

    /**
     * 根据位置获取干支文字
     */
    private fun formatStemsBranchString(index: Int): String {
        return if (index < 0) "" else CalendarConstant.Gan[index % 10] + CalendarConstant.Zhi[index % 12]
    }

    private fun getCalendarForDate(date: Date?): Calendar {
        val calendar = Calendar.getInstance()
        calendar.time = date
        return calendar
    }

    @Synchronized
    fun getStemsBranchDay(date: LocalDate?): Int {
        val dayOffset = getIntervalDays(
            BASE_STEMS_DATE,
            date
        )
        if (dayOffset > 0) {
            val t = ((dayOffset + 9) % 10).toInt()
            val b = ((dayOffset + 3) % 12).toInt()
            return (6 * t - 5 * b + 60) % 60
        }
        return -1
    }

    private fun getIntervalDays(
        baseDate: LocalDate,
        date: LocalDate?
    ): Long {
        return Days.daysBetween(baseDate, date.nN()).days.toLong()
    }

    fun getPZBJ(localDate: LocalDate?): String {
        val gzDay = getStemsBranchDay(localDate)
        val dayTg = gzDay % 10
        val dayDz = gzDay % 12
        return CalendarConstant.mPzStemArray[dayTg] + " " + CalendarConstant.mPzBranchArray[dayDz] //彭祖百忌
    }

    fun getWx(localDate: LocalDate?): String {
        var gzStr = getStemsBranchDayAsString(localDate)
        var wx = ""
        if (CalendarConstant.mWxMap[gzStr] != null) {
            wx = CalendarConstant.mWxMap[gzStr].nN()
        }
        return wx
    }

    /**
     * 冲煞
     */
    fun getCS(localDate: LocalDate?): String {
        val branchIndex = branchIndexOfLocalDate(localDate)
        val cindex = chongIndexOfDateTime(branchIndex)
        val sindex = shaDirectionOfDateTime(branchIndex)
        val dayDz = getStemsBranchDay(localDate) % 12
        return try {
            CalendarConstant.ANIMAL[dayDz] + "日冲" + CalendarConstant.ANIMAL[cindex] + " 煞" + CalendarConstant.CompassNames[sindex].replace(
                "正",
                ""
            )
        } catch (e: Exception) {
            ""
        }
    }


    private fun branchIndexOfLocalDate(localDate: LocalDate?, hour: Int = -2): Int {
        var branchIndex = -1
        branchIndex = if (hour == -2) {
            return getBranchDay(localDate)
        } else {
            getStemBranchHour(localDate.nN().toDate(), hour) % 12
        }
        return branchIndex
    }

    /**
     * 子午相冲，丑未相冲，寅申相冲，辰戌相冲，巳亥相冲，卯酉相冲
     */
    private fun chongIndexOfDateTime(branchIndex: Int): Int {
        var value = when (branchIndex) {
            0, 1, 2, 3, 4, 5 -> branchIndex + 6
            6, 7, 8, 9, 10, 11 -> branchIndex - 6
            else -> -1
        }
        return value
    }

    /**
     * 逢巳日、酉日、丑日必是“煞东”；亥日、卯日、未日必“煞西”；申日、子日、辰日必“煞南”；寅日、午日、戌日必“煞北”
     */
    private fun shaDirectionOfDateTime(branchIndex: Int): Int {

        var value = when (branchIndex) {
            0, 4, 8 -> 4
            1, 5, 9 -> 2
            2, 6, 10 -> 0
            3, 7, 11 -> 6
            else -> -1

        }
        return value
    }

    private fun getBranchDay(solar: LocalDate?): Int {
        val dayOffset = getIntervalDays(
            BASE_STEMS_DATE,
            solar
        )
        return if (dayOffset > 0) {
            floor((dayOffset + 3) % 12.toDouble()).toInt()
        } else 0
    }

    fun getStemBranchHour(date: Date, lunarHour: Int): Int {
        var baseDate: Date? = null
        try {
            baseDate = SimpleDateFormat("yyyy-MM-dd").parse("1899-02-04")
        } catch (var8: ParseException) {
            var8.printStackTrace()
        }
        val dayOffset =
            getIntervalDays(baseDate, date)
        val dt = (dayOffset + 9) % 10
        val ht = (lunarHour + (if (dt > 4) dt - 5 else dt) * 2) % 10
        return (6 * ht - 5 * lunarHour + 60) % 60
    }


    fun stars28OfDate(localeData: LocalDate?): String {
        val date = localeData.nN().toDate()
        val calendar = getCalendarForDate(date)
        var B = (calendar[Calendar.YEAR] - 1) * 365
        for (i in 0 until calendar[Calendar.MONTH]) {
            B += dayCountOfMonth(i, false)
        }
        B += calendar[Calendar.DATE]
        var fixValue1 = 0
        val fixValue2 = 13
        if (isLeapYear(calendar[Calendar.YEAR]) && (calendar[Calendar.MONTH] + 1 > 3 || calendar[Calendar.MONTH] + 1 == 3 && calendar[Calendar.DAY_OF_MONTH] >= 1)
        ) {
            fixValue1 = 1
        }
        val C =
            floor(((calendar[Calendar.YEAR] - 1) / 4 - fixValue2 + fixValue1).toDouble())
                .toInt()
        val A = B + C
        val index_28Stars = (A + 23) % 28
        return CalendarConstant.star28Arr[index_28Stars]
    }

    /**
     * 这里返回当月是多少天
     * @param month
     * @param isLeap
     * @return
     */
    fun dayCountOfMonth(month: Int, isLeap: Boolean): Int {
        return when (month + 1) {
            1, 3, 5, 7, 8, 10, 12 -> 31
            2 -> {
                if (!isLeap) {
                    28
                } else 29
            }
            4, 6, 9, 11 -> 30
            else -> 0
        }
    }


    private fun isLeapYear(year: Int): Boolean {
        return year % 4 == 0 && year % 100 != 0 || year % 400 == 0
    }


    fun getStemsBranchHourAsString(date: Date, lunarHuor: Int): String {
        val hourgz = getStemBranchHour(date, lunarHuor)
        return formatStemsBranchString(hourgz)
    }


    fun getHS(date: Date): String {
        val year = String.format("%tY", date)
        val hanshuArr =
            CalendarConstant.fujiuDay[year] as Array<Int>?
        return if (hanshuArr != null && hanshuArr.size != 0) {
            val offset = dayOfYear(date)
            val calendar = Calendar.getInstance()
            calendar.time = date
            var i: Int
            if (offset < 100) {
                i = hanshuArr.size - 1
                while (i >= 0) {
                    if (offset == hanshuArr[i]) {
                        return CalendarConstant.fujiuName[i].toString() + "第1天"
                    }
                    --i
                }
            } else {
                i = 0
                while (i < hanshuArr.size) {
                    if (offset == hanshuArr[i]) {
                        return CalendarConstant.fujiuName[i].toString() + "第1天"
                    }
                    ++i
                }
            }
            getFJ(date)
        } else {
            ""
        }
    }

    fun getFJ(date: Date): String {
        val year = String.format("%tY", date)
        val fujiuDays =
            CalendarConstant.fujiuDay[year] as Array<Int>?

        return if (fujiuDays != null && fujiuDays.isNotEmpty()) {
            val offset = dayOfYear(date)
            if (offset in fujiuDays[0] until fujiuDays[1]) {
                "初伏第" + (offset - fujiuDays[0] + 1) + "天"
            } else if (offset in fujiuDays[1] until fujiuDays[2]) {
                "中伏第" + (offset - fujiuDays[1] + 1) + "天"
            } else if (offset >= fujiuDays[2] && offset <= fujiuDays[2] + 9) {
                "末伏第" + (offset - fujiuDays[2] + 1) + "天"
            } else if (offset in fujiuDays[3] until fujiuDays[4]) {
                "一九第" + (offset - fujiuDays[3] + 1) + "天"
            } else if (offset >= fujiuDays[4] - 1) {
                "二九第" + (offset - fujiuDays[4] + 1) + "天"
            } else if (offset < fujiuDays[5]) {
                "二九第" + (10 - (fujiuDays[5] - offset)) + "天"
            } else if (offset in fujiuDays[5] until fujiuDays[6]) {
                "三九第" + (offset - fujiuDays[5] + 1) + "天"
            } else if (offset in fujiuDays[6] until fujiuDays[7]) {
                "四九第" + (offset - fujiuDays[6] + 1) + "天"
            } else if (offset in fujiuDays[7] until fujiuDays[8]) {
                "五九第" + (offset - fujiuDays[7] + 1) + "天"
            } else if (offset in fujiuDays[8] until fujiuDays[9]) {
                "六九第" + (offset - fujiuDays[8] + 1) + "天"
            } else if (offset in fujiuDays[9] until fujiuDays[10]) {
                "七九第" + (offset - fujiuDays[9] + 1) + "天"
            } else if (offset in fujiuDays[10] until fujiuDays[11]) {
                "八九第" + (offset - fujiuDays[10] + 1) + "天"
            } else {
                if (offset >= fujiuDays[11] && offset <= fujiuDays[11] + 8) "九九第" + (offset - fujiuDays[11] + 1) + "天" else ""
            }
        } else {
            ""
        }

    }

    fun dayOfYear(date: Date): Int {
        val dateArr =
            intArrayOf(31, 28, 31, 30, 31, 30, 31, 31, 30, 31, 30, 31)
        val day = getYMDForDate(
            date,
            Calendar.DAY_OF_MONTH
        )
        val month = getYMDForDate(
            date,
            Calendar.MONTH
        )
        val year = getYMDForDate(
            date,
            Calendar.YEAR
        )
        var result = 0
        for (i in 0 until month) {
            result += dateArr[i]
        }
        result += day
        if (month > 1 && year % 4 == 0 && year % 100 != 0 || year % 400 == 0) {
            ++result
        }
        return result
    }

    fun getYMDForDate(date: Date, i: Int): Int {
        val cal = Calendar.getInstance()
        cal.time = date
        return cal[i]
    }


    /**
     * 获取十二时辰吉凶，返回值只有吉凶，不包含时辰
     *
     * @param solar
     * @return
     */
    fun get12hourJX(localDate: LocalDate?): List<String> {
        val jxList: MutableList<String> =
            ArrayList()
        val solarHour = getYMDForDate(Date(), Calendar.HOUR_OF_DAY)
        val targetClendarStr = yyyy_MM_dd_Format.format(localDate.nN().toDate())
        var targetCalendar: Calendar? = null
        try {
            targetCalendar = getCalendarForDate(
                yyyy_MM_dd_Format.parse(
                    targetClendarStr
                )
            )
        } catch (e: ParseException) {
            e.printStackTrace()
        }
        if (targetCalendar == null) {
            return jxList
        }
        val day = targetCalendar[Calendar.DATE]
        if (solarHour >= 23) {
            targetCalendar[Calendar.DATE] = day + 1
        }
        for (i in 0..11) {
            jxList.add(
                jixiongStatusOfDateTime(
                    localDate,
                    2 * i
                )
            )
        }
        return jxList
    }

    /**
     * 计算当前时辰吉凶
     *
     * @param solar
     * @param hourNow
     * @return
     */
    fun jixiongStatusOfDateTime(localDate: LocalDate?, hourNow: Int): String {
        var status = -1
        val JXStatusJi = 0
        val JXStatusXiong = 1
        val stemIndex = getStemsBranchDay(localDate)
        if (stemIndex > -1 && stemIndex < 60) {
            val hexValue =
                CalendarConstant.JXTable[stemIndex]
            val chineseHour =
                getLumarHourIndex(hourNow) //[datetime ylChineseNumHour];
            val moveCount = 11 - chineseHour
            val value = hexValue shr moveCount and 0x1
            status = if (value > 0) JXStatusJi else JXStatusXiong
        }
        return getJXName(status)
    }

    fun getLumarHourIndex(hourNow: Int): Int {
        return ((floor(hourNow / 2.toDouble()) + hourNow % 2) % 12).toInt()
    }

    private fun getJXName(value: Int): String {
        return if (value < 0 || value > CalendarConstant.JXNames.size) {
            ""
        } else CalendarConstant.JXNames[value]
    }


    fun daysBetween(localDate: LocalDate?): Array<Int> {
        var mLocalDate: LocalDate
        if (localDate.nN().year > 2049) {
            mLocalDate = defaultLocalDate
        } else {
            mLocalDate = defaultUPLocalDate
        }
        var solarTermAmount = CalendarUtil.getSolarTermAmount(localDate)

        var localeData1 = localDate.nN().minusDays(solarTermAmount)
        var jx = Days.daysBetween(mLocalDate, localeData1).days % 12
        var gz = getStemsBranchDay(localDate)

        var i = arrayOf(jx, gz)
        return i
    }
}