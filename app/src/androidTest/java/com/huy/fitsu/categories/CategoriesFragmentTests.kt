package com.huy.fitsu.categories

import android.os.Build
import androidx.fragment.app.testing.FragmentScenario
import androidx.fragment.app.testing.launchFragmentInContainer
import androidx.navigation.NavDirections
import androidx.navigation.Navigator
import androidx.test.core.app.ApplicationProvider
import androidx.test.espresso.Espresso.onView
import androidx.test.espresso.action.ViewActions.click
import androidx.test.espresso.assertion.ViewAssertions.matches
import androidx.test.espresso.contrib.RecyclerViewActions
import androidx.test.espresso.matcher.ViewMatchers.*
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.huy.fitsu.BaseTest
import com.huy.fitsu.FitsuApplication
import com.huy.fitsu.R
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.repository.CategoryRepository
import com.huy.fitsu.util.wrapEspressoIdlingResource
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.ArgumentMatchers
import org.mockito.Mockito.*

@RunWith(AndroidJUnit4::class)
class CategoriesFragmentTests : BaseTest<CategoriesFragment>() {

    private lateinit var categoryRepository: CategoryRepository

    private val testCategory = Category(title = "ATest")

    override fun launchFragment(): FragmentScenario<CategoriesFragment> {
        return launchFragmentInContainer(null, R.style.Theme_Fitsu)
    }

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
    }

    @After
    fun tearDown() = wrapEspressoIdlingResource {
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
    fun editCategory_shouldNavigate_toAddEditCategoryFragment() {
        val navController = launchFragmentWithMockNavController()
        val destination = CategoriesFragmentDirections
            .toAddEditCategoryFragment(testCategory.id)

        onView(withId(R.id.categories_list))
            .perform(
                RecyclerViewActions.actionOnItemAtPosition<CategoriesAdapter.CategoryItem>(
                    0,
                    click()
                )
            )



        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            verify(navController).navigate(
                eq(destination),
                any(Navigator.Extras::class.java)
            )
        } else {
            verify(navController).navigate(eq(destination))
        }
    }

    @Test
    fun addCategory_shouldNavigate_toAddEditCategoryFragment() {
        val navController = launchFragmentWithMockNavController()

        onView(withId(R.id.categories_add_button))
            .perform(click())

        verify(navController).navigate(
            ArgumentMatchers.any(NavDirections::class.java)
        )
    }

}