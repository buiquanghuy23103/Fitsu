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
import java.time.LocalDate
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
                recalculateWeekBudget(transaction.createdAt)
            }
        }
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                recalculateBudget(transaction)
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

    private suspend fun recalculateWeekBudget(date: LocalDate) {
        val firstDayOfWeek = DateConverter.firstDayOfWeek(date)
            ?.toEpochDay() ?: 0
        val lastDayOfWeek = DateConverter.lastDayOfWeek(date)
            ?.toEpochDay() ?: 0
        val weekReport = transactionDao.calculateExpense(firstDayOfWeek, lastDayOfWeek)
        DateConverter.localDateToSemanticWeek(date)?.let {
            budgetDao.getWeekBudgetByWeekNumberAndYear(it.weekNumber, it.year)
        }?.let { budget ->
            budgetDao.updateExpense(budget.id, weekReport.sum)
        }
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

            if (oldWeek != newWeek) {
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
}