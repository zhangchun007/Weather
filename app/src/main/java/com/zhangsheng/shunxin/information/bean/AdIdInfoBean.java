package com.zhangsheng.shunxin.information.bean;

import com.kwad.sdk.core.response.model.AdInfo;

import java.util.List;

/**
 * @Description:
 * @Author: zhangchun
 * @CreateDate: 2021/5/24
 * @Version: 1.0
 */
public class AdIdInfoBean {
    public List<AdIdInfo>list;

    public List<AdIdInfo> getList() {
        return list;
    }
    public void setList(List<AdIdInfo> list) {
        this.list = list;
    }

    public static class AdIdInfo{
        private String pgtype;
        private String jrAdID;
        private String jrWeight;
        private String gdtId;
        private String gdtWeight;

        public String getPgtype() {
            return pgtype;
        }

        public void setPgtype(String pgtype) {
            this.pgtype = pgtype;
        }

        public String getJrAdID() {
            return jrAdID;
        }

        public void setJrAdID(String jrAdID) {
            this.jrAdID = jrAdID;
        }

        public String getJrWeight() {
            return jrWeight;
        }

        public void setJrWeight(String jrWeight) {
            this.jrWeight = jrWeight;
        }

        public String getGdtId() {
            return gdtId;
        }

        public void setGdtId(String gdtId) {
            this.gdtId = gdtId;
        }

        public String getGdtWeight() {
            return gdtWeight;
        }

        public void setGdtWeight(String gdtWeight) {
            this.gdtWeight = gdtWeight;
        }
    }
}
