package com.zhangsheng.shunxin.information.utils;

import android.content.Context;

import java.io.InputStream;

public class AssetsUtils {

    /**
     * 读取assets下文件内容
     * @param context
     * @param fileName
     * @return
     */
    public static String getFromAssets(Context context, String fileName) {
        String result = "";
        try {
            InputStream in = context.getResources().getAssets().open(fileName);
            // 获取文件的字节数
            int lenght = in.available();
            // 创建byte数组
            byte[] buffer = new byte[lenght];
            // 将文件中的数据读到byte数组中
            in.read(buffer);
            result = new String(buffer);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }
}
