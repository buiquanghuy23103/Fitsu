package com.huy.fitsu.data.repository

import com.huy.fitsu.data.manager.CategoryLocalDataSource
import com.huy.fitsu.data.manager.DataSourceModule
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Result
import com.huy.fitsu.util.wrapEspressoIdlingResource
import javax.inject.Inject

class CategoryRepositoryUpgradeImpl @Inject constructor(
    @DataSourceModule.CategoryLocalDataSource
    private val categoryLocalDataSource: CategoryLocalDataSource
) : CategoryRepositoryUpgrade {

    override suspend fun getCategories(): Result<List<Category>> {
        return wrapEspressoIdlingResource {
            categoryLocalDataSource.getCategories()
        }
    }

    override suspend fun getCategory(id: String): Result<Category> {
        return wrapEspressoIdlingResource {
            categoryLocalDataSource.getCategory(id)
        }
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