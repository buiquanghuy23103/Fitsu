package com.huy.fitsu.dashboard

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.espresso.matcher.ViewMatchers.withText
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.huy.fitsu.BaseTest
import com.huy.fitsu.R
import com.huy.fitsu.data.local.DEFAULT_ACCOUNT_BALANCE
import com.huy.fitsu.util.toCurrencyString
import org.junit.Assert.*
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class DashboardFragmentTest : BaseTest<DashboardFragment>() {

    override fun launchFragment(): FragmentScenario<DashboardFragment> =
        launchFragmentInContainer(null, R.style.Theme_Fitsu)

    @Test
    fun shouldShowDefaultAccountBalance() {
        launchFragment()

        onView(withId(R.id.dashboard_account_text))
            .check(matches(withText(DEFAULT_ACCOUNT_BALANCE.toCurrencyString())))
    }
}