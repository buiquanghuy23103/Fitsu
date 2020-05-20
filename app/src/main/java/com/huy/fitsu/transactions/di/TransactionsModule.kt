package com.huy.fitsu.transactions.di

import androidx.lifecycle.ViewModel
import com.huy.fitsu.di.ViewModelKey
import com.huy.fitsu.transactions.TransactionsViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TransactionsModule {

    @Binds
    @IntoMap
    @ViewModelKey(TransactionsViewModel::class)
    abstract fun viewModel(viewModel: TransactionsViewModel): ViewModel

}