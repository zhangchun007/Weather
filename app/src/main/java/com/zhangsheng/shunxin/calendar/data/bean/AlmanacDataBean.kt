package com.zhangsheng.shunxin.calendar.data.bean

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2020/4/21 16:13
 */
class AlmanacDataBean {
    //彭祖百忌
    var pzbj: String? = ""

    //五行
    var wx: String? = ""

    //冲煞
    var cs: String? = ""

    //值神
    var zhishen: String? = ""

    //建除十二神
    var jianchu: String? = ""

    //二十八星宿
    var stars28: String? = ""

    //今日胎神
    var taishen: String? = ""

    //吉神宜趋
    var jsyq: String? = ""

    //凶神宜忌
    var xsyj: String? = ""

    override fun toString(): String {
        return "AlmanacDataBean{" +
                "pzbj='" + pzbj + '\'' +
                ", wx='" + wx + '\'' +
                ", cs='" + cs + '\'' +
                ", zhishen='" + zhishen + '\'' +
                ", jianchu='" + jianchu + '\'' +
                ", stars28='" + stars28 + '\'' +
                ", taishen='" + taishen + '\'' +
                ", jsyq='" + jsyq + '\'' +
                ", xsyj='" + xsyj + '\'' +
                '}'
    }
}