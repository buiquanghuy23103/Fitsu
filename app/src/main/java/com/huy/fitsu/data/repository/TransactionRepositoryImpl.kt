package com.huy.fitsu.data.repository

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.huy.fitsu.data.local.TransactionLocalDataSource
import com.huy.fitsu.data.model.CategoryExpense
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.model.TransactionDetail
import com.huy.fitsu.util.wrapEspressoIdlingResource
import java.time.YearMonth
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    private val transactionLocalDataSource: TransactionLocalDataSource
) : TransactionRepository {

    override suspend fun getTransaction(id: String): Transaction? =
        wrapEspressoIdlingResource {
            transactionLocalDataSource.getTransactionById(id)
        }

    override suspend fun insertNewTransaction(transaction: Transaction) =
        wrapEspressoIdlingResource {
            transactionLocalDataSource.insertTransaction(transaction)
        }

    @VisibleForTesting
    override suspend fun deleteAllTransactions() =
        wrapEspressoIdlingResource {
            transactionLocalDataSource.deleteAllTransactions()
        }

    override suspend fun deleteTransaction(transaction: Transaction) =
        wrapEspressoIdlingResource {
            transactionLocalDataSource.deleteTransaction(transaction)
        }


    override suspend fun updateTransaction(transaction: Transaction) =
        wrapEspressoIdlingResource {
            transactionLocalDataSource.updateTransaction(transaction)
        }


    override fun getTransactionDetailPagedList(): LiveData<PagedList<TransactionDetail>>
        = wrapEspressoIdlingResource {
            transactionLocalDataSource.getTransactionDetailDataSourceFactory()
                .toLiveData(pageSize = 5)
        }

    override fun getTransactionDetailLiveData(id: String): LiveData<TransactionDetail> =
        wrapEspressoIdlingResource {
            transactionLocalDataSource.getTransactionDetailLiveData(id)
        }

    override fun getTransactionLiveData(id: String): LiveData<Transaction> =
        wrapEspressoIdlingResource {
            transactionLocalDataSource.getTransactionLiveData(id)
        }


    override fun getCategoryExpenseByYearMonth(yearMonth: YearMonth): LiveData<List<CategoryExpense>> =
        wrapEspressoIdlingResource {
            transactionLocalDataSource.getCategoryExpenseOfYearMonth(yearMonth)
        }

    override fun getAccountBalanceLiveData(): LiveData<Float> =
        wrapEspressoIdlingResource {
            transactionLocalDataSource.getAccountBalanceLiveData()
        }

    override fun saveAccountBalance(accountBalance: Float) =
        wrapEspressoIdlingResource {
            transactionLocalDataSource.saveAccountBalance(accountBalance)
        }
}