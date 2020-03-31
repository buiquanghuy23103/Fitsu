package com.huy.fitsu.data.repository

import androidx.lifecycle.LiveData
import com.huy.fitsu.data.local.CategoryDao
import com.huy.fitsu.data.model.Category
import io.reactivex.Completable
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    private val categoryDao: CategoryDao
): CategoryRepository {

    override fun getAllCategories(): LiveData<List<Category>> {
        return categoryDao.getAll()
    }

    override fun addCategory(category: Category): Completable {
        return categoryDao.insert(category)
    }

}