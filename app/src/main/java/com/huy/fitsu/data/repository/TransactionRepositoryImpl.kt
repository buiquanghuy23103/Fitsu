package com.huy.fitsu.data.repository

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.huy.fitsu.data.local.LocalDataSource
import com.huy.fitsu.data.model.*
import com.huy.fitsu.util.wrapEspressoIdlingResource
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val localDataSource: LocalDataSource
) : TransactionRepository {

    override suspend fun getTransaction(id: String): Transaction? =
        wrapEspressoIdlingResource {
            localDataSource.getTransactionById(id)
        }

    override suspend fun insertNewTransaction(transaction: Transaction) =
        wrapEspressoIdlingResource {
            localDataSource.insertTransaction(transaction)
        }

    @VisibleForTesting
    override suspend fun deleteAllTransactions() =
        wrapEspressoIdlingResource {
            localDataSource.deleteAllTransactions()
        }

    override suspend fun deleteTransaction(transaction: Transaction) =
        wrapEspressoIdlingResource {
            localDataSource.deleteTransaction(transaction)
        }


    override suspend fun updateTransaction(transaction: Transaction) =
        wrapEspressoIdlingResource {
            localDataSource.updateTransaction(transaction)
        }


    override fun getTransactionDetailPagedList(): LiveData<PagedList<TransactionDetail>>
        = wrapEspressoIdlingResource {
            localDataSource.getTransactionDetailDataSourceFactory()
                .toLiveData(pageSize = 5)
        }

    override fun getTransactionDetailLiveData(id: String): LiveData<TransactionDetail> =
        wrapEspressoIdlingResource {
            localDataSource.getTransactionDetailLiveData(id)
        }

    override fun getTransactionLiveData(id: String): LiveData<Transaction> =
        wrapEspressoIdlingResource {
            localDataSource.getTransactionLiveData(id)
        }


    override fun transactionCountByCategory(): LiveData<List<CategoryReport>> =
        wrapEspressoIdlingResource {
            localDataSource.getTransactionSumByCategory()
        }


}