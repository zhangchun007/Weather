package com.zhangsheng.shunxin.information.utils;

import android.content.Context;
import android.text.TextUtils;

import com.zhangsheng.shunxin.information.bean.SignatureBean;
import com.zhangsheng.shunxin.information.constant.Constants;
import com.zhangsheng.shunxin.weather.ext.AppExtKt;
import com.xm.xmcommon.XMParam;
import com.zhangsheng.shunxin.information.bean.SignatureBean;

import java.security.NoSuchAlgorithmException;
import java.util.ArrayList;
import java.util.Collections;

/**
 * @Author:liupengbing
 * @Data: 2020/5/21 7:12 PM
 * @Email:aliupengbing@163.com
 */
public class CommUtil {
    // 两次点击间隔不能少于1000ms
    private static final int FAST_CLICK_DELAY_TIME = 500;
    private static long lastClickTime;

    /**
     * 获取6位随机数
     *
     * @return
     */
    public static long getRandomTo6() {

        long random = (long) (Math.random() * 9 + 1) * 100000;

        return random;
    }

    /**
     * 获取信息流签名signature
     *
     * @return
     */
    public static SignatureBean getSignature() throws NoSuchAlgorithmException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = String.valueOf(CommUtil.getRandomTo6());

        String keyStr = genjoinstr(Constants.secure_key, timestamp, random);
        String signaSture = Hash.sha1(keyStr);

        SignatureBean signatureBean = new SignatureBean();
        signatureBean.timestamp = timestamp;
        signatureBean.nonce = random;
        signatureBean.signature = signaSture;

