package com.gonzalezcs.coviddatemvvm.data.network

import com.gonzalezcs.coviddatemvvm.data.model.CovidDateModel
import com.gonzalezcs.coviddatemvvm.data.model.DataCovidModel
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Headers
import retrofit2.http.Query


interface CovidDateClient {

    @GET("reports/total")
    @Headers("Content-Type: application/json")
    suspend fun getApi(@Header("X-RapidAPI-Key") apiKey: String,
                       @Query("date") date: String): Response<CovidDateModel>

}

