package com.huy.fitsu.data.repository

import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class FakeRepositoryModule {


    @Singleton
    @Binds
    abstract fun categoriesRepository(repository: FakeCategoryRepository): CategoryRepository

    @Singleton
    @Binds
    abstract fun transactionsRepository(repo: FakeTransactionRepository): TransactionRepository

    @Singleton
    @Binds
    abstract fun budgetRepository(repo: FakeBudgetRepository): BudgetRepository


}