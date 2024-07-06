package com.zhangsheng.shunxin.information;


import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;

import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.airbnb.lottie.LottieAnimationView;
import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import com.google.gson.reflect.TypeToken;
import com.maiya.thirdlibrary.net.bean.None;
import com.maiya.thirdlibrary.net.callback.CallResult;
import com.maiya.thirdlibrary.utils.CacheUtil;
import com.maiya.weather.util.java_bridge.JNetUtils;
import com.zhangsheng.shunxin.R;
import com.zhangsheng.shunxin.information.adapter.InfoStreamAdapter;
import com.zhangsheng.shunxin.information.bean.InfoBean;
import com.zhangsheng.shunxin.information.bean.RegistBean;
import com.zhangsheng.shunxin.information.bean.SignatureBean;
import com.zhangsheng.shunxin.information.constant.Constants;
import com.zhangsheng.shunxin.information.refresh.InfoRefreshFooter;
import com.zhangsheng.shunxin.information.refresh.InfoRefreshHeader;
import com.zhangsheng.shunxin.information.utils.AnimUtils;
import com.zhangsheng.shunxin.information.utils.CommUtil;
import com.zhangsheng.shunxin.information.utils.JrttPostBackUtils;
import com.zhangsheng.shunxin.information.utils.NetworkUtil;
import com.zhangsheng.shunxin.information.utils.PhoneInfoUtils;
import com.zhangsheng.shunxin.weather.livedata.LiveDataBus;
import com.scwang.smart.refresh.layout.SmartRefreshLayout;
import com.scwang.smart.refresh.layout.api.RefreshLayout;
import com.scwang.smart.refresh.layout.listener.OnLoadMoreListener;
import com.scwang.smart.refresh.layout.listener.OnRefreshListener;
import com.xm.xmcommon.XMParam;


import org.apache.commons.lang3.StringEscapeUtils;
import org.jetbrains.annotations.NotNull;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;


public class InformationFragment extends Fragment implements OnLoadMoreListener, OnRefreshListener {

    private static String News_Tab_Entity = "News_Tab_Entity";
    private  List<InfoBean.DataBean> dataList = new ArrayList<>();


    static RecyclerView mNewsRecommendsRecycleView;

    SmartRefreshLayout mSmartRefreshLayout;

    private LinearLayoutManager layoutManager;
    private InfoStreamAdapter mInfoAdapter;

    //Fragment的View加载完毕的标记
    private boolean isViewCreated;
    //Fragment对用户可见的标记
    private boolean isUIVisible;
    private LinearLayout commLoadingRlyt;

    // 请求内容页码
    private int mPageIndex = 1;
    private int pullState=0;
    private String channelCode;
    private TextView refreshTipsTv;
    private InfoRefreshHeader mRefreshHeader;
    private RelativeLayout no_more_data_view;
    private boolean isFirstPull=true;
    private LottieAnimationView lottli_anim_loading;
    private InfoRefreshFooter mRefresFooter;
    private boolean loading=true;
    private View rootView;
    private int requestNum=0;
    private LinearLayout ll_no_net;
    private List<InfoBean.DataBean> localDataList;
    private TextView reload;
    private int oldFirstVisibleItem=0;
    private int firstVisibleItem=0;
    public static int lastVisibleItem=0;
    private int totalItemCount=0;
    private long startTime=0;
    private long stayTime;

    private Boolean firstPage = true;

    public static InformationFragment newInstance(String channelCode) {
        InformationFragment fragment = new InformationFragment();
        fragment.setChannelCode(channelCode);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        if (rootView == null) {
//            Log.w("lpb","rootView1");

            rootView = inflater.inflate(R.layout.fragment_information, container, false);
            //在这里做一些初始化处理
            initLayout();
            isViewCreated = true;
            initCurData();
        } else {
//            Log.w("lpb","rootView2");

            ViewGroup viewGroup = (ViewGroup) rootView.getParent();
            if (viewGroup != null)
                viewGroup.removeView(rootView);
        }



        return rootView;
    }

    private void initLayout() {
        mNewsRecommendsRecycleView=rootView.findViewById(R.id.news_recommends_recycle_view);
        mSmartRefreshLayout=rootView.findViewById(R.id.smart_refresh_layout);
        ll_no_net=rootView.findViewById(R.id.ll_no_net);
        reload=rootView.findViewById(R.id.reload);
        commLoadingRlyt=rootView.findViewById(R.id.comm_loading_rlyt);
        refreshTipsTv=rootView.findViewById(R.id.news_recommends_refresh_tips);
        no_more_data_view=rootView.findViewById(R.id.no_more_data_view);
        lottli_anim_loading=rootView.findViewById(R.id.lottli_anim_loading);
        lottli_anim_loading.playAnimation();
        startTime=System.currentTimeMillis();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {

        super.onViewCreated(view, savedInstanceState);

    }
    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        //isVisibleToUser这个boolean值表示:该Fragment的UI 用户是否可见
//        Log.w("lpb","isVisibleToUser"+isVisibleToUser);

        if (isVisibleToUser) {
            isUIVisible = true;
            lazyLoad();
        } else {
            isUIVisible = false;
        }
    }
    /**
     * 懒加载
     */
    private void lazyLoad() {
//        Log.w("lpb","lazyLoad");

        if (isViewCreated && isUIVisible) {
//            Log.w("lpb","lazyLoad------true");

            if(isFirstPull) {
//                Log.w("lpb","isFirstPull"+isFirstPull);

                requestData(0,"lazyLoad");
            }
        }
    }
    private void setChannelCode(String channelCode){
     this.channelCode=channelCode;
        Constants.category=channelCode;
    }
    public void initCurData() {
//        AppExtKt.showReport("tq_3080041","null","page");

        initRefreshLayout();
        initRecyclerView();
        initListener();
        lazyLoad();
    }

    private void initListener() {
        reload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                requestData(0,"reload");
            }
        });
    }

    private void initRefreshLayout() {

        mRefreshHeader=new InfoRefreshHeader(getActivity());
        mSmartRefreshLayout.setRefreshHeader(mRefreshHeader);

        mRefresFooter=new InfoRefreshFooter(getActivity());
        mSmartRefreshLayout.setRefreshFooter(mRefresFooter);

        mSmartRefreshLayout.setOnRefreshListener(this);
        mSmartRefreshLayout.setOnLoadMoreListener(this);

        mSmartRefreshLayout.setEnableLoadMore(true);
        mSmartRefreshLayout.setEnableRefresh(true);

    }
    private void initRecyclerView() {
         mInfoAdapter = new InfoStreamAdapter(getActivity());
        mNewsRecommendsRecycleView.setNestedScrollingEnabled(false);
        mNewsRecommendsRecycleView.setAdapter(mInfoAdapter);
        layoutManager = new LinearLayoutManager(getActivity());
        mNewsRecommendsRecycleView.setLayoutManager(layoutManager);

//        mNewsRecommendsRecycleView.setFocusable(false);

        mNewsRecommendsRecycleView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);

            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                //获取最后一个完全显示的ItemPosition
                 firstVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
                 lastVisibleItem = layoutManager.findLastCompletelyVisibleItemPosition();
                 totalItemCount = layoutManager.getItemCount();
                 if(oldFirstVisibleItem!=firstVisibleItem){
                     oldFirstVisibleItem=firstVisibleItem;
                     stayTime=System.currentTimeMillis()-startTime;
                     startTime+=stayTime;
                     CacheUtil.INSTANCE.put("oldPositionEnd"+oldFirstVisibleItem,stayTime);
//                     Log.w("lpb","stayTime:"+stayTime+",oldFirstVisibleItem:"+oldFirstVisibleItem);
                 }
                // 判断是否滚动到底部
                if (lastVisibleItem == totalItemCount-3&&dy>0) {
                    if(requestNum==0) {
                        requestNum = 1;
                        //加载更多功能的代码
                        requestData(1,"onScrolled");

                        JrttPostBackUtils.getInstance().infoSahowPostBack(lastVisibleItem);
                    }
                }

            }
        });

    }



    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {

        requestData(1,"onLoadMore");
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        mPageIndex = 1;
        isFirstPull=true;
        requestData(0,"onRefresh");

    }

    /**
     * 请求今日头条资讯信息
     */
    private void requestData(int state,String from) {
        this.pullState=state;
        if ("lazyLoad".equals(from)) {
            firstPage = true;
        } else {
            firstPage = false;
        }
        if(NetworkUtil.isNetworkActive(getContext())) {
            ll_no_net.setVisibility(View.GONE);
            requestInfo();

        }else{
           //没有网络
            getLocalData();

        }
    }

    private void requestInfo() {
        String access_token = CacheUtil.INSTANCE.getString(Constants.ACCESS_TOKEN, "");
        if (null == access_token || access_token.length() == 0) {
            //获取token
            getTokenByOaid();
        } else {
            //用本地的token
            getInfo(access_token);
        }
    }


    private void getTokenByOaid() {
        SignatureBean signBean = null;
        try {
            signBean = CommUtil.getSignatureByRegist(getContext());

        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        if (signBean == null) {
            return;
        }
        String timestamp = signBean.timestamp;
        String nonce = signBean.nonce;
        String uuid = signBean.uuid;
        String oaid = signBean.oaid;
        if (TextUtils.isEmpty(oaid)) {
            oaid = uuid;
        }
        String signature = signBean.signature;

        JNetUtils.INSTANCE.getTokenByOaid(timestamp, signature, nonce, Constants.partner, uuid, oaid, new CallResult<RegistBean>() {
            @Override
            public void ok(@NotNull RegistBean result) {
                super.ok(result);
                if (result != null) {
                    if (result.getRet() == 0) {
                        if (result.getData() != null) {
                            String access_token = result.getData().getAccess_token();
                            CacheUtil.INSTANCE.put(Constants.ACCESS_TOKEN, access_token);
                            //获取信息流信息
                            getInfo(access_token);
                        }
                    }else{
                        Log.w("lpb",result.getMsg());
                    }
                }
            }

            @Override
            public void failed(int code, @NotNull String msg) {
                super.failed(code, msg);
            }

        });
    }
    private int allNum=0;
    private int tokenNum=0;
    private void getInfo(String access_token) {
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

        String uuid = XMParam.getDeviceId();
        String oaid = XMParam.getOAID();
        if (TextUtils.isEmpty(oaid)) {
            oaid = uuid;
        }

        String dt= PhoneInfoUtils.getPhoneModel();
        //分辨率
        String resolution=PhoneInfoUtils.getResolution(getActivity());
        String ip=PhoneInfoUtils.getIp(getActivity());
//        Log.w("lpb","ip:"+ip);
        String os_version=PhoneInfoUtils.getSystemVersion();
        String type="1";
        String os="Android";
        String category=channelCode;
        String city="";
        if(TextUtils.isEmpty(category)){
            city="";
        }else if(category.equals("news_local")){
            city = Constants.locationCity;
        }
        String ac=NetworkUtil.getNetworkType(getContext());
        Log.w("lpb","ac:"+ac);

        JNetUtils.INSTANCE.getInfo(timestamp, signature, nonce, Constants.partner, uuid, oaid,
                access_token,dt,ip,type,os,os_version,resolution,category,city,ac,
                new CallResult<InfoBean>() {
            @Override
            public void ok(@NotNull  InfoBean result) {
                super.ok(result);
//                Log.w("lpb--->",result.getMessage());
                requestNum=0;
                if (mInfoAdapter == null||result==null) {
                    return;
                }

                if(result.getRet()==0){

                    List<InfoBean.DataBean> newDataList=result.getData();
                    if(newDataList==null||newDataList.size()==0){
                        //没有数据
                        report(0);
                        updateNoDataUI();
                        return;
                    }
                    dataList=newDataList;
                    if(pullState==0){
                        //下拉刷新
                        showFinishTips(dataList.size());
                        mInfoAdapter.addData(dataList,true,firstPage, channelCode);
                    }else{
                        mInfoAdapter.addData(dataList,false,firstPage, channelCode);
                    }
                    saveLocalData(dataList);
                    report(dataList.size());
                    allNum=dataList.size();
                    reportAllNum();

                }else{
                    //token失效
                    if(!TextUtils.isEmpty(result.getMessage())){
                        if(result.getMessage().trim().contains("token")){
                            //获取token
                            if(tokenNum<3) {
                                getTokenByOaid();
                            }
                            tokenNum++;
                        }
                    }

                }
                updateUI();

                // 数据请求成功页面加一
                mPageIndex++;
            }

            @Override
            public void failed(int code, @NotNull String msg) {
                super.failed(code, msg);
                Log.w("lpb--->","msg："+msg);
                requestNum=0;
                updateUI();
                mSmartRefreshLayout.finishRefresh();

                mSmartRefreshLayout.finishLoadMore();


            }

        });

    }

    private void reportAllNum() {
        //日
        int day = Calendar.getInstance().get(Calendar.DAY_OF_MONTH);
        int spDay=CacheUtil.INSTANCE.getInt("curData",0);

        int spNum=CacheUtil.INSTANCE.getInt("allNum",0);
        allNum+=spNum;

        if(spDay==0) {
            CacheUtil.INSTANCE.put("curData",day);

        }else{
            if (day != spDay) {
                //不是同一天
                CacheUtil.INSTANCE.put("allNum",0);
            }else{
                CacheUtil.INSTANCE.put("allNum",allNum);
            }
        }

    }

    private void saveLocalData(List<InfoBean.DataBean> dataList) {
        String jsonString = new Gson().toJson(dataList);
        CacheUtil.INSTANCE.putObj(channelCode,jsonString);
    }
    private void getLocalData() {
        try {

            String jsonString = CacheUtil.INSTANCE.getString(channelCode, "");
            if(jsonString!=null&&jsonString.length()>0) {
                //去掉转义字符
                String data = StringEscapeUtils.unescapeJava(jsonString);
                //去掉首位""
                String data1 = data.substring(1, data.length() - 1);
                localDataList = new Gson().fromJson(data1, new TypeToken<List<InfoBean.DataBean>>() {
                }.getType());
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
        if(localDataList!=null&&localDataList.size()>0){
            //展示缓存数据
            mInfoAdapter.addData(localDataList,true,firstPage, channelCode);
            //tips展示没有网络
            updateNoNetUI(true);
        }else{
            //展示没有网络页面
            updateNoNetUI(false);
        }
    }
    private void report(int onceNum) {
    }

    private void updateNoDataUI() {
        if (pullState == 0) {
            isFirstPull=false;
            mSmartRefreshLayout.finishRefresh();
            commLoadingRlyt.setVisibility(View.GONE);
            lottli_anim_loading.pauseAnimation();
            showFinishTips(0);
        } else {
            showFinishTips(0);
            mSmartRefreshLayout.finishLoadMore();

        }
    }
    private void updateNoNetUI(boolean localData) {
        if(localData){
            ll_no_net.setVisibility(View.GONE);
            showFinishTips(-1);
        }else{
            ll_no_net.setVisibility(View.VISIBLE);

        }
        commLoadingRlyt.setVisibility(View.GONE);
        lottli_anim_loading.pauseAnimation();
        mSmartRefreshLayout.finishRefresh();
        mSmartRefreshLayout.finishLoadMore();
    }



    private void showFinishTips(int size) {
        if (refreshTipsTv == null) {
            return;
        }
        String target = "";
        if(size>0) {
             target = String.format(getActivity().getResources().getString(R.string.comm_refresh_tips), "" + size);
            refreshTipsTv.setTextColor(Color.parseColor("#2287F5"));
            refreshTipsTv.setBackgroundColor(Color.parseColor("#D2E7FD"));
        }else if(size==0){
            target="没有更多数据了哦";
            refreshTipsTv.setTextColor(Color.parseColor("#2287F5"));
            refreshTipsTv.setBackgroundColor(Color.parseColor("#D2E7FD"));
        }else if(size==-1){
            target="网络异常，请检查网络后重试!";
            refreshTipsTv.setTextColor(Color.parseColor("#FF4B4B"));
            refreshTipsTv.setBackgroundColor(Color.parseColor("#FFDBDB"));
        }
        refreshTipsTv.setText(target);
        if(refreshTipsTv.getVisibility() == View.VISIBLE) {
            mHandler.removeMessages(MSG_HIDE);
            mHandler.sendEmptyMessageDelayed(MSG_HIDE, DELAY_TIME);
            return;
        }
        AnimUtils.showTips(refreshTipsTv,getContext());
        refreshTipsTv.setVisibility(View.VISIBLE);

        mHandler.removeMessages(MSG_HIDE);
        mHandler.sendEmptyMessageDelayed(MSG_HIDE, DELAY_TIME);
    }
    // 动画延迟时间
    public static final int DELAY_TIME = 2000;
    // 隐藏消息类型
    private static final int MSG_HIDE = 123;
    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case MSG_HIDE:
                    if (refreshTipsTv != null&&getContext()!=null) {
                        AnimUtils.dissmissTips(refreshTipsTv,getContext());
                    }
                    break;
            }
        }
    };
    private void updateUI() {
        no_more_data_view.setVisibility(View.GONE);
        if (mInfoAdapter != null) {
            int itemCount = mInfoAdapter.getItemCount();
//            Log.w("lpb","itemCount"+itemCount);
            if (itemCount >=0) {
                lottli_anim_loading.pauseAnimation();
                commLoadingRlyt.setVisibility(View.GONE);
            } else {
                commLoadingRlyt.setVisibility(View.VISIBLE);
            }
        }
//        Log.w("lpb","mInfoAdapter");

        if (pullState == 0) {
            isFirstPull=false;
            mSmartRefreshLayout.finishRefresh();
        } else {
            mSmartRefreshLayout.finishLoadMore();

        }
        ll_no_net.setVisibility(View.GONE);
    }
    private long pauseTime=0L;
    @Override
    public void onPause() {
        super.onPause();
        pauseTime=System.currentTimeMillis();
        Log.w("lpb","onPause---"+pauseTime);

    }

    @Override
    public void onResume() {
        super.onResume();
        if(pauseTime!=0) {
            Long leadTime = System.currentTimeMillis() - pauseTime;
            Log.w("lpb", "onResume--leadTime--" + leadTime);
            if (leadTime >= 60 * 60 * 1000) {
                Log.w("lpb", "leadTime >= 60 * 60 * 1000--" + leadTime);
                LiveDataBus.INSTANCE.getChannel("ScreenBean").postValue(new None());
            }
        }


    }
}
