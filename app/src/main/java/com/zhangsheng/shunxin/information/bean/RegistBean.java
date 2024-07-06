package com.zhangsheng.shunxin.information.bean;

/**
 * @Author:liupengbing
 * @Data: 2020/5/22 5:45 PM
 * @Email:aliupengbing@163.com
 */
public class RegistBean {

    /**
     * data : {"access_token":"3015987806791667375141530701e0d8","expires_in":7776000}
     * msg : success
     * req_id : 2020052217443901001405001312034FCC
     * ret : 0
     */

    private DataBean data;
    private String msg;
    private String req_id;
    private int ret;

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

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

    public static class DataBean {
        /**
         * access_token : 3015987806791667375141530701e0d8
         * expires_in : 7776000
         */

        private String access_token;
        private int expires_in;

        public String getAccess_token() {
            return access_token;
        }

        public void setAccess_token(String access_token) {
            this.access_token = access_token;
        }

        public int getExpires_in() {
            return expires_in;
        }

        public void setExpires_in(int expires_in) {
            this.expires_in = expires_in;
        }
    }
}
