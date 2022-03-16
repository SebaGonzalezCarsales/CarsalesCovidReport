package com.gonzalezcs.coviddatemvvm.di

import android.content.Context
import com.gonzalezcs.coviddatemvvm.ui.view.MainActivity
import com.gonzalezcs.coviddatemvvm.ui.view.SplashActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

//dagger usará AplicationModule para saber como crear objetos
@Singleton
@Component(
    modules = [ApplicationModule::class]
)

interface ApplicationComponent {

    //aquí inject activitys and fragments
    fun inject(mainActivity: MainActivity)
    fun inject(splashActivity: SplashActivity)


    // method create() is the one we'll use to create an instance of ApplicationComponent
    // Context is needed to create instances of MyDatabase
    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}