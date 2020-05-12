package com.huy.fitsu.data.local

import androidx.annotation.VisibleForTesting
import com.huy.fitsu.data.local.database.FitsuDatabase
import com.huy.fitsu.data.model.Budget
import com.huy.fitsu.data.model.SemanticWeek
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.di.DispatcherModule
import com.huy.fitsu.util.round
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.YearMonth
import javax.inject.Inject

class LocalDataSource @Inject constructor(
    db: FitsuDatabase,
    private val fitsuSharedPrefManager: FitsuSharedPrefManager,
    @DispatcherModule.IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) {

    private val transactionDao = db.transactionDao()
    private val transactionDetailDao = db.transactionDetailDao()
    private val budgetDao = db.budgetDao()

    suspend fun getTransactionById(id: String): Transaction? = withContext(ioDispatcher) {
        transactionDao.findById(id)
    }

    suspend fun insertTransaction(transaction: Transaction) = withContext(ioDispatcher) {
        transactionDao.insert(transaction)
    }

    @VisibleForTesting
    suspend fun deleteAllTransactions() = withContext(ioDispatcher) {
        transactionDao.deleteAll()
        budgetDao.deleteAll()
    }

    suspend fun deleteTransaction(transaction: Transaction) = withContext(ioDispatcher) {
        transactionDao.delete(transaction)

        val date = transaction.createdAt
        addMonthExpense(-transaction.value, date.year, date.monthValue)

        recalculateBalance(0f, transaction.value)
    }

    suspend fun updateTransaction(transaction: Transaction) = withContext(ioDispatcher) {
        recalculate(transaction)
        transactionDao.update(transaction)
    }

    fun getTransactionDetailDataSourceFactory() =
        transactionDetailDao.getDataSourceFactory()

    fun getTransactionDetailLiveData(id: String) =
        transactionDetailDao.findByIdLiveData(id)

    fun getTransactionLiveData(id: String) =
        transactionDao.findByIdLiveData(id)

    fun getTransactionSumByCategory() =
        transactionDetailDao.transactionSumByCategory()

    private suspend fun recalculate(newTransaction: Transaction) {
        transactionDao.findById(newTransaction.id)?.let { oldTransaction ->
            recalculateMonthBudget(newTransaction, oldTransaction)
            recalculateBalance(newTransaction.value, oldTransaction.value)
        }
    }

    private suspend fun recalculateMonthBudget(
        newTransaction: Transaction,
        oldTransaction: Transaction
    ) {
        val oldDate = oldTransaction.createdAt
        val oldYearMonth = YearMonth.of(oldDate.year, oldDate.month)

        val newDate = newTransaction.createdAt
        val newYearMonth = YearMonth.of(newDate.year, newDate.monthValue)

        if (oldYearMonth == newYearMonth) {
            addMonthExpense(
                -oldTransaction.value,
                oldDate.year,
                oldDate.monthValue
            )
            addMonthExpense(
                newTransaction.value,
                newDate.year,
                newDate.monthValue
            )
        } else {
            val difference = newTransaction.value - oldTransaction.value
            addMonthExpense(
                difference.round(2),
                newDate.year,
                newDate.monthValue
            )
        }
    }

    private suspend fun addMonthExpense(
        newExpense: Float,
        year: Int,
        month: Int
    ) {
        val result = budgetDao.addNewExpenseByYearAndMonth(newExpense, year, month)
        if (result <= 0) {
            val semanticWeek = SemanticWeek(weekNumber = 0, month = month, year = year)
            val defaultBudget = fitsuSharedPrefManager.getDefaultBudget()
            val newBudget = Budget(
                value = defaultBudget,
                expense = newExpense,
                semanticWeek = semanticWeek
            )
            budgetDao.insert(newBudget)
        }
    }

    private fun recalculateBalance(newTransactionValue: Float, oldTransactionValue: Float) {
        val difference = newTransactionValue - oldTransactionValue
        val oldBalance = fitsuSharedPrefManager.getAccountBalanceValue()
        val newBalance = oldBalance + difference
        fitsuSharedPrefManager.saveAccountBalance(newBalance)
    }
}