package com.zhangsheng.shunxin.weather.net.bean

import com.google.gson.annotations.JsonAdapter
import com.google.gson.annotations.SerializedName
import com.zhangsheng.shunxin.weather.common.Constant
import com.zhangsheng.shunxin.weather.common.UrlConstant

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2020/7/23 17:30
 */
class ControlBean {

    var polling_time: String? = null
    var save_pop: List<SavePop>? = null
    var new_old_days: String = "3"
    var app_audit: AppAuditBean? = null
    var keep_alive: AppAuditBean? = null
    var adv_boot: List<AdvBootBean>? = null
    var hot_city: List<HotCityBean>? = null
    var swtich_all: List<SwtichAllBean>? = null
    var cctv: List<CctvBean>? = null
    var local_pop: List<LocalPopBean>? = null
    var adv_pop: List<AdvPopBean>? = null
    var adv_window: List<AdvWindowBean>? = null
    var user_need: UserNeed? = null
    var android_software_update: AndroidSoftwareUpdateBean? = null
    var ad_location: List<Ad_location>? = null

    var info_stream: List<InfoStream>? = null


    class SavePop {
        var make_start_days: String = ""   //安装后几天弹
        var days: String = ""  //间隔几天
        var times: String = ""    //总共次数
        var brand: String = "2"  //1 全部 2华为
    }


    class InfoStream {
        val title: String = ""
        val appkey: String = ""
        val appid: String = ""
        val type: String = ""
        val column: List<String>? = null

    }

    class UserNeed {
        var title = "邮箱："
        var contact = UrlConstant.EMAIL
    }

    class AppAuditBean {
        /**
         * onoff : false
         */
        var onoff = ""

    }

    class AdvBootBean {
        var make_start_days: String = "0"  //安装后第几天启动展示开屏
        var launch_times: String = "1"  //启动次数
        var open_inerval: String = "300"  //展示间隔


    }

    data class HotCityBean(var code: String = "", var title: String = "")

    class SwtichAllBean {
        //1:全部用户显示2:全部用户关闭 3新用户展示 4老用户韩式
        var report: String = "1"   //cctv
        var life: String = "1"  //生活指数万年历入口
        var infostream: String = "2"  //信息流开关
        var outactswitch: String = "1" //体外开关
        var videoentrance: String = "1"  //控制小视频卡片入口
        var infostreamtap: String = "2"  //控制信息流tab
        var smallvideo: String = "2"  //小视频tab
        var calendar: String = "2" //日历tab
        var yelentrance: String = "1"  //黄历入口开关
        var settap: String = "1"  //setting tab
        var tabfuntion: String = "1" // 实用工具tab
        var airlive: String = "1" // 空气实况图入口
        var typhoon: String = "1" // 台风路径入口


    }

    class CctvBean {
        /**
         * cover : https://h5.jiandantianqi.com/img/1921c074b7530ce266d3dbb1097fa436.png
         * title : 天气预报
         * only_id :
         * sub_title : 中国气象局
         */
        var cover: String? = null
        var title: String? = null
        var sub_title: String? = null

    }

    class LocalPopBean { //定位授权弹窗
        var days: String? = null  //间隔天数
        var times: String? = null //总共展示次数
        var mday: String? = null //第几天展示
        override fun toString(): String {
            return "LocalPopBean(days=$days, times=$times, mday=$mday)"
        }

    }

    class AdvPopBean {
        var first_open_interval: String = "0" //首次启动展示广告间隔
        var everyday_show_times: String = "1"   //每日展示次数上限
        var launch_times: String = "2"   //每日启动展示广告
        var open_inerval: String = "36000"   //展示间隔时间（秒）

    }

    class AdvWindowBean {
        var index: String? = null  //(只有3叶志斌说的)
        var window_url: String = ""
        var display_stoptime: String = ""   //结束时间
        var event_type: String? = null   //类型 1: H5(外部) 2:H5(内部)
        var img: String? = null
        var display_starttime: String = ""    //开始时间

    }

    class CPS {
        var index: String = ""
        var title: String = ""
        var img: String = ""
        var show_type: String = "2"   //1:全部用户展示  2:非任务用户展示
        var url: String = ""
        var show_pos: String = "0"
        var btn_title: String = ""
        var sub_title: String = ""
    }

    class Ad_location {
        var bigbtpre15: String = "1"
        var bigpop: String = "1"
        var bannertop: String = "1"
        var bigkqzlxq: String = "1"
        var bigdrawdrtq: String = "1"
        var bigdrawjmxq: String = "1"
        var bigrlbottom: String = "1"
        var bighuangli: String = "1"
        var bigyjejym: String = "1"
        var bigset: String = "1"
        var biglist11: String = "1"
        var biglist12: String = "1"
        var biglist13: String = "1"
        var bigxwxq: String = "1"
        var smallxwxq1: String = "1"
        var bigdrawbtshzs2: String = "1"
        var bigdrawshzs2: String = "1"
        var citybottom: String = "1"
        var bigtqspxf: String = "1"
        var smallsyxt: String = "1"
        var predict40days:String = "1"
    }

    class AndroidSoftwareUpdateBean {

        var des: String = ""
        var update_type: String = ""  //强更 2   //非强制更新 1
        var update2v: String = ""
        var remind_days: String = "1"
        var file_md5: String = ""
        var url: String = ""
        override fun toString(): String {
            return "AndroidSoftwareUpdateBean(des='$des', update_type='$update_type', update2v='$update2v', remind_days='$remind_days', url='$url')"
        }
    }

}