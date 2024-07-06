package com.zhangsheng.shunxin.weather.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Typeface;
import android.text.TextUtils;
import android.util.AttributeSet;

import androidx.appcompat.widget.AppCompatTextView;

import com.zhangsheng.shunxin.R;

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2020/4/23 21:05
 */
public class DinProTextView extends AppCompatTextView {



    public DinProTextView(Context context) {
        super(context,null);
    }

    public DinProTextView(Context context, AttributeSet attrs) {
        super(context, attrs,android.R.attr.textViewStyle);
        this.init(context,attrs);
    }

    public DinProTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    private void init(Context context,AttributeSet attrs) {
        TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.LunarTextView);
        String path="";
        try {
            path=ta.getString(R.styleable.LunarTextView_fontsPath);
            if (!TextUtils.isEmpty(path)&&!this.isInEditMode()){
                Typeface tf = Typeface.createFromAsset(this.getContext().getAssets(), path);
                this.setTypeface(tf);
            }
        }catch (Exception e){

        }
        ta.recycle();
    }
}
