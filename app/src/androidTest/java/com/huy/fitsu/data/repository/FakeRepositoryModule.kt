package com.huy.fitsu.data.repository

import dagger.Binds
import dagger.Module

@Module
abstract class FakeRepositoryModule {


    @Binds
    abstract fun categoriesRepository(repository: FakeCategoryRepository): CategoryRepository

    @Binds
    abstract fun transactionsRepository(repo: TransactionRepositoryImpl): TransactionRepository


}