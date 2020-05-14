package com.huy.fitsu.transactionHistory

import android.os.Build
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavDirections
import androidx.navigation.Navigator
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.isDisplayed
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.huy.fitsu.BaseTest
import com.huy.fitsu.R
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito

@RunWith(AndroidJUnit4::class)
class TransactionHistoryFragmentTest : BaseTest<TransactionHistoryFragment>() {


    override fun launchFragment(): FragmentScenario<TransactionHistoryFragment> {
        return launchFragmentInContainer(null, R.style.Theme_Fitsu)
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
    fun shouldShowTransactionHistoryList() {
        launchFragment()

        onView(withId(R.id.transaction_history_list))
            .check(matches(isDisplayed()))
    }

    @Test
    fun clickTransactionItem_navigateToAddEditTransactionFragment() {
        val navController = launchFragmentWithMockNavController()

        onView(withId(R.id.transaction_history_list))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<TransactionHistoryAdapter.TransactionItem>(
                    0, ViewActions.click()
                )
            )

        val destination = TransactionHistoryFragmentDirections
            .toAddEditTransactionFragment(testTransaction.id)


        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            Mockito.verify(navController).navigate(
                Mockito.eq(destination),
                Mockito.any(Navigator.Extras::class.java)
            )
        } else {
            Mockito.verify(navController).navigate(Mockito.eq(destination))
        }

    }

    @Test
    fun clickAddTransactionFab_navigateToAddEditTransactionFragment() {
        val navController = launchFragmentWithMockNavController()

        onView(withId(R.id.transaction_history_add_trans_button))
            .perform(ViewActions.click())

        Mockito.verify(navController).navigate(Mockito.any<NavDirections>())
    }

}