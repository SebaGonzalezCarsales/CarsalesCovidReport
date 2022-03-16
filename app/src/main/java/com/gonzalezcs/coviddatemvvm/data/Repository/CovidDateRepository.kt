package com.gonzalezcs.coviddatemvvm.data.Repository

import com.gonzalezcs.coviddatemvvm.data.model.DataCovidModel
import com.gonzalezcs.coviddatemvvm.data.model.network.database.DatabaseApp
import com.gonzalezcs.coviddatemvvm.data.network.CovidDateService
import javax.inject.Inject

class CovidDateRepository
        @Inject constructor(private val covidDateLocalSource: DatabaseApp,
                            private val covidDateRemoteSource: CovidDateService) {

        suspend fun getRepoCovidByDate(date: String):DataCovidModel?{
            //if no db call service
            return covidDateRemoteSource.getCovidData(date)
        }
}