package com.zhangsheng.shunxin.information.bean;

import java.util.List;

/**
 * @Author:liupengbing
 * @Data: 2020/6/9 17:48
 * @Email:aliupengbing@163.com
 */
public class PostBackParamsBean {

    /**
     * category : news_hot
     * from_gid : 6112112412415225
     * from_vid : asffasfgsafqfqgqg
     * params : [{"group_id":123,"duration":8230,"max_duration":2340,"event_time":1707315081},{"group_id":456,"duration":1234,"max_duration":5678,"event_time":1707315081,"vid":"qsgfqgwegqgqe"},"..."]
     */

    public String category;
    public Long from_gid;
    public String from_vid;
    public List<ParamsBean> params;



    public static class ParamsBean {
        /**
         * group_id : 123
         * duration : 8230
         * max_duration : 2340
         * event_time : 1707315081
         * vid : qsgfqgwegqgqe
         */

        public Long group_id;
        public Long duration;
        public Long max_duration;
        public Long event_time;
        public String vid;


    }
}
