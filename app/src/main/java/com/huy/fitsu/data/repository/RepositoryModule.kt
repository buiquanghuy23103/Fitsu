package com.huy.fitsu.data.repository

import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun categoryRepository(repo: CategoryRepositoryImpl): CategoryRepository

    @Singleton
    @Binds
    abstract fun transactionRepository(repo: TransactionRepositoryImpl): TransactionRepository

    @Singleton
    @Binds
    abstract fun dashboardRepository(repo: DashboardRepositoryImpl): DashboardRepository

}