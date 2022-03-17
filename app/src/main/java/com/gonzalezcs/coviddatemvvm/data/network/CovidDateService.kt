package com.gonzalezcs.coviddatemvvm.data.network

import com.gonzalezcs.coviddatemvvm.data.model.DataCovidModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.async
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.withContext
import okhttp3.Dispatcher
import javax.inject.Inject
import kotlin.concurrent.thread

class CovidDateService @Inject constructor(private val covidDateClient: CovidDateClient ){

    private val HEADER = "96afa298cbmsh913f910f914494cp110c39jsn01a32d68445e"
    //todo: como obtener un parametro desde activity o fragment here

    suspend fun getCovidData(date:String): DataCovidModel? {
        //thread secundary not interface
       return withContext(Dispatchers.IO){
           return@withContext try {
               val response = covidDateClient.getApi(HEADER,date)
               response.body()?.data
           } catch (e:Throwable){
               null
           }

        }
    }
}