package com.necer.entity;

import org.joda.time.LocalDate;

import java.io.Serializable;

/***
 * 日历日期 包含公历、农历、节气、节日
 * @author necer
 */
public class CalendarDate2 implements Serializable {

    /**
     * 公历日期
     */
    public LocalDate localDate;

    /**
     * 农历
     */
    public Lunar lunar;


    /**
     * 节气
     */
    public String solarTerm;

    /**
     * GZ
     */
    public String GZ;

    /**
     *
     */
    public String date;
}
