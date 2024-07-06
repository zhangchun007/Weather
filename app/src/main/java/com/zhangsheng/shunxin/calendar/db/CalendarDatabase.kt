package com.zhangsheng.shunxin.calendar.db

import androidx.annotation.NonNull
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import com.maiya.thirdlibrary.utils.AppContext


@Database(
    entities = [
        CalendarYJData::class,
        CalendarAdvices::class,
        CalendarIndexTable::class],
    version = 17,
    exportSchema = false
)
abstract class CalendarDatabase : RoomDatabase() {

    abstract fun calenYjdao(): CalendarYJDao
    abstract fun calendarAdvicesDao(): CalendarAdvicesDao
    abstract fun calendarIndexTableDao(): CalendarIndexTableDao

    companion object {
        @Volatile
        private var Instance: CalendarDatabase? = null
        private const val DB_NAME = "calendar11.db"

        private fun get(): CalendarDatabase {
            if (null == Instance) {
                synchronized(CalendarDatabase::class.java) {
                    if (null == Instance) {
                        Instance = Room.databaseBuilder(
                            AppContext.getContext(), CalendarDatabase::class.java, DB_NAME
                        )
                            .addMigrations(MIGRATION_16_17)
                            .createFromAsset("calendar.db")
                            .build()
                    }
                }
            }
            return Instance!!
        }

        fun getInstance() = get()
        fun getCalendarYJDao() = get().calenYjdao()
        fun getCalendarAdvicesDao() = get().calendarAdvicesDao()
        fun getCalendarIndexTableDao() = get().calendarIndexTableDao()

        val MIGRATION_16_17: Migration = object : Migration(16, 17) {
            override fun migrate(@NonNull database: SupportSQLiteDatabase) {
                //执行升级相关操作
//                database.execSQL("ALTER TABLE IndexTable ADD COLUMN uid INTEGER")
//                database.execSQL("ALTER TABLE YJData  ADD COLUMN uid INT")
//                database.execSQL("ALTER TABLE advices  ADD COLUMN uid INT")
            }
        }
    }
}