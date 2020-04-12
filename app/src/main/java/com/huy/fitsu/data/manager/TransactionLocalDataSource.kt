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

    override suspend fun getTransaction(id: String) : Transaction? = withContext(ioDispatcher) {
        transactionDao.getTransaction(id)
    }

    override suspend fun insertNewTransaction(transaction: Transaction) = withContext(ioDispatcher) {
        transactionDao.insert(transaction)
    }

    override suspend fun deleteAllTransactions() = withContext(ioDispatcher) {
        transactionDao.deleteAll()
    }

    override suspend fun updateTransaction(transaction: Transaction) = withContext(ioDispatcher) {
        transactionDao.update(transaction)
    }

    override fun getTransactionDetails(): DataSource.Factory<Int, TransactionDetail> {
        return transactionDao.getPagedListLiveData()
    }

    override fun getTransactionDetail(id: String): LiveData<TransactionDetail> {
        return transactionDao.getTransactionDetail(id)
    }

    override fun getTransactionLiveData(id: String): LiveData<Transaction> {
        return transactionDao.findByIdLiveData(id)
    }
}