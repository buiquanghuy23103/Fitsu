package com.huy.fitsu.dashboard.di

import androidx.lifecycle.ViewModel
import com.huy.fitsu.dashboard.DashboardViewModel
import com.huy.fitsu.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class DashboardModule {

    @Binds
    @IntoMap
    @ViewModelKey(DashboardViewModel::class)
    abstract fun viewModel(viewModel: DashboardViewModel): ViewModel

}