package com.huy.fitsu.budgetHistory.di

import com.huy.fitsu.budgetHistory.DashboardFragment
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