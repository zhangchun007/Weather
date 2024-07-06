package com.zhangsheng.shunxin.information.dialog;


import android.app.AlertDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Handler;

import android.text.TextUtils;
import android.view.Gravity;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.AnimationSet;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;

import com.google.gson.Gson;
import com.google.gson.JsonSyntaxException;

import com.google.gson.reflect.TypeToken;
import com.maiya.thirdlibrary.utils.CacheUtil;
import com.maiya.weather.information.bean.InfoFragmentSkip;
import com.zhangsheng.shunxin.R;
import com.zhangsheng.shunxin.information.adapter.DragAdapter;
import com.zhangsheng.shunxin.information.adapter.OtherAdapter;
import com.zhangsheng.shunxin.information.bean.AllChannelBean;
import com.zhangsheng.shunxin.information.bean.TabBean;
import com.zhangsheng.shunxin.information.bean.TabData;
import com.zhangsheng.shunxin.information.constant.Constants;
import com.zhangsheng.shunxin.information.utils.AssetsUtils;
import com.zhangsheng.shunxin.information.widget.DragGridView;
import com.zhangsheng.shunxin.information.widget.MyGridView;
import com.zhangsheng.shunxin.weather.livedata.LiveDataBus;
import com.zhangsheng.shunxin.weather.net.bean.ControlBean;


import org.apache.commons.lang3.StringEscapeUtils;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author:liupengbing
 * @Data: 2020/5/21 10:13 PM
 * @Email:aliupengbing@163.com
 */
public class ChannelDialog extends AlertDialog implements AdapterView.OnItemClickListener {
    private MyGridView mOtherGv;
    private DragGridView mUserGv;
    private List<String> mUserList = new ArrayList<>();
    private List<String> mOtherList = new ArrayList<>();
    private OtherAdapter mOtherAdapter;
    private DragAdapter mUserAdapter;
    private String compile="编辑";
    private boolean isCompileState=false;
    private Gson gson;
    List<TabBean> tabList =new ArrayList<>();
    List<TabBean> otherList =new ArrayList<>();
    List<TabBean> finalOtherList =new ArrayList<>();

    private  int curPos=0;
    public ChannelDialog(@NonNull Context context) {
        super(context, R.style.MyDialogTheme);
    }

    public ChannelDialog(@NonNull  Context context, int themeResId) {
        super(context, themeResId);
    }


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.dialog_channel_layout);

        initWindow();

        initView();


    }


    private void initWindow() {
        Window window = this.getWindow();
        window.getDecorView().setPadding(0,0,0,0);
        //设置位置在屏幕底部
        window.setGravity(Gravity.BOTTOM);
        //设置弹入弹出动画
        window.setWindowAnimations(R.style.dialog_animation);
        //设置为全屏dialog
        WindowManager.LayoutParams params = window.getAttributes();
        params.width = WindowManager.LayoutParams.MATCH_PARENT;
        params.height = WindowManager.LayoutParams.MATCH_PARENT-50;
        window.setAttributes(params);
    }
    private void initView() {
        mUserGv = (DragGridView) findViewById(R.id.userGridView);
        mOtherGv = (MyGridView) findViewById(R.id.otherGridView);
        gson=new Gson();
        List<String> orderList=getTabList();


        if (orderList == null) {
            return;
        }
        //根据code从全部频道查找title
        AllChannelBean.DataBeanX allChannelBean=gson.fromJson(CacheUtil.INSTANCE.getString(Constants.SP_INFO_ALL_CHANNEL,""), AllChannelBean.DataBeanX.class);
        if(allChannelBean==null||allChannelBean.getData()==null){
            //本地默认
            String tabJson= AssetsUtils.getFromAssets(getContext(), "info_all_tab_data.json");
            TabData tabData=gson.fromJson(tabJson, TabData.class);
            List<TabBean> tab_list=tabData.tab_list;

            for (int i = 0; i < orderList.size(); i++) {
                for (int j = 0; j < tab_list.size(); j++) {
                    if (orderList.get(i).equals(tab_list.get(j).code)) {
                        TabBean tabBean = new TabBean();
                        tabBean.code = tab_list.get(j).code;
                        if(tab_list.get(j).code.equals("news_local")) {
                            if(!TextUtils.isEmpty(Constants.locationCity)){
                                tabBean.title = Constants.locationCity;
                                tabList.add(tabBean);
                            }
                        }else{
                            tabBean.title = tab_list.get(j).title;
                            tabList.add(tabBean);
                        }


                    }
                }
            }


            for (int i = 0; i < tab_list.size(); i++) {
                TabBean tabBean = new TabBean();
                tabBean.title = tab_list.get(i).title;
                tabBean.code = tab_list.get(i).code;
                otherList.add(tabBean);
                finalOtherList.add(tabBean);
            }

        }else {

            for (int i = 0; i < orderList.size(); i++) {
                for (int j = 0; j < allChannelBean.getData().size(); j++) {
                    if (orderList.get(i).equals(allChannelBean.getData().get(j).getCode())) {
                        TabBean tabBean = new TabBean();
                        tabBean.code = allChannelBean.getData().get(j).getCode();
                        if("news_local".equals(allChannelBean.getData().get(j).getCode())) {
                            if(!TextUtils.isEmpty(Constants.locationCity)){
                                tabBean.title = Constants.locationCity;
                                tabList.add(tabBean);
                            }
                        }else{
                            tabBean.title = allChannelBean.getData().get(j).getTitle();
                            tabList.add(tabBean);

                        }


                    }
                }
            }

            for (int i = 0; i < allChannelBean.getData().size(); i++) {
                TabBean tabBean = new TabBean();
                tabBean.title = allChannelBean.getData().get(i).getTitle();
                tabBean.code = allChannelBean.getData().get(i).getCode();
                otherList.add(tabBean);
                finalOtherList.add(tabBean);
            }
        }

        for (int i = 0; i <otherList.size() ; i++) {
            for (int j = 0; j <tabList.size() ; j++) {
                if(otherList.get(i).code.equals(tabList.get(j).code)) {
                    finalOtherList.remove(otherList.get(i));
                }
            }
        }



        mUserAdapter = new DragAdapter(getContext(), tabList,true);
        mOtherAdapter = new OtherAdapter(getContext(), finalOtherList,false);
        mUserGv.setAdapter(mUserAdapter);
        mOtherGv.setAdapter(mOtherAdapter);
        mUserGv.setOnItemClickListener(this);
        mOtherGv.setOnItemClickListener(this);

        ImageView tv_close = findViewById(R.id.iv_channel_close);
        Button btn_compile = findViewById(R.id.btn_compile);
        tv_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                click();


            }
        });

        btn_compile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               String  btnText= btn_compile.getText().toString();
               if(btnText.endsWith(compile)){
                   isCompileState=true;
                   btn_compile.setText("完成");
                   mUserAdapter.addDelteIcon(true);
                   mUserAdapter.notifyDataSetChanged();
               }else{
                   isCompileState=false;
                   btn_compile.setText("编辑");
                   mUserAdapter.addDelteIcon(false);

               }
            }
        });

        mUserAdapter.setChannelTextColor(curPos);

//        AppExtKt.showReport("tq_3080071","null","entry");


    }

    private void click() {
        Boolean isListChanged=mUserAdapter.isListChanged();
        if(isListChanged){

            skipFragment(-1);
        }else{
            dismiss();
        }
    }

    private void setTabCode(int position) {
        if (position>=0&&tabList != null && tabList.size() > position) {
            Constants.curTabCode = tabList.get(position).code;
            Constants.curTabTitle = tabList.get(position).title;
        }
    }

    private  List<String> getTabList() {
        //用户编辑本地频道
        List<TabBean>dataBean = null;
        try {

             String jsonString = CacheUtil.INSTANCE.getString(Constants.SP_INFO_LOCAL_ORDER_CHANNEL, "");
            if(jsonString!=null&&jsonString.length()>0) {
                //去掉转义字符
                String data = StringEscapeUtils.unescapeJava(jsonString);
                //去掉首位""
                String data1 = data.substring(1, data.length() - 1);
                dataBean = gson.fromJson(data1, new TypeToken<List<TabBean>>() {
                }.getType());
            }
        } catch (JsonSyntaxException e) {
            e.printStackTrace();
        }
            if (dataBean == null || dataBean.size() == 0) {
                //订阅频道
                ControlBean dataBean1 = gson.fromJson(CacheUtil.INSTANCE.getString(Constants.SP_INFO_ORDER_CHANNEL,""), ControlBean.class);
                if (dataBean1 == null || dataBean1.getInfo_stream() == null ||dataBean1.getInfo_stream().size()==0
                        || dataBean1.getInfo_stream().get(0).getColumn() == null
                        || dataBean1.getInfo_stream().get(0).getColumn().size() == 0) {
                    //本地默认
                    String tabJson=AssetsUtils.getFromAssets(getContext(), "info_order_tab_data.json");
                    TabData tabData=gson.fromJson(tabJson, TabData.class);
                    List<TabBean> tab_list=tabData.tab_list;
                    List<String> mList=new ArrayList<>();
                    if(tab_list!=null){
                        for (int i = 0; i <tab_list.size() ; i++) {
                            mList.add(tab_list.get(i).code);
                        }
                    }
                    return mList;
                } else {
                    return dataBean1.getInfo_stream().get(0).getColumn();
                }

            } else {
                List<String> mList=new ArrayList<>();
                for (int i = 0; i <dataBean.size() ; i++) {
                    mList.add(dataBean.get(i).code);
                }
                return mList;
            }


    }

    /**
     *获取点击的Item的对应View，
     *因为点击的Item已经有了自己归属的父容器MyGridView，所有我们要是有一个ImageView来代替Item移动
     * @param view
     * @return
     */
    private ImageView getView(View view) {
        view.destroyDrawingCache();
        view.setDrawingCacheEnabled(true);
        Bitmap cache = Bitmap.createBitmap(view.getDrawingCache());
        view.setDrawingCacheEnabled(false);
        ImageView iv = new ImageView(getContext());
        iv.setImageBitmap(cache);
        iv.setBackgroundResource(R.drawable.shap_radius5_gray);
        return iv;
    }
    /**
     * 获取移动的VIEW，放入对应ViewGroup布局容器
     * @param viewGroup
     * @param view
     * @param initLocation
     * @return
     */
    private View getMoveView(ViewGroup viewGroup, View view, int[] initLocation) {
        int x = initLocation[0];
        int y = initLocation[1];
        viewGroup.addView(view);
        LinearLayout.LayoutParams mLayoutParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mLayoutParams.leftMargin = x;
        mLayoutParams.topMargin = y;
        view.setLayoutParams(mLayoutParams);
        return view;
    }

    /**
     * 创建移动的ITEM对应的ViewGroup布局容器
     * 用于存放我们移动的View
     */
    private ViewGroup getMoveViewGroup() {
        //window中最顶层的view
        ViewGroup moveViewGroup = (ViewGroup) getWindow().getDecorView();
        LinearLayout moveLinearLayout = new LinearLayout(getContext());
        moveLinearLayout.setLayoutParams(new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.MATCH_PARENT));
        moveViewGroup.addView(moveLinearLayout);
        return moveLinearLayout;
    }
    /**
     * 点击ITEM移动动画
     *
     * @param moveView
     * @param startLocation
     * @param endLocation
     * @param moveChannel
     * @param clickGridView
     */
    private void MoveAnim(View moveView, int[] startLocation, int[] endLocation, final String moveChannel,
                          final GridView clickGridView, final boolean isUser) {
        int[] initLocation = new int[2];
        //获取传递过来的VIEW的坐标
        moveView.getLocationInWindow(initLocation);
        //得到要移动的VIEW,并放入对应的容器中
        final ViewGroup moveViewGroup = getMoveViewGroup();
        final View mMoveView = getMoveView(moveViewGroup, moveView, initLocation);
        //创建移动动画
        TranslateAnimation moveAnimation = new TranslateAnimation(
                startLocation[0], endLocation[0], startLocation[1],
                endLocation[1]);
        moveAnimation.setDuration(300L);//动画时间
        //动画配置
        AnimationSet moveAnimationSet = new AnimationSet(true);
        moveAnimationSet.setFillAfter(false);//动画效果执行完毕后，View对象不保留在终止的位置
        moveAnimationSet.addAnimation(moveAnimation);
        mMoveView.startAnimation(moveAnimationSet);
        moveAnimationSet.setAnimationListener(new Animation.AnimationListener() {

            @Override
            public void onAnimationStart(Animation animation) {
            }

            @Override
            public void onAnimationRepeat(Animation animation) {
            }

            @Override
            public void onAnimationEnd(Animation animation) {
                moveViewGroup.removeView(mMoveView);
                // 判断点击的是DragGrid还是OtherGridView
                if (isUser) {
                    mOtherAdapter.setVisible(true);
                    mOtherAdapter.notifyDataSetChanged();
//                    mUserAdapter.remove();
                } else {
                    mUserAdapter.setVisible(true);
                    mUserAdapter.notifyDataSetChanged();

//                    mOtherAdapter.remove();

                }
            }
        });
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, final int position, long id) {
        switch (parent.getId()) {
            case R.id.userGridView:
                //position为 0的不可以进行任何操作
                if(position==0){
                    mUserAdapter.setChannelTextColor(position);
                    skipFragment(position);
                }
                if (position != 0 ) { //改为-1前2个就可以移出的
                    if(!isCompileState){
                        //点击跳转到详情页
//                        Toast.makeText(getContext(), "跳转到详情页", Toast.LENGTH_SHORT).show();
                        mUserAdapter.setChannelTextColor(position);
                        skipFragment(position);


                    }else {
                        //编辑操作
//                        if(CommUtil.isFastClick()){
//                            return;
//                        }
                        final ImageView moveImageView = getView(view);
                        if (moveImageView != null) {

                            TextView newTextView = (TextView) view.findViewById(R.id.text_item);//
                            newTextView.setTextColor(getContext().getResources().getColor(R.color.color_2287F5));
                            final int[] startLocation = new int[2];
                            newTextView.getLocationInWindow(startLocation);
                            final TabBean channel = ((DragAdapter) parent.getAdapter()).getItem(position);//获取点击的频道内容
//                            Toast.makeText(getContext(), channel.title + "dsa", Toast.LENGTH_SHORT).show();
                            mOtherAdapter.setVisible(false);
                            //添加到最后一个
                            mOtherAdapter.addItem(channel);

                            mUserAdapter.setRemove(position);
                            mUserAdapter.remove();

                            new Handler().postDelayed(new Runnable() {
                                public void run() {
                                    try {
                                        int[] endLocation = new int[2];
                                        //获取终点的坐标
                                        mOtherGv.getChildAt(mOtherGv.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                        MoveAnim(moveImageView, startLocation, endLocation, channel.title, mUserGv, true);
//                                        mUserAdapter.setRemove(position);
                                    } catch (Exception localException) {
                                    }
                                }
                            }, 50L);
                        }
                    }
                }
                break;
            case R.id.otherGridView:
//                if(CommUtil.isFastClick()){
//                    return;
//                }
                final ImageView moveImageView = getView(view);
                if (moveImageView != null) {
                    TextView newTextView = (TextView) view.findViewById(R.id.text_item);
                    final int[] startLocation = new int[2];
                    newTextView.getLocationInWindow(startLocation);
                    final TabBean channel = ((OtherAdapter) parent.getAdapter()).getItem(position);
                    mUserAdapter.setVisible(false);
//                    Toast.makeText(getContext(), channel+"dsa", Toast.LENGTH_SHORT).show();
                    //添加到最后一个
                    mUserAdapter.addItem(channel);

                    mOtherAdapter.setRemove(position);
                    mOtherAdapter.remove();

                    new Handler().postDelayed(new Runnable() {
                        public void run() {
                            try {
                                int[] endLocation = new int[2];
                                //获取终点的坐标
                                mUserGv.getChildAt(mUserGv.getLastVisiblePosition()).getLocationInWindow(endLocation);
                                MoveAnim(moveImageView, startLocation, endLocation, channel.title, mOtherGv,false);
//                                mOtherAdapter.setRemove(position);
                            } catch (Exception localException) {
                            }
                        }
                    }, 50L);



                }
                break;
            default:
                break;
        }
    }

    private void skipFragment(int position) {
        setTabCode(position);

        InfoFragmentSkip infoFragmentInit=new InfoFragmentSkip();
        infoFragmentInit.setPos(position);
        LiveDataBus.INSTANCE.getChannel("InfoFragmentSkip").postValue(infoFragmentInit);
        dismiss();
    }

    public   void setData(int pos){
        this.curPos=pos;
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {

        if (event.getAction() == KeyEvent.ACTION_DOWN
                && KeyEvent.KEYCODE_BACK == keyCode) {
            click();
            return true;
        }
        return super.onKeyDown(keyCode, event);

    }

}
