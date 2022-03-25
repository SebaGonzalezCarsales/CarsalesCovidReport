package com.gonzalezcs.coviddatemvvm.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.gonzalezcs.coviddatemvvm.data.Dao.DataCovidModelDao
import com.gonzalezcs.coviddatemvvm.data.model.DataCovidModel

@Database(entities = [DataCovidModel::class], version = 1)
abstract class AppDatabase : RoomDatabase() {
    abstract fun dataCovidModelDao(): DataCovidModelDao
}