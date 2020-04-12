package com.huy.fitsu.addEditTransaction.di

import com.huy.fitsu.addEditTransaction.AddEditTransactionFragment
import dagger.Subcomponent

@Subcomponent(
    modules = [AddEditTransactionModule::class]
)
interface AddEditTransactionComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AddEditTransactionComponent
    }

    fun inject(fragment: AddEditTransactionFragment)

}