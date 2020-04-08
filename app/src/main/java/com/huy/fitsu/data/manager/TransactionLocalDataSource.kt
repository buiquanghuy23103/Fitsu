package com.huy.fitsu.data.manager

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.huy.fitsu.data.local.TransactionDao
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.model.TransactionDetail
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext

class TransactionLocalDataSource(
    private val transactionDao: TransactionDao,
    private val ioDispatcher: CoroutineDispatcher
): TransactionDataSource {

    override suspend fun insertNewTransaction(transaction: Transaction) = withContext(ioDispatcher) {
        transactionDao.insertNewTransaction(transaction)
    }

    override fun getTransactionDetails(): DataSource.Factory<Int, TransactionDetail> {
        return transactionDao.getTransactionDetails()
    }

    override fun getTransaction(id: String): LiveData<Transaction> {
        return transactionDao.getTransaction(id)
    }
}