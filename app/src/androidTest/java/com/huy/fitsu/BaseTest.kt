package com.huy.fitsu

import androidx.fragment.app.Fragment
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import org.mockito.Mockito

abstract class BaseTest<T: Fragment> {

    abstract fun launchFragment(): FragmentScenario<T>

    protected fun launchFragmentWithMockNavController(): NavController {
        val navController = Mockito.mock(NavController::class.java)
        launchFragment().onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }
        return navController
    }

}