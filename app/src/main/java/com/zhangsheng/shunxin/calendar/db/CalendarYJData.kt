package com.zhangsheng.shunxin.calendar.db

import androidx.room.ColumnInfo
import androidx.room.Entity


@Entity(tableName = "YJData", primaryKeys = ["jx", "gz"])
class CalendarYJData {

    @ColumnInfo(name = "jx")
    var jx: String = ""

    @ColumnInfo(name = "gz")
    var gz: String = ""

    @ColumnInfo(name = "ji")
    var ji: String? = ""

    @ColumnInfo(name = "yi")
    var yi: String? = ""
}