package com.huy.fitsu.addEditCategory

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.huy.fitsu.FakeFitsuScheduler
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Event
import com.huy.fitsu.data.repository.CategoryRepository
import com.nhaarman.mockitokotlin2.*
import io.reactivex.Completable
import io.reactivex.CompletableObserver
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

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

    @Mock
    private lateinit var updateCategoryCompletableIO: Completable

    @Mock
    private lateinit var exception: Exception

    private lateinit var viewModel: AddEditCategoryViewModel

    private val scheduler = FakeFitsuScheduler()

    private val errorMessage = "error"

    @Before
    fun setup() {
        val completable = mock<Completable>()
        whenever(repository.updateCategory(any()))
            .thenReturn(completable)
        whenever(completable.subscribeOn(scheduler.io()))
            .thenReturn(updateCategoryCompletableIO)
        whenever(exception.message)
            .thenReturn(errorMessage)

        viewModel = AddEditCategoryViewModel(repository, scheduler)

        viewModel.navigateBackLiveData().observeForever(navigateBackObserver)
        viewModel.loadingLiveData().observeForever(loadingObserver)
        viewModel.errorLiveData().observeForever(errorObserver)

    }

    @Test
    fun getCategory_shouldUseInjectedId() {
        val id = "id"
        viewModel.setCategoryId(id)

        viewModel.getCategory()

        verify(repository).findCategoryById(eq(id))
    }

    @Test
    fun updateCategory_shouldDelegateToRepository() {
        val category = Category()

        viewModel.updateCategory(category)

        verify(repository).updateCategory(eq(category))
    }

    @Test
    fun updateCategory_withSuccess_shouldNavigateBack() {
        val category = Category()
        updateCategory_withSuccess()

        viewModel.updateCategory(category)

        verify(navigateBackObserver).onChanged(notNull())
    }

    @Test
    fun updateCategory_shouldShowLoading() {
        viewModel.updateCategory(Category())

        verify(loadingObserver).onChanged(eq(true))
    }

    @Test
    fun updateCategory_withSuccess_shouldHideLoading() {
        updateCategory_withSuccess()

        viewModel.updateCategory(Category())

        verify(loadingObserver).onChanged(eq(false))
    }

    @Test
    fun updateCategory_withError_shouldHideLoading() {
        updateCategory_withError()

        viewModel.updateCategory(Category())

        verify(loadingObserver).onChanged(eq(false))
    }

    @Test
    fun updateCategory_shouldHideError() {
        viewModel.updateCategory(Category())

        verify(errorObserver).onChanged(eq(""))
    }

    @Test
    fun updateCategory_withError_shouldShowError() {
        updateCategory_withError()

        viewModel.updateCategory(Category())

        verify(errorObserver).onChanged(eq(errorMessage))
    }

    private fun updateCategory_withSuccess() {

        doAnswer {
            val callback: CompletableObserver = it.getArgument(0)
            callback.onComplete()
        }.whenever(updateCategoryCompletableIO)
            .subscribe(any<CompletableObserver>())
    }

    private fun updateCategory_withError() {
        doAnswer {
            val callback: CompletableObserver = it.getArgument(0)
            callback.onError(exception)
        }.whenever(updateCategoryCompletableIO)
            .subscribe(any<CompletableObserver>())
    }

}