package com.zhangsheng.shunxin.information.widget;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.GridView;

/**
 * @Author:liupengbing
 * @Data: 2020/5/22 11:03 AM
 * @Email:aliupengbing@163.com
 */
public class MyGridView extends GridView {
    public MyGridView(Context paramContext, AttributeSet paramAttributeSet) {
        super(paramContext, paramAttributeSet);
    }

    @Override
    public void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int expandSpec = MeasureSpec.makeMeasureSpec(Integer.MAX_VALUE >> 2,
                MeasureSpec.AT_MOST);
        super.onMeasure(widthMeasureSpec, expandSpec);
    }
}
