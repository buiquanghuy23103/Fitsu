package com.huy.fitsu.categories

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.huy.fitsu.FitsuApplication
import com.huy.fitsu.R
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.repository.CategoryRepository
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mockito.mock
import org.mockito.Mockito.verify

@RunWith(AndroidJUnit4::class)
class CategoriesFragmentTests {

    private lateinit var categoryRepository: CategoryRepository
    private val testCategory = Category(title = "ATest")

    @Before
    fun setup() {
        categoryRepository = ApplicationProvider.getApplicationContext<FitsuApplication>().appComponent
            .categoryRepository
        runBlocking {
            categoryRepository.insertNewCategory(testCategory)
        }
    }

    @After
    fun tearDown() {
        runBlocking {
            categoryRepository.deleteAllCategories()
        }
    }


    @Test
    fun displayCategory() {
        launchFragment()

        onView(withId(R.id.categories_list))
            .perform(RecyclerViewActions.scrollToPosition<CategoriesAdapter.CategoryItem>(0))

        onView(withText(testCategory.title))
            .check(matches(isDisplayed()))
    }

    @Test
    fun editCategory() {
        val scenario = launchFragment()
        val navController = mock(NavController::class.java)
        scenario.onFragment {
            Navigation.setViewNavController(it.view!!, navController)
        }

        onView(withId(R.id.categories_list))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<CategoriesAdapter.CategoryItem>(
                    0,
                    click()
                )
            )

        verify(navController).navigate(
            CategoriesFragmentDirections.toAddEditCategoryFragment(testCategory.id)
        )
    }


    private fun launchFragment(): FragmentScenario<CategoriesFragment> {
        return launchFragmentInContainer<CategoriesFragment>(null, R.style.AppTheme)
    }

}