package com.zhangsheng.shunxin.weather.db.city_db

import androidx.room.ColumnInfo
import androidx.room.Entity

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2019/11/4 10:06
 */

@Entity
class InnerJoinResult {
    @ColumnInfo(name = "CODE")
    var code: String? = ""

    @ColumnInfo(name = "NAME_SEARCH_CN")
    var name_search_cn: String? = ""

    @ColumnInfo(name = "DISTRICT_SEARCH_CN")
    var district_search_cn: String? = ""

    @ColumnInfo(name = "NAME_CN")
    var name_cn: String? = ""

    @ColumnInfo(name = "DISTRICT_CN")
    var district_cn: String? = ""

    @ColumnInfo(name = "PROV_CN")
    var prov_cn: String? = ""
    override fun toString(): String {
        return "InnerJoinResult(code=$code, name_search_cn=$name_search_cn, district_search_cn=$district_search_cn, name_cn=$name_cn, district_cn=$district_cn, prov_cn=$prov_cn)"
    }


}