package com.gonzalezcs.coviddatemvvm.data.Repository

import com.gonzalezcs.coviddatemvvm.data.LocalStorage.CovidDataLocalSource
import com.gonzalezcs.coviddatemvvm.data.model.DataCovidModel
import com.gonzalezcs.coviddatemvvm.data.network.CovidDateService
import javax.inject.Inject

class CovidDateRepository @Inject constructor(private val covidDateRemoteSource: CovidDateService, private val covidDataLocalSource: CovidDataLocalSource) {

    suspend fun getRepoCovidByDate(date: String):DataCovidModel?{
        val selectedObject = covidDataLocalSource.getSelectedDate(date)
        return if(selectedObject != null){
            selectedObject
        }else{
            val dataCovid = covidDateRemoteSource.getCovidData(date)
            if (dataCovid != null) {
                covidDataLocalSource.insertSelectedDate(dataCovid)
            }
            return dataCovid
        }
    }
}