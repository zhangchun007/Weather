package com.zhangsheng.shunxin.information.bean;

import java.util.List;

/**
 * @Author:liupengbing
 * @Data: 2020/5/24 12:24 PM
 * @Email:aliupengbing@163.com
 */
public class AllChannelBean {

    /**
     * ret : 200
     * data : {"code":0,"msg":"","data":[{"code":"__all__","title":"推荐"},{"code":"news_hot","title":"热点"},{"code":"news_local","title":"本地"},{"code":"news_society","title":"社会"},{"code":"news_entertainment","title":"娱乐"},{"code":"news_tech","title":"科技"},{"code":"news_car","title":"懂车帝"},{"code":"news_finance","title":"财经"},{"code":"news_military","title":"军事"},{"code":"news_sports","title":"体育"},{"code":"news_pet","title":"宠物"},{"code":"news_culture","title":"人文"},{"code":"news_world","title":"国际"},{"code":"news_fashion","title":"时尚"},{"code":"news_game","title":"游戏"},{"code":"news_travel","title":"旅游"},{"code":"news_history","title":"历史"},{"code":"news_discovery","title":"探索"},{"code":"news_food","title":"美食"},{"code":"news_regimen","title":"养生"},{"code":"news_health","title":"健康"},{"code":"news_baby","title":"育儿"},{"code":"news_story","title":"故事"},{"code":"news_essay","title":"美文"},{"code":"news_edu","title":"教育"},{"code":"news_house","title":"房产"},{"code":"news_career","title":"职场"},{"code":"news_photography","title":"摄影"},{"code":"news_comic","title":"动漫"},{"code":"news_astrology","title":"星座"}]}
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
         * data : [{"code":"__all__","title":"推荐"},{"code":"news_hot","title":"热点"},{"code":"news_local","title":"本地"},{"code":"news_society","title":"社会"},{"code":"news_entertainment","title":"娱乐"},{"code":"news_tech","title":"科技"},{"code":"news_car","title":"懂车帝"},{"code":"news_finance","title":"财经"},{"code":"news_military","title":"军事"},{"code":"news_sports","title":"体育"},{"code":"news_pet","title":"宠物"},{"code":"news_culture","title":"人文"},{"code":"news_world","title":"国际"},{"code":"news_fashion","title":"时尚"},{"code":"news_game","title":"游戏"},{"code":"news_travel","title":"旅游"},{"code":"news_history","title":"历史"},{"code":"news_discovery","title":"探索"},{"code":"news_food","title":"美食"},{"code":"news_regimen","title":"养生"},{"code":"news_health","title":"健康"},{"code":"news_baby","title":"育儿"},{"code":"news_story","title":"故事"},{"code":"news_essay","title":"美文"},{"code":"news_edu","title":"教育"},{"code":"news_house","title":"房产"},{"code":"news_career","title":"职场"},{"code":"news_photography","title":"摄影"},{"code":"news_comic","title":"动漫"},{"code":"news_astrology","title":"星座"}]
         */

        private int code;
        private String msg;
        private List<DataBean> data;

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

        public List<DataBean> getData() {
            return data;
        }

        public void setData(List<DataBean> data) {
            this.data = data;
        }

        public static class DataBean {
            /**
             * code : __all__
             * title : 推荐
             */

            private String code;
            private String title;

            public String getCode() {
                return code;
            }

            public void setCode(String code) {
                this.code = code;
            }

            public String getTitle() {
                return title;
            }

            public void setTitle(String title) {
                this.title = title;
            }
        }
    }
}
