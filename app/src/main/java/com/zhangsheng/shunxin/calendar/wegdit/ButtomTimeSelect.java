package com.zhangsheng.shunxin.calendar.wegdit;

import android.app.Dialog;
import android.content.Context;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Gravity;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import com.zhangsheng.shunxin.R;
import com.zhangsheng.shunxin.calendar.wegdit.data.ChineseCalendar;
import com.zhangsheng.shunxin.calendar.wegdit.view.GregorianLunarCalendarView;
import com.zhangsheng.shunxin.calendar.wegdit.data.ChineseCalendar;
import com.zhangsheng.shunxin.calendar.wegdit.view.GregorianLunarCalendarView;

import java.util.Calendar;


public class ButtomTimeSelect extends Dialog implements View.OnClickListener {

    private Context mContext;
    private GregorianLunarCalendarView mGLCView;
    private TextView mButtonGetData;
    private RadioButton rb_gregorian;
    private RadioButton rb_lunar;
    private RadioGroup rg_calendar;
    private TextView returnToday;

    public ButtomTimeSelect(Context context) {
        super(context, R.style.bottom_dialog_anima_style);
        mContext = context;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.buttom_time_select);
        initWindow();
        mGLCView = (GregorianLunarCalendarView) this.findViewById(R.id.calendar_view);
        rg_calendar = (RadioGroup) this.findViewById(R.id.rg_calendar);
        mButtonGetData = (TextView) this.findViewById(R.id.button_get_data);
        mButtonGetData.setOnClickListener(this);
        rb_gregorian = (RadioButton) this.findViewById(R.id.rb_gregorian);
        rb_lunar = (RadioButton) this.findViewById(R.id.rb_lunar);
        returnToday = (TextView) this.findViewById(R.id.btn_cancle);
        returnToday.setOnClickListener(this);

    }

    public void initCalendar(Calendar calendar) {
        rb_gregorian.setChecked(true);
        mGLCView.init(calendar);

        rg_calendar.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup radioGroup, int i) {
                switch (i) {
                    case R.id.rb_gregorian:

                        toGregorianMode();
                        isLunar = false;
                        if (timeSelectedOnclickListener != null) {
                            timeSelectedOnclickListener.onLunarSelect(isLunar);
                        }

                        break;
                    case R.id.rb_lunar:
                        toLunarMode();
                        isLunar = true;
                        if (timeSelectedOnclickListener != null) {
                            timeSelectedOnclickListener.onLunarSelect(isLunar);
                        }
                        break;
                    default:
                        break;
                }
            }
        });
    }

    //是否是农历
    private boolean isLunar = false;


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.button_get_data:
                GregorianLunarCalendarView.CalendarData calendarData = mGLCView.getCalendarData();
                ChineseCalendar calendar = (ChineseCalendar) calendarData.getCalendar();
                if (calendar.get(Calendar.YEAR) > 2099) {
                    calendar = (ChineseCalendar) new GregorianLunarCalendarView.CalendarData(2099, 12, 31, true).getCalendar();

                } else if (calendar.get(Calendar.YEAR) < 1901) {
                    calendar = (ChineseCalendar) new GregorianLunarCalendarView.CalendarData(1901, 1, 1, true).getCalendar();
                }
                if (timeSelectedOnclickListener != null) {
                    timeSelectedOnclickListener.onAffirm(calendar);
                }
                dismiss();
                break;
            case R.id.btn_cancle:
                if (timeSelectedOnclickListener != null) {
                    timeSelectedOnclickListener.onFinish();
                }
                dismiss();
                break;
            default:
                break;
        }
    }

    private void toGregorianMode() {
        mGLCView.toGregorianMode();
    }

    private void toLunarMode() {
        mGLCView.toLunarMode();
    }

    private void initWindow() {
        Window win = this.getWindow();
        win.getDecorView().setPadding(0, 0, 0, 0);
        WindowManager.LayoutParams lp = win.getAttributes();
        lp.width = (int) (0.90 * getScreenWidth(getContext()));
        if (lp.width > dp2px(getContext(), 480)) {
            lp.width = dp2px(getContext(), 480);
        }
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        lp.gravity = Gravity.BOTTOM;
        win.setAttributes(lp);
    }

    private int getScreenWidth(Context context) {
        DisplayMetrics dm = new DisplayMetrics();
        WindowManager wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
        wm.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    private int dp2px(Context context, float dipValue) {
        final float scale = context.getResources().getDisplayMetrics().density;
        return (int) (dipValue * scale + 0.5f);
    }

    public TimeSelectedOnclickListener getDialogGLCOnclickListener() {
        return timeSelectedOnclickListener;
    }

    public void setTimeSelectedOnclickListener(TimeSelectedOnclickListener dialogGLCOnclickListener) {
        this.timeSelectedOnclickListener = dialogGLCOnclickListener;
    }

    public boolean isLunar() {
        return isLunar;
    }

    public void setLunar(boolean lunar) {
        isLunar = lunar;
    }

    public interface TimeSelectedOnclickListener {
        abstract void onAffirm(Calendar calendarData);

        abstract void onFinish();

        abstract void onLunarSelect(boolean isLunar);
    }

    private TimeSelectedOnclickListener timeSelectedOnclickListener;


    @Override
    public void setOnDismissListener(OnDismissListener listener) {
        super.setOnDismissListener(listener);
        timeSelectedOnclickListener = null;
    }

    @Override
    public void show() {
        super.show();
    }
}