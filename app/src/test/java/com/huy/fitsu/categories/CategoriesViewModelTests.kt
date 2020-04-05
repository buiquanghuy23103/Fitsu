package com.huy.fitsu.categories

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.huy.fitsu.data.model.Event
import com.huy.fitsu.data.repository.CategoryRepository
import com.nhaarman.mockitokotlin2.notNull
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CategoriesViewModelTests {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: CategoriesViewModel

    @Mock
    private lateinit var repository: CategoryRepository

    @Mock
    private lateinit var editCategoryEventObserver: Observer<Event<String>>

    @Before
    fun setup() {
        viewModel = CategoriesViewModel(repository)

        viewModel.editCategoryEventLiveData()
            .observeForever(editCategoryEventObserver)
    }

    @Test
    fun getAllCategories_shouldGetFromRepository() {
        viewModel.getAllCategories()

        verify(repository).getCategories()
    }

    @Test
    fun editCategoryWithId_shouldNavigateToEditScreen() {
        val id = "id"

        viewModel.editCategoryWithId(id)

        verify(editCategoryEventObserver).onChanged(notNull())
    }

}