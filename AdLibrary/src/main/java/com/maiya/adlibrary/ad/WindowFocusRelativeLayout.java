package com.maiya.adlibrary.ad;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

public class WindowFocusRelativeLayout extends RelativeLayout {
    private WindowFocusChangeListener listener;

    public WindowFocusRelativeLayout(Context context) {
        super(context);
    }

    public WindowFocusRelativeLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public WindowFocusRelativeLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    public void setWindowFocusChangeListener(WindowFocusChangeListener listener) {
        this.listener = listener;
    }


    @Override
    public void onWindowFocusChanged(boolean hasWindowFocus) {
        super.onWindowFocusChanged(hasWindowFocus);
        if(listener != null) {
            listener.onWindowFocusChanged(hasWindowFocus);
        }
    }

    public interface WindowFocusChangeListener {
        void onWindowFocusChanged(boolean hasWindowFocus);
    }
}
