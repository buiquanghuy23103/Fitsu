package com.huy.fitsu.di

import android.content.Context
import com.huy.fitsu.addEditCategory.di.AddEditCategoryComponent
import com.huy.fitsu.addEditTransaction.di.AddEditTransactionComponent
import com.huy.fitsu.budgetHistory.di.BudgetHistoryComponent
import com.huy.fitsu.categories.di.CategoriesComponent
import com.huy.fitsu.data.repository.CategoryRepository
import com.huy.fitsu.data.repository.RepositoryModule
import com.huy.fitsu.data.repository.TransactionRepository
import com.huy.fitsu.scheduler.SchedulerModule
import com.huy.fitsu.transactionHistory.di.TransactionHistoryComponent
import dagger.BindsInstance
import dagger.Component
import dagger.Module
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        LocalStorageModule::class,
        SubComponentModule::class,
        ViewModelBuilderModule::class,
        RepositoryModule::class,
        SchedulerModule::class,
        DispatcherModule::class
    ]
)
interface AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

    fun categoriesComponent(): CategoriesComponent.Factory
    fun addEditCategoryComponent(): AddEditCategoryComponent.Factory
    fun dashboardComponent(): BudgetHistoryComponent.Factory
    fun addEditTransactionComponent(): AddEditTransactionComponent.Factory
    fun transactionHistoryComponent(): TransactionHistoryComponent.Factory

    val categoryRepository : CategoryRepository
    val transactionRepository: TransactionRepository

}

@Module(
    subcomponents = [
        CategoriesComponent::class,
        AddEditCategoryComponent::class,
        BudgetHistoryComponent::class,
        TransactionHistoryComponent::class
    ]
)
object SubComponentModule