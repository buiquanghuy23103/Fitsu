package com.huy.fitsu.transactionHistory.di

import com.huy.fitsu.transactionHistory.TransactionHistoryFragment
import dagger.Subcomponent

@Subcomponent(modules = [TransactionHistoryModule::class])
interface TransactionHistoryComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): TransactionHistoryComponent
    }

    fun inject(fragment: TransactionHistoryFragment)

}