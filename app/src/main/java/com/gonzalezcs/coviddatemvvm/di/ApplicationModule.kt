package com.gonzalezcs.coviddatemvvm.di

import android.content.Context
import com.gonzalezcs.coviddatemvvm.data.model.network.database.DatabaseApp
import com.gonzalezcs.coviddatemvvm.data.network.CovidDateClient
import dagger.Module
import dagger.Provides
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton


// @Module informs Dagger that this class is a Dagger Module
@Module
class ApplicationModule {

    private val base_url = "https://covid-19-statistics.p.rapidapi.com/"

    // le indicamos a dagger como crear la instancia de servicios de librerias
    // estos se proveen al proyecto cuandos se requieren
    // singleton : unica instancia y no se vuelve a crear
    // use provide como prefijo
    //

    @Provides
    @Singleton
    fun provideDatabase(context: Context): DatabaseApp{
        return DatabaseApp(context)
    }

    @Provides
    @Singleton
    fun provideRetrofit(): Retrofit {
        val okhttpClient = OkHttpClient()
        return Retrofit.Builder().baseUrl(base_url)
            .addConverterFactory(GsonConverterFactory.create())
            .client(okhttpClient)
            .build()
    }


    @Singleton
    @Provides
    fun provideCovidDateClient(retrofit: Retrofit):CovidDateClient{
        return retrofit.create(CovidDateClient::class.java)
    }
}