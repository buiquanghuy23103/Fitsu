package com.huy.fitsu.categories

import androidx.lifecycle.LiveData
import com.huy.fitsu.data.local.database.FitsuDatabase
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.di.DispatcherModule
import com.huy.fitsu.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultCategoriesRepository @Inject constructor(
    db: FitsuDatabase,
    @DispatcherModule.IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) : CategoriesRepository {

    private val categoryDao = db.categoryDao()

    override fun getCategoriesLiveData(): LiveData<List<Category>> {
        return categoryDao.getAllLiveData()
    }

    override suspend fun insertNewCategory(category: Category) {
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                categoryDao.insert(category)
            }
        }
    }

}