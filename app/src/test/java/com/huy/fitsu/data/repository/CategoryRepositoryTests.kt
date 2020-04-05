package com.huy.fitsu.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.huy.fitsu.data.manager.CategoryDataSource
import com.huy.fitsu.data.model.Category
import com.nhaarman.mockitokotlin2.eq
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@RunWith(MockitoJUnitRunner::class)
class CategoryRepositoryTests {

    @get:Rule
    val taskRuleExecutor = InstantTaskExecutorRule()

    @Mock
    private lateinit var localDataSource: CategoryDataSource

    private lateinit var repository: CategoryRepository

    @Before
    fun setup() {
        repository = CategoryRepositoryImpl(localDataSource)
    }

    @Test
    fun insertNewCategory_shouldDelegateTo_localDataSource() = runBlocking {
        val newCategory = Category()
        repository.insertNewCategory(newCategory)

        verify(localDataSource).insertNewCategory(eq(newCategory))
    }

    @Test
    fun getAllCategories_shouldDelegateTo_localDataSource() {
        repository.getCategories()

        verify(localDataSource).getCategories()
    }

    @Test
    fun findCategoryById_shouldDelegateTo_localDataSource() {
        val id = "id"

        repository.getCategory(id)

        verify(localDataSource).getCategory(eq(id))
    }

    @Test
    fun updateCategory() = runBlocking {
        val updatedCategory = Category()

        repository.updateCategory(updatedCategory)

        verify(localDataSource).updateCategory(updatedCategory)
    }

}