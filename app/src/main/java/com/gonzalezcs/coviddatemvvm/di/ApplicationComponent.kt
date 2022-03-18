package com.gonzalezcs.coviddatemvvm.di

import android.content.Context
import com.gonzalezcs.coviddatemvvm.ui.view.MainActivity
import com.gonzalezcs.coviddatemvvm.ui.view.SplashActivity
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [ApplicationModule::class]
)

interface ApplicationComponent {

    fun inject(mainActivity: MainActivity)
    fun inject(splashActivity: SplashActivity)

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): ApplicationComponent
    }
}