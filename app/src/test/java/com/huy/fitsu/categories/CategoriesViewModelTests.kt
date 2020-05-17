package com.huy.fitsu.categories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.huy.fitsu.data.model.Event
import com.huy.fitsu.data.repository.CategoryRepository
import com.nhaarman.mockitokotlin2.notNull
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CategoriesViewModelTests {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CategoriesViewModel

    @Mock
    private lateinit var repository: CategoryRepository

    @Mock
    private lateinit var editCategoryEventObserver: Observer<Event<String>>

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        viewModel = CategoriesViewModel(repository, testDispatcher)

        viewModel.editCategoryEventLiveData()
            .observeForever(editCategoryEventObserver)
    }

    @Test
    fun getAllCategories_shouldGetFromRepository() {
        viewModel.getCategoriesLiveData()

        verify(repository).getCategoriesLiveData()
    }

    @Test
    fun editCategoryWithId_shouldNavigateToEditScreen() {
        val id = "id"

        viewModel.editCategoryWithId(id)

        verify(editCategoryEventObserver).onChanged(notNull())
    }

    @Test
    fun addCategory_shouldNavigateToEditScreen() = testDispatcher.runBlockingTest {
        viewModel.addCategory()

        verify(editCategoryEventObserver).onChanged(notNull())
    }

}