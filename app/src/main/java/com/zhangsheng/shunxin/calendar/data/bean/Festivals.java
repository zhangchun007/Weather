package com.zhangsheng.shunxin.calendar.data.bean;

import java.util.List;
import java.util.Map;


public class Festivals {

    /**
     * L : {}
     * S : {}
     * W : {}
     */

    private Map<String, List<Festival>> L;
    private Map<String,List<Festival>> S;
    private Map<String,List<Festival>> W;

    public Map<String, List<Festival>> getL() {
        return L;
    }

    public void setL(Map<String, List<Festival>> l) {
        L = l;
    }

    public Map<String, List<Festival>> getS() {
        return S;
    }

    public void setS(Map<String, List<Festival>> s) {
        S = s;
    }

    public Map<String, List<Festival>> getW() {
        return W;
    }

    public void setW(Map<String, List<Festival>> w) {
        W = w;
    }
}
