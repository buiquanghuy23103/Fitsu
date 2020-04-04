package com.huy.fitsu.data.repository

import androidx.lifecycle.LiveData
import com.huy.fitsu.data.manager.CategoryDataSource
import com.huy.fitsu.data.manager.DataSourceModule
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.util.wrapEspressoIdlingResource
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    @DataSourceModule.CategoryLocalDataSource
    private val categoryLocalDataSource: CategoryDataSource
) : CategoryRepository {

    override fun getCategories(): LiveData<List<Category>> {
        return categoryLocalDataSource.getCategories()
    }

    override fun getCategory(id: String): LiveData<Category> {
        return categoryLocalDataSource.getCategory(id)
    }

    override suspend fun insertNewCategory(category: Category) {
        wrapEspressoIdlingResource {
            categoryLocalDataSource.insertNewCategory(category)
        }
    }

    override suspend fun updateCategory(category: Category) {
        wrapEspressoIdlingResource {
            categoryLocalDataSource.updateCategory(category)
        }
    }
}