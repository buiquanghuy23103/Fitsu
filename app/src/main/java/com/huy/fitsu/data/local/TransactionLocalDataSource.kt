package com.huy.fitsu.data.local

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import com.huy.fitsu.data.local.database.FitsuDatabase
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.di.DispatcherModule
import com.huy.fitsu.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TransactionLocalDataSource @Inject constructor(
    db: FitsuDatabase,
    private val fitsuSharedPrefManager: FitsuSharedPrefManager,
    @DispatcherModule.IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) {

    private val transactionDao = db.transactionDao()
    private val transactionDetailDao = db.transactionDetailDao()

    suspend fun getTransactionById(id: String): Transaction? = withContext(ioDispatcher) {
        transactionDao.findById(id)
    }

    suspend fun insertTransaction(transaction: Transaction) = withContext(ioDispatcher) {
        transactionDao.insert(transaction)
    }

    @VisibleForTesting
    suspend fun deleteAllTransactions() = withContext(ioDispatcher) {
        transactionDao.deleteAll()
    }

    suspend fun deleteTransaction(transaction: Transaction) = withContext(ioDispatcher) {
        transactionDao.delete(transaction)
        recalculateBalance(0f, transaction.value)
    }

    suspend fun updateTransaction(newTransaction: Transaction) = withContext(ioDispatcher) {
        transactionDao.findById(newTransaction.id)?.let { oldTransaction ->
            recalculateBalance(newTransaction.value, oldTransaction.value)
        }
        transactionDao.update(newTransaction)
    }

    fun getTransactionDetailDataSourceFactory() =
        transactionDetailDao.getDataSourceFactory()

    fun getTransactionDetailLiveData(id: String) =
        transactionDetailDao.findByIdLiveData(id)

    fun getTransactionLiveData(id: String) =
        transactionDao.findByIdLiveData(id)

    fun getTransactionSumByCategory() =
        transactionDetailDao.transactionSumByCategory()

    fun getAccountBalanceLiveData(): LiveData<Float> =
        wrapEspressoIdlingResource {
            fitsuSharedPrefManager.getAccountBalanceLiveData()
        }

    fun saveAccountBalance(accountBalance: Float) =
        wrapEspressoIdlingResource {
            fitsuSharedPrefManager.saveAccountBalance(accountBalance)
        }

    private fun recalculateBalance(newTransactionValue: Float, oldTransactionValue: Float) {
        val difference = newTransactionValue - oldTransactionValue
        val oldBalance = fitsuSharedPrefManager.getAccountBalanceValue()
        val newBalance = oldBalance + difference
        fitsuSharedPrefManager.saveAccountBalance(newBalance)
    }

}