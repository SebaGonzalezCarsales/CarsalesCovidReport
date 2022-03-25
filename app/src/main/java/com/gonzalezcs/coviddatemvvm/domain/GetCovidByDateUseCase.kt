package com.gonzalezcs.coviddatemvvm.domain

import com.gonzalezcs.coviddatemvvm.data.Repository.CovidDateRepository
import com.gonzalezcs.coviddatemvvm.data.model.DataCovidModel
import javax.inject.Inject

class GetCovidByDateUseCase @Inject constructor(private val covidDateRepository: CovidDateRepository){
    suspend fun getCovidByDate(date:String): DataCovidModel?{
        return covidDateRepository.getRepoCovidByDate(date)
    }
}