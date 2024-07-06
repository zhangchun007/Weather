package com.zhangsheng.shunxin.weather.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.SweepGradient;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.View;

import androidx.annotation.Nullable;

public class DashBoardView extends View {

    private int mStartAngle = 135; // 起始角度
    private int mSweepAngle = 270; // 绘制角度
    private int MaxValues = 500; // 最大值

    private float mCenterX, mCenterY; // 圆心坐标

    private float progressSweepAngle;//进度条圆弧扫过的角度
    //    private float progressNum;//空气质量的数字
    private int animatedValue; // 中间数字跳动的内容
    private Shader mShader;

    private Paint mPaint, textPaint, insidPaint;
    private Matrix matrix;

    private float wRadius; // 内部白色圆的半径
    private float insideWide; // 内圆弧的宽度
    private float outWide; // 外圆弧的宽度
    private float outMoveWide; // 移动的外圆弧的宽度

//    private int textColor = Color.parseColor("#0FD692");

    private RectF insideRectF;
    private RectF outRectF;
    private RectF outMoveRectF;

    //    private String text = "优";

    private int[] color_list = new int[]{
            Color.parseColor("#00000000"),
            Color.parseColor("#00000000")};

    public DashBoardView(Context context) {
        this(context, null);
    }

    public DashBoardView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public DashBoardView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        mPaint = new Paint();
        insidPaint = new Paint();

        textPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        textPaint.setStyle(Paint.Style.FILL);
        textPaint.setTextAlign(Paint.Align.CENTER);

        matrix = new Matrix();
        matrix.setRotate(mStartAngle - 5, mCenterX, mCenterY);
        float[] position = new float[2];
        position[0] = 0.0f;
        position[1] = 0.5f;
        mShader = new SweepGradient(mCenterX, mCenterY, color_list, position);
        mShader.setLocalMatrix(matrix);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);

        int height = measureSize(dp2px(600), heightMeasureSpec);
        int width = measureSize(dp2px(3000), widthMeasureSpec);
        int min = Math.min(width, height);// 获取View最短边的长度
        setMeasuredDimension(min, min);// 强制改View为以最短边为长度的正方形

        mCenterX = getMeasuredWidth() / 2f;
        mCenterY = getMeasuredHeight() / 2f;

        outMoveWide = dp2px(6);
        outWide = dp2px(16);
        insideWide = dp2px(10);

        wRadius = mCenterX - outWide - outMoveWide;

        init(mCenterX, outWide, insideWide);
    }

    private void init(float centerX, float outWide, float insideWide) {

        outMoveRectF = new RectF(outMoveWide, outMoveWide,
                2 * centerX - outMoveWide, 2 * centerX - outMoveWide);

        outRectF = new RectF(outWide / 2 + outMoveWide, outWide / 2 + outMoveWide,
                2 * centerX - outWide / 2 - outMoveWide, 2 * centerX - outWide / 2 - outMoveWide);

        insideRectF = new RectF(outMoveWide + outWide + insideWide / 2 , outMoveWide + outWide + insideWide / 2 ,
                2 * centerX - outWide - insideWide / 2 - outMoveWide , 2 * centerX - outWide - insideWide / 2 - outMoveWide );
    }

    private int measureSize(int defaultSize, int measureSpec) {
        int result = defaultSize;
        int specMode = View.MeasureSpec.getMode(measureSpec);
        int specSize = View.MeasureSpec.getSize(measureSpec);

        if (specMode == View.MeasureSpec.EXACTLY) {
            result = specSize;
        } else if (specMode == View.MeasureSpec.AT_MOST) {
            result = Math.min(result, specSize);
        }
        return result;
    }


    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        /*
         * 画白色圆形
         * */
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setColor(Color.WHITE);
        canvas.drawCircle(mCenterX, mCenterY, wRadius, mPaint);

        /*
         * 画圆外的圆弧
         * */
        mPaint.reset();
        mPaint.setAntiAlias(true);
//        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(outWide);
        mPaint.setColor(Color.parseColor("#66FFFFFF"));
        canvas.drawArc(outRectF, mStartAngle, mSweepAngle, false, mPaint);

        /*
         * 画圆里的圆弧
         * */
        insidPaint.reset();
        insidPaint.setAntiAlias(true);
        insidPaint.setStrokeCap(Paint.Cap.ROUND);
        insidPaint.setStyle(Paint.Style.STROKE);
        insidPaint.setStrokeWidth(insideWide);
        insidPaint.setShader(mShader);
        canvas.drawArc(insideRectF, mStartAngle + 3, progressSweepAngle - 3, false, insidPaint);



        /*
         * 画圆外的 运动圆弧
         * */
        mPaint.reset();
        mPaint.setAntiAlias(true);
        mPaint.setStrokeCap(Paint.Cap.ROUND);
        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setStrokeWidth(outMoveWide);
        mPaint.setColor(Color.parseColor("#ffffff"));
        canvas.drawArc(outMoveRectF, mStartAngle, progressSweepAngle, false, mPaint);
    }


    private int dp2px(int dp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp,
                Resources.getSystem().getDisplayMetrics());
    }

    private int sp2px(int sp) {
        return (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, sp,
                Resources.getSystem().getDisplayMetrics());
    }

    public void setProgressNum(float progressNum) {
        matrix = new Matrix();
        matrix.setRotate(mStartAngle - 5, mCenterX, mCenterY);
        float[] position = new float[2];
        position[0] = 0.0f;
        position[1] = progressNum / 500 * 1f;
        mShader = new SweepGradient(mCenterX, mCenterY, color_list, position);
        mShader.setLocalMatrix(matrix);
    }

    public void starAnimator(int progressNum) {
        animatedValue = progressNum;
        progressSweepAngle = animatedValue * mSweepAngle / MaxValues;//这里计算进度条的比例
        postInvalidate();
    }


    public void setTextColors(int[] list) {
        color_list = list;
    }


//    private float[] getCoordinatePoint(float radius, float angle) {
//        double arcAngle = Math.toRadians(angle); //将角度转换为弧度
//        if (angle < 90) {
//            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
//            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
//        } else if (angle == 90) {
//            point[0] = mCenterX;
//            point[1] = mCenterY + radius;
//        } else if (angle > 90 && angle < 180) {
//            arcAngle = Math.PI * (180 - angle) / 180.0;
//            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
//            point[1] = (float) (mCenterY + Math.sin(arcAngle) * radius);
//        } else if (angle == 180) {
//            point[0] = mCenterX - radius;
//            point[1] = mCenterY;
//        } else if (angle > 180 && angle < 270) {
//            arcAngle = Math.PI * (angle - 180) / 180.0;
//            point[0] = (float) (mCenterX - Math.cos(arcAngle) * radius);
//            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
//        } else if (angle == 270) {
//            point[0] = mCenterX;
//            point[1] = mCenterY - radius;
//        } else {
//            arcAngle = Math.PI * (360 - angle) / 180.0;
//            point[0] = (float) (mCenterX + Math.cos(arcAngle) * radius);
//            point[1] = (float) (mCenterY - Math.sin(arcAngle) * radius);
//        }
//
//        return point;
//    }
//
//    /**
//     * 相对起始角度计算信用分所对应的角度大小
//     */
//    private float calculateRelativeAngleWithValue(int value) {
//        return mSweepAngle * value * 1f / 500;
//    }
}
