package com.zhangsheng.shunxin.calendar.wegdit;

import android.content.Context;
import android.graphics.Typeface;

import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2020/4/23 21:05
 */
public class SolarTermsTextView extends AppCompatTextView {

    public SolarTermsTextView(Context context) {
        super(context);
        this.init();
    }

    public SolarTermsTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
        this.init();
    }

    public SolarTermsTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        this.init();
    }

    private void init() {
        if (!this.isInEditMode()) {
            Typeface tf = Typeface.createFromAsset(this.getContext().getAssets(), "fonts/lingdong.ttf");
            this.setTypeface(tf);
        }

    }
}
