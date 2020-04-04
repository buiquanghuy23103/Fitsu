package com.huy.fitsu.data.repository

import dagger.Binds
import dagger.Module

@Module
abstract class RepositoryModule {

    @Binds
    abstract fun categoriesRepository(repository: CategoryRepositoryImpl): CategoryRepository

}