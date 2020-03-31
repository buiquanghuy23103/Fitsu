package com.huy.fitsu.categories

import com.huy.fitsu.data.repository.CategoryRepository
import com.nhaarman.mockitokotlin2.verify
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CategoriesViewModelTests {

    private lateinit var viewModel: CategoriesViewModel

    @Mock
    private lateinit var repository: CategoryRepository

    @Before
    fun setup() {
        viewModel = CategoriesViewModel(repository)
    }

    @Test
    fun getAllCategories_shouldGetFromRepository() {
        viewModel.getAllCategories()

        verify(repository).getAllCategories()
    }

}