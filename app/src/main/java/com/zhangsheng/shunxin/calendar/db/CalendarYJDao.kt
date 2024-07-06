package com.zhangsheng.shunxin.calendar.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query

@Dao
interface CalendarYJDao {

    @Query("SELECT * FROM YJData WHERE jx LIKE :jx AND gz LIKE :gz")
    suspend fun query(jx: String, gz: String): List<CalendarYJData>

    @Query("SELECT * FROM YJData ")
    fun queryAll(): LiveData<List<CalendarYJData>>
}