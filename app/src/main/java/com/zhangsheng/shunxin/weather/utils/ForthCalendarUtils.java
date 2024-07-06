package com.zhangsheng.shunxin.weather.utils;


import android.util.Log;

import com.zhangsheng.shunxin.information.bean.FortyCalendarInfo;
import com.zhangsheng.shunxin.weather.net.bean.FortyWeatherBean;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

/**
 * @Description:
 * @Author: zhangchun
 * @CreateDate: 2021/7/20
 * @Version: 1.0
 */
public class ForthCalendarUtils {
    /**
     * 获取当前月份
     *
     * @return
     */
    public static int getCurrentMonth() {
        Calendar cal = Calendar.getInstance();
        int month = (cal.get(Calendar.MONTH) + 1);
        return month;
    }


    /**
     * 获取当前年份
     *
     * @return
     */
    public static int getCurrentYear() {
        Calendar cal = Calendar.getInstance();
        int year = cal.get(Calendar.YEAR);
        return year;
    }

    /**
     * 获取当前月份最大天数
     *
     * @return
     */
    public static int getCurrentMonthMaxDays() {
        Calendar cal = Calendar.getInstance();
        cal.set(Calendar.DATE, 1);
        cal.roll(Calendar.DATE, -1);
        int maxDate = cal.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 获取当月第一天是星期的第几天
     *
     * @return
     */
    public static String getFirstDayOfWeekInCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, 1);
        SimpleDateFormat format = new SimpleDateFormat("E");
        return format.format(calendar.getTime());
    }

    /**
     * 获取当月第一天是星期的第几天
     *
     * @return
     */
    public static String getLastDayOfWeekInCurrentMonth() {
        Calendar calendar = Calendar.getInstance();
        calendar.set(Calendar.DAY_OF_MONTH, getCurrentMonthMaxDays());
        SimpleDateFormat format = new SimpleDateFormat("E");
        return format.format(calendar.getTime());
    }

    /**
     * 获得今天是本月的第几天
     *
     * @return
     */
    public static int getCurrentDay() {
        Calendar cal = Calendar.getInstance();
        int currentDay = cal.get(Calendar.DAY_OF_MONTH);
        return currentDay;
    }

    /**
     * 获取当前月份的下个月份
     *
     * @return
     */
    public static int getNextMonth() {
        Calendar cal = Calendar.getInstance();
        int month = (cal.get(Calendar.MONTH) + 1);
        if (month == 12) {
            month = 1;
        } else {
            month++;
        }
        return month;
    }

    /**
     * 获取当下个月份的年份
     *
     * @return
     */
    public static int getNextMonthYear() {
        Calendar cal = Calendar.getInstance();
        int month = (cal.get(Calendar.MONTH) + 1);
        int year;
        if (month == 12) {
            year = getCurrentYear() + 1;
        } else {
            year = getCurrentYear();
        }
        return year;
    }

    /**
     * 获取第三个月的月份
     *
     * @return
     */
    public static int getThirdMonth() {
        int month = getNextMonth();
        if (month == 12) {
            month = 1;
        } else {
            month++;
        }
        return month;
    }

    /**
     * 获取第三个月的年份
     *
     * @return
     */
    public static int getThirdMonthYear() {
        int month = getNextMonth();
        int year;
        if (month == 12) {
            year = getNextMonthYear() + 1;
        } else {
            year = getNextMonthYear();
        }
        return year;
    }

    /**
     * 根据 年、月 获取对应的月份 的 天数
     */
    public static int getDaysByYearMonth(int year, int month) {
        Calendar a = Calendar.getInstance();
        a.set(Calendar.YEAR, year);
        a.set(Calendar.MONTH, month - 1);
        a.set(Calendar.DATE, 1);
        a.roll(Calendar.DATE, -1);
        int maxDate = a.get(Calendar.DATE);
        return maxDate;
    }

    /**
     * 根据日期 找到对应日期的 星期几
     *
     * @param date 比如传参：2018-07-13 将返回“周五”
     */
    public static String getDayOfWeekByDate(String date) {
        String dayOfweek = "-1";
        try {
            SimpleDateFormat myFormatter = new SimpleDateFormat("yyyy-MM-dd");
            Date myDate = myFormatter.parse(date);
            SimpleDateFormat formatter = new SimpleDateFormat("E");
            String str = formatter.format(myDate);
            dayOfweek = str;
        } catch (Exception e) {
            System.out.println("错误!");
        }
        return dayOfweek;
    }

    /**
     * 根据当前日期获得是星期几
     * time=yyyy-MM-dd
     *
     * @return
     */
    public static String getWeek(String time) {
        String Week = "";
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd");
        Calendar c = Calendar.getInstance();
        try {
            c.setTime(format.parse(time));
        } catch (ParseException e) {
            e.printStackTrace();
        }

        int wek = c.get(Calendar.DAY_OF_WEEK);

        if (wek == 1) {
            Week += "星期日";
        }
        if (wek == 2) {
            Week += "星期一";
        }
        if (wek == 3) {
            Week += "星期二";
        }
        if (wek == 4) {
            Week += "星期三";
        }
        if (wek == 5) {
            Week += "星期四";
        }
        if (wek == 6) {
            Week += "星期五";
        }
        if (wek == 7) {
            Week += "星期六";
        }
        return Week;
    }


    //是否需要三页数据
    public static boolean needThreeFragment() {
        int currentDays = getCurrentMonthMaxDays() - getCurrentDay() + 1;
        int nextMonthDays = getDaysByYearMonth(getNextMonthYear(), getNextMonth());
        if (currentDays + nextMonthDays < 40) return true;
        return false;
    }

    /**
     * 获取当月数据
     *
     * @return
     */
    public static List<FortyCalendarInfo> getCurrentMonthData() {
        List<FortyCalendarInfo> list = new ArrayList<>();
        int needAddCount = 0;
        switch (getFirstDayOfWeekInCurrentMonth()) {
            case "周日":
                needAddCount = 0;
                break;
            case "周一":
                needAddCount = 1;
                break;
            case "周二":
                needAddCount = 2;
                break;
            case "周三":
                needAddCount = 3;
                break;
            case "周四":
                needAddCount = 4;
                break;
            case "周五":
                needAddCount = 5;
                break;
            case "周六":
                needAddCount = 6;
                break;
        }
        for (int i = 0; i < needAddCount; i++) {
            FortyCalendarInfo info = new FortyCalendarInfo();
            info.setNongli("");
            info.setYinli("");
            info.setWeatherType(-1);
            list.add(info);
        }
        for (int i = 1; i <= getCurrentMonthMaxDays(); i++) {
            FortyCalendarInfo info = new FortyCalendarInfo();
            info.setYinli(i + "");
            info.setNongli("");
            info.setWeatherType(-1);
            if (i == getCurrentDay()) {//是否是今天
                info.setToday(true);
            }
            //2021-06-27
            String dataTime = getCurrentYear() + "-" + getCurrentMonth() + "-" + i;
            info.setDataTime(dataTime);
            info.setYear(getCurrentYear());
            info.setMonth(getCurrentMonth());
            info.setDayOfMonth(i);

            list.add(info);
        }
        return list;
    }

    /**
     * 获取下个月数据
     *
     * @return
     */
    public static List<FortyCalendarInfo> getNextMonthData() {
        List<FortyCalendarInfo> list = new ArrayList<>();
        int needAddCount = 0;
        String nextMonthData = getNextMonthYear() + "-" + getNextMonth() + "-" + "1";
        Log.e("getNextMonthData--", "nextMonthData=" + nextMonthData);
        switch (getDayOfWeekByDate(nextMonthData)) {
            case "周日":
                needAddCount = 0;
                break;
            case "周一":
                needAddCount = 1;
                break;
            case "周二":
                needAddCount = 2;
                break;
            case "周三":
                needAddCount = 3;
                break;
            case "周四":
                needAddCount = 4;
                break;
            case "周五":
                needAddCount = 5;
                break;
            case "周六":
                needAddCount = 6;
                break;
        }
        Log.e("getNextMonthData--", "needAddCount=" + needAddCount);
        Log.e("getNextMonthData--", "max=" + getDaysByYearMonth(getNextMonthYear(), getNextMonth()));
        for (int i = 0; i < needAddCount; i++) {
            FortyCalendarInfo info = new FortyCalendarInfo();
            info.setNongli("");
            info.setYinli("");
            info.setWeatherType(-1);
            list.add(info);
        }
        for (int i = 1; i <= getDaysByYearMonth(getNextMonthYear(), getNextMonth()); i++) {
            FortyCalendarInfo info = new FortyCalendarInfo();
            info.setYinli(i + "");
            info.setNongli("");
            String dataTime = getNextMonthYear() + "-" + getNextMonth() + "-" + i;
            info.setDataTime(dataTime);
            info.setYear(getNextMonthYear());
            info.setMonth(getNextMonth());
            info.setDayOfMonth(i);
            info.setWeatherType(-1);
            list.add(info);
        }

        return list;
    }


    /**
     * 获取第三个月数据
     *
     * @return
     */
    public static List<FortyCalendarInfo> getThirdMonthData() {
        List<FortyCalendarInfo> list = new ArrayList<>();
        int needAddCount = 0;
        String thirdMonthData = getThirdMonthYear() + "-" + getThirdMonth() + "-" + "1";
        Log.e("getThirdMonthData--", "thirdMonthData=" + thirdMonthData);
        switch (getDayOfWeekByDate(thirdMonthData)) {
            case "周日":
                needAddCount = 0;
                break;
            case "周一":
                needAddCount = 1;
                break;
            case "周二":
                needAddCount = 2;
                break;
            case "周三":
                needAddCount = 3;
                break;
            case "周四":
                needAddCount = 4;
                break;
            case "周五":
                needAddCount = 5;
                break;
            case "周六":
                needAddCount = 6;
                break;
        }
        Log.e("getThirdMonthData--", "needAddCount=" + needAddCount);
        Log.e("getThirdMonthData--", "max=" + getDaysByYearMonth(getThirdMonthYear(), getThirdMonth()));
        for (int i = 0; i < needAddCount; i++) {
            FortyCalendarInfo info = new FortyCalendarInfo();
            info.setNongli("");
            info.setYinli("");
            info.setWeatherType(-1);
            list.add(info);
        }
        for (int i = 1; i <= getDaysByYearMonth(getThirdMonthYear(), getThirdMonth()); i++) {
            FortyCalendarInfo info = new FortyCalendarInfo();
            info.setYinli(i + "");
            info.setNongli("");
            String dataTime = getThirdMonthYear() + "-" + getThirdMonth() + "-" + i;
            info.setDataTime(dataTime);
            info.setYear(getThirdMonthYear());
            info.setMonth(getThirdMonth());
            info.setDayOfMonth(i);
            info.setWeatherType(-1);
            list.add(info);
        }
        return list;
    }


}
