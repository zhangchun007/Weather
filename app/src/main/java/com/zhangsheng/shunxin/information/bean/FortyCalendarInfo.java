package com.zhangsheng.shunxin.information.bean;

import com.necer.entity.CalendarDate;
import com.zhangsheng.shunxin.calendar.wegdit.view.GregorianLunarCalendarView;

/**
 * @Description:
 * @Author: zhangchun
 * @CreateDate: 2021/7/20
 * @Version: 1.0
 */
public class FortyCalendarInfo extends CalendarDate {
    private String yinli;//阴历内容
    private String nongli;//农历内容（暂时没用到）
    private int weatherType;//天气类型
    private int year;//当前年份
    private int month;//当前月份
    private int dayOfMonth;//当前是哪一天
    private String dataTime;//当前日期：2021-06-27
    private boolean isToday = false;//是否是今天
    private boolean isSelect = false;//是否是选中状态

    private String fct; //时间 格式："2021-04-15"
    private String tcd;//最高温度
    private String tcn; //最低温度
    private String wdir; //西风
    private String wslv; //风速等级
    private String wt; //全天天气现象
    private String wtid; //全天天气id
    private String wt1; //白天天气现象
    private String wt1id; //白天天气现象id
    private String wt2;//	晚上天气现象
    private String wt2id; //晚上天气现象id

    public String getFct() {
        return fct;
    }

    public void setFct(String fct) {
        this.fct = fct;
    }

    public String getTcd() {
        return tcd;
    }

    public void setTcd(String tcd) {
        this.tcd = tcd;
    }

    public String getTcn() {
        return tcn;
    }

    public void setTcn(String tcn) {
        this.tcn = tcn;
    }

    public String getWdir() {
        return wdir;
    }

    public void setWdir(String wdir) {
        this.wdir = wdir;
    }

    public String getWslv() {
        return wslv;
    }

    public void setWslv(String wslv) {
        this.wslv = wslv;
    }

    public String getWt() {
        return wt;
    }

    public void setWt(String wt) {
        this.wt = wt;
    }

    public String getWtid() {
        return wtid;
    }

    public void setWtid(String wtid) {
        this.wtid = wtid;
    }

    public String getWt1() {
        return wt1;
    }

    public void setWt1(String wt1) {
        this.wt1 = wt1;
    }

    public String getWt1id() {
        return wt1id;
    }

    public void setWt1id(String wt1id) {
        this.wt1id = wt1id;
    }

    public String getWt2() {
        return wt2;
    }

    public void setWt2(String wt2) {
        this.wt2 = wt2;
    }

    public String getWt2id() {
        return wt2id;
    }

    public void setWt2id(String wt2id) {
        this.wt2id = wt2id;
    }

    public boolean isSelect() {
        return isSelect;
    }

    public void setSelect(boolean select) {
        isSelect = select;
    }

    public boolean isToday() {
        return isToday;
    }

    public void setToday(boolean today) {
        isToday = today;
    }

    public int getYear() {
        return year;
    }

    public void setYear(int year) {
        this.year = year;
    }

    public int getMonth() {
        return month;
    }

    public void setMonth(int month) {
        this.month = month;
    }

    public int getDayOfMonth() {
        return dayOfMonth;
    }

    public void setDayOfMonth(int dayOfMonth) {
        this.dayOfMonth = dayOfMonth;
    }

    public String getDataTime() {
        return dataTime;
    }

    public void setDataTime(String dataTime) {
        this.dataTime = dataTime;
    }

    public String getYinli() {
        return yinli;
    }

    public void setYinli(String yinli) {
        this.yinli = yinli;
    }

    public String getNongli() {
        return nongli;
    }

    public void setNongli(String nongli) {
        this.nongli = nongli;
    }

    public int getWeatherType() {
        return weatherType;
    }

    public void setWeatherType(int weatherType) {
        this.weatherType = weatherType;
    }
}
