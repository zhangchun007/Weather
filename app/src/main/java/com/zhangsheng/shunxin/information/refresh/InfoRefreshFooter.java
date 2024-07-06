package com.zhangsheng.shunxin.information.refresh;

import android.content.Context;

import android.util.AttributeSet;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zhangsheng.shunxin.R;
import com.scwang.smart.refresh.layout.api.RefreshFooter;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;

/**
 * @Author:liupengbing
 * @Data: 2020/5/27 2:10 PM
 * @Email:aliupengbing@163.com
 */
public class InfoRefreshFooter extends LinearLayout implements RefreshFooter {

    private final TextView tv_title;
    private final ProgressBar progress;

    public InfoRefreshFooter(Context context) {
        this(context, null, 0);
    }

    public InfoRefreshFooter(Context context, @Nullable AttributeSet attrs) {
        this(context, null, 0);
    }

    public InfoRefreshFooter(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        View view = View.inflate(context, R.layout.refresh_footer_info, this);
        tv_title = view.findViewById(R.id.tv_title);
        progress = view.findViewById(R.id.progress);


    }


    @Override
    public boolean setNoMoreData(boolean noMoreData) {
        return false;
    }

    @NonNull
    @Override
    public View getView() {
        return this;
    }

    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;
    }

    @Override
    public void setPrimaryColors(int... colors) {

    }

    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
        //控制是否稍微上滑动就刷新
        kernel.getRefreshLayout().setEnableAutoLoadMore(false);
    }

    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
    }

    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {

        progress.setVisibility(GONE);
        tv_title.setText("加载完成");
        return 0;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState, @NonNull RefreshState newState) {
        switch (newState) {
            case None:
            case PullUpToLoad:
                progress.setVisibility(VISIBLE);
                tv_title.setText("正在加载...");
                break;
            case Loading:

            case LoadReleased:

                break;
            case ReleaseToLoad:
                break;
        }
    }
}
