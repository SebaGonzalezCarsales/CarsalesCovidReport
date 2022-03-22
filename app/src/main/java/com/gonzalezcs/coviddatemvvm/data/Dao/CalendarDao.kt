package com.gonzalezcs.coviddatemvvm.data.Dao
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.gonzalezcs.coviddatemvvm.data.model.DataCovidModel

@Dao
interface DataCovidModelDao {
    @Query("SELECT * FROM dataCovidModel")
    fun getAll(): List<DataCovidModel>

    @Insert
    fun insertAll(vararg dataCovid: DataCovidModel)

    @Query("SELECT * FROM dataCovidModel WHERE date = :selectedDate ")
    fun findByDate(selectedDate: String): DataCovidModel
}
