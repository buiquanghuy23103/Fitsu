package com.huy.fitsu.addEditTransaction

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.huy.fitsu.BaseTest
import com.huy.fitsu.R
import com.huy.fitsu.util.DateConverter
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify


@RunWith(AndroidJUnit4::class)
class AddEditTransactionFragmentTest : BaseTest<AddEditTransactionFragment>() {

    override fun launchFragment(): FragmentScenario<AddEditTransactionFragment> {
        val bundle = AddEditTransactionFragmentArgs(testTransaction.id).toBundle()
        return launchFragmentInContainer<AddEditTransactionFragment>(bundle, R.style.Theme_Fitsu)
    }

    @Before
    fun setUp() {
        baseSetup()
    }

    @After
    fun tearDown() {
        baseTearDown()
    }

    @Test
    fun displayTransactionDetails() {
        val dateString = DateConverter.localDateToString(testTransaction.createdAt)

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
        val navController = launchFragmentWithMockNavController()

        onView(withId(R.id.transaction_update_button))
            .perform(click())

        verify(navController).navigateUp()
    }

    @Test
    fun deleteTransaction_shouldNavigateUp() {
        val navController = launchFragmentWithMockNavController()

        onView(withId(R.id.transaction_delete_button))
            .perform(click())

        verify(navController).navigateUp()
    }

}