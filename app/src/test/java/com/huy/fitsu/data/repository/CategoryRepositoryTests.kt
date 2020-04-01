package com.huy.fitsu.data.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.huy.fitsu.data.local.CategoryDao
import com.huy.fitsu.data.model.Category
import com.nhaarman.mockitokotlin2.eq
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

    private lateinit var repository: CategoryRepository

    @Mock
    private lateinit var categoryDao: CategoryDao

    @Before
    fun setup() {
        repository = CategoryRepositoryImpl(categoryDao)
    }

    @Test
    fun addCategory_shouldDelegateToCategoryDao() {
        val category = Category()

        repository.addCategory(category)

        verify(categoryDao).insert(eq(category))

    }

    @Test
    fun getAllCategories_shouldDelegateToCategoryDao() {
        repository.getAllCategories()

        verify(categoryDao).getAll()
    }

    @Test
    fun findCategoryById_shouldDelegateToCategoryDao() {
        val id = "id"

        repository.findCategoryById(id)

        verify(categoryDao).findById(eq(id))
    }

    @Test
    fun updateCategory_shouldDelegateToCategoryDao() {
        val newCategory = Category()

        repository.updateCategory(newCategory)

        verify(categoryDao).update(newCategory)
    }

}