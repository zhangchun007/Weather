package com.zhangsheng.shunxin.weather.exception;

/**
 * 说明：自定义的捕获到的异常
 * 作者：刘鹏
 * 添加时间：7/2/21 6:42 PM
 * 修改人：liupe
 * 修改时间：7/2/21 6:42 PM
 */
public class CapturedException extends Throwable {

    public CapturedException(Throwable t) {
        super("This is a captured exception, It will not cause the application to crash", t);
    }
}
