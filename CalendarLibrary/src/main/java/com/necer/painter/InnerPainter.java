package com.necer.painter;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.Typeface;
import android.graphics.drawable.Drawable;
import androidx.core.content.ContextCompat;
import android.text.TextUtils;

import com.necer.R;
import com.necer.calendar.ICalendar;
import com.necer.entity.CalendarDate;
import com.necer.utils.Attrs;
import com.necer.utils.CalendarUtil;
import com.necer.utils.DensityUtil;
import com.necer.utils.DrawableUtil;

import org.joda.time.LocalDate;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author necer
 * @date 2019/1/3
 */
public class InnerPainter implements CalendarPainter {

    private Attrs mAttrs;
    private Paint mTextPaint;
    private Paint paint;

    private int noAlphaColor = 255;

    private List<LocalDate> mHolidayList;
    private List<LocalDate> mWorkdayList;

    private List<LocalDate> mPointList;
    private Map<LocalDate, String> mReplaceLunarStrMap;
    private Map<LocalDate, Integer> mReplaceLunarColorMap;
    private Map<LocalDate, String> mStretchStrMap;

    private ICalendar mCalendar;

    private Drawable mDefaultCheckedBackground;
    private Drawable mTodayCheckedBackground;
    private Drawable mTodayunCheckedBackground;

    private Drawable mDefaultCheckedPoint;
    private Drawable mDefaultUnCheckedPoint;
    private Drawable mTodayCheckedPoint;
    private Drawable mTodayUnCheckedPoint;


    private Context mContext;


    public InnerPainter(Context context, ICalendar calendar) {
        this.mAttrs = calendar.getAttrs();
        this.mContext = context;
        this.mCalendar = calendar;

        mTextPaint = new Paint();
        mTextPaint.setAntiAlias(true);
        mTextPaint.setTextAlign(Paint.Align.CENTER);
        paint = new Paint();
        paint.setAntiAlias(true);
        paint.setTextAlign(Paint.Align.CENTER);
        Typeface font = Typeface.createFromAsset(mContext.getAssets(), "fonts/DINNumber.ttf");
        paint.setTypeface(font);
        mPointList = new ArrayList<>();
        mHolidayList = new ArrayList<>();
        mWorkdayList = new ArrayList<>();
        mReplaceLunarStrMap = new HashMap<>();
        mReplaceLunarColorMap = new HashMap<>();
        mStretchStrMap = new HashMap<>();

        mDefaultCheckedBackground = ContextCompat.getDrawable(context, mAttrs.defaultCheckedBackground);
        mTodayCheckedBackground = ContextCompat.getDrawable(context, mAttrs.todayCheckedBackground);
        mTodayunCheckedBackground = ContextCompat.getDrawable(context, mAttrs.todayunCheckedBackground);

        mDefaultCheckedPoint = ContextCompat.getDrawable(context, mAttrs.defaultCheckedPoint);
        mDefaultUnCheckedPoint = ContextCompat.getDrawable(context, mAttrs.defaultUnCheckedPoint);
        mTodayCheckedPoint = ContextCompat.getDrawable(context, mAttrs.todayCheckedPoint);
        mTodayUnCheckedPoint = ContextCompat.getDrawable(context, mAttrs.todayUnCheckedPoint);

        List<String> holidayList = CalendarUtil.getHolidayList();
        for (int i = 0; i < holidayList.size(); i++) {
            mHolidayList.add(new LocalDate(holidayList.get(i)));
        }
        List<String> workdayList = CalendarUtil.getWorkdayList();
        for (int i = 0; i < workdayList.size(); i++) {
            mWorkdayList.add(new LocalDate(workdayList.get(i)));
        }
    }


    @Override
    public void onDrawToday(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> checkedDateList) {
        if (checkedDateList.contains(localDate)) {
            drawCheckedBackground(canvas, mTodayCheckedBackground, rectF, noAlphaColor);
            drawSolar(canvas, rectF, localDate, mAttrs.todayCheckedSolarTextColor, noAlphaColor);
            drawLunar(canvas, rectF, localDate, mAttrs.todayCheckedLunarTextColor, noAlphaColor, true);
            drawPoint(canvas, rectF, localDate, mTodayCheckedPoint, noAlphaColor);
            drawHolidayWorkday(canvas, rectF, localDate, mAttrs.todayCheckedHoliday, mAttrs.todayCheckedWorkday, mAttrs.todayCheckedHolidayTextColor, mAttrs.todayCheckedWorkdayTextColor, noAlphaColor);
        } else {
            drawCheckedBackground(canvas, mTodayunCheckedBackground, rectF, noAlphaColor);
            drawSolar(canvas, rectF, localDate, Color.WHITE, noAlphaColor);
            drawLunar(canvas, rectF, localDate, Color.WHITE, noAlphaColor, false);
            drawPoint(canvas, rectF, localDate, mTodayUnCheckedPoint, noAlphaColor);
            drawHolidayWorkday(canvas, rectF, localDate, mAttrs.todayUnCheckedHoliday, mAttrs.todayUnCheckedWorkday, mAttrs.todayUnCheckedHolidayTextColor, mAttrs.todayUnCheckedWorkdayTextColor, noAlphaColor);
        }
        drawStretchText(canvas, rectF, noAlphaColor, localDate);
    }

    @Override
    public void onDrawCurrentMonthOrWeek(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> checkedDateList) {
        if (checkedDateList.contains(localDate)) {
            drawCheckedBackground(canvas, mDefaultCheckedBackground, rectF, noAlphaColor);

            if (localDate.getDayOfWeek() == 6 || localDate.getDayOfWeek() == 7) {
                drawSolar(canvas, rectF, localDate, mAttrs.defaultSolarTextColorWeekSixSeven, noAlphaColor);
            } else {
                drawSolar(canvas, rectF, localDate, mAttrs.defaultCheckedSolarTextColor, noAlphaColor);
            }


            drawLunar(canvas, rectF, localDate, mAttrs.defaultCheckedLunarTextColor, noAlphaColor, true);
            drawPoint(canvas, rectF, localDate, mDefaultCheckedPoint, noAlphaColor);
            drawHolidayWorkday(canvas, rectF, localDate, mAttrs.defaultCheckedHoliday, mAttrs.defaultCheckedWorkday, mAttrs.defaultCheckedHolidayTextColor, mAttrs.defaultCheckedWorkdayTextColor, noAlphaColor);

        } else {
            if (localDate.getDayOfWeek() == 6 || localDate.getDayOfWeek() == 7) {
                drawSolar(canvas, rectF, localDate, mAttrs.defaultSolarTextColorWeekSixSeven, noAlphaColor);
            } else {
                drawSolar(canvas, rectF, localDate, mAttrs.defaultUnCheckedSolarTextColor, noAlphaColor);
            }
//            drawSolar(canvas, rectF, localDate, mAttrs.defaultUnCheckedSolarTextColor, noAlphaColor);
            drawLunar(canvas, rectF, localDate, mAttrs.defaultUnCheckedLunarTextColor, noAlphaColor, false);
            drawPoint(canvas, rectF, localDate, mDefaultUnCheckedPoint, noAlphaColor);
            drawHolidayWorkday(canvas, rectF, localDate, mAttrs.defaultUnCheckedHoliday, mAttrs.defaultUnCheckedWorkday, mAttrs.defaultUnCheckedHolidayTextColor, mAttrs.defaultUnCheckedWorkdayTextColor, noAlphaColor);
        }
        drawStretchText(canvas, rectF, noAlphaColor, localDate);

    }

    @Override
    public void onDrawLastOrNextMonth(Canvas canvas, RectF rectF, LocalDate localDate, List<LocalDate> checkedDateList) {
        //上下月当天日期 要将当天未选中的背景进行
        if (checkedDateList.contains(localDate)) {
            drawCheckedBackground(canvas, mDefaultCheckedBackground, rectF, mAttrs.lastNextMothAlphaColor);
            if (localDate.getDayOfWeek() == 6 || localDate.getDayOfWeek() == 7) {
                drawSolar(canvas, rectF, localDate, mAttrs.defaultSolarTextColorWeekSixSeven, mAttrs.lastNextMothAlphaColor);
            } else {
                drawSolar(canvas, rectF, localDate, mAttrs.defaultCheckedSolarTextColor, mAttrs.lastNextMothAlphaColor);
            }

            drawLunar(canvas, rectF, localDate, mAttrs.defaultCheckedLunarTextColor, mAttrs.lastNextMothAlphaColor, true);
            drawPoint(canvas, rectF, localDate, mDefaultCheckedPoint, mAttrs.lastNextMothAlphaColor);
            drawHolidayWorkday(canvas, rectF, localDate, mAttrs.defaultCheckedHoliday, mAttrs.defaultCheckedWorkday, mAttrs.defaultCheckedHolidayTextColor, mAttrs.defaultCheckedWorkdayTextColor, mAttrs.lastNextMothAlphaColor);
        } else {
            if (CalendarUtil.isToday(localDate)) {
                drawCheckedBackground(canvas, mTodayunCheckedBackground, rectF, mAttrs.lastNextMothAlphaColor);
            }
            if (localDate.getDayOfWeek() == 6 || localDate.getDayOfWeek() == 7) {
                drawSolar(canvas, rectF, localDate, mAttrs.defaultSolarTextColorWeekSixSeven, mAttrs.lastNextMothAlphaColor);
            } else {
                drawSolar(canvas, rectF, localDate, mAttrs.defaultUnCheckedSolarTextColor, mAttrs.lastNextMothAlphaColor);
            }
            drawLunar(canvas, rectF, localDate, mAttrs.defaultUnCheckedLunarTextColor, mAttrs.lastNextMothAlphaColor, false);
            drawPoint(canvas, rectF, localDate, mDefaultUnCheckedPoint, mAttrs.lastNextMothAlphaColor);
            drawHolidayWorkday(canvas, rectF, localDate, mAttrs.defaultUnCheckedHoliday, mAttrs.defaultUnCheckedWorkday, mAttrs.defaultUnCheckedHolidayTextColor, mAttrs.defaultUnCheckedWorkdayTextColor, mAttrs.lastNextMothAlphaColor);
        }
        drawStretchText(canvas, rectF, mAttrs.lastNextMothAlphaColor, localDate);
    }

    @Override
    public void onDrawDisableDate(Canvas canvas, RectF rectF, LocalDate localDate) {
        drawSolar(canvas, rectF, localDate, mAttrs.defaultUnCheckedSolarTextColor, mAttrs.disabledAlphaColor);
        drawLunar(canvas, rectF, localDate, mAttrs.defaultUnCheckedLunarTextColor, mAttrs.disabledAlphaColor, false);
        drawPoint(canvas, rectF, localDate, mDefaultUnCheckedPoint, mAttrs.disabledAlphaColor);
        drawHolidayWorkday(canvas, rectF, localDate, mAttrs.defaultUnCheckedHoliday, mAttrs.defaultUnCheckedWorkday, mAttrs.defaultUnCheckedHolidayTextColor, mAttrs.defaultUnCheckedWorkdayTextColor, mAttrs.disabledAlphaColor);
        drawStretchText(canvas, rectF, mAttrs.disabledAlphaColor, localDate);
    }

    //选中背景
    private void drawCheckedBackground(Canvas canvas, Drawable drawable, RectF rectF, int alphaColor) {
        Rect drawableBounds = DrawableUtil.getDrawableBounds((int) rectF.centerX(), (int) rectF.centerY(), drawable);
        drawable.setBounds(drawableBounds);
        drawable.setAlpha(alphaColor);
        drawable.draw(canvas);
    }


    //绘制公历
    private void drawSolar(Canvas canvas, RectF rectF, LocalDate date, int color, int alphaColor) {
        paint.setColor(color);
        paint.setAlpha(alphaColor);
        paint.setTextSize(mAttrs.solarTextSize);
        paint.setFakeBoldText(mAttrs.solarTextBold);
        canvas.drawText(date.getDayOfMonth() + "", rectF.centerX(), mAttrs.showLunar ? rectF.centerY() + DensityUtil.dp2px(1) : getTextBaseLineY(rectF.centerY()), paint);
    }

    //绘制农历
    private void drawLunar(Canvas canvas, RectF rectF, LocalDate localDate, int color, int alphaColor, boolean isSelectedToday) {
        if (mAttrs.showLunar) {
            CalendarDate calendarDate = CalendarUtil.getCalendarDate(localDate);
            //农历部分文字展示优先顺序 替换的文字、农历节日、节气、公历节日、正常农历日期
            String lunarString = (String) this.mReplaceLunarStrMap.get(calendarDate.localDate);
            String replaceString = mReplaceLunarStrMap.get(calendarDate.localDate);
            if (lunarString == null) {

                if (!TextUtils.isEmpty(calendarDate.holiday)) {
                    this.mTextPaint.setColor(Color.parseColor("#D13F3F"));
                    lunarString = calendarDate.holiday;
                } else if (!TextUtils.isEmpty(calendarDate.solarTerm)) {
                    this.mTextPaint.setColor(Color.parseColor("#36995D"));
                    lunarString = calendarDate.solarTerm;
                } else if (!TextUtils.isEmpty(calendarDate.lunarHoliday)) {
                    this.mTextPaint.setColor(Color.parseColor("#D13F3F"));
                    lunarString = calendarDate.lunarHoliday;
                } else if (!TextUtils.isEmpty(calendarDate.solarHoliday)) {
                    this.mTextPaint.setColor(Color.parseColor("#D13F3F"));
                    lunarString = calendarDate.solarHoliday;
                } else {
                    mTextPaint.setColor(color);
                    if (calendarDate.lunar != null) {
                        lunarString = "初一".equals(calendarDate.lunar.lunarOnDrawStr) ? calendarDate.lunar.lunarMonthStr : calendarDate.lunar.lunarOnDrawStr;

                    }
                }

                if (lunarString!= null && lunarString.contains("清明")){
                    this.mTextPaint.setColor(Color.parseColor("#36995D"));
                }

            }
            //這裏去判斷是否是當天選中 是當天選中狀態 設置正常顔色
            if (CalendarUtil.isToday(localDate)) {
                this.mTextPaint.setColor(color);
            }
//            Integer replaceColor = mReplaceLunarColorMap.get(calendarDate.localDate);
//            mTextPaint.setColor(replaceColor == null ? color : replaceColor);
            if (lunarString != null) {
                if (lunarString.length() > 3) {
                    mTextPaint.setTextSize(DensityUtil.dp2px(11));
                } else {
                    mTextPaint.setTextSize(mAttrs.lunarTextSize);
                }


                mTextPaint.setAlpha(alphaColor);
                mTextPaint.setFakeBoldText(mAttrs.lunarTextBold);
                canvas.drawText(lunarString, rectF.centerX(), rectF.centerY() + mAttrs.lunarDistance, mTextPaint);
            }
        }
    }


    //绘制标记
    private void drawPoint(Canvas canvas, RectF rectF, LocalDate date, Drawable drawable, int alphaColor) {
        if (mPointList.contains(date)) {
            float centerY = mAttrs.pointLocation == Attrs.DOWN ? (rectF.centerY() + mAttrs.pointDistance) : (rectF.centerY() - mAttrs.pointDistance);
            Rect drawableBounds = DrawableUtil.getDrawableBounds((int) rectF.centerX(), (int) centerY, drawable);
            drawable.setBounds(drawableBounds);
            drawable.setAlpha(alphaColor);
            drawable.draw(canvas);
        }
    }

    //绘制节假日
    private void drawHolidayWorkday(Canvas canvas, RectF rectF, LocalDate localDate, Drawable holidayDrawable, Drawable workdayDrawable, int holidayTextColor, int workdayTextColor, int alphaColor) {
        if (mAttrs.showHolidayWorkday) {
            int[] holidayLocation = getHolidayWorkdayLocation(rectF.centerX(), rectF.centerY());
            if (mHolidayList.contains(localDate)) {
                if (holidayDrawable == null) {
                    mTextPaint.setTextSize(mAttrs.holidayWorkdayTextSize);
                    mTextPaint.setColor(holidayTextColor);
                    mTextPaint.setAlpha(alphaColor);
                    canvas.drawCircle(holidayLocation[0],holidayLocation[1], DensityUtil.dp2px(7),mTextPaint);
                    mTextPaint.setAlpha(255);
                    mTextPaint.setColor(Color.WHITE);
                    canvas.drawText(TextUtils.isEmpty(mAttrs.holidayText) ? mContext.getString(R.string.N_holidayText) : mAttrs.holidayText, holidayLocation[0], getTextBaseLineY(holidayLocation[1]), mTextPaint);
                } else {
                    Rect drawableBounds = DrawableUtil.getDrawableBounds(holidayLocation[0], holidayLocation[1], holidayDrawable);
                    holidayDrawable.setBounds(drawableBounds);
                    holidayDrawable.setAlpha(alphaColor);
                    holidayDrawable.draw(canvas);
                }
            } else if (mWorkdayList.contains(localDate)) {
                if (workdayDrawable == null) {
                    mTextPaint.setTextSize(mAttrs.holidayWorkdayTextSize);
                    mTextPaint.setColor(workdayTextColor);
                    mTextPaint.setAlpha(alphaColor);
                    canvas.drawCircle(holidayLocation[0],holidayLocation[1], DensityUtil.dp2px(7),mTextPaint);
                    mTextPaint.setColor(Color.WHITE);
                    mTextPaint.setAlpha(255);
                    mTextPaint.setFakeBoldText(mAttrs.holidayWorkdayTextBold);
                    canvas.drawText(TextUtils.isEmpty(mAttrs.workdayText) ? mContext.getString(R.string.N_workdayText) : mAttrs.workdayText, holidayLocation[0], getTextBaseLineY(holidayLocation[1]), mTextPaint);
                } else {
                    Rect drawableBounds = DrawableUtil.getDrawableBounds(holidayLocation[0], holidayLocation[1], workdayDrawable);
                    workdayDrawable.setBounds(drawableBounds);
                    workdayDrawable.setAlpha(alphaColor);
                    workdayDrawable.draw(canvas);
                }
            }
        }
    }

    //绘制拉伸的文字
    private void drawStretchText(Canvas canvas, RectF rectF, int alphaColor, LocalDate localDate) {
        float v = rectF.centerY() + mAttrs.stretchTextDistance;
        //超出当前矩形 不绘制
        if (v <= rectF.bottom) {
            String stretchText = mStretchStrMap.get(localDate);
            if (!TextUtils.isEmpty(stretchText)) {
                mTextPaint.setTextSize(mAttrs.stretchTextSize);
                mTextPaint.setColor(mAttrs.stretchTextColor);
                mTextPaint.setAlpha(alphaColor);
                mTextPaint.setFakeBoldText(mAttrs.stretchTextBold);
                canvas.drawText(stretchText, rectF.centerX(), rectF.centerY() + mAttrs.stretchTextDistance, mTextPaint);
            }
        }
    }

    //canvas.drawText的基准线
    private float getTextBaseLineY(float centerY) {
        Paint.FontMetrics fontMetrics = mTextPaint.getFontMetrics();
        return centerY - (fontMetrics.bottom - fontMetrics.top) / 2 - fontMetrics.top;
    }


    //HolidayWorkday的位置
    private int[] getHolidayWorkdayLocation(float centerX, float centerY) {
        int[] location = new int[2];
        switch (mAttrs.holidayWorkdayLocation) {
            case Attrs.TOP_LEFT:
                location[0] = (int) (centerX - mAttrs.holidayWorkdayDistanceX);
                location[1] = (int) (centerY - mAttrs.holidayWorkdayDistanceY);
                break;
            case Attrs.BOTTOM_RIGHT:
                location[0] = (int) (centerX + mAttrs.holidayWorkdayDistanceX);
                location[1] = (int) (centerY + mAttrs.holidayWorkdayDistanceY);
                break;
            case Attrs.BOTTOM_LEFT:
                location[0] = (int) (centerX - mAttrs.holidayWorkdayDistanceX);
                location[1] = (int) (centerY + mAttrs.holidayWorkdayDistanceY);
                break;
            case Attrs.TOP_RIGHT:
            default:
                location[0] = (int) (centerX + mAttrs.holidayWorkdayDistanceX);
                location[1] = (int) (centerY - mAttrs.holidayWorkdayDistanceY);
                break;
        }
        return location;

    }

    //设置标记
    public void setPointList(List<String> list) {
        mPointList.clear();
        for (int i = 0; i < list.size(); i++) {
            LocalDate localDate = null;
            try {
                localDate = new LocalDate(list.get(i));
            } catch (Exception e) {
                throw new RuntimeException("setPointList的参数需要 yyyy-MM-dd 格式的日期");
            }
            mPointList.add(localDate);
        }
        mCalendar.notifyCalendar();
    }

    public void addPointList(List<String> list) {
        for (int i = 0; i < list.size(); i++) {
            LocalDate localDate = null;
            try {
                localDate = new LocalDate(list.get(i));
            } catch (Exception e) {
                throw new RuntimeException("setPointList的参数需要 yyyy-MM-dd 格式的日期");
            }
            if (!mPointList.contains(localDate)) {
                mPointList.add(localDate);
            }
        }
        mCalendar.notifyCalendar();
    }

    //设置替换农历的文字
    public void setReplaceLunarStrMap(Map<String, String> replaceLunarStrMap) {
        mReplaceLunarStrMap.clear();
        for (String key : replaceLunarStrMap.keySet()) {
            LocalDate localDate;
            try {
                localDate = new LocalDate(key);
            } catch (Exception e) {
                throw new RuntimeException("setReplaceLunarStrMap的参数需要 yyyy-MM-dd 格式的日期");
            }
            mReplaceLunarStrMap.put(localDate, replaceLunarStrMap.get(key));
        }
        mCalendar.notifyCalendar();
    }

    //设置替换农历的颜色
    public void setReplaceLunarColorMap(Map<String, Integer> replaceLunarColorMap) {
        mReplaceLunarColorMap.clear();
        for (String key : replaceLunarColorMap.keySet()) {
            LocalDate localDate;
            try {
                localDate = new LocalDate(key);
            } catch (Exception e) {
                throw new RuntimeException("setReplaceLunarColorMap的参数需要 yyyy-MM-dd 格式的日期");
            }
            mReplaceLunarColorMap.put(localDate, replaceLunarColorMap.get(key));
        }
        mCalendar.notifyCalendar();
    }


    /**
     * 设置调休调班日。根据
     */
    public void updataHoildayListOfYear(){
        setLegalHolidayList(CalendarUtil.getHolidayList(),CalendarUtil.getWorkdayList());
    }


    //设置法定节假日和补班
    @Override
    public void setLegalHolidayList(List<String> holidayList, List<String> workdayList) {
        mHolidayList.clear();
        mWorkdayList.clear();

        for (int i = 0; i < holidayList.size(); i++) {
            LocalDate holidayLocalDate;
            try {
                holidayLocalDate = new LocalDate(holidayList.get(i));
            } catch (Exception e) {
                throw new RuntimeException("setLegalHolidayList集合中的参数需要 yyyy-MM-dd 格式的日期");
            }
            mHolidayList.add(holidayLocalDate);
        }

        for (int i = 0; i < workdayList.size(); i++) {
            LocalDate workdayLocalDate;
            try {
                workdayLocalDate = new LocalDate(workdayList.get(i));
            } catch (Exception e) {
                throw new RuntimeException("setLegalHolidayList集合中的参数需要 yyyy-MM-dd 格式的日期");
            }
            mWorkdayList.add(workdayLocalDate);
        }
        mCalendar.notifyCalendar();
    }


    public void setStretchStrMap(Map<String, String> stretchStrMap) {
        mStretchStrMap.clear();
        for (String key : stretchStrMap.keySet()) {
            LocalDate localDate;
            try {
                localDate = new LocalDate(key);

            } catch (Exception e) {
                throw new RuntimeException("setStretchStrMap的参数需要 yyyy-MM-dd 格式的日期");
            }
            mStretchStrMap.put(localDate, stretchStrMap.get(key));
        }
        mCalendar.notifyCalendar();
    }
}
