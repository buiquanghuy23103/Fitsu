package com.huy.fitsu.addEditCategory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Event
import com.huy.fitsu.data.repository.CategoryRepository
import com.nhaarman.mockitokotlin2.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runBlockingTest
import kotlinx.coroutines.test.setMain
import org.junit.After
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddEditCategoryViewModelTests {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var repository: CategoryRepository

    @Mock
    private lateinit var navigateBackObserver: Observer<Event<Unit>>

    @Mock
    private lateinit var loadingObserver: Observer<Boolean>

    @Mock
    private lateinit var errorObserver: Observer<String>

    private lateinit var viewModel: AddEditCategoryViewModel

    private val testDispatcher = TestCoroutineDispatcher()

    private val errorMessage = "error"

    @Before
    fun setup() {
        Dispatchers.setMain(testDispatcher)
        viewModel = AddEditCategoryViewModel(repository, testDispatcher)

        viewModel.navigateBackLiveData().observeForever(navigateBackObserver)
        viewModel.loadingLiveData().observeForever(loadingObserver)
        viewModel.errorLiveData().observeForever(errorObserver)

    }

    @After
    fun cleanUp() {
        Dispatchers.resetMain()
        testDispatcher.cleanupTestCoroutines()
    }

    @Test
    fun getCategory_shouldUseInjectedId() {
        val id = "id"
        viewModel.setCategoryId(id)

        viewModel.getCategory()

        verify(repository).getCategory(eq(id))
    }

    @Test
    fun updateCategory_shouldDelegateToRepository() = testDispatcher.runBlockingTest {
        val category = Category()

        viewModel.updateCategory(category)

        verify(repository).updateCategory(eq(category))
    }

    @Test
    fun updateCategory_withSuccess_shouldNavigateBack() = testDispatcher.runBlockingTest {
        val category = Category()

        viewModel.updateCategory(category)

        verify(navigateBackObserver).onChanged(notNull())
    }

    @Test
    fun updateCategory_shouldShowLoading() = testDispatcher.runBlockingTest {
        testDispatcher.pauseDispatcher {
            viewModel.updateCategory(Category())

            verify(loadingObserver).onChanged(eq(true))
        }
    }

    @Test
    fun updateCategory_withSuccess_shouldHideLoading() = testDispatcher.runBlockingTest {
        viewModel.updateCategory(Category())

        verify(loadingObserver).onChanged(eq(false))
    }

    @Test(expected = Exception::class)
    fun updateCategory_withError_shouldHideLoading() = testDispatcher.runBlockingTest {
        updateCategory_withError()

        viewModel.updateCategory(Category())

        verify(loadingObserver).onChanged(eq(false))
    }

    @Test
    fun updateCategory_shouldHideError() = testDispatcher.runBlockingTest {
        viewModel.updateCategory(Category())

        verify(errorObserver).onChanged(eq(""))
    }

    @Test(expected = Exception::class)
    fun updateCategory_withError_shouldShowError() = testDispatcher.runBlockingTest {
        updateCategory_withError()

        viewModel.updateCategory(Category())

        verify(errorObserver).onChanged(eq(errorMessage))
    }

    private suspend fun updateCategory_withError() {
        whenever(repository.updateCategory(any()))
            .thenThrow(Exception(errorMessage))
    }

}