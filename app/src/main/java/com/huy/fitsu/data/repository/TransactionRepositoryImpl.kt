package com.huy.fitsu.data.repository

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.huy.fitsu.data.local.FitsuDatabase
import com.huy.fitsu.data.model.Budget
import com.huy.fitsu.data.model.SemanticWeek
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.model.TransactionDetail
import com.huy.fitsu.di.DispatcherModule
import com.huy.fitsu.util.DateConverter
import com.huy.fitsu.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.YearMonth
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    db: FitsuDatabase,
    @DispatcherModule.IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) : TransactionRepository {

    private val transactionDao = db.transactionDao()
    private val transactionDetailDao = db.transactionDetailDao()
    private val budgetDao = db.budgetDao()

    override suspend fun getTransaction(id: String): Transaction? {
        return wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                transactionDao.findById(id)
            }
        }
    }

    override suspend fun insertNewTransaction(transaction: Transaction) {
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                transactionDao.insert(transaction)
            }
        }
    }

    @VisibleForTesting
    override suspend fun deleteAllTransactions() {
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                transactionDao.deleteAll()
            }
        }
    }

    override suspend fun deleteTransaction(transaction: Transaction) {
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                transactionDao.delete(transaction)
                val date = transaction.createdAt
                addNewNewExpenseToBudgetByYearMonth(-transaction.value, date.year, date.monthValue)
            }
        }
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                recalculateMonthBudget(transaction)
                transactionDao.update(transaction)
            }
        }
    }


    override fun getTransactionDetailPagedList(): LiveData<PagedList<TransactionDetail>> {
        val factory = transactionDetailDao.getPagedListLiveData()
        return factory.toLiveData(pageSize = 5)
    }

    override fun getTransactionDetailLiveData(id: String): LiveData<TransactionDetail> {
        return transactionDetailDao.findByIdLiveData(id)
    }

    override fun getTransactionLiveData(id: String): LiveData<Transaction> {
        return transactionDao.findByIdLiveData(id)
    }

    private suspend fun recalculateBudget(newTransaction: Transaction) =
        transactionDao.findById(newTransaction.id)?.let { oldTransaction ->
            val newDate = newTransaction.createdAt
            val newWeek = DateConverter.localDateToSemanticWeek(newDate)
            val newTransactionValue = newTransaction.value
            val oldDate = oldTransaction.createdAt
            val oldWeek = DateConverter.localDateToSemanticWeek(oldDate)
            val oldTransactionValue = oldTransaction.value

            if (oldWeek == null || newWeek == null) return@let

            val oldWeekBudget = getBudgetBySemanticWeek(oldWeek)
            val newWeekBudget = getBudgetBySemanticWeek(newWeek)

            if (!oldWeek.isEqualTo(newWeek)) {
                val oldWeekExpense = oldWeekBudget.expense.minus(oldTransactionValue)
                budgetDao.updateExpense(oldWeekBudget.id, oldWeekExpense)

                val newWeekExpense = newWeekBudget.expense.plus(newTransactionValue)
                budgetDao.updateExpense(newWeekBudget.id, newWeekExpense)
            } else if (newTransactionValue != oldTransactionValue) {
                val newExpense = newWeekBudget.expense
                    .plus(newTransactionValue - oldTransactionValue)
                budgetDao.updateExpense(newWeekBudget.id, newExpense)
            }
        }

    private suspend fun getBudgetBySemanticWeek(semanticWeek: SemanticWeek): Budget {
        val budget = budgetDao.getWeekBudgetByWeekNumberAndYear(
            semanticWeek.weekNumber, semanticWeek.year
        )
        if (budget == null) {
            val newBudget = Budget(semanticWeek = semanticWeek)
            budgetDao.insert(newBudget)
            return newBudget
        }
        return budget
    }

    private suspend fun recalculateMonthBudget(newTransaction: Transaction) {
        transactionDao.findById(newTransaction.id)?.let { oldTransaction ->
            val oldTransactionDate = oldTransaction.createdAt
            val oldTransactionYearMonth =
                YearMonth.of(oldTransactionDate.year, oldTransactionDate.month)

            val newTransactionDate = newTransaction.createdAt
            val newTransactionYearMonth =
                YearMonth.of(newTransactionDate.year, newTransactionDate.monthValue)

            if (oldTransactionYearMonth == newTransactionYearMonth) {
                addNewNewExpenseToBudgetByYearMonth(
                    -oldTransaction.value,
                    oldTransactionDate.year,
                    oldTransactionDate.monthValue
                )
                addNewNewExpenseToBudgetByYearMonth(
                    newTransaction.value,
                    newTransactionDate.year,
                    newTransactionDate.monthValue
                )
            } else {
                addNewNewExpenseToBudgetByYearMonth(
                    newTransaction.value - oldTransaction.value,
                    newTransactionDate.year,
                    newTransactionDate.monthValue
                )
            }
        }
    }

    private suspend fun addNewNewExpenseToBudgetByYearMonth(
        newExpense: Int,
        year: Int,
        month: Int
    ) {
        val result = budgetDao.addNewExpenseByYearAndMonth(newExpense, year, month)
        if (result <= 0) {
            val semanticWeek = SemanticWeek(weekNumber = 0, month = month, year = year)
            val newBudget = Budget(expense = newExpense, semanticWeek = semanticWeek)
            budgetDao.insert(newBudget)
        }
    }

    private suspend fun recalculateBudgetOnTransactionDelete(transaction: Transaction) =
        DateConverter.localDateToSemanticWeek(transaction.createdAt)?.let {
            val budget = getBudgetBySemanticWeek(it)
            val newExpense = budget.expense.minus(transaction.value)
            budgetDao.updateExpense(budget.id, newExpense)
        }
}