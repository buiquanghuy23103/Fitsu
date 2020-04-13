package com.huy.fitsu.data.repository

import androidx.lifecycle.LiveData
import com.huy.fitsu.data.local.FitsuDatabase
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.di.DispatcherModule
import com.huy.fitsu.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class CategoryRepositoryImpl @Inject constructor(
    db: FitsuDatabase,
    @DispatcherModule.IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) : CategoryRepository {

    private val categoryDao = db.categoryDao()

    override suspend fun getCategory(id: String): Category? {
        return categoryDao.findById(id)
    }

    override fun getCategoriesLiveData(): LiveData<List<Category>> {
        return categoryDao.getAllLiveData()
    }

    override fun getCategoryLiveData(id: String): LiveData<Category> {
        return categoryDao.findByIdLiveData(id)
    }

    override suspend fun insertNewCategory(category: Category) {
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                categoryDao.insert(category)
            }
        }
    }

    override suspend fun updateCategory(category: Category) {
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                categoryDao.update(category)
            }
        }
    }

    override suspend fun deleteAllCategories() {
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                categoryDao.deleteAll()
            }
        }
    }
}