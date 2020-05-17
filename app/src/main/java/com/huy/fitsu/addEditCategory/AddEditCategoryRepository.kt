package com.huy.fitsu.addEditCategory

import androidx.lifecycle.LiveData
import com.huy.fitsu.data.local.database.FitsuDatabase
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.di.DispatcherModule
import com.huy.fitsu.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddEditCategoryRepository @Inject constructor(
    db: FitsuDatabase,
    @DispatcherModule.IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) {

    private val categoryDao = db.categoryDao()
    private val transactionDao = db.transactionDao()

    fun getCategoryLiveDataById(id: String): LiveData<Category> {
        return categoryDao.findByIdLiveData(id)
    }

    suspend fun updateCategory(category: Category) =
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                categoryDao.update(category)
            }
        }

    suspend fun deleteCategoryById(id: String) =
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                categoryDao.deleteById(id)
                transactionDao.deleteByCategoryId(id)
            }
        }

}