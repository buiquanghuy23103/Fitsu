package com.huy.fitsu.addEditCategory

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.matcher.ViewMatchers.withId
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.huy.fitsu.FitsuApplication
import com.huy.fitsu.R
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.repository.CategoryRepository
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class AddEditCategoryFragmentTest {

    private lateinit var categoryRepository: CategoryRepository

    private val testCategory = Category("A Test")

    @Before
    fun setUp() {
        val appComponent = ApplicationProvider.getApplicationContext<FitsuApplication>()
            .appComponent
        categoryRepository = appComponent.categoryRepository

        runBlocking {
            categoryRepository.insertNewCategory(testCategory)
        }
    }

    @Test
    fun deleteCategory_shouldNavigateUp() {
        val navController = mock(NavController::class.java)
        launchFragment().onFragment {
            Navigation.setViewNavController(it.requireView(), navController)
        }

        onView(withId(R.id.category_delete_button))
            .perform(click())
        onView(withId(android.R.id.button1))
            .perform(click()) // Press "OK" button on the dialog

        verify(navController).navigateUp()
    }

    private fun launchFragment(): FragmentScenario<AddEditCategoryFragment> {
        val bundle = AddEditCategoryFragmentArgs(testCategory.id).toBundle()
        return launchFragmentInContainer<AddEditCategoryFragment>(bundle, R.style.AppTheme)
    }
}