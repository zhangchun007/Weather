package com.zhangsheng.shunxin.weather.net.bean

/**
 * @Description:
 * @Author: Qrh
 * @CreateDate: 2020/4/23 20:43
 */
class TtsTokenBean {
    /**
     * appkey : 7UYRWsiObytjfhTf
     * token : 9d1647448d3c420e9ffb4be654f69dc9
     * expire : 1587648155
     */
    var appkey: String=""
    var token: String=""
    var expire :Long= 0L
    var vaildtime:String="0"
    override fun toString(): String {
        return "TtsTokenBean(appkey='$appkey', token='$token', expire=$expire)"
    }


}