package com.zhangsheng.shunxin.weather.widget;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.ViewSwitcher;


import com.zhangsheng.shunxin.R;
import com.zhangsheng.shunxin.information.utils.DensityUtil;

import java.util.ArrayList;

public class VerticalTextview extends TextSwitcher implements ViewSwitcher.ViewFactory {

    private static final int FLAG_START_AUTO_SCROLL = 0;
    private static final int FLAG_STOP_AUTO_SCROLL = 1;

    private static final int STATE_PAUSE = 2;
    private static final int STATE_SCROLL = 3;
    private long lastTime = 0L;
    private float mTextSize = 14;
    private int mdrawablePadding = DensityUtil.dip2px(getContext(), 5);
    private int mPaddingtb = DensityUtil.dip2px(getContext(), 6);
    private int mPaddingr = DensityUtil.dip2px(getContext(), 9);
    private int drawableSize = DensityUtil.dip2px(getContext(), 20);
    private int textColor = Color.WHITE;
    private LayoutParams lp = new LayoutParams(LayoutParams.MATCH_PARENT, LayoutParams.MATCH_PARENT);
    private int mScrollState = STATE_PAUSE;

    /**
     * @param textSize  textsize
     * @param textColor textcolor
     */
    public void setText(float textSize, int textColor) {
        mTextSize = textSize;
        this.textColor = textColor;
    }

    private OnItemClickListener itemClickListener;
    private Context mContext;
    private int currentId = -1;
    private ArrayList<String> textList;
    private ArrayList<String> tagList;
    private Handler handler;

    public VerticalTextview(Context context) {
        this(context, null);
        mContext = context;
    }

    public VerticalTextview(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        textList = new ArrayList<String>();
    }

    public void setAnimTime() {
        removeAllViews();
        setFactory(this);
        setInAnimation(getContext(), R.anim.slide_in_bottom);
        setOutAnimation(getContext(), R.anim.slide_out_top);
    }

    /**
     * set time.
     *
     * @param time
     */
    public void setTextStillTime(final long time) {
        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                switch (msg.what) {
                    case FLAG_START_AUTO_SCROLL:
                        if (textList.size() > 0) {
                            currentId++;
                            setText(textList.get(currentId % textList.size()));
                            setDrawable(((TextView) getCurrentView()));
                        }
                        handler.removeMessages(FLAG_START_AUTO_SCROLL);
                        handler.sendEmptyMessageDelayed(FLAG_START_AUTO_SCROLL, time);
                        break;
                    case FLAG_STOP_AUTO_SCROLL:
                        handler.removeMessages(FLAG_START_AUTO_SCROLL);
                        break;
                }
            }
        };
    }

    /**
     * set Data list.
     *
     * @param titles
     */
    public void setTextList(ArrayList<String> titles) {
        textList.clear();
        textList.addAll(titles);
        currentId = -1;
    }

    public void setTagList(ArrayList<String> tagList) {
        this.tagList = tagList;
    }

    /**
     * start auto scroll
     */
    public void startAutoScroll() {
        if (Math.abs(lastTime - System.currentTimeMillis()) > 500) {
            if (textList.size() > 1 && handler != null) {
                mScrollState = STATE_SCROLL;
                handler.removeMessages(FLAG_START_AUTO_SCROLL);
                handler.sendEmptyMessage(FLAG_START_AUTO_SCROLL);
            }
            lastTime = System.currentTimeMillis();
        }
    }

    /**
     * stop auto scroll
     */
    public void stopAutoScroll() {
        if (handler != null) {
            mScrollState = STATE_PAUSE;
            handler.sendEmptyMessage(FLAG_STOP_AUTO_SCROLL);
        }
    }

    @Override
    public View makeView() {
        TextView t = new TextView(mContext);
        if (!textList.isEmpty()) {
            t.setGravity(Gravity.CENTER);
            t.setLayoutParams(lp);
            t.setMaxLines(1);
//            t.setPadding(mdrawablePadding, mPaddingtb, mPaddingr, mPaddingtb);
            t.setPadding(mPaddingr, 0, mPaddingr, 0);
            t.setTextColor(textColor);
            t.setIncludeFontPadding(false);
            t.setTextSize(TypedValue.COMPLEX_UNIT_DIP, mTextSize);
            t.setClickable(true);
            if (currentId < 0 || currentId > textList.size()) {
                t.setText(textList.get(0));
                t.setTag(tagList.get(0));
                currentId = 0;
            } else {
                t.setText(textList.get(currentId));
                t.setTag(tagList.get(currentId));
            }
            setDrawable(t);
            t.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (isClickable) {
                        isClickable = false;
                        postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                isClickable = true;
                            }
                        }, 500);
                        if (itemClickListener != null && textList.size() > 0 && currentId != -1) {
                            itemClickListener.onItemClick(currentId % textList.size());
                        }
                    }

                }
            });
        }
        return t;
    }

    boolean isClickable = true;

    private void setDrawable(TextView t) {
        try {
            if (t != null) {
                Drawable weatherWarnLeft = null;
                String content = t.getTag().toString();
                if (content.contains("蓝")) {
                    weatherWarnLeft = getContext().getResources().getDrawable(R.drawable.waringbgbule);
                } else if (content.contains("黄")) {
                    weatherWarnLeft = getContext().getResources().getDrawable(R.drawable.waringbgyellow);
                } else if (content.contains("橙")) {
                    weatherWarnLeft = getContext().getResources().getDrawable(R.drawable.waringbgcheng);
                } else if (content.contains("红")) {
                    weatherWarnLeft = getContext().getResources().getDrawable(R.drawable.waringbgred);
                }
//                t.setCompoundDrawablePadding(mdrawablePadding);
//                if (weatherWarnLeft != null) {
//                    weatherWarnLeft.setBounds(1, 1, drawableSize, drawableSize);
//                    t.setCompoundDrawables(weatherWarnLeft, null, null, null);
//                }
                if (weatherWarnLeft != null) {
                    t.setBackground(weatherWarnLeft);
                }
            }
        } catch (Resources.NotFoundException e) {
            e.printStackTrace();
        }
    }

    /**
     * set onclick listener
     *
     * @param itemClickListener listener
     */
    public void setOnItemClickListener(OnItemClickListener itemClickListener) {
        this.itemClickListener = itemClickListener;
    }

    /**
     * item click listener
     */
    public interface OnItemClickListener {
        /**
         * callback
         *
         * @param position position
         */
        void onItemClick(int position);
    }

    public boolean isScroll() {
        return mScrollState == STATE_SCROLL;
    }

    public boolean isPause() {
        return mScrollState == STATE_PAUSE;
    }

    //memory leancks.
    @Override
    protected void onDetachedFromWindow() {
        super.onDetachedFromWindow();
        if (handler != null) {
            handler.removeCallbacksAndMessages(null);
        }
    }
}