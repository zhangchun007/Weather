package com.zhangsheng.shunxin.information.utils;

import android.app.Activity;
import android.content.Intent;


import com.zhangsheng.shunxin.information.InfoDetails.InfoDetailsActivity;
import com.zhangsheng.shunxin.information.InformationFragment;
import com.zhangsheng.shunxin.information.bean.InfoBean;
import com.zhangsheng.shunxin.information.InformationFragment;
import com.zhangsheng.shunxin.information.bean.InfoBean;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:liupengbing
 * @Data: 2020/5/28 6:19 PM
 * @Email:aliupengbing@163.com
 */
public class ActivityUtil {
    private static final String TAG = "ActivityControl";

    private static ActivityUtil sInstance = null;
    private final List<Activity> mList = new ArrayList<>();

    public static ActivityUtil getInstance() {
        if (sInstance == null) {
            synchronized (ActivityUtil.class) {
                if (sInstance == null) {
                    sInstance = new ActivityUtil();
                }
            }
        }
        return sInstance;
    }

    public void add(Activity activity) {
        mList.add(activity);
    }

    public void close(Activity activity) {
        if (mList.contains(activity)) {
            mList.remove(activity);
            activity.finish();
        }
    }

    public void closeAll() {
        try {
            for (int i = 0; i < mList.size(); i++) {
                Activity activity = mList.get(i);
                if (activity != null) {
                    mList.remove(activity);
                    activity.finish();
                    i--; //因为位置发生改变，所以必须修改i的位置,否则会有ConcurrentModificationException
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public void skipInfoDetailsActivity(InfoBean.DataBean dataBean, Activity activity) {
        if (dataBean == null) {
            return;
        }

        String url = dataBean.getArticle_url() + "&hide_comments=1&hide_relate_news=1";
        Intent intent = new Intent(activity, InfoDetailsActivity.class);
        intent.putExtra("Article_url", url);
        intent.putExtra("item_id", String.valueOf(dataBean.getItem_id()));
        intent.putExtra("group_id", String.valueOf(dataBean.getGroup_id()));
        intent.putExtra("has_video", dataBean.isHas_video());
        activity.startActivityForResult(intent, 200);

        JrttPostBackUtils.getInstance().clickPostBack(dataBean);
        JrttPostBackUtils.getInstance().infoSahowPostBack(InformationFragment.lastVisibleItem);
    }


















}
