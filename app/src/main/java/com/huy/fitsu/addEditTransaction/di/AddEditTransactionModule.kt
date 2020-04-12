package com.huy.fitsu.addEditTransaction.di

import androidx.lifecycle.ViewModel
import com.huy.fitsu.addEditTransaction.AddEditTransactionViewModel
import com.huy.fitsu.di.ViewModelKey
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class AddEditTransactionModule {

    @Binds
    @IntoMap
    @ViewModelKey(AddEditTransactionViewModel::class)
    abstract fun viewModel(viewModel: AddEditTransactionViewModel): ViewModel

}