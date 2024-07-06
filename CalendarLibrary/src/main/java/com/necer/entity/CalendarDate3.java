package com.necer.entity;

import java.io.Serializable;

/***
 * 日历日期 包含公历、农历、节气、节日
 * @author necer
 */
public class CalendarDate3 implements Serializable {

    /**
     * 公历格式2020年4月
     */
    public String dateString;

    /**
     * [4,21]
     */
    public String[] mdArrays;

    /**
     * 农历 三月二十
     */
    public String lunarStr;

    /**
     * GZ
     */
    public String GZ;

    /**
     * 周几
     */
    public String week;
    /**
     * 1天后
     */
    public String comingDay;
}
