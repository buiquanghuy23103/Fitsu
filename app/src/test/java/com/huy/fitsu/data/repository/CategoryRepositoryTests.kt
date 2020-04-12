package com.huy.fitsu.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.huy.fitsu.data.local.CategoryDao
import com.huy.fitsu.data.local.FitsuDatabase
import com.huy.fitsu.data.model.Category
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.whenever
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.test.TestCoroutineDispatcher
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito.verify
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class CategoryRepositoryTests {

    @get:Rule
    val taskRuleExecutor = InstantTaskExecutorRule()

    @Mock
    private lateinit var db: FitsuDatabase

    @Mock
    private lateinit var categoryDao: CategoryDao

    private lateinit var repository: CategoryRepository

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setup() {
        whenever(db.categoryDao())
            .thenReturn(categoryDao)
        repository = CategoryRepositoryImpl(db, testDispatcher)
    }

    @Test
    fun insertNewCategory_shouldDelegateTo_localDataSource() = runBlocking {
        val newCategory = Category()
        repository.insertNewCategory(newCategory)

        verify(categoryDao).insert(eq(newCategory))
    }

    @Test
    fun getAllCategories_shouldDelegateTo_localDataSource() {
        repository.getCategoriesLiveData()

        verify(categoryDao).getAllLiveData()
    }

    @Test
    fun findCategoryById_shouldDelegateTo_localDataSource() {
        val id = "id"

        repository.getCategoryLiveData(id)

        verify(categoryDao).findByIdLiveData(eq(id))
    }

    @Test
    fun updateCategory() = runBlocking {
        val updatedCategory = Category()

        repository.updateCategory(updatedCategory)

        verify(categoryDao).update(updatedCategory)
    }

}