package com.huy.fitsu.data.local

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import com.huy.fitsu.data.local.database.DatabaseTypeConverters
import com.huy.fitsu.data.local.database.FitsuDatabase
import com.huy.fitsu.data.model.CategoryExpense
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.di.DispatcherModule
import com.huy.fitsu.util.round
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.YearMonth
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
        transactionDetailDao.getAllLiveData()

    fun getTransactionDetailLiveData(id: String) =
        transactionDetailDao.findByIdLiveData(id)

    fun getTransactionLiveData(id: String) =
        transactionDao.findByIdLiveData(id)

    fun getCategoryExpenseOfYearMonth(yearMonth: YearMonth): LiveData<List<CategoryExpense>> {
        val startDateOfMonth = yearMonth.atDay(1)
        val start = DatabaseTypeConverters.fromLocalDate(startDateOfMonth)

        val endDateOfMonth = yearMonth.atEndOfMonth()
        val end = DatabaseTypeConverters.fromLocalDate(endDateOfMonth)

        return transactionDetailDao.getCategoryExpenseBetween(start, end)
    }

    private fun recalculateBalance(newTransactionValue: Float, oldTransactionValue: Float) {
        val difference = newTransactionValue - oldTransactionValue
        val oldBalance = fitsuSharedPrefManager.getAccountBalanceValue()
        val newBalance = oldBalance + difference
        fitsuSharedPrefManager.saveAccountBalance(newBalance.round(2))
    }

}