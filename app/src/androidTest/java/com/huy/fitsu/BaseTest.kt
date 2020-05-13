package com.huy.fitsu

import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.huy.fitsu.data.local.FitsuSharedPrefManager
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.repository.CategoryRepository
import com.huy.fitsu.data.repository.TransactionRepository
import com.huy.fitsu.di.AppComponent
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.mockito.Mockito

abstract class BaseTest<T: Fragment> {

    abstract fun launchFragment(): FragmentScenario<T>

    private lateinit var appComponent: AppComponent
    private lateinit var transactionRepository: TransactionRepository
    private lateinit var categoryRepository: CategoryRepository
    private lateinit var fitsuSharedPrefManager: FitsuSharedPrefManager

    protected val testCategory = Category(title = "A Test")
    protected val testTransaction = Transaction(value = 100f, categoryId = testCategory.id)

    protected fun baseSetup() {
        val appComponent = ApplicationProvider.getApplicationContext<FitsuApplication>()
            .appComponent
        transactionRepository = appComponent.transactionRepository
        categoryRepository = appComponent.categoryRepository
        fitsuSharedPrefManager = appComponent.fitsuSharedPrefManager


        runBlocking {
            categoryRepository.insertNewCategory(testCategory)
            transactionRepository.insertNewTransaction(testTransaction)
            fitsuSharedPrefManager.reset()
        }
    }

    protected fun baseTearDown() = runBlocking {
        categoryRepository.deleteAllCategories()
        transactionRepository.deleteAllTransactions()
        fitsuSharedPrefManager.reset()
    }

    protected fun launchFragmentWithMockNavController(): NavController {
        val navController = Mockito.mock(NavController::class.java)
        launchFragment().onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }
        return navController
    }

}