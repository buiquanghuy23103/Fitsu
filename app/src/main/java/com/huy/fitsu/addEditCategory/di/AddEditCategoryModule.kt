package com.huy.fitsu.addEditCategory.di

import androidx.lifecycle.ViewModel
import com.huy.fitsu.addEditCategory.AddEditCategoryViewModel
import com.huy.fitsu.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AddEditCategoryModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddEditCategoryViewModel::class)
    abstract fun viewModel(viewModel: AddEditCategoryViewModel): ViewModel

}