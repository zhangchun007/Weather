package com.zhangsheng.shunxin.weather.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.util.Log;

import androidx.appcompat.widget.AppCompatTextView;

import com.zhangsheng.shunxin.weather.utils.FontUtils;


/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2020/4/23 21:05
 */
public class GOTHICNumberTextView extends AppCompatTextView {

    public GOTHICNumberTextView(Context context) {
        super(context);
        this.init();
    }

    public GOTHICNumberTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public GOTHICNumberTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init() {
        try {
            if (!this.isInEditMode()) {
                this.setTypeface(FontUtils.getInstance().getGOTHICFont(getContext()));
            }
        }catch (Exception e){
            Log.e("TAG", "init: "+e.getMessage() );
        }


    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }
}
