package com.zhangsheng.shunxin.information.InfoDetails;

import android.content.Context;

import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.widget.NestedScrollView;

/**
 * @Author:liupengbing
 * @Data: 2020/5/23 5:31 PM
 * @Email:aliupengbing@163.com
 */
public class InfoScrollView extends NestedScrollView {
    private boolean mIsWebViewOnBottom;
    private boolean mIsOthersLayoutShow;
    private float mDownY;
    private boolean isVideo=false;

    public InfoScrollView(@NonNull Context context) {
        super(context);
    }

    public InfoScrollView(@NonNull Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
    }

    public InfoScrollView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }
    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        float y = ev.getY();
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mDownY = y;
                break;
            case MotionEvent.ACTION_MOVE:
                float dy = y - mDownY;
                if (dy < 0) { // 手指向上滑

                    if(!isVideo) {
                        if (!mIsWebViewOnBottom) {

                            return false; // 网页未到底，不拦截事件

                        }
                    }


                } else { // 手指向下滑
                    if (!mIsOthersLayoutShow)
                        return false; // 底部原生layout完全隐藏，不拦截事件
                }
                break;
        }
        return super.onInterceptTouchEvent(ev);
    }
    public void setIsWebViewOnBottom(boolean onBottom) {
        this.mIsWebViewOnBottom = onBottom;
    }
    public void setIsOthersLayoutShow(boolean isOthersLayoutShow) {
        this.mIsOthersLayoutShow = isOthersLayoutShow;
    }
    public void isVideo(boolean isVideo) {
        this.isVideo = isVideo;
    }

}