        return signatureBean;
    }

    /**
     * 获取信息流签名signature
     *
     * @return
     */
    public static SignatureBean getSignatureByRegist(Context context) throws NoSuchAlgorithmException {
        String timestamp = String.valueOf(System.currentTimeMillis());
        String random = String.valueOf(CommUtil.getRandomTo6());
        String oaid = XMParam.getOAID();
        String uuid = XMParam.getDeviceId();
        if (TextUtils.isEmpty(oaid)) {
            oaid = uuid;
        }
        String keyStr = genjoinstrByregist(timestamp, random, Constants.secure_key, uuid, oaid);
        String signaSture = Hash.sha1(keyStr);


        SignatureBean signatureBean = new SignatureBean();
        signatureBean.timestamp = timestamp;
        signatureBean.nonce = random;
        signatureBean.oaid = oaid;
        signatureBean.uuid = uuid;
        signatureBean.signature = signaSture;
        ;
        return signatureBean;
    }

    /**
     * 排序
     *
     * @param API_KEY
     * @param timestamp
     * @param nonce
     * @return
     */
    public static String genjoinstr(String API_KEY, String timestamp, String nonce) {

        ArrayList<String> beforesort = new ArrayList<String>();
        beforesort.add(API_KEY);
        beforesort.add(timestamp);
        beforesort.add(nonce);
        ;

        Collections.sort(beforesort);
        StringBuffer aftersort = new StringBuffer();
        for (int i = 0; i < beforesort.size(); i++) {
            aftersort.append(beforesort.get(i));
        }

        String join_str = aftersort.toString();
        return join_str;
    }

    /**
     * 排序
     *
     * @param timestamp
     * @param nonce
     * @param API_KEY
     * @param uuid
     * @param oaid
     * @return
     */
    public static String genjoinstrByregist(String timestamp, String nonce, String API_KEY, String uuid, String oaid) {

        ArrayList<String> beforesort = new ArrayList<String>();
        beforesort.add(timestamp);
        beforesort.add(nonce);
        beforesort.add(API_KEY);
        beforesort.add(uuid);
        beforesort.add(oaid);
        Collections.sort(beforesort);
        StringBuffer aftersort = new StringBuffer();
        for (int i = 0; i < beforesort.size(); i++) {
            aftersort.append(beforesort.get(i));
        }

        String join_str = aftersort.toString();
        return join_str;
    }

    /**
     * 防止快速点击
     *
     * @return
     */
    public static boolean isFastClick() {
        boolean flag = true;
        long currentClickTime = System.currentTimeMillis();
        if ((currentClickTime - lastClickTime) >= FAST_CLICK_DELAY_TIME) {
            flag = false;
        }
        lastClickTime = currentClickTime;
        return flag;
    }

    public  static  int curPos=0;
    public static void infoTabCLickOrDetailsShowReport(String type,int position) {
        switch (Constants.curTabCode){
            case "__all__":
                report(type,"tq_3080001","tq_3080041","5020031","5030033",position);
                break;
            case "news_hot":
                report(type,"tq_3080002","tq_3080042","5020002","5030034",position);
                break;
            case "news_local":
                report(type,"tq_3080003","tq_3080043","5020003","5030035",position);
                break;
            case "news_health":
                report(type,"tq_3080004","tq_3080044","5020004","5030004",position);
                break;
            case "news_entertainment":
                report(type,"tq_3080005","tq_3080045","5020005","5030005",position);
                break;
            case "news_baby":
                report(type,"tq_3080006","tq_3080046","5020006","5030036",position);
                break;
            case "news_society":
                report(type,"tq_3080008","tq_3080048","5020008","5030008",position);
                break;
            case "news_regimen":
                report(type,"tq_3080007","tq_3080047","5020007","5030007",position);
                break;
            case "news_edu":
                report(type,"tq_3080009","tq_3080049","5020009","5030009",position);
                break;
            case "news_culture":
                report(type,"tq_3080025","tq_3080065","5020025","5030025",position);
                break;
            case "news_tech":
                report(type,"tq_3080011","tq_3080051","5020011","5030011",position);
                break;
            case "news_car":
                report(type,"tq_3080012","tq_3080052","5020012","5030037",position);
                break;
            case "news_finance":
                report(type,"tq_3080013","tq_3080053","5020013","5030038",position);
                break;
            case "news_military":
                report(type,"tq_3080014","tq_3080054","5020014","5030039",position);
                break;
            case "news_sports":
                report(type,"tq_3080015","tq_3080055","5020015","5030040",position);
                break;
            case "news_pet":
                report(type,"tq_3080016","tq_3080056","5020016","5030016",position);
                break;
            case "news_world":
                report(type,"tq_3080017","tq_3080057","5020017","5030017",position);
                break;
            case "news_fashion":
                report(type,"tq_3080018","tq_3080058","5020018","5030018",position);
                break;
            case "news_game":
                report(type,"tq_3080019","tq_3080059","5020019","5030019",position);
                break;
            case "news_travel":
                report(type,"tq_3080020","tq_3080060","5020020","5030020",position);
                break;
            case "news_history":
                report(type,"tq_3080021","tq_3080061","5020021","5030021",position);
                break;
            case "news_discovery":
                report(type,"tq_3080022","tq_3080062","5020022","5030022",position);
                break;
            case "news_food":
                report(type,"tq_3080023","tq_3080063","5020023","5030023",position);
                break;
            case "news_essay":
                report(type,"tq_3080024","tq_3080064","5020024","5030024",position);
                break;
            case "news_story":
                report(type,"tq_3080010","tq_3080050","5020025","5030025",position);
                break;
            case "news_house":
                report(type,"tq_3080026","tq_3080056","5020026","5030026",position);
                break;
            case "news_career":
                report(type,"tq_3080027","tq_3080057","5020027","5030027",position);
                break;
            case "news_photography":
                report(type,"tq_3080028","tq_3080058","5020028","5030028",position);
                break;
            case "news_comic":
                report(type,"tq_3080029","tq_3080059","5020029","5030029",position);
                break;
            case "news_astrology":
                report(type,"tq_3080030","tq_3080060","5020030","5030030",position);
                break;
           }
        }

    private static void report(String type,String tabClickId,String detailsShowId,String tabPageId,String infoDetailsId,int postion) {
//        if(type.equals("1")){
//            //tab 点击
//            //列表页面停留时间
//            if(curPos!=postion){
//                Log.w("在线时长 news","id:"+ReportUtils.INSTANCE.getPageId()+"time:"+(System.currentTimeMillis() - ReportUtils.INSTANCE.getPageStartTime()));
//                ReportUtils.INSTANCE.uploadAppOnline(
//                        ReportUtils.INSTANCE.getPageId(),
//                        ReportUtils.INSTANCE.getPageUrl(),
//                        System.currentTimeMillis() - ReportUtils.INSTANCE.getPageStartTime()
//                );
//            }
//            ReportUtils.INSTANCE.setPageId(tabPageId);
//            ReportUtils.INSTANCE.setPageStartTime(System.currentTimeMillis());
//            curPos=postion;
//        }else if(type.equals("2")){
//            //详情页show
//            AppExtKt.showReport(detailsShowId,"null","page");
//            //详情页时间统计
//            ReportUtils.INSTANCE.setPageId(infoDetailsId);
//            ReportUtils.INSTANCE.setPageStartTime(System.currentTimeMillis());
//        }
    }



}
