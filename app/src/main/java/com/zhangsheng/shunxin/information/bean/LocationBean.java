package com.zhangsheng.shunxin.information.bean;

/**
 * @Author:liupengbing
 * @Data: 2020/5/31 3:02 PM
 * @Email:aliupengbing@163.com
 */
public class LocationBean {

    /**
     * ret : 200
     * data : {"code":0,"msg":"","data":{"ip":"180.164.176.214","beginip":"180.163.190.0","endip":"180.166.76.48","country":"上海","area":"电信"}}
     * msg :
     */

    private int ret;
    private DataBeanX data;
    private String msg;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public DataBeanX getData() {
        return data;
    }

    public void setData(DataBeanX data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBeanX {
        /**
         * code : 0
         * msg :
         * data : {"ip":"180.164.176.214","beginip":"180.163.190.0","endip":"180.166.76.48","country":"上海","area":"电信"}
         */

        private int code;
        private String msg;
        private DataBean data;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getMsg() {
            return msg;
        }

        public void setMsg(String msg) {
            this.msg = msg;
        }

        public DataBean getData() {
            return data;
        }

        public void setData(DataBean data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * ip : 180.164.176.214
             * beginip : 180.163.190.0
             * endip : 180.166.76.48
             * country : 上海
             * area : 电信
             */

            private String ip;
            private String beginip;
            private String endip;
            private String country;
            private String area;

            public String getIp() {
                return ip;
            }

            public void setIp(String ip) {
                this.ip = ip;
            }

            public String getBeginip() {
                return beginip;
            }

            public void setBeginip(String beginip) {
                this.beginip = beginip;
            }

            public String getEndip() {
                return endip;
            }

            public void setEndip(String endip) {
                this.endip = endip;
            }

            public String getCountry() {
                return country;
            }

            public void setCountry(String country) {
                this.country = country;
            }

            public String getArea() {
                return area;
            }

            public void setArea(String area) {
                this.area = area;
            }
        }
    }
}
