package com.huy.fitsu

import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider
import com.huy.fitsu.data.local.FitsuSharedPrefManager
import com.huy.fitsu.data.local.database.CategoryDao
import com.huy.fitsu.data.local.database.TransactionDao
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.di.AppComponent
import kotlinx.coroutines.runBlocking
import org.mockito.Mockito

abstract class BaseTest<T: Fragment> {

    abstract fun launchFragment(): FragmentScenario<T>

    private lateinit var appComponent: AppComponent
    private lateinit var transactionDao: TransactionDao
    private lateinit var categoryDao: CategoryDao
    private lateinit var fitsuSharedPrefManager: FitsuSharedPrefManager

    protected val testCategory = Category(title = "A Test")
    protected val testTransaction = Transaction(value = 100f, categoryId = testCategory.id)

    protected fun baseSetup() {
        val appComponent = ApplicationProvider.getApplicationContext<FitsuApplication>()
            .appComponent

        val db = appComponent.db
        transactionDao = db.transactionDao()
        categoryDao = db.categoryDao()
        fitsuSharedPrefManager = appComponent.fitsuSharedPrefManager


        runBlocking {
            categoryDao.insert(testCategory)
            transactionDao.insert(testTransaction)
            fitsuSharedPrefManager.reset()
        }
    }

    protected fun baseTearDown() = runBlocking {
        categoryDao.deleteAll()
        transactionDao.deleteAll()
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