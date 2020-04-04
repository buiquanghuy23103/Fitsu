package com.huy.fitsu.data.manager

import com.huy.fitsu.data.local.CategoryDao
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Result
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryLocalDataSource @Inject constructor(
    private val categoryDao: CategoryDao,
    private val ioDispatcher: CoroutineDispatcher
): CategoryDataSource {

    override suspend fun getCategory(id: String): Result<Category> = withContext(ioDispatcher) {
        try {
            val category = categoryDao.getCategoryById(id)
            if (category != null) {
                return@withContext Result.Success(category)
            } else {
                return@withContext Result.Error(Exception("Category not found"))
            }
        } catch (e: Exception) {
            return@withContext Result.Error(e)
        }
    }

    override suspend fun getCategories(): Result<List<Category>> = withContext(ioDispatcher) {
        return@withContext try {
            Result.Success(categoryDao.getAllCategories())
        } catch (e: Exception) {
            Result.Error(e)
        }
    }

    override suspend fun insertNewCategory(category: Category) = withContext(ioDispatcher) {
        categoryDao.insertNewCategory(category)
    }

    override suspend fun updateCategory(category: Category) = withContext(ioDispatcher) {
        categoryDao.updateCategory(category)
    }
}