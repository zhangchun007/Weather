package com.zhangsheng.shunxin.information.bean;

import java.io.Serializable;

/**
 * @Author:liupengbing
 * @Data: 2020/5/22 5:54 PM
 * @Email:aliupengbing@163.com
 */
public class SignatureBean implements Serializable {
    public String timestamp;
    public String  nonce;
    public String uuid;
    public String oaid;
    public String signature;
}
