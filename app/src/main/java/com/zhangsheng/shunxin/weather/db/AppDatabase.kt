package com.zhangsheng.shunxin.weather.db

import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.maiya.thirdlibrary.utils.AppContext
import com.zhangsheng.shunxin.weather.db.city_db.CityDao
import com.zhangsheng.shunxin.weather.db.city_db.DbCityBean

@Database(entities = [DbCityBean::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun cityDao(): CityDao

    companion object {
        @Volatile
        private var Instance: AppDatabase? = null

        private fun get(): AppDatabase {
            if (null == Instance) {
                synchronized(AppDatabase::class.java) {
                    if (null == Instance) {
                        Instance = Room.databaseBuilder(
                            AppContext.getContext(),
                            AppDatabase::class.java,
                            "weather"
                        )
                            .createFromAsset("cbcaiyun.db")
                            .build()
                    }
                }
            }
            return Instance!!
        }

        fun getInstance() = get()
        fun getCityDao() = get().cityDao()


    }
}