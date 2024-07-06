package com.zhangsheng.shunxin.calendar.db

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.zhangsheng.shunxin.calendar.db.CalendarAdvices

@Dao
interface CalendarAdvicesDao {

    @Query("SELECT * FROM advices ")
    fun queryAll(): LiveData<List<CalendarAdvices>>

    @Query("SELECT * FROM advices WHERE Code LIKE :code AND dayGz LIKE :daygz")
    suspend fun query(code: String, daygz: String): List<CalendarAdvices>
}