package com.huy.fitsu.transactions

import androidx.lifecycle.LiveData
import com.huy.fitsu.data.local.database.FitsuDatabase
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.model.TransactionDetail
import com.huy.fitsu.di.DispatcherModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class DefaultTransactionRepository @Inject constructor(
    db: FitsuDatabase,
    @DispatcherModule.IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
): TransactionsRepository {

    private val transactionDao = db.transactionDao()
    private val transactionDetailDao = db.transactionDetailDao()

    override fun getTransactionsLiveData(): LiveData<List<TransactionDetail>> {
        return transactionDetailDao.getAllLiveData()
    }

    override suspend fun insertNewTransaction(transaction: Transaction) =
        withContext(ioDispatcher) {
            transactionDao.insert(transaction)
        }
}