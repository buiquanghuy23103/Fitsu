package com.huy.fitsu.budgets.di

import androidx.lifecycle.ViewModel
import com.huy.fitsu.budgets.BudgetsViewModel
import com.huy.fitsu.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class BudgetsModule {

    @Binds
    @IntoMap
    @ViewModelKey(BudgetsViewModel::class)
    abstract fun viewModel(viewModel: BudgetsViewModel): ViewModel

}