package com.huy.fitsu

import com.huy.fitsu.di.AppComponent
import com.huy.fitsu.di.DaggerTestAppComponent

class TestApplication: FitsuApplication() {

    override fun initAppComponent(): AppComponent {
        return DaggerTestAppComponent.factory().create(applicationContext)
    }

}