package com.huy.fitsu.categories

import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavController
import androidx.navigation.Navigation
import androidx.navigation.testing.TestNavHostController
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
import com.huy.fitsu.util.wrapEspressoIdlingResource
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class CategoriesFragmentTests {

    private lateinit var categoryRepository: CategoryRepository

    private val testCategory = Category(title = "ATest")

    private lateinit var navController: NavController

    @Before
    fun setup() {
        categoryRepository =
            ApplicationProvider.getApplicationContext<FitsuApplication>().appComponent
                .categoryRepository
        wrapEspressoIdlingResource {
            runBlocking {
                categoryRepository.insertNewCategory(testCategory)
            }
        }

        val appContext = ApplicationProvider.getApplicationContext<FitsuApplication>()
        navController = TestNavHostController(appContext).apply {
            setGraph(R.navigation.nav_graph)
        }

        launchFragment().onFragment {
            Navigation.setViewNavController(it.requireView(), navController)
        }
    }

    @After
    fun tearDown() = wrapEspressoIdlingResource {
        runBlocking {
            categoryRepository.deleteAllCategories()
        }
    }


    @Test
    fun displayCategory() {
        onView(withId(R.id.categories_list))
            .perform(RecyclerViewActions.scrollToPosition<CategoriesAdapter.CategoryItem>(0))

        onView(withText(testCategory.title))
            .check(matches(isDisplayed()))
    }

    @Test
    fun editCategory_shouldNavigate_toAddEditCategoryFragment() {
        onView(withId(R.id.categories_list))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<CategoriesAdapter.CategoryItem>(
                    0,
                    click()
                )
            )

        Assert.assertEquals(navController.currentDestination?.id, R.id.addEditCategoryFragment)
    }

    @Test
    fun addCategory_shouldNavigate_toAddEditCategoryFragment() {
        onView(withId(R.id.categories_add_button))
            .perform(click())

        Assert.assertEquals(navController.currentDestination?.id, R.id.addEditCategoryFragment)
    }


    private fun launchFragment(): FragmentScenario<CategoriesFragment> {
        return launchFragmentInContainer<CategoriesFragment>(null, R.style.AppTheme)
    }

}