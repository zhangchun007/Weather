package com.zhangsheng.shunxin.information.bean;

import java.util.List;

/**
 * @Author:liupengbing
 * @Data: 2020/6/9 13:45
 * @Email:aliupengbing@163.com
 */
public class PostBackBean {

    /**
     * data : []
     * msg : success
     * req_id : 202006091328330101940982295D479784
     * ret : 0
     */

    private String msg;
    private String req_id;
    private int ret;
    private List<?> data;

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getReq_id() {
        return req_id;
    }

    public void setReq_id(String req_id) {
        this.req_id = req_id;
    }

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public List<?> getData() {
        return data;
    }

    public void setData(List<?> data) {
        this.data = data;
    }
}
