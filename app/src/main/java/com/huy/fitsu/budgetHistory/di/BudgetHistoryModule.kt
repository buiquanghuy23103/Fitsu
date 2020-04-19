package com.huy.fitsu.budgetHistory.di

import androidx.lifecycle.ViewModel
import com.huy.fitsu.budgetHistory.DashboardViewModel
import com.huy.fitsu.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class BudgetHistoryModule {

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    abstract fun viewModel(viewModel: DashboardViewModel): ViewModel

}