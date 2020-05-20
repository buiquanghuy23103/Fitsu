package com.huy.fitsu.transactions.di

import dagger.Subcomponent

@Subcomponent
interface TransactionsComponent {
    @Subcomponent.Factory
    interface Factory {
        fun create(): TransactionsComponent
    }
}