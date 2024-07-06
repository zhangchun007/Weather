package com.zhangsheng.shunxin.weather.widget;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.CornerPathEffect;
import android.graphics.Paint;
import android.graphics.Path;
import android.os.Build;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.xm.xmlog.bean.XMActivityBean;
import com.zhangsheng.shunxin.weather.common.EnumType;
import com.zhangsheng.shunxin.weather.ext.AppExtKt;
import com.zhangsheng.shunxin.weather.net.bean.FortyWeatherBean;
import com.zhangsheng.shunxin.weather.utils.DataUtil;

import java.util.ArrayList;

public class LineChartView extends View {

    private Paint mLinePaint;//刻度线画笔
    private Paint mRulerPaint;//指示线画笔
    private Paint mTextPaint;//指示数字画笔
    private Paint mDateTextPaint;//日期画笔
    private Paint mTextBgPaint;//文体背景画笔
    private Paint mCurvePaint;//折线画笔
    private Paint mShadowPaint;//填充颜色画笔

    private float position = 0;
    private int mWidth1 = 0;
    private int mHight = 0;
    private ArrayList<FortyWeatherBean.YbdsBean> dataList = new ArrayList<>(); // 温度数据
    private int maxTemp = 0;//最高温度
    private int minTemp = 0;//最低温度
    private float tempDiff = 0; // 温差
    private String maxTempText;
    private String minTempText;

    public LineChartView(Context context) {
        super(context);
        init();
    }

    public LineChartView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public LineChartView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLinePaint = new Paint();
        mLinePaint.setColor(Color.parseColor("#F3F1F4"));
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(1);

        mCurvePaint = new Paint();
        mCurvePaint.setColor(Color.parseColor("#4595EF"));
        mCurvePaint.setAntiAlias(true);
        mCurvePaint.setStyle(Paint.Style.STROKE);
        mCurvePaint.setStrokeWidth(6);
        mCurvePaint.setPathEffect(new CornerPathEffect(20));

        mShadowPaint = new Paint();
        mShadowPaint.setColor(Color.parseColor("#1a4595EF"));
        mShadowPaint.setAntiAlias(true);
        mShadowPaint.setPathEffect(new CornerPathEffect(20));
        mShadowPaint.setStyle(Paint.Style.FILL);

        mTextBgPaint = new Paint();
        mTextBgPaint.setColor(Color.parseColor("#3797FF"));
        mTextBgPaint.setAntiAlias(true);
        mTextBgPaint.setStyle(Paint.Style.FILL);
        mTextBgPaint.setStrokeWidth(10);

        mRulerPaint = new Paint();
        mRulerPaint.setAntiAlias(true);
        mRulerPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mRulerPaint.setColor(Color.parseColor("#45B2EF"));
        mRulerPaint.setStrokeWidth(3);

        mTextPaint = new Paint();
        mTextPaint.setColor(Color.parseColor("#ffffff"));
        mTextPaint.setAntiAlias(true);
        mTextPaint.setStyle(Paint.Style.FILL);
        mTextPaint.setStrokeWidth(2);
        mTextPaint.setTextSize(24);

        mDateTextPaint = new Paint();
        mDateTextPaint.setColor(Color.parseColor("#7A7B80"));
        mDateTextPaint.setAntiAlias(true);
        mDateTextPaint.setStyle(Paint.Style.FILL);
        mDateTextPaint.setStrokeWidth(2);
        mDateTextPaint.setTextSize(24);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        setMeasuredDimension(setMeasureWidth(widthMeasureSpec), setMeasureHeight(heightMeasureSpec));
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mWidth1 = w;
        mHight = h;
    }

    private int setMeasureHeight(int spec) {
        int mode = MeasureSpec.getMode(spec);
        int size = MeasureSpec.getSize(spec);
        int result = Integer.MAX_VALUE;
        switch (mode) {
            case MeasureSpec.AT_MOST:
                size = Math.min(result, size);
                break;
            case MeasureSpec.EXACTLY:
                break;
            default:
                size = result;
                break;
        }
        return size;
    }

    private int setMeasureWidth(int spec) {
        int mode = MeasureSpec.getMode(spec);
        int size = MeasureSpec.getSize(spec);
        int result = Integer.MAX_VALUE;
        switch (mode) {
            case MeasureSpec.AT_MOST:
                size = Math.min(result, size);
                break;
            case MeasureSpec.EXACTLY:
                break;
            default:
                size = result;
                break;
        }
        return size;
    }

    private Path path = new Path();
    private StringBuilder builder = new StringBuilder();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (dataList.isEmpty()) return;

        //画文字温度
        float temp1 = Math.max(mDateTextPaint.measureText(maxTempText) + 5, mDateTextPaint.measureText(minTempText) + 5);
        canvas.drawText(maxTempText, 0, 30, mDateTextPaint);
        canvas.drawText(minTempText, 0, mHight - 50, mDateTextPaint);
        canvas.translate(temp1, 0);

        float mWidth = mWidth1 - temp1;

        canvas.save();
        for (int i = 0; i < dataList.size(); i++) {
            canvas.drawLine(0, 0, 0, mHight - 45, mLinePaint);

            String date = DataUtil.INSTANCE.timeStamp2Date(DataUtil.INSTANCE.date2Long(dataList.get(i).getFct(), "yyyy-MM-dd"), "MM.dd");
            if (i == 0) {
                canvas.drawText(date, 5, mHight - 5, mDateTextPaint);
            } else if (i == 12) {
                canvas.drawText(date, -mDateTextPaint.measureText(date) / 2, mHight - 5, mDateTextPaint);
            } else if (i == 25) {
                canvas.drawText(date, -mDateTextPaint.measureText(date) / 2, mHight - 5, mDateTextPaint);
            } else if (i == 39) {
                canvas.drawText(date, -mDateTextPaint.measureText(date), mHight - 5, mDateTextPaint);
            }
            canvas.translate((float) mWidth / (dataList.size() - 1), 0);
        }
        canvas.restore();

        path.reset();
        for (int i = 0; i < dataList.size(); i++) {
            float y = (maxTemp - dataList.get(i).getTcd()) * tempDiff == 0 ? 5 : (maxTemp - dataList.get(i).getTcd()) * tempDiff;
            if (i == 0) {
                path.moveTo(0, y);
            } else {
                path.lineTo(mWidth / (dataList.size() - 1) * i, y);
            }
        }
        canvas.drawPath(path, mCurvePaint);
        path.lineTo(mWidth, mHight - 45);
        path.lineTo(0, mHight - 45);
        canvas.drawPath(path, mShadowPaint);

        if (position >= mWidth) {
            position = mWidth;
        }

        //绘制指示线
        canvas.drawLine(position, 0, position, mHight - 45, mRulerPaint);
        //绘制文字
        int index = (int) (position / (mWidth / 40));
        if (index >= 40) index = 39;
        if (index < 0) index = 0;
        builder.setLength(0);
        builder.append(DataUtil.INSTANCE.timeStamp2Date(DataUtil.INSTANCE.date2Long(dataList.get(index).getFct(), "yyyy-MM-dd"), "MM月dd日"));
        builder.append(" ");
        builder.append(dataList.get(index).getTcd());
        builder.append("°");
        String text = builder.toString();
        float txtWidth = mTextPaint.measureText(text);
        if (position < txtWidth / 2 + 20) {
            canvas.drawRoundRect(0, 0, txtWidth + 40, 50, txtWidth, txtWidth, mTextBgPaint);
            canvas.drawText(text, 20, 32, mTextPaint);
        } else if (mWidth - position <= txtWidth / 2 + 20) {
            canvas.drawRoundRect(mWidth - txtWidth - 40, 0, mWidth, 50, txtWidth, txtWidth, mTextBgPaint);
            canvas.drawText(text, mWidth - txtWidth - 20, 32, mTextPaint);
        } else {
            canvas.drawRoundRect(position - txtWidth / 2 - 20, 0, position + txtWidth / 2 + 20, 50, txtWidth, txtWidth, mTextBgPaint);
            canvas.drawText(text, position - txtWidth / 2, 32, mTextPaint);
        }
    }

    private boolean isMove = false;

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                if (x <= 0) {
                    setPosition(0);
                } else if (x > mWidth1) {
                    setPosition(mWidth1);
                } else {
                    setPosition((int) x);
                }
                isMove = true;
            case MotionEvent.ACTION_CANCEL:
                if (isMove) {
                    AppExtKt.clickReport(EnumType.上报埋点.INSTANCE.get温度趋势左右滑动(), "null", "null", XMActivityBean.ENTRY_TYPE_ENTRY);
                    isMove = false;
                }
                break;
            default:
        }
        return true;
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        //解决刻度尺和viewPager的滑动冲突
        //当滑动刻度尺时，告知父控件不要拦截事件，交给子view处理
        getParent().requestDisallowInterceptTouchEvent(true);
        return super.dispatchTouchEvent(ev);
    }

    public void setPosition(int i) {
        position = i;
        invalidate();
    }


    public void setData(ArrayList<FortyWeatherBean.YbdsBean> dataList, int maxTemp, int minTemp) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.tempDiff = (float) (mHight - 45) / (maxTemp - minTemp);
        maxTempText = maxTemp + "°";
        minTempText = minTemp + "°";
        invalidate();
    }
}
