package com.zhangsheng.shunxin.weather.db.city_db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * @Description:
 * @Author:         Qrh
 * @CreateDate:     2019/10/29 17:33
 */
@Entity(tableName = "CITY")
class DbCityBean {

    @PrimaryKey
    @ColumnInfo(name = "ID")
    var id: Int? = 0
    @ColumnInfo(name = "CODE")
    var code: String? = ""
    @ColumnInfo(name = "NAME_EN")
    var name_en: String? = ""
    @ColumnInfo(name = "NAME_CN")
    var name_cn: String? = ""
    @ColumnInfo(name = "NAME_SEARCH_CN")
    var name_search_cn: String? = ""
    @ColumnInfo(name = "DISTRICT_CN")
    var district_cn: String? = ""
    @ColumnInfo(name = "DISTRICT_EN")
    var district_en: String? = ""
    @ColumnInfo(name = "DISTRICT_SEARCH_CN")
    var district_search_cn: String? = ""
    @ColumnInfo(name = "PROVCN_SEARCH_CN")
    var provcn_search_cn: String? = ""
    @ColumnInfo(name = "NATION_EN")
    var nation_en: String? = ""
    @ColumnInfo(name = "NATION_CN")
    var nation_cn: String? = ""
    @ColumnInfo(name = "PROV_EN")
    var prov_en: String? = ""
    @ColumnInfo(name = "PROV_CN")
    var prov_cn: String? = ""
    @ColumnInfo(name = "CONTINENT_EN")
    var continent_en: String? = ""
    @ColumnInfo(name = "CONTINENT_CN")
    var continent_cn: String? = ""
    @ColumnInfo(name = "LNG")
    var lng: String? = ""
    @ColumnInfo(name = "LAT")
    var lat: String? = ""

}