package com.zhangsheng.shunxin.information.utils;

import android.util.Log;

import com.google.gson.Gson;
import com.maiya.thirdlibrary.net.callback.CallResult;
import com.maiya.thirdlibrary.utils.CacheUtil;
import com.maiya.weather.util.java_bridge.JNetUtils;
import com.zhangsheng.shunxin.information.bean.InfoBean;
import com.zhangsheng.shunxin.information.bean.InfoCommendBean;
import com.zhangsheng.shunxin.information.bean.PostBackBean;
import com.zhangsheng.shunxin.information.bean.PostBackParamsBean;
import com.zhangsheng.shunxin.information.bean.SignatureBean;
import com.zhangsheng.shunxin.information.constant.Constants;


import org.jetbrains.annotations.NotNull;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.List;

import okhttp3.RequestBody;

/**
 * @Author:liupengbing
 * @Data: 2020/6/9 14:22
 * @Email:aliupengbing@163.com
 */
public class JrttPostBackUtils {
    private static JrttPostBackUtils sInstance = null;

    public static JrttPostBackUtils getInstance() {
        if (sInstance == null) {
            synchronized (JrttPostBackUtils.class) {
                if (sInstance == null) {
                    sInstance = new JrttPostBackUtils();
                }
            }
        }
        return sInstance;
    }

    public void clickPostBack(InfoBean.DataBean dataBean) {
        if (dataBean == null) {
            return;
        }
        String access_token = CacheUtil.INSTANCE.getString(Constants.ACCESS_TOKEN, "");
        SignatureBean signBean = null;
        try {
            signBean = CommUtil.getSignature();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (signBean == null) {
            return;
        }
        String timestamp = signBean.timestamp;
        String nonce = signBean.nonce;
        String signature = signBean.signature;
        String category = Constants.category;
        Long group_id = dataBean.getGroup_id();
        String event_time = System.currentTimeMillis() + "";

        JNetUtils.INSTANCE.clickPostBack(timestamp, signature, nonce, Constants.partner, access_token,
                group_id, category, event_time,
                new CallResult<PostBackBean>() {
                    @Override
                    public void ok(@NotNull PostBackBean result) {
                        super.ok(result);
                        Log.w("lpb--->", result.getMsg());
                    }

                    @Override
                    public void failed(int code, @NotNull String msg) {
                        super.failed(code, msg);
                        Log.w("lpb--->", "msg：" + msg);
                    }

                });
    }

    public void clickPostBack(InfoCommendBean.DataBean dataBean) {
        if (dataBean == null) {
            return;
        }
        String access_token = CacheUtil.INSTANCE.getString(Constants.ACCESS_TOKEN, "");
        SignatureBean signBean = null;
        try {
            signBean = CommUtil.getSignature();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (signBean == null) {
            return;
        }
        String timestamp = signBean.timestamp;
        String nonce = signBean.nonce;
        String signature = signBean.signature;
        String category = Constants.category;
        Long group_id = dataBean.getGroup_id();
        String event_time = System.currentTimeMillis() + "";

        JNetUtils.INSTANCE.clickPostBack(timestamp, signature, nonce, Constants.partner, access_token,
                group_id, category, event_time,
                new CallResult<PostBackBean>() {
                    @Override
                    public void ok(@NotNull  PostBackBean result) {
                        super.ok(result);
                        Log.w("lpb--->", result.getMsg());
                    }

                    @Override
                    public void failed(int code, @NotNull String msg) {
                        super.failed(code, msg);
                        Log.w("lpb--->", "msg：" + msg);
                    }

                });
    }


    public void infoDetilsStayTimePostBack(Long stay_time) {
        InfoBean.DataBean dataBean = CacheUtil.INSTANCE.getObj("InfoBeanDataBean", InfoBean.DataBean.class);
        if (dataBean == null) {
            return;
        }
        String access_token = CacheUtil.INSTANCE.getString(Constants.ACCESS_TOKEN, "");
        SignatureBean signBean = null;
        try {
            signBean = CommUtil.getSignature();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (signBean == null) {
            return;
        }
        String timestamp = signBean.timestamp;
        String nonce = signBean.nonce;
        String signature = signBean.signature;
        String category = Constants.category;
        Long group_id = dataBean.getGroup_id();
        String event_time = System.currentTimeMillis() + "";
        JNetUtils.INSTANCE.infoDetilsStayTimePostBack(timestamp, signature, nonce, Constants.partner, access_token,
                group_id, category, event_time, stay_time,
                new CallResult<PostBackBean>() {
                    @Override
                    public void ok(@NotNull  PostBackBean result) {
                        super.ok(result);
                        Log.w("lpb--->", result.getMsg());
                    }

                    @Override
                    public void failed(int code, @NotNull String msg) {
                        super.failed(code, msg);
                        Log.w("lpb--->", "msg：" + msg);
                    }

                });
    }

    public void infoDetilsVideoPlayStartPostBack(String position) {
        InfoBean.DataBean dataBean = CacheUtil.INSTANCE.getObj("InfoBeanDataBean", InfoBean.DataBean.class);
        if (dataBean == null) {
            return;
        }
        String access_token = CacheUtil.INSTANCE.getString(Constants.ACCESS_TOKEN, "");
        SignatureBean signBean = null;
        try {
            signBean = CommUtil.getSignature();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (signBean == null) {
            return;
        }
        String timestamp = signBean.timestamp;
        String nonce = signBean.nonce;
        String signature = signBean.signature;
        String category = Constants.category;
        Long group_id = dataBean.getGroup_id();
        String event_time = System.currentTimeMillis() + "";
        JNetUtils.INSTANCE.infoDetilsVideoPlayStartPostBack(timestamp, signature, nonce, Constants.partner, access_token,
                group_id, category, event_time, position,
                new CallResult<PostBackBean>() {
                    @Override
                    public void ok(@NotNull  PostBackBean result) {
                        super.ok(result);
                        Log.w("lpb", result.getMsg());
                    }

                    @Override
                    public void failed(int code, @NotNull String msg) {
                        super.failed(code, msg);
                        Log.w("lpb--->", "msg：" + msg);
                    }

                });
    }

    public void infoSahowPostBack( int position) {
        List<InfoBean.DataBean> mList = Constants.mList;
        if (mList == null || mList.size() == 0 || position >= mList.size()) {
            return;
        }

        String access_token = CacheUtil.INSTANCE.getString(Constants.ACCESS_TOKEN, "");
        SignatureBean signBean = null;
        try {
            signBean = CommUtil.getSignature();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (signBean == null) {
            return;
        }
        String timestamp = signBean.timestamp;
        String nonce = signBean.nonce;
        String signature = signBean.signature;
        String category = Constants.category;



        PostBackParamsBean postBackParamsBean = new PostBackParamsBean();

        List<PostBackParamsBean> paramsList = new ArrayList<>();
        List<PostBackParamsBean.ParamsBean> paramsBeanList = new ArrayList<>();


        for (int i = 0; i <position ; i++) {
            InfoBean.DataBean  dataBean = mList.get(position);
            if (dataBean == null) {
                return;
            }
            Long group_id = dataBean.getGroup_id();
            Long event_time = System.currentTimeMillis();
            String vid = "";
            if (dataBean.isHas_video()) {
                vid = dataBean.getVideo_id();
            }
            Long max_duration = 0L;
            PostBackParamsBean.ParamsBean paramsBean = new PostBackParamsBean.ParamsBean();
            Long du=CacheUtil.INSTANCE.getLong("oldPositionEnd"+i,0L);
//            Log.w("lpb","du:"+du+",oldPositionEnd:"+i);

            paramsBean.duration =du;
            paramsBean.event_time = event_time;
            paramsBean.group_id = group_id;
            paramsBean.max_duration = max_duration;
            paramsBean.vid = vid;
            paramsBeanList.add(paramsBean);
        }


        postBackParamsBean.category = category;
        postBackParamsBean.params = paramsBeanList;

        paramsList.add(postBackParamsBean);

        Gson gson = new Gson();
        String info = gson.toJson(paramsList);
        if (info.length() < 2) {
            return;
        }
        String info1 = info.substring(1, info.length() - 1);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), info1);

        JNetUtils.INSTANCE.infoShowPostBack(timestamp, signature, nonce, Constants.partner, access_token, body,
                new CallResult<PostBackBean>() {
                    @Override
                    public void ok(@NotNull  PostBackBean result) {
                        super.ok(result);
                        Log.w("lpb-infoShowPostBack:", result.getMsg());
                    }

                    @Override
                    public void failed(int code, @NotNull String msg) {
                        super.failed(code, msg);
                        Log.w("lpb--->", "msg：" + msg);
                    }

                });
    }

    public void infoDetailsShowPostBack(Long duration, int position) {
        InfoCommendBean.DataBean dataBean;
        List<InfoCommendBean.DataBean> mList = Constants.mCommenList;
        if (mList == null || mList.size() == 0 || position >= mList.size()) {
            return;
        }
        dataBean = mList.get(position);

        if (dataBean == null) {
            return;
        }
        String access_token = CacheUtil.INSTANCE.getString(Constants.ACCESS_TOKEN, "");
        SignatureBean signBean = null;
        try {
            signBean = CommUtil.getSignature();

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (signBean == null) {
            return;
        }
        String timestamp = signBean.timestamp;
        String nonce = signBean.nonce;
        String signature = signBean.signature;
        String category = Constants.category;
        Long group_id = dataBean.getGroup_id();
        Long event_time = System.currentTimeMillis();
        String vid = "";
        if (dataBean.isHas_video()) {
            vid = dataBean.getVideo_id();

        }
        Long max_duration = 0L;

        String from_vid = vid;
        Long from_gid = group_id;


        PostBackParamsBean postBackParamsBean = new PostBackParamsBean();
        PostBackParamsBean.ParamsBean paramsBean = new PostBackParamsBean.ParamsBean();

        List<PostBackParamsBean> paramsList = new ArrayList<>();
        List<PostBackParamsBean.ParamsBean> paramsBeanList = new ArrayList<>();


        postBackParamsBean.category = category;

        postBackParamsBean.from_gid = from_gid;

        if (dataBean.isHas_video()) {
            postBackParamsBean.from_vid = from_vid;

        }
        paramsBean.duration = duration;
        paramsBean.event_time = event_time;
        paramsBean.group_id = group_id;
        paramsBean.max_duration = max_duration;
        paramsBean.vid = vid;
        paramsBeanList.add(paramsBean);

        postBackParamsBean.params = paramsBeanList;

        paramsList.add(postBackParamsBean);

        Gson gson = new Gson();
        String info = gson.toJson(paramsList);
        if (info.length() < 2) {
            return;
        }
        String
                info1 = info.substring(1, info.length() - 1);
        RequestBody body = RequestBody.create(okhttp3.MediaType.parse("application/json; charset=utf-8"), info1);

        JNetUtils.INSTANCE.infoShowPostBack(timestamp, signature, nonce, Constants.partner, access_token, body,
                new CallResult<PostBackBean>() {
                    @Override
                    public void ok(@NotNull  PostBackBean result) {
                        super.ok(result);
                        Log.w("lpb-infoShowPostBack:", result.getMsg());
                    }

                    @Override
                    public void failed(int code, @NotNull String msg) {
                        super.failed(code, msg);
                        Log.w("lpb--->", "msg：" + msg);
                    }

                });
    }
}
