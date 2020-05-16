package com.huy.fitsu.data.repository

import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun categoriesRepository(repository: CategoryRepositoryImpl): CategoryRepository

    @Singleton
    @Binds
    abstract fun transactionsRepository(repo: TransactionRepositoryImpl): TransactionRepository

}