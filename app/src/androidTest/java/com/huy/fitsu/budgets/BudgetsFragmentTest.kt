package com.huy.fitsu.budgets

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.huy.fitsu.BaseTest
import com.huy.fitsu.R
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class BudgetsFragmentTest : BaseTest<BudgetsFragment>() {


    override fun launchFragment(): FragmentScenario<BudgetsFragment> {
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

}