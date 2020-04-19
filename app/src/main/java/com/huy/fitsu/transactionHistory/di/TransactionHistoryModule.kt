package com.huy.fitsu.transactionHistory.di

import androidx.lifecycle.ViewModel
import com.huy.fitsu.di.ViewModelKey
import com.huy.fitsu.transactionHistory.TransactionHistoryViewModel
import dagger.Binds
import dagger.Module
import dagger.multibindings.IntoMap

@Module
abstract class TransactionHistoryModule {

    @Binds
    @IntoMap
    @ViewModelKey(TransactionHistoryViewModel::class)
    abstract fun viewModel(viewModel: TransactionHistoryViewModel): ViewModel

}