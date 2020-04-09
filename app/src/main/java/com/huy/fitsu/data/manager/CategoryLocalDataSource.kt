package com.huy.fitsu.data.manager

import androidx.lifecycle.LiveData
import com.huy.fitsu.data.local.CategoryDao
import com.huy.fitsu.data.model.Category
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class CategoryLocalDataSource (
    private val categoryDao: CategoryDao,
    private val ioDispatcher: CoroutineDispatcher
): CategoryDataSource {

    override fun getCategory(id: String): LiveData<Category> {
        return categoryDao.findById(id)
    }

    override fun getCategories(): LiveData<List<Category>> {
        return categoryDao.getAll()
    }
    override suspend fun insertNewCategory(category: Category) = withContext(ioDispatcher) {
        categoryDao.insertNewCategory(category)
    }

    override suspend fun updateCategory(category: Category) = withContext(ioDispatcher) {
        categoryDao.updateCategory(category)
    }

    override suspend fun deleteAllCategories() = withContext(ioDispatcher) {
        categoryDao.deleteAllCategories()
    }
}