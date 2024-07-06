package com.zhangsheng.shunxin.weather.db.city_db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface CityDao {

    @Query("SELECT * FROM CITY WHERE NAME_SEARCH_CN LIKE :str")
    fun selectCityZ(str: String): LiveData<List<InnerJoinResult>>

    @Query("SELECT * FROM CITY WHERE NAME_SEARCH_CN LIKE :str")
    suspend fun selectCityWidgetZ(str: String): List<InnerJoinResult>

    @Query("SELECT * from CITY where NAME_EN like :str")
    fun selectCityE(str: String): LiveData<List<InnerJoinResult>>

    @Query("SELECT * from CITY where PROV_EN like :str")
    fun selectProvE(str: String): LiveData<List<InnerJoinResult>>


    @Query("SELECT * from CITY where PROVCN_SEARCH_CN like :str")
    fun selectProvZ(str: String): LiveData<List<InnerJoinResult>>

    @Query("SELECT * from CITY where DISTRICT_CN like :str")
    fun queryCityDistrict(str: String): LiveData<List<InnerJoinResult>>

    @Query("SELECT * from CITY where PROV_CN like :str")
    fun queryCityProvcn(str: String): LiveData<List<InnerJoinResult>>
}