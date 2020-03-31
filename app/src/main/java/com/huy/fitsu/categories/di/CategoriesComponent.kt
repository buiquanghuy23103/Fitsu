package com.huy.fitsu.categories.di

import com.huy.fitsu.categories.CategoriesFragment
import dagger.Subcomponent

@Subcomponent(modules = [CategoriesModule::class])
interface CategoriesComponent {

    @Subcomponent.Factory
    interface Factory {
        fun create(): CategoriesComponent
    }

    fun inject(fragment: CategoriesFragment)

}