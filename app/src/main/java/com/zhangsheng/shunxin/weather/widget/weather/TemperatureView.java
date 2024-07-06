package com.zhangsheng.shunxin.weather.widget.weather;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.util.AttributeSet;
import android.view.View;

import com.maiya.thirdlibrary.utils.DisplayUtil;
import com.zhangsheng.shunxin.R;


public class TemperatureView extends View {

    private int maxTemp = 1;
    private int minTemp = 0;


    private int temperatureDay = 0;
    private int nextTemp = 0;
    private int preTemp = 0;

    private Paint pointPaint;
    private Paint linePaint;
    private Paint shadowPaint;
    private boolean isCircle = false;
    private Bitmap pic = BitmapFactory.decodeResource(getResources(), R.mipmap.icon_char_point);


    private int radius = 8;
    private int shadow = 20;
    private int padding = 5;

    private int pointY;

    public TemperatureView(Context context) {
        this(context, null);
    }

    public TemperatureView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    public TemperatureView(Context context, AttributeSet attrs, int defStyleAttr) {
        this(context, attrs);
    }

    private void init(Context context, AttributeSet attrs) {

        radius = DisplayUtil.INSTANCE.dip2px(4);
        shadow = DisplayUtil.INSTANCE.dip2px(10);
        initPaint(context, attrs);
    }

    private void initPaint(Context context, AttributeSet attrs) {
        pointPaint = new Paint();
        linePaint = new Paint();
        shadowPaint = new Paint();
        shadowPaint.setStrokeWidth(shadow);
        shadowPaint.setAntiAlias(true);
        shadowPaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(4);
        linePaint.setAntiAlias(true);
        linePaint.setStyle(Paint.Style.STROKE);
        pointPaint.setColor(Color.parseColor("#FFFF8000"));
        pointPaint.setStyle(Paint.Style.FILL);
        pointPaint.setAntiAlias(true);
    }

    public void setLineWidth(float width){
        linePaint.setStrokeWidth(width);
    }

    public void drawChar(int lineColor, int maxTemp, int minTemp, int preTemp, int curTemp, int nextTemp, boolean isCircle) {
        this.preTemp = preTemp;
        this.maxTemp = maxTemp;
        this.minTemp = minTemp;
        this.isCircle = isCircle;
        temperatureDay = curTemp;
        this.nextTemp = nextTemp;
        pointPaint.setColor(lineColor);
        linePaint.setColor(lineColor);
        linePaint.setPathEffect(null);
        shadowPaint.setColor(lineColor);
        shadowPaint.setAlpha(10);
        linePaint.setStrokeWidth(4);
        invalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        drawLine(canvas);
        drawPoint(canvas);

    }

    private void drawLine(Canvas canvas) {
        pointY = getTempY(temperatureDay);

        if (preTemp != 888) {
            int x = 0;
            int y = (getTempY(preTemp) + pointY) / 2;
            int endx = getWidth() / 2;
            int endY = pointY;
            if (temperatureDay != preTemp) {
                Path path = new Path();
                path.moveTo(x, y);
                path.quadTo((endx - x) / 2, endY, endx, endY);
                canvas.drawPath(path, linePaint);
                y = y + 8;
                endY = endY + 8;
                path.moveTo(x, y);
                path.quadTo((endx - x) / 2, endY, endx, endY);
                canvas.drawPath(path, shadowPaint);
            } else {
                canvas.drawLine(x, y, endx, endY, linePaint);
                y = y + 8;
                endY = endY + 8;
                canvas.drawLine(x, y, endx, endY, shadowPaint);
            }
        }

        if (nextTemp != 888) {
            int x = getWidth() / 2;
            int y = pointY;
            int endx = getWidth();
            int endy = (getTempY(nextTemp) + pointY) / 2;
            if (temperatureDay != nextTemp) {
                Path path2 = new Path();
                path2.moveTo(x, y);
                path2.quadTo((endx - x / 2), y, endx, endy);
                canvas.drawPath(path2, linePaint);
                y += 8;
                endy += 8;
                path2.moveTo(x, y);
                path2.quadTo((endx - x / 2), y, endx, endy);
                canvas.drawPath(path2, shadowPaint);
            } else {
                canvas.drawLine(x, y, endx, endy, linePaint);
                y += 8;
                endy += 8;
                canvas.drawLine(x, y, endx, endy, shadowPaint);
            }
        }


    }


    private void drawPoint(Canvas canvas) {
        pointY = getTempY(temperatureDay);
        int x = getWidth() / 2;
        if (isCircle) {
            canvas.drawCircle(x, pointY, radius, pointPaint);
        } else {
            canvas.drawBitmap(pic, x, pointY - pic.getHeight() / 2, pointPaint);
        }

    }


    private int getTempY(int temp) {
        int y = 0;
        try {
            y = (getHeight() - radius - (getHeight() - shadow - radius) * (temp - minTemp) / (maxTemp - minTemp)) - padding;
        } catch (Exception e) {
            y = 0;
        }
        return y;
    }


}

