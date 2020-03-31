package com.huy.fitsu.categories.di

import androidx.lifecycle.ViewModel
import com.huy.fitsu.categories.CategoriesViewModel
import com.huy.fitsu.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class CategoriesModule {

    @Binds
    @IntoMap
    @ViewModelKey(CategoriesViewModel::class)
    abstract fun viewModel(viewModel: CategoriesViewModel): ViewModel

}