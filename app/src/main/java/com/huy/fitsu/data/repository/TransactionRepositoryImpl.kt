package com.huy.fitsu.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.huy.fitsu.data.manager.DataSourceModule
import com.huy.fitsu.data.manager.TransactionDataSource
import com.huy.fitsu.data.model.Transaction
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    @DataSourceModule.TransactionLocalDataSource
    private val transactionLocalDataSource: TransactionDataSource
) : TransactionRepository {

    override suspend fun insertNewTransaction(transaction: Transaction) {
        transactionLocalDataSource.insertNewTransaction(transaction)
    }

    override fun getTransactions(): DataSource.Factory<Int, Transaction> {
        return transactionLocalDataSource.getTransactions()
    }

    override fun getTransaction(id: String): LiveData<Transaction> {
        TODO("Not yet implemented")
    }
}