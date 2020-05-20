package com.huy.fitsu.di

import com.huy.fitsu.addEditCategory.AddEditCategoryRepository
import com.huy.fitsu.addEditCategory.DefaultAddEditCategoryRepository
import com.huy.fitsu.addEditTransaction.AddEditTransactionRepository
import com.huy.fitsu.addEditTransaction.DefaultAddEditTransactionRepository
import com.huy.fitsu.budgets.BudgetsRepository
import com.huy.fitsu.budgets.DefaultBudgetsRepository
import com.huy.fitsu.categories.CategoriesRepository
import com.huy.fitsu.categories.DefaultCategoriesRepository
import com.huy.fitsu.dashboard.DashboardRepository
import com.huy.fitsu.dashboard.DefaultDashboardRepository
import com.huy.fitsu.transactions.DefaultTransactionRepository
import com.huy.fitsu.transactions.TransactionsRepository
import dagger.Binds
import dagger.Module
import javax.inject.Singleton

@Module
abstract class RepositoryModule {

    @Singleton
    @Binds
    abstract fun addEditCategoryRepo(repo: DefaultAddEditCategoryRepository)
            : AddEditCategoryRepository

    @Singleton
    @Binds
    abstract fun addEditTransactionRepo(repo: DefaultAddEditTransactionRepository)
            : AddEditTransactionRepository

    @Singleton
    @Binds
    abstract fun budgetsRepo(repo: DefaultBudgetsRepository)
            : BudgetsRepository

    @Singleton
    @Binds
    abstract fun categoriesRepo(repo: DefaultCategoriesRepository)
            : CategoriesRepository

    @Singleton
    @Binds
    abstract fun dashboardRepo(repo: DefaultDashboardRepository)
            : DashboardRepository

    @Singleton
    @Binds
    abstract fun transactionRepo(repo: DefaultTransactionRepository)
            : TransactionsRepository

}