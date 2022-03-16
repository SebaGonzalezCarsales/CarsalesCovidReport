package com.gonzalezcs.coviddatemvvm

import android.app.Application
import com.gonzalezcs.coviddatemvvm.di.ApplicationComponent
import com.gonzalezcs.coviddatemvvm.di.DaggerApplicationComponent


class MyAppApplication: Application() {
    // Reference to the application graph that is used across the whole app
    val applicationComponent: ApplicationComponent by lazy {
        DaggerApplicationComponent.factory().create(applicationContext)
    }

}

/*fun getApiCovidData(date: String,  listener: CovidDateListener) {
    call.create(ApiCovidDateService::class.java).getApi(HEADER, date).enqueue(object :
        Callback<CovidDateModel?> {
        override fun onResponse(call: Call<CovidDateModel?>, response: Response<CovidDateModel?>) {
            listener.onSuccess(response.body())
        }
        override fun onFailure(call: Call<CovidDateModel?>, t: Throwable) {
            listener.onError(t)
        }
    });
}*/
