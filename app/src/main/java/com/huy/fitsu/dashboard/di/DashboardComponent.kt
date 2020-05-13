package com.huy.fitsu.dashboard.di

import com.huy.fitsu.dashboard.DashboardFragment
import dagger.Subcomponent

@Subcomponent(
    modules = [DashboardModule::class]
)
interface DashboardComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): DashboardComponent
    }

    fun inject(fragment: DashboardFragment)

}