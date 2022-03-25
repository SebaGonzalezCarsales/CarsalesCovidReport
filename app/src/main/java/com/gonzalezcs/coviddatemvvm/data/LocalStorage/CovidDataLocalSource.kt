package com.gonzalezcs.coviddatemvvm.data.LocalStorage

import com.gonzalezcs.coviddatemvvm.data.AppDatabase
import com.gonzalezcs.coviddatemvvm.data.model.DataCovidModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CovidDataLocalSource @Inject constructor(private val database: AppDatabase) {

    suspend fun getSelectedDate(selectedDate: String): DataCovidModel?{
        return withContext(Dispatchers.IO){
            return@withContext try {
                val covidDao = database.dataCovidModelDao()
                covidDao.findByDate(selectedDate)
            } catch (e:Throwable){
                null
            }
        }
    }

    suspend fun insertSelectedDate(selectedObject: DataCovidModel){
        withContext(Dispatchers.IO){
            return@withContext try {
                val covidDao = database.dataCovidModelDao()
                covidDao.insertAll(selectedObject)
            } catch (e:Throwable){
                null
            }
        }
    }
}