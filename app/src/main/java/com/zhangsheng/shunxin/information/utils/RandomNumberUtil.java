package com.zhangsheng.shunxin.information.utils;

/**
 * @Author:liupengbing
 * @Data: 2020/5/23 10:54 AM
 * @Email:aliupengbing@163.com
 */
public class RandomNumberUtil {
    private RandomNumberUtil(){

    }

    /**
     * 产生n位随机数
     * @return
     */
    public static long generateRandomNumber(int n){
        if(n<1){
            throw new IllegalArgumentException("随机数位数必须大于0");
        }
        return (long)(Math.random()*9*Math.pow(10,n-1)) + (long)Math.pow(10,n-1);
    }
}
