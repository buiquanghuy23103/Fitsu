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
    private val db: FitsuDatabase,
    private val fitsuSharedPrefManager: FitsuSharedPrefManager,
    @DispatcherModule.IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) {

    private val transactionDao = db.transactionDao()
    private val transactionDetailDao = db.transactionDetailDao()
    private val budgetDao = db.budgetDao()
    private val defaultBudget = fitsuSharedPrefManager.getDefaultBudget()

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
    }

    suspend fun updateTransaction(transaction: Transaction) = withContext(ioDispatcher) {
        recalculateMonthBudget(transaction)
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

    private suspend fun recalculateMonthBudget(newTransaction: Transaction) {
        transactionDao.findById(newTransaction.id)?.let { oldTransaction ->
            val oldTransactionDate = oldTransaction.createdAt
            val oldTransactionYearMonth =
                YearMonth.of(oldTransactionDate.year, oldTransactionDate.month)

            val newTransactionDate = newTransaction.createdAt
            val newTransactionYearMonth =
                YearMonth.of(newTransactionDate.year, newTransactionDate.monthValue)

            if (oldTransactionYearMonth == newTransactionYearMonth) {
                addMonthExpense(
                    -oldTransaction.value,
                    oldTransactionDate.year,
                    oldTransactionDate.monthValue
                )
                addMonthExpense(
                    newTransaction.value,
                    newTransactionDate.year,
                    newTransactionDate.monthValue
                )
            } else {
                addMonthExpense(
                    (newTransaction.value - oldTransaction.value).round(2),
                    newTransactionDate.year,
                    newTransactionDate.monthValue
                )
            }
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
            val newBudget = Budget(
                value = defaultBudget,
                expense = newExpense,
                semanticWeek = semanticWeek
            )
            budgetDao.insert(newBudget)
        }
    }
}