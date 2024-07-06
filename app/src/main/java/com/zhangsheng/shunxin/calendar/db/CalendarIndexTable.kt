package com.zhangsheng.shunxin.calendar.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "IndexTable")
class CalendarIndexTable {

    @PrimaryKey
    @ColumnInfo(name = "_Date")
    var data: String = ""

    @ColumnInfo
    var jx: String? = ""

    @ColumnInfo
    var gz: String? = ""
}