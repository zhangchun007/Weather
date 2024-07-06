package com.zhangsheng.shunxin.calendar.db

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "advices")
class CalendarAdvices {

    @PrimaryKey
    @ColumnInfo(name = "AutoCode")
    var autoCode: String = ""

    @ColumnInfo(name = "Code")
    var code: String? = ""

    @ColumnInfo(name = "dayGz")
    var dayGz: String? = ""

    @ColumnInfo(name = "fetus")
    var fetus: String? = ""

    @ColumnInfo(name = "favonian")
    var favonian: String? = ""

    @ColumnInfo(name = "terrible")
    var terrible: String? = ""
    override fun toString(): String {
        return "CalendarAdvices(autoCode='$autoCode', code=$code, dayGz=$dayGz, fetus=$fetus, favonian=$favonian, terrible=$terrible)"
    }
}