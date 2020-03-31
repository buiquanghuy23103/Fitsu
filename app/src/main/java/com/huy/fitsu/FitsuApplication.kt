package com.huy.fitsu

import android.app.Application
import com.huy.fitsu.di.AppComponent
import com.huy.fitsu.di.DaggerAppComponent
import timber.log.Timber

class FitsuApplication: Application() {

    val appComponent: AppComponent by lazy {
        initAppComponent()
    }

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

    private fun initAppComponent(): AppComponent {
        return DaggerAppComponent.factory().create(applicationContext)
    }

}