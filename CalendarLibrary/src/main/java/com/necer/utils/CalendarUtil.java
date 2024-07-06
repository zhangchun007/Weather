package com.necer.utils;

import android.text.TextUtils;

import com.necer.entity.CalendarDate;
import com.necer.entity.CalendarDate2;
import com.necer.entity.CalendarDate3;
import com.necer.entity.Lunar;

import org.joda.time.DateTime;
import org.joda.time.LocalDate;
import org.joda.time.Months;
import org.joda.time.Weeks;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;

import javax.security.auth.login.LoginException;

/**
 * Created by necer on 2017/6/9.
 */

public class CalendarUtil {


    /**
     * 两个日期是否同月
     */
    public static boolean isEqualsMonth(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return date1.getYear() == date2.getYear() && date1.getMonthOfYear() == date2.getMonthOfYear();
    }

    /**
     * 两个日期是否同年
     */
    public static boolean isEqualsYear(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return date1.getYear() == date2.getYear();
    }

    /**
     * 两个日期是否同日
     */
    public static boolean isEqualsDay(LocalDate date1, LocalDate date2) {
        if (date1 == null || date2 == null) {
            return false;
        }
        return date1.getYear() == date2.getYear() && date1.getMonthOfYear() == date2.getMonthOfYear() && date1.getDayOfMonth() == date2.getDayOfMonth();
    }

    /**
     * 第一个是不是第二个的上一个月,只在此处有效
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isLastMonth(LocalDate date1, LocalDate date2) {
        LocalDate date = date2.plusMonths(-1);
        return date1.getMonthOfYear() == date.getMonthOfYear();
    }


    /**
     * 第一个是不是第二个的下一个月，只在此处有效
     *
     * @param date1
     * @param date2
     * @return
     */
    public static boolean isNextMonth(LocalDate date1, LocalDate date2) {
        LocalDate date = date2.plusMonths(1);
        return date1.getMonthOfYear() == date.getMonthOfYear();
    }


    /**
     * 获得两个日期距离几个月
     *
     * @return
     */
    public static int getIntervalMonths(LocalDate date1, LocalDate date2) {
        date1 = date1.withDayOfMonth(1);
        date2 = date2.withDayOfMonth(1);
        return Months.monthsBetween(date1, date2).getMonths();
    }

    /**
     * 获得两个日期距离几周
     *
     * @param date1
     * @param date2
     * @param type  一周
     * @return
     */
    public static int getIntervalWeek(LocalDate date1, LocalDate date2, int type) {

        if (type == Attrs.MONDAY) {
            date1 = getMonFirstDayOfWeek(date1);
            date2 = getMonFirstDayOfWeek(date2);
        } else {
            date1 = getSunFirstDayOfWeek(date1);
            date2 = getSunFirstDayOfWeek(date2);
        }

        return Weeks.weeksBetween(date1, date2).getWeeks();

    }

    public static boolean isPassDay(LocalDate date) {
        if (date == null) {
            return false;
        }
        LocalDate today = new LocalDate();
        return today.toDate().getTime() > date.toDate().getTime();
    }

    /**
     * 是否是今天
     *
     * @param date
     * @return
     */
    public static boolean isToday(LocalDate date) {
        return new LocalDate().equals(date);
    }

    /**
     * @param localDate
     * @param weekType  300，周日，301周一
     * @return
     */
    public static List<LocalDate> getMonthCalendar(LocalDate localDate, int weekType, boolean isAllMonthSixLine) {

        LocalDate lastMonthDate = localDate.plusMonths(-1);//上个月
        LocalDate nextMonthDate = localDate.plusMonths(1);//下个月

        int days = localDate.dayOfMonth().getMaximumValue();//当月天数
        int lastMonthDays = lastMonthDate.dayOfMonth().getMaximumValue();//上个月的天数
        int firstDayOfWeek = new LocalDate(localDate.getYear(), localDate.getMonthOfYear(), 1).getDayOfWeek();//当月第一天周几
        int endDayOfWeek = new LocalDate(localDate.getYear(), localDate.getMonthOfYear(), days).getDayOfWeek();//当月最后一天周几

        List<LocalDate> dateList = new ArrayList<>();


        //周一开始的
        if (weekType == Attrs.MONDAY) {

            //周一开始的
            for (int i = 0; i < firstDayOfWeek - 1; i++) {
                LocalDate date = new LocalDate(lastMonthDate.getYear(), lastMonthDate.getMonthOfYear(), lastMonthDays - (firstDayOfWeek - i - 2));
                dateList.add(date);
            }
            for (int i = 0; i < days; i++) {
                LocalDate date = new LocalDate(localDate.getYear(), localDate.getMonthOfYear(), i + 1);
                dateList.add(date);
            }
            for (int i = 0; i < 7 - endDayOfWeek; i++) {
                LocalDate date = new LocalDate(nextMonthDate.getYear(), nextMonthDate.getMonthOfYear(), i + 1);
                dateList.add(date);
            }

        } else {
            //上个月
            if (firstDayOfWeek != 7) {
                for (int i = 0; i < firstDayOfWeek; i++) {
                    LocalDate date = new LocalDate(lastMonthDate.getYear(), lastMonthDate.getMonthOfYear(), lastMonthDays - (firstDayOfWeek - i - 1));
                    dateList.add(date);
                }
            }
            //当月
            for (int i = 0; i < days; i++) {
                LocalDate date = new LocalDate(localDate.getYear(), localDate.getMonthOfYear(), i + 1);
                dateList.add(date);
            }
            //下个月
            if (endDayOfWeek == 7) {
                endDayOfWeek = 0;
            }
            for (int i = 0; i < 6 - endDayOfWeek; i++) {
                LocalDate date = new LocalDate(nextMonthDate.getYear(), nextMonthDate.getMonthOfYear(), i + 1);
                dateList.add(date);
            }
        }

        //某些年的2月份28天，又正好日历只占4行
        if (dateList.size() == 28) {
            for (int i = 0; i < 7; i++) {
                LocalDate date = new LocalDate(nextMonthDate.getYear(), nextMonthDate.getMonthOfYear(), i + 1);
                dateList.add(date);
            }
        }

        //是否所有月份都6行
        if (isAllMonthSixLine && dateList.size() == 35) {
            LocalDate endLocalDate = dateList.get(dateList.size() - 1);
            int dayOfMonth = endLocalDate.getDayOfMonth();
            //如果是当月最后一天，直接加上下个月的7天，如果不是当月了，已经是下月了，就顺着下月数7天
            if (dayOfMonth == days) {
                for (int i = 0; i < 7; i++) {
                    LocalDate date = new LocalDate(nextMonthDate.getYear(), nextMonthDate.getMonthOfYear(), i + 1);
                    dateList.add(date);
                }
            } else {
                for (int i = 0; i < 7; i++) {
                    LocalDate date = new LocalDate(nextMonthDate.getYear(), nextMonthDate.getMonthOfYear(), dayOfMonth + i + 1);
                    dateList.add(date);
                }
            }
        }


        return dateList;

    }


    /**
     * 周视图的数据
     *
     * @param localDate
     * @return
     */
    public static List<LocalDate> getWeekCalendar(LocalDate localDate, int type) {
        List<LocalDate> dateList = new ArrayList<>();

        if (type == Attrs.MONDAY) {
            localDate = getMonFirstDayOfWeek(localDate);
        } else {
            localDate = getSunFirstDayOfWeek(localDate);
        }

        for (int i = 0; i < 7; i++) {
            LocalDate date = localDate.plusDays(i);
            dateList.add(date);
        }
        return dateList;
    }

    public static List<String> getHolidayList() {

        ArrayList<String> holidayList = new ArrayList<>(HolidayUtil.holidayList);
        for (String key : HolidayUtil.holidayListOfYear.keySet()) {
            for (String holiday : HolidayUtil.holidayListOfYear.get(key)) {
                holidayList.add(holiday);
            }
        }
        return holidayList;
    }

    public static List<String> getWorkdayList() {
        ArrayList<String> workdayList = new ArrayList<>(HolidayUtil.workdayList);

        for (String key : HolidayUtil.workdayListOfYear.keySet()) {
            for (String holiday : HolidayUtil.workdayListOfYear.get(key)) {
                workdayList.add(holiday);
            }
        }
        return workdayList;
    }

    /**
     * 设置调休调班日。根据
     *
     * @param year
     * @param holidayList
     * @param workdayList
     */
    public static void setHoildayListOfYear(String year, List<String> holidayList, List<String> workdayList) {
        if (holidayList != null && holidayList.size() > 0) {
            for (int i = 0; i < holidayList.size(); i++) {
                holidayList.set(i, year + "-" + holidayList.get(i));
            }
            HolidayUtil.holidayListOfYear.put(year, holidayList);
        }
        if (workdayList != null && workdayList.size() > 0) {
            for (int i = 0; i < workdayList.size(); i++) {
                workdayList.set(i, year + "-" + workdayList.get(i));
            }
            HolidayUtil.workdayListOfYear.put(year, workdayList);
        }

    }


    /**
     * 转化一周从周日开始
     *
     * @param date
     * @return
     */
    public static LocalDate getSunFirstDayOfWeek(LocalDate date) {
        if (date.dayOfWeek().get() == 7) {
            return date;
        } else {
            return date.minusWeeks(1).withDayOfWeek(7);
        }
    }

    /**
     * 转化一周从周一开始
     *
     * @param date
     * @return
     */
    public static LocalDate getMonFirstDayOfWeek(LocalDate date) {
        return date.dayOfWeek().withMinimumValue();
    }


    /**
     * 获取CalendarDate  CalendarDate包含需要显示的信息 农历，节气等
     *
     * @param localDate
     * @return
     */
    public static CalendarDate getCalendarDate(LocalDate localDate) {
        CalendarDate calendarDate = new CalendarDate();
        int solarYear = localDate.getYear();
        int solarMonth = localDate.getMonthOfYear();
        int solarDay = localDate.getDayOfMonth();
        Lunar lunar = LunarUtil.getLunar(solarYear, solarMonth, solarDay);
        if (solarYear != 1900) {
            calendarDate.lunar = lunar;
            calendarDate.localDate = localDate;
            calendarDate.solarTerm = SolarTermUtil.getSolatName(solarYear, (solarMonth < 10 ? "0" + solarMonth : solarMonth + "") + solarDay);
            calendarDate.solarHoliday = HolidayUtil.getSolarHoliday(solarYear, solarMonth, solarDay);
            calendarDate.lunarHoliday = HolidayUtil.getLunarHoliday(lunar.lunarYear, lunar.lunarMonth, lunar.lunarDay, solarYear, solarMonth, solarDay, calendarDate.solarTerm);
            calendarDate.holiday = HolidayUtil.getHoliday(lunar.lunarYear, lunar.lunarMonth, lunar.lunarDay, solarYear, solarMonth, solarDay);
        }

        return calendarDate;
    }


    public static int getSolarTermAmount(LocalDate localDate) {

        int amount = 0;
        for (int i = 1; i <= localDate.dayOfMonth().get(); i++) {

            CalendarDate calendarDate = new CalendarDate();
            int solarYear = localDate.getYear();
            int solarMonth = localDate.getMonthOfYear();
            int solarDay = i;

            String solatName = SolarTermUtil.getSolatName(solarYear, (solarMonth < 10 ? "0" + solarMonth : solarMonth + "") + solarDay);
            if (!TextUtils.isEmpty(solatName)) {
                return 1;
            }
        }

        return 0;
    }


    public static Lunar getCalendarLunar(Calendar calendar) {
        int solarYear = calendar.get(Calendar.YEAR);
        int solarMonth = calendar.get(Calendar.MONTH) + 1;
        int solarDay = calendar.get(Calendar.DAY_OF_MONTH);
        return LunarUtil.getLunar(solarYear, solarMonth, solarDay);
    }

    /**
     * 这里提供一个方法返回 黄历页面索要的信息
     */
    public static CalendarDate2 getCalendarDate2(LocalDate localDate) {
        CalendarDate2 calendarDate = new CalendarDate2();
        int solarYear = localDate.getYear();
        int solarMonth = localDate.getMonthOfYear();
        int solarDay = localDate.getDayOfMonth();

        Lunar lunar = LunarUtil.getLunar(solarYear, solarMonth, solarDay);
        if (solarYear != 1900) {
            calendarDate.lunar = lunar;
            calendarDate.localDate = localDate;
            calendarDate.solarTerm = SolarTermUtil.getSolatName(solarYear, (solarMonth < 10 ? "0" + solarMonth : solarMonth + "") + solarDay);
            calendarDate.GZ = LunarUtil.ymdGZ(lunar.lunarYear, localDate.toDate());
            int i = cutTwoDateToDay(new Date(), localDate.toDate(), true);
            String str = i == 0 ? "今天" : (i > 0 ? Math.abs(i) + "天后" : Math.abs(i) + "天前");
            calendarDate.date = getWeekOfMonth(localDate) + " " + getDAY_OF_WEEK(localDate) + " " + str;

        }
        return calendarDate;
    }

    private static String[] getDateMD(LocalDate localDate) {
        Date date = localDate.toDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-M-d");
        String format = simpleDateFormat.format(date);
        return format.split("-");
    }

    private static String getDateFormat(LocalDate localDate) {
        Date date = localDate.toDate();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy年M月");
        String format = simpleDateFormat.format(date);
        return format;
    }

    /**
     * 这里提供一个方法返回 黄历页面索要的信息 属相
     */
    public static CalendarDate3 getCalendarDate3(LocalDate localDate) {
        CalendarDate3 calendarDate = new CalendarDate3();
        int solarYear = localDate.getYear();
        int solarMonth = localDate.getMonthOfYear();
        int solarDay = localDate.getDayOfMonth();
        Lunar lunar = LunarUtil.getLunar(solarYear, solarMonth, solarDay);
        if (solarYear != 1900) {
            calendarDate.dateString = getDateFormat(localDate);
            calendarDate.mdArrays = getDateMD(localDate);
            calendarDate.lunarStr = lunar.lunarMonthStr + lunar.lunarDayStr;
            calendarDate.GZ = LunarUtil.ymdAnimalGZ(lunar.lunarYear, localDate.toDate());
            int i = cutTwoDateToDay(new Date(), localDate.toDate(), true);
            String str = i == 0 ? "今天" : (i > 0 ? Math.abs(i) + "天后" : Math.abs(i) + "天前");
            calendarDate.week = getDAY_OF_WEEK(localDate);
            calendarDate.comingDay = str;
        }
        return calendarDate;
    }


    public static String[] getCalendarData(LocalDate localDate) {
        String[] str = new String[2];
        int solarYear = localDate.getYear();
        int solarMonth = localDate.getMonthOfYear();
        int solarDay = localDate.getDayOfMonth();
        Lunar lunar = LunarUtil.getLunar(solarYear, solarMonth, solarDay);
        if (solarYear != 1900) {
            str[0] = lunar.lunarMonthStr + lunar.lunarDayStr;
            str[1] = LunarUtil.yGZ(lunar.lunarYear, localDate.toDate());
        }
        return str;
    }


    public static String getSolarTerm(LocalDate localDate) {
        int solarYear = localDate.getYear();
        int solarMonth = localDate.getMonthOfYear();
        int solarDay = localDate.getDayOfMonth();
        return SolarTermUtil.getSolatName(solarYear, (solarMonth < 10 ? "0" + solarMonth : solarMonth + "") + solarDay);

    }


    /**
     * 计算两个日期之间的差距天数
     *
     * @return
     */
    public static int cutTwoDateToDay(Date beginDate, Date endDate, boolean inputFlag) {
        Calendar calendar = Calendar.getInstance();
        long intervalDays = 0;
        calendar.setTime(beginDate);
        long begin = calendar.getTimeInMillis();
        calendar.setTime(endDate);
        long end = calendar.getTimeInMillis();
        long totalM = end - begin;
        intervalDays = totalM / (24 * 60 * 60 * 1000);
        long intervalHours = (totalM - (intervalDays * 24 * 60 * 60 * 1000)) / (60 * 60 * 1000);
        long intervalMin = (totalM - intervalDays * (24 * 60 * 60 * 1000) - intervalHours * 60 * 60 * 1000) / (60 * 1000);
        if (inputFlag) {
            if (totalM > 0L && totalM % (24 * 60 * 60 * 1000) > 0L) {
                intervalDays = intervalDays + 1;
            }
        } else {

        }
        return (int) intervalDays;
    }

    public static String getWeekOfMonth(LocalDate localDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(localDate.toDate());
        int i = calendar.get(Calendar.WEEK_OF_YEAR);
        return "第" + i + "周";
    }

    public static int getWeekOfMonthToInt(LocalDate localDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(localDate.toDate());
        int i = calendar.get(Calendar.WEEK_OF_MONTH);
        return i;
    }

    public static String[] WeekOfMonth = {"第一周", "第二周", "第三周", "第四周", "第五周", "第六周"};

    public static String getDAY_OF_WEEK(LocalDate localDate) {
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(localDate.toDate());
        int i = calendar.get(Calendar.DAY_OF_WEEK);
        return DAY_OF_WEEK[i - 1];
    }

    public static String[] weekOfMonth = {"第一周", "第二周", "第三周", "第四周", "第五周", "第六周"};
    public static String[] DAY_OF_WEEK = {"周日", "周一", "周二", "周三", "周四", "周五", "周六"};
    public static String[] DAY_OF_WEEK_TOW = {"周一", "周二", "周三", "周四", "周五", "周六", "周日"};


    /**
     * 判断当前时间 是不是星期三
     * （根据传入的参数进行可控）
     * 以及是本月的第几个星期三
     */
    public static int countDate(Date date, int week) {
        String weekCn = DAY_OF_WEEK_TOW[week - 1];
        SimpleDateFormat format = new SimpleDateFormat("EEE");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        try {
            String format2 = simpleDateFormat.format(date);
            if (!format.format(date).equals(weekCn)) {
                return 0;
            }
            int day = Integer.parseInt(new SimpleDateFormat("dd").format(date));
            String yearMonth = new SimpleDateFormat("yyyy-MM-").format(date);
            int number = 0;
            for (int i = 1; i <= day; i++) {
                String dayStr = i + "";
                if (dayStr.length() == 1) {
                    dayStr = "0" + dayStr;
                }
                String weekText = format.format(simpleDateFormat.parse(yearMonth + dayStr));
                if (weekText.equals(weekCn)) {
                    number++;
                }
            }
            return number;
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return 0;
    }


    public static int getDaysOfMonth(LocalDate localDate) {
        DateTime dateTime = new DateTime(localDate.getYear(), localDate.getMonthOfYear(), 14, 12, 0, 0, 000);
        return dateTime.dayOfMonth().getMaximumValue();
    }
}
