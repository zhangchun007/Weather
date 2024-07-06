package com.zhangsheng.shunxin.weather.bottom

import com.maiya.thirdlibrary.ext.listIndex
import com.maiya.thirdlibrary.ext.parseInt
import com.maiya.thirdlibrary.utils.AppContext
import com.meituan.android.walle.WalleChannelReader
import com.zhangsheng.shunxin.weather.ext.getAppControl
import com.zhangsheng.shunxin.weather.ext.isControl
import com.zhangsheng.shunxin.weather.ext.isControlShow

/**
 * 说明：底部tab管理
 * 作者：刘鹏
 * 添加时间：2021/4/28 15:51
 * 修改人：liupe
 * 修改时间：2021/4/28 15:51
 */
class BottomItemManage private constructor(){

    companion object {
        val instance: BottomItemManage by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) {
            BottomItemManage()
        }

    }

    /**
     * 说明：初始化默认的底部tab配置
     * 作者：刘鹏
     * 添加时间：2021/4/28 15:53
     * 修改人：liupe
     * 修改时间：2021/4/28 15:53
     */
    fun initBottomBarList() : List<BottomBarItem> {
        // 默认配置
        val items = ArrayList<BottomBarItem>()
        for (cmd in BottomTabConfig.bottomTabConfigs) {
            if (isControl()) {
                // 审核控制，根据打包时的配置判断是否展示
                if (!checkPackageOpen(cmd)) {
                    // 不展示
                    continue
                }
            } else {
                // 非审核控制，根据云控数据判断是否展示
                if (!checkControlOpen(cmd)) {
                    // 不展示
                    continue
                }
            }
            // 展示该tab，创建对应的item
            BottomTabConfig.createBottomBarItem(cmd)?.let {
                it.setPosition(items.size)
                items.add(it)
            }
        }
        return items
    }

    /**
     * 说明：云控tab配置，根据tab名字，判断该tab是否展示
     * 作者：刘鹏
     * 添加时间：2021/4/28 15:56
     * 修改人：liupe
     * 修改时间：2021/4/28 15:56
     */
    private fun checkControlOpen(tag: String): Boolean {
        val controlStr = when (tag) {
            BottomBarItem.CMD_STREAM -> getAppControl().swtich_all.listIndex(0).infostreamtap
            BottomBarItem.CMD_VIDEO -> getAppControl().swtich_all.listIndex(0).smallvideo
            BottomBarItem.CMD_CALENDAR -> getAppControl().swtich_all.listIndex(0).calendar
            BottomBarItem.CMD_SETTING -> getAppControl().swtich_all.listIndex(0).settap
            BottomBarItem.CMD_TOOLBOX -> getAppControl().swtich_all.listIndex(0).tabfuntion
            else -> ""
        }
        // 天气tab一定展示
        if (BottomBarItem.CMD_WEATHER == tag) {
            return true
        }
        return if (controlStr.isEmpty()) false else isControlShow(controlStr)
    }


    /**
     * 说明：打包tab配置，根据tab名字，判断该tab是否展示
     * 作者：刘鹏
     * 添加时间：2021/4/28 15:57
     * 修改人：liupe
     * 修改时间：2021/4/28 15:57
     */
    private fun checkPackageOpen(id: String): Boolean {
        return WalleChannelReader.get(AppContext.getContext(), id).parseInt(2) == 1
    }

}