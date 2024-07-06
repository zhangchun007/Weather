package com.zhangsheng.shunxin.information.refresh;

import android.content.Context;

import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.zhangsheng.shunxin.R;
import com.scwang.smart.refresh.layout.api.RefreshHeader;
import com.scwang.smart.refresh.layout.api.RefreshKernel;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.constant.RefreshState;
import com.scwang.smart.refresh.layout.constant.SpinnerStyle;

/**
 * @Author:liupengbing
 * @Data: 2020/5/23 4:31 PM
 * @Email:aliupengbing@163.com
 */
public class InfoRefreshHeader extends LinearLayout implements RefreshHeader {
    private View mRefrushSmartHeadView;
    private View mRefrush_smart_head_pg;
    private ImageView srl_classics_icon;
    private TextView mRefrush_smart_head_tv;
    private Animation rotateAnimation;

    public InfoRefreshHeader(Context context) {
        super(context);
        initRefrushView(context);
    }

    public InfoRefreshHeader(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        initRefrushView(context);
    }

    public InfoRefreshHeader(Context context, @Nullable  AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initRefrushView(context);
    }

    /**
     * 打气一个view,并进行初始化
     *
     * @param context
     */
    private void initRefrushView(Context context) {
        //  设置居中
        setGravity(Gravity.CENTER);
        mRefrushSmartHeadView = LayoutInflater.from(context).inflate(R.layout.refresh_header_for_info, null);
        addView(mRefrushSmartHeadView);
        srl_classics_icon = mRefrushSmartHeadView.findViewById(R.id.srl_classics_icon);
        mRefrush_smart_head_tv = mRefrushSmartHeadView.findViewById(R.id.srl_classics_title);

        //先设置图片的开始动画   设置的时候src直接设置的 animation
        srl_classics_icon.getDrawable();
         rotateAnimation = AnimationUtils.loadAnimation(context, R.anim.rotate_anim);
        LinearInterpolator lin = new LinearInterpolator();
        rotateAnimation.setInterpolator(lin);

    }

//  下面的都是RefreshHeader继承的方法  start

    /**
     * 获取真实视图（必须返回，不能为null）  当前的head就是一个view
     *
     * @return
     */
    @NonNull
    @Override
    public View getView() {
        return this;
    }

    /**
     * 获取变换方式（必须指定一个：平移、拉伸、固定、全屏）  我们类似于经典刷新  选择平移
     *
     * @return
     */
    @NonNull
    @Override
    public SpinnerStyle getSpinnerStyle() {
        return SpinnerStyle.Translate;//指定为平移，不能null;
    }

    /**
     * 设置主题颜色 （如果自定义的Header没有注意颜色，本方法可以什么都不处理）
     *
     * @param colors 对应Xml中配置的 srlPrimaryColor srlAccentColor
     */
    @Override
    public void setPrimaryColors(int... colors) {

    }

    /**
     * 尺寸定义初始化完成 （如果高度不改变（代码修改：setHeader），只调用一次, 在RefreshLayout#onMeasure中调用）
     *
     * @param kernel        RefreshKernel 核心接口（用于完成高级Header功能）
     * @param height        HeaderHeight or FooterHeight
     * @param maxDragHeight 最大拖动高度
     */
    @Override
    public void onInitialized(@NonNull RefreshKernel kernel, int height, int maxDragHeight) {
    }

    /**
     * 手指拖动下拉（会连续多次调用，用于实时控制动画关键帧）
     *
     * @param percent       下拉的百分比 值 = offset/headerHeight (0 - percent - (headerHeight+maxDragHeight) / headerHeight )
     * @param offset        下拉的像素偏移量  0 - offset - (headerHeight+maxDragHeight)
     * @param height        Header的高度
     * @param maxDragHeight 最大拖动高度
     */
    @Override
    public void onMoving(boolean isDragging, float percent, int offset, int height, int maxDragHeight) {

    }

    @Override
    public void onReleased(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {

    }

    /**
     * 开始动画（开始刷新或者开始加载动画）
     *
     * @param refreshLayout RefreshLayout
     * @param height        HeaderHeight or FooterHeight
     * @param maxDragHeight 最大拖动高度
     */
    @Override
    public void onStartAnimator(@NonNull RefreshLayout refreshLayout, int height, int maxDragHeight) {
        if(rotateAnimation!=null) {
            srl_classics_icon.startAnimation(rotateAnimation);
        }
    }

    /**
     * 动画结束
     *
     * @param refreshLayout RefreshLayout
     * @param success       数据是否成功刷新或加载
     * @return 完成动画所需时间 如果返回 Integer.MAX_VALUE 将取消本次完成事件，继续保持原有状态
     */
    @Override
    public int onFinish(@NonNull RefreshLayout refreshLayout, boolean success) {
        // 图片动画定义
        srl_classics_icon.clearAnimation();
        srl_classics_icon.setVisibility(GONE);
        if (success) {
            mRefrush_smart_head_tv.setText("");
        } else {
//            mRefrush_smart_head_tv.setText("刷新失败");
        }

        return 0;
    }

    @Override
    public void onHorizontalDrag(float percentX, int offsetX, int offsetMax) {

    }

    @Override
    public boolean isSupportHorizontalDrag() {
        return false;
    }

    /**
     * 不同的状态控制内部控件的显示和旋转
     *
     * @param refreshLayout
     * @param oldState
     * @param newState
     */
    @Override
    public void onStateChanged(@NonNull RefreshLayout refreshLayout, @NonNull RefreshState oldState,
                               @NonNull RefreshState newState) {
        switch (newState){
            case None:
            case ReleaseToRefresh:  //下拉将要释放开始刷新
//                mRefrush_smart_head_tv.setText("松开立即刷新");
                break;
            case PullDownToRefresh: // 开始下拉
                srl_classics_icon.setVisibility(VISIBLE);
                mRefrush_smart_head_tv.setText("松开立即刷新");
                break;
            case Refreshing: //  正在刷新
//                mRefrush_smart_head_tv.setText("松开立即刷新");

                break;

        }
    }


}
