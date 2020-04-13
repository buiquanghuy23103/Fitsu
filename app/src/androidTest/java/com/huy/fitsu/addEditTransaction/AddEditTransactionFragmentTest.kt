package com.huy.fitsu.addEditTransaction

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onData
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.action.ViewActions.replaceText
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.huy.fitsu.FitsuApplication
import com.huy.fitsu.R
import com.huy.fitsu.childAtPosition
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.repository.CategoryRepository
import com.huy.fitsu.data.repository.TransactionRepository
import com.huy.fitsu.util.DateConverter
import kotlinx.coroutines.runBlocking
import org.hamcrest.CoreMatchers.allOf
import org.hamcrest.CoreMatchers.anything
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify


@RunWith(AndroidJUnit4::class)
class AddEditTransactionFragmentTest {


    private lateinit var transactionRepository: TransactionRepository
    private lateinit var categoryRepository: CategoryRepository

    private val testCategory = Category("A Test")
    private val testCategory2 = Category("Food")
    private val testTransaction = Transaction(value = 100, categoryId = testCategory.id)

    @Before
    fun setup() {
        val appComponent = ApplicationProvider.getApplicationContext<FitsuApplication>()
            .appComponent
        transactionRepository = appComponent.transactionRepository
        categoryRepository = appComponent.categoryRepository

        runBlocking {
            categoryRepository.insertNewCategory(testCategory)
            categoryRepository.insertNewCategory(testCategory2)
            transactionRepository.insertNewTransaction(testTransaction)
        }
    }


    @After
    fun tearDown() = runBlocking {
        categoryRepository.deleteAllCategories()
        transactionRepository.deleteAllTransactions()
    }

    @Test
    fun displayTransactionDetails() {
        val dateString = DateConverter.dateToString(testTransaction.date)

        launchFragment()

        onView(withId(R.id.transaction_value_edit_text))
            .check(matches(withText(testTransaction.value.toString())))
        onView(withId(R.id.transaction_date_button))
            .check(matches(withText(dateString)))
        onView(withId(R.id.transaction_category_button))
            .check(matches(withText(testCategory.title)))
    }

    @Test
    fun updateTransaction_shouldNavigate_toDashboardFragment() {
        val navController = mock(NavController::class.java)
        launchFragment().onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(R.id.transaction_update_button))
            .perform(click())

        verify(navController).navigateUp()
    }
@Test
    fun deleteTransaction_shouldNavigateUp() {
        val navController = mock(NavController::class.java)
        launchFragment().onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(R.id.transaction_delete_button))
            .perform(click())

        verify(navController).navigateUp()
    }

    @Test
    fun updateTransaction_shouldDisplayOnDashboard() {
        val navController = mock(NavController::class.java)
        launchFragment().onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        // Change transaction value
        onView(withId(R.id.transaction_value_edit_text))
            .perform(replaceText("123"))


        // Change transaction's category
        onView(withId(R.id.transaction_category_button))
            .perform(click())
        val appCompatCheckedTextView = onData(anything())
            .inAdapterView(
                allOf(
                    withId(R.id.select_dialog_listview),
                    childAtPosition(
                        withId(R.id.contentPanel),
                        1
                    )
                )
            )
            .atPosition(0)
        appCompatCheckedTextView.perform(click())
        onView(withId(android.R.id.button1))
            .perform(click())

        // Click "Update" fab
        onView(withId(R.id.transaction_update_button))
            .perform(click())

        onView(withText(testCategory2.title))
            .check(matches(isDisplayed()))

    }

    private fun launchFragment(): FragmentScenario<AddEditTransactionFragment> {
        val bundle = AddEditTransactionFragmentArgs(testTransaction.id).toBundle()
        return launchFragmentInContainer<AddEditTransactionFragment>(bundle, R.style.AppTheme)
    }
}