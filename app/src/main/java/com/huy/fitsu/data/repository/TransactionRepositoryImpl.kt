package com.huy.fitsu.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.huy.fitsu.data.manager.DataSourceModule
import com.huy.fitsu.data.manager.TransactionDataSource
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.model.TransactionDetail
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    @DataSourceModule.TransactionLocalDataSource
    private val transactionLocalDataSource: TransactionDataSource
) : TransactionRepository {

    override suspend fun insertNewTransaction(transaction: Transaction) {
        transactionLocalDataSource.insertNewTransaction(transaction)
    }

    override suspend fun deleteAllTransactions() {
        transactionLocalDataSource.deleteAllTransactions()
    }

    override fun getTransactionDetails(): LiveData<PagedList<TransactionDetail>> {
        val factory = transactionLocalDataSource.getTransactionDetails()
        return factory.toLiveData(pageSize = 5)
    }

    override fun getTransaction(id: String): LiveData<Transaction> {
        return transactionLocalDataSource.getTransaction(id)
    }
}