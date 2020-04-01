package com.huy.fitsu.addEditCategory.di

import dagger.Subcomponent

@Subcomponent(modules = [AddEditCategoryModule::class])
interface AddEditCategoryComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AddEditCategoryComponent
    }

}