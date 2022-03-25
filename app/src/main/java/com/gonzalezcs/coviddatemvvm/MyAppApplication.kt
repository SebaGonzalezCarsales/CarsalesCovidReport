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