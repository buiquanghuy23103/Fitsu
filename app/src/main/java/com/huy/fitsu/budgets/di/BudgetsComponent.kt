package com.huy.fitsu.budgets.di

import com.huy.fitsu.budgets.BudgetsFragment
import com.huy.fitsu.budgets.BudgetsViewPagerFragment
import dagger.Subcomponent

@Subcomponent(modules = [BudgetsModule::class])
interface BudgetsComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): BudgetsComponent
    }

    fun inject(fragment: BudgetsFragment)
    fun inject(fragment: BudgetsViewPagerFragment)

}