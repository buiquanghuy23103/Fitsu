package com.huy.fitsu.addEditCategory

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.huy.fitsu.BaseTest
import com.huy.fitsu.R
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class AddEditCategoryFragmentTest : BaseTest<AddEditCategoryFragment>() {

    override fun launchFragment(): FragmentScenario<AddEditCategoryFragment> {
        val bundle = AddEditCategoryFragmentArgs(testCategory.id).toBundle()
        return launchFragmentInContainer<AddEditCategoryFragment>(bundle, R.style.Theme_Fitsu)
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
    fun deleteCategory_shouldNavigateUp() {
        val navController = launchFragmentWithMockNavController()

        onView(withId(R.id.category_delete_button))
            .perform(click())
        onView(withId(android.R.id.button1))
            .perform(click()) // Press "OK" button on the dialog

        verify(navController).navigateUp()
    }

    @Test
    fun saveCategory_shouldNavigateUp() {
        val navController = launchFragmentWithMockNavController()

        onView(withId(R.id.category_update_button))
            .perform(click())

        verify(navController).navigateUp()
    }

}