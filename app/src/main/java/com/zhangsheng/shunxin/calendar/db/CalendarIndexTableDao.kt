package com.zhangsheng.shunxin.calendar.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.zhangsheng.shunxin.calendar.db.CalendarIndexTable

@Dao
interface CalendarIndexTableDao {

    @Query("SELECT * FROM IndexTable WHERE _Date LIKE :data")
    suspend fun query(data: String): List<CalendarIndexTable>

    @Query("SELECT * FROM IndexTable ")
    fun queryAll(): LiveData<List<CalendarIndexTable>>
}