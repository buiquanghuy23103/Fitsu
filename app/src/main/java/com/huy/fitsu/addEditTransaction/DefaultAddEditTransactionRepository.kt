package com.huy.fitsu.addEditTransaction

import com.huy.fitsu.data.local.database.FitsuDatabase
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.di.DispatcherModule
import com.huy.fitsu.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultAddEditTransactionRepository @Inject constructor(
    db: FitsuDatabase,
    @DispatcherModule.IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) : AddEditTransactionRepository {
    private val transactionDao = db.transactionDao()
    private val categoryDao = db.categoryDao()

    override fun getTransactionLiveDataById(id: String) =
        transactionDao.findByIdLiveData(id)

    override fun getCategoriesLiveData() =
        categoryDao.getAllLiveData()

    override suspend fun deleteTransaction(transaction: Transaction) = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            transactionDao.delete(transaction)
        }
    }

    override suspend fun updateTransaction(transaction: Transaction) = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            transactionDao.update(transaction)
        }
    }

}