package com.huy.fitsu

import android.app.Application
import timber.log.Timber

class FitsuApplication: Application() {

    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) Timber.plant(Timber.DebugTree())
    }

}