package com.zhangsheng.shunxin.weather.wallpaper

import androidx.annotation.Keep

/**
 * @Description:
 * @Author:         zhangchun
 * @CreateDate:     2021/6/15
 * @Version:        1.0
 */
@Keep
class WallPaperBean {
    //code参数 wallpaper_background 和  wallpaper_voice 请求下载壁纸。
    var wallPaperCode = ""

    //日期
    var timestamp = 0L

    //总的次数
    var totalCount = 0
}