package com.huy.fitsu.addEditCategory.di

import com.huy.fitsu.addEditCategory.AddEditCategoryFragment
import dagger.Subcomponent

@Subcomponent(modules = [AddEditCategoryModule::class])
interface AddEditCategoryComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): AddEditCategoryComponent
    }

    fun inject(fragment: AddEditCategoryFragment)

}