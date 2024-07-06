package com.zhangsheng.shunxin.weather.utils

import android.content.Context
import android.text.TextUtils
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.maiya.thirdlibrary.ext.Try
import com.maiya.thirdlibrary.ext.listIndex
import com.maiya.thirdlibrary.ext.nN
import com.maiya.thirdlibrary.utils.CacheUtil
import com.zhangsheng.shunxin.information.bean.AllChannelBean
import com.zhangsheng.shunxin.information.bean.TabBean
import com.zhangsheng.shunxin.information.bean.TabData
import com.zhangsheng.shunxin.information.constant.Constants
import com.zhangsheng.shunxin.information.utils.AssetsUtils
import com.zhangsheng.shunxin.weather.net.bean.ControlBean
import org.apache.commons.lang3.StringEscapeUtils

/**
 * @Author:liupengbing
 * @Data: 2020/11/27 18:23
 * @Email:aliupengbing@163.com
 */
object NewsChannelDataUtils {

    fun getOldTabList(context: Context): List<String>? {
        var mList = mutableListOf<String>()
        //用户编辑本地频道
        var dataBean: List<TabBean>? = null
        var jsonString = CacheUtil.getString(Constants.SP_INFO_LOCAL_ORDER_CHANNEL, "")
        if (jsonString != null && jsonString.isNotEmpty()) {
            //去掉转义字符
            val data = StringEscapeUtils.unescapeJava(jsonString)
            //去掉首位""
            val data1 = data.substring(1, data.length - 1)

            try {
                dataBean =
                    Gson().fromJson(data1, object : TypeToken<ArrayList<TabBean>?>() {}.getType())
            } catch (e: Exception) {

            }
        }

        if (dataBean == null || dataBean?.size == 0) {

            //订阅频道
            var orderBean =
                CacheUtil.getObj(Constants.SP_INFO_ORDER_CHANNEL, ControlBean::class.java)
            if (orderBean?.info_stream == null || orderBean.info_stream.nN()
                    .listIndex(0).column == null || orderBean.info_stream.nN()
                    .listIndex(0).column?.size == 0
            ) {
                //本地默认
                try {
                    var tabJson: String = AssetsUtils.getFromAssets(
                        context,
                        "info_order_tab_data.json"
                    )

                    var tabData: TabData =
                        Gson().fromJson(tabJson, TabData::class.java) ?: return null
                    var tabList: List<TabBean> = tabData?.tab_list
                    if (tabList != null) {
                        for (i in tabList.indices) {
                            mList.add(tabList.nN().get(i).code)
                        }
                    }
                } catch (e: Exception) {
                }
                return mList

            } else {
                return orderBean.info_stream.nN().listIndex(0).column
            }

        } else {
            for (i in dataBean.indices) {
                mList.add(dataBean.nN()[i].code)
            }
            return mList
        }
        return null

    }

    fun getNewTabList(context: Context) : List<TabBean>?{
        var tabList = mutableListOf<TabBean>()
        //获取订阅频道code
        val orderList: List<String> = getOldTabList(context) ?: return tabList
        //根据code从全部频道查找title
        var allChannelBean =
            CacheUtil.getObj(Constants.SP_INFO_ALL_CHANNEL, AllChannelBean.DataBeanX::class.java)
        if (allChannelBean == null || allChannelBean.data == null) {
            //本地默认
            try {
                val tabJson = AssetsUtils.getFromAssets(context, "info_all_tab_data.json")
                val tabData: TabData = Gson().fromJson(tabJson, TabData::class.java)
                val allTabList = tabData?.tab_list
                if (allTabList != null && allTabList.size > 0) {
                    for (i in orderList.indices) {
                        for (j in allTabList.indices) {
                            if (orderList.nN().get(i).equals(allTabList.get(j).code)) {
                                var tabBean =
                                    TabBean()
                                tabBean?.code = allTabList[j].code
                                tabBean.position = j
                                if (tabBean?.code == "news_local") {
                                    if (!TextUtils.isEmpty(Constants.locationCity)) {
                                        tabBean?.title = Constants.locationCity
                                        Constants.city = Constants.locationCity
                                        tabList.add(tabBean)
                                    }
                                } else {
                                    tabBean?.title = allTabList.get(j).title
                                    tabList.add(tabBean)
                                }
                            }
                        }
                    }
                }
            } catch (e: Exception) {
            }
        } else {
            for (i in orderList.indices) {
                for (j in allChannelBean.data.indices) {
                    if (orderList.nN()[i] == allChannelBean.data.get(j).code) {
                        var tabBean =
                            TabBean()
                        tabBean?.code = allChannelBean.data[j].code
                        tabBean.position = j
                        if (tabBean?.code == "news_local") {
                            if (!TextUtils.isEmpty(Constants.locationCity)) {
                                tabBean?.title = Constants.locationCity
                                Constants.city = Constants.locationCity
                                tabList.add(tabBean)
                            }
                        } else {
                            tabBean?.title = allChannelBean.data.get(j).title
                            tabList.add(tabBean)
                        }
                    }
                }
            }
        }

        return tabList
    }
}