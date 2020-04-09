package com.huy.fitsu.dashboard

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.huy.fitsu.FitsuApplication
import com.huy.fitsu.R
import com.huy.fitsu.categories.CategoriesAdapter
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.repository.CategoryRepository
import com.huy.fitsu.data.repository.TransactionRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class DashboardFragmentTests {

    private lateinit var transactionRepository: TransactionRepository
    private lateinit var categoryRepository: CategoryRepository

    private val testCategory = Category("A Test")
    private val testTransaction = Transaction(value = 100, categoryId = testCategory.id)

    @Before
    fun setup() {
        val appComponent = ApplicationProvider.getApplicationContext<FitsuApplication>()
            .appComponent
        transactionRepository = appComponent.transactionRepository
        categoryRepository = appComponent.categoryRepository

        runBlocking {
            categoryRepository.insertNewCategory(testCategory)
            transactionRepository.insertNewTransaction(testTransaction)
        }
    }

    @After
    fun tearDown() = runBlocking {
        categoryRepository.deleteAllCategories()
        transactionRepository.deleteAllTransactions()
    }

    @Test
    fun clickTransactionItem_navigateToAddEditTransactionFragment() {
        val navController = mock(NavController::class.java)
        launchFragment().onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(R.id.transaction_list))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<CategoriesAdapter.CategoryItem>(
                    0, click()
                )
            )

        verify(navController).navigate(
            DashboardFragmentDirections.toAddEditTransactionFragment(testTransaction.id)
        )
    }

    private fun launchFragment(): FragmentScenario<DashboardFragment> {
        return launchFragmentInContainer<DashboardFragment>(null, R.style.AppTheme)
    }

}