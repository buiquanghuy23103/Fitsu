package com.huy.fitsu.addEditTransaction

import com.huy.fitsu.data.local.database.FitsuDatabase
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.di.DispatcherModule
import com.huy.fitsu.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class AddEditTransactionRepository @Inject constructor(
    db: FitsuDatabase,
    @DispatcherModule.IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) {
    private val transactionDao = db.transactionDao()
    private val categoryDao = db.categoryDao()

    fun getTransactionLiveDataById(id: String) =
        transactionDao.findByIdLiveData(id)

    fun getCategoriesLiveData() =
        categoryDao.getAllLiveData()

    suspend fun deleteTransaction(transaction: Transaction) = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            transactionDao.delete(transaction)
        }
    }

    suspend fun updateTransaction(transaction: Transaction) = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            transactionDao.update(transaction)
        }
    }

}