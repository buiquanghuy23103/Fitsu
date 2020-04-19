package com.huy.fitsu.budgetHistory.di

import com.huy.fitsu.budgetHistory.DashboardFragment
import dagger.Subcomponent

@Subcomponent(
    modules = [BudgetHistoryModule::class]
)
interface BudgetHistoryComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): BudgetHistoryComponent
    }

    fun inject(fragment: DashboardFragment)

}