package com.zhangsheng.shunxin.weather.widget;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.graphics.Color;
import android.os.Handler;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.maiya.thirdlibrary.widget.shapview.ShapeView;
import com.zhangsheng.shunxin.R;
import com.zhangsheng.shunxin.weather.net.bean.WeatherBean;
import com.zhangsheng.shunxin.weather.utils.WeatherUtils;
import java.util.ArrayList;
import java.util.List;

/**
 * 上下滚动的 textView
 */
public class ScrollTextView extends LinearLayout {
    private TextView banner_tv2,banner_tv1;
    private ImageView icon_warms,icon_warms2;
    private ShapeView bg_color,bg_color2;
    private LinearLayout banner1,banner2;
    private Handler handler;
    private boolean isShow = false;
    private int startY1, endY1, startY2, endY2;
    private Runnable runnable;
    private List<WeatherBean.Warns> list=new  ArrayList();
    private int position = 0;
    private int offsetY = 100;
    private boolean hasPostRunnable = false;
    public ScrollTextView(Context context) {
        this(context, null);
    }

    public ScrollTextView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    private WeatherBean.Warns warns;
    private ShapeView.Config config;
    public ScrollTextView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);

        try {
            View view = LayoutInflater.from(context).inflate(R.layout.widget_weather_warns, this);
            banner1=view.findViewById(R.id.ll_banner1);
            banner2=view.findViewById(R.id.ll_banner2);
            banner_tv1 = view.findViewById(R.id.banner_tv1);
            icon_warms=view.findViewById(R.id.icon_warms);
            icon_warms2=view.findViewById(R.id.icon_warms2);
            banner_tv2 = view.findViewById(R.id.banner_tv2);
            bg_color=view.findViewById(R.id.bg_color);
            bg_color2=view.findViewById(R.id.bg_color2);
            handler = new Handler();
            runnable = () -> {
                isShow = !isShow;
                if (position == list.size() - 1) {
                    position = 0;
                }
                if (isShow) {
                    warns=list.get(position);
                    banner_tv1.setText(warns.getType()+warns.getLevel()+"预警");
                    icon_warms.setImageResource(WeatherUtils.INSTANCE.hightAlertIcon(warns.getType()));
                    config=bg_color.getConfig();
                    config.setStartColor(Color.parseColor(WeatherUtils.INSTANCE.hightAlertColors(warns.getLevel())[0]));
                    config.setEndColor(Color.parseColor(WeatherUtils.INSTANCE.hightAlertColors(warns.getLevel())[1]));
                    bg_color.exeConfig(config);
                    position++;
                    warns=list.get(position);
                    config=bg_color2.getConfig();
                    config.setStartColor(Color.parseColor(WeatherUtils.INSTANCE.hightAlertColors(warns.getLevel())[0]));
                    config.setEndColor(Color.parseColor(WeatherUtils.INSTANCE.hightAlertColors(warns.getLevel())[1]));
                    bg_color2.exeConfig(config);
                    icon_warms2.setImageResource(WeatherUtils.INSTANCE.hightAlertIcon(warns.getType()));
                    banner_tv2.setText(warns.getType()+warns.getLevel()+"预警");
                } else {
                    warns=list.get(position);
                    banner_tv2.setText(warns.getType()+warns.getLevel()+"预警");
                    icon_warms2.setImageResource(WeatherUtils.INSTANCE.hightAlertIcon(warns.getType()));
                    config=bg_color2.getConfig();
                    config.setStartColor(Color.parseColor(WeatherUtils.INSTANCE.hightAlertColors(warns.getLevel())[0]));
                    config.setEndColor(Color.parseColor(WeatherUtils.INSTANCE.hightAlertColors(warns.getLevel())[1]));
                    bg_color2.exeConfig(config);
                    position++;
                    warns=list.get(position);
                    icon_warms.setImageResource(WeatherUtils.INSTANCE.hightAlertIcon(warns.getType()));
                    banner_tv1.setText(warns.getType()+warns.getLevel()+"预警");
                    config=bg_color.getConfig();
                    config.setStartColor(Color.parseColor(WeatherUtils.INSTANCE.hightAlertColors(warns.getLevel())[0]));
                    config.setEndColor(Color.parseColor(WeatherUtils.INSTANCE.hightAlertColors(warns.getLevel())[1]));
                    bg_color.exeConfig(config);
                }

                startY1 = isShow ? 0 : offsetY;
                endY1 = isShow ? -offsetY : 0;
                ObjectAnimator.ofFloat(banner1, "translationY", startY1, endY1).setDuration(300).start();

                startY2 = isShow ? offsetY : 0;
                endY2 = isShow ? 0 : -offsetY;
                ObjectAnimator.ofFloat(banner2, "translationY", startY2, endY2).setDuration(300).start();

                handler.postDelayed(runnable, 3000);
            };
        }catch (Exception e){

        }

    }

    public List<WeatherBean.Warns> getList() {
        return list;
    }

    public void setList(List<WeatherBean.Warns> data) {
        list.clear();
        list.addAll(data);
        //处理最后一条数据切换到第一条数据 太快的问题
        if (list.size() > 1) {
            list.add(list.get(0));
        }
    }

    public void startScroll() {
        try {
            position=0;
            if (!list.isEmpty()){
                warns=list.get(position);
                banner_tv1.setText(warns.getType()+warns.getLevel()+"预警");
                icon_warms.setImageResource(WeatherUtils.INSTANCE.hightAlertIcon(warns.getType()));
                config=bg_color.getConfig();
                config.setStartColor(Color.parseColor(WeatherUtils.INSTANCE.hightAlertColors(warns.getLevel())[0]));
                config.setEndColor(Color.parseColor(WeatherUtils.INSTANCE.hightAlertColors(warns.getLevel())[1]));
                bg_color.exeConfig(config);
                if (list.size() > 1) {
                    if(!hasPostRunnable) {
                        hasPostRunnable = true;
                        handler.removeCallbacks(runnable);
                        handler.postDelayed(runnable,3000);
                    }
                } else {
                    //只有一条数据不进行滚动
                    hasPostRunnable = false;
                }
            }
        }catch (Exception e){

        }
    }


    public void stopScroll() {
        try {
            handler.removeCallbacks(runnable);
            hasPostRunnable = false;
        }catch (Exception e){

        }
    }


}
