package com.zhangsheng.shunxin.weather.widget;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

import androidx.annotation.RequiresApi;

import com.zhangsheng.shunxin.R;
import com.zhangsheng.shunxin.weather.net.bean.FortyWeatherBean;
import com.zhangsheng.shunxin.weather.utils.DataUtil;

import java.util.ArrayList;

public class RulerView extends View {

    private Paint mLinePaint;//刻度线画笔
    private Paint mRoundPaint;//指示图片画笔
    private Paint mRulerPaint;//指示线画笔
    private Paint mTextPaint;//指示数字画笔
    private Paint mDateTextPaint;//日期画笔
    private Paint mTextBgPaint;//文体背景画笔

    private float position = 0;
    private int mWidth = 0;
    private int mHight = 0;

    private ArrayList<FortyWeatherBean.FallsBean> dataList = new ArrayList();

    private Bitmap pic = BitmapFactory.decodeResource(getResources(), R.mipmap.im_vector);
    private Bitmap pic2 = BitmapFactory.decodeResource(getResources(), R.mipmap.im_ellipse);

    public RulerView(Context context) {
        super(context);
        init();
    }

    public RulerView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public RulerView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mLinePaint = new Paint();
        mLinePaint.setColor(Color.parseColor("#F3F1F4"));
        mLinePaint.setAntiAlias(true);
        mLinePaint.setStyle(Paint.Style.STROKE);
        mLinePaint.setStrokeWidth(1);

        mTextBgPaint = new Paint();
        mTextBgPaint.setColor(Color.parseColor("#3797FF"));
        mTextBgPaint.setAntiAlias(true);
        mTextBgPaint.setStyle(Paint.Style.FILL);
        mTextBgPaint.setStrokeWidth(10);

        mRoundPaint = new Paint();
        mRoundPaint.setAntiAlias(true);
        mRoundPaint.setStyle(Paint.Style.FILL);

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
        mWidth = w;
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

    private StringBuilder builder = new StringBuilder();

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (dataList.isEmpty()) return;

        canvas.save();
        for (int i = 0; i < dataList.size(); i++) {
            canvas.drawLine(pic.getWidth() / 2, 0, pic.getWidth() / 2, mHight - 45, mLinePaint);
            if ("1".equals(dataList.get(i).getRss())) {
                canvas.drawBitmap(pic, 0, (mHight - 45) / 2 - pic.getHeight() / 2, mRoundPaint);
            } else {
                canvas.drawBitmap(pic2, 0, (mHight - 45) / 2 - pic2.getHeight() / 2, mRoundPaint);
            }

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

            canvas.translate((float) mWidth / 40, 0);
        }
        canvas.restore();

        //绘制指示线
        canvas.drawLine(position, 0, position, mHight - 45, mRulerPaint);
        //绘制文字
        int index = (int) (position / (mWidth / 40));
        if (index >= 40) index = 39;
        if (index < 0) index = 0;
        builder.setLength(0);
        builder.append(DataUtil.INSTANCE.timeStamp2Date(DataUtil.INSTANCE.date2Long(dataList.get(index).getFct(), "yyyy-MM-dd"), "MM月dd日"));
        builder.append(" ");
        builder.append(dataList.get(index).getWt());
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

    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                float x = event.getX();
                if (x <= pic.getWidth() / 2) {
                    setPosition(pic.getWidth() / 2);
                } else if (x > mWidth) {
                    setPosition(mWidth);
                } else {
                    setPosition((int) x);
                }
            case MotionEvent.ACTION_CANCEL:
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

    public void setData(ArrayList<FortyWeatherBean.FallsBean> dataList) {
        this.dataList.clear();
        this.dataList.addAll(dataList);
        invalidate();
    }
}
