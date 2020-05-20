package com.huy.fitsu.transactions.di

import com.huy.fitsu.transactions.TransactionsFragment
import dagger.Subcomponent

@Subcomponent(modules = [TransactionsModule::class])
interface TransactionsComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): TransactionsComponent
    }


    fun inject(fragment: TransactionsFragment)
}