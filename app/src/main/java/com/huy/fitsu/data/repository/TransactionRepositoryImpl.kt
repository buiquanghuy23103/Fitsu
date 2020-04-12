package com.huy.fitsu.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.huy.fitsu.data.manager.DataSourceModule
import com.huy.fitsu.data.manager.TransactionDataSource
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.model.TransactionDetail
import com.huy.fitsu.util.wrapEspressoIdlingResource
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    @DataSourceModule.TransactionLocalDataSource
    private val transactionLocalDataSource: TransactionDataSource
) : TransactionRepository {

    override suspend fun getTransaction(id: String): Transaction? {
        return wrapEspressoIdlingResource {
            transactionLocalDataSource.getTransaction(id)
        }
    }

    override suspend fun insertNewTransaction(transaction: Transaction) {
        wrapEspressoIdlingResource {
            transactionLocalDataSource.insertNewTransaction(transaction)
        }
    }

    override suspend fun deleteAllTransactions() {
        wrapEspressoIdlingResource {
            transactionLocalDataSource.deleteAllTransactions()
        }
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        wrapEspressoIdlingResource {
            transactionLocalDataSource.updateTransaction(transaction)
        }
    }

    override fun getTransactionDetails(): LiveData<PagedList<TransactionDetail>> {
        val factory = transactionLocalDataSource.getTransactionDetails()
        return factory.toLiveData(pageSize = 5)
    }

    override fun getTransactionDetail(id: String): LiveData<TransactionDetail> {
        return transactionLocalDataSource.getTransactionDetail(id)
    }

    override fun getTransactionLiveData(id: String): LiveData<Transaction> {
        return transactionLocalDataSource.getTransactionLiveData(id)
    }
}