package com.huy.fitsu.data.repository

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.huy.fitsu.data.local.BudgetSharedPrefManager
import com.huy.fitsu.data.local.FitsuDatabase
import com.huy.fitsu.data.model.*
import com.huy.fitsu.di.DispatcherModule
import com.huy.fitsu.util.round
import com.huy.fitsu.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.YearMonth
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    db: FitsuDatabase,
    budgetSharedPrefManager: BudgetSharedPrefManager,
    @DispatcherModule.IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) : TransactionRepository {

    private val transactionDao = db.transactionDao()
    private val transactionDetailDao = db.transactionDetailDao()
    private val budgetDao = db.budgetDao()
    private val defaultBudget = budgetSharedPrefManager.getDefaultBudget()

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

    override fun transactionCountByCategory(): LiveData<List<CategoryReport>> =
        transactionDetailDao.transactionCountByCategory()

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
                    (newTransaction.value - oldTransaction.value).round(2),
                    newTransactionDate.year,
                    newTransactionDate.monthValue
                )
            }
        }
    }

    private suspend fun addNewNewExpenseToBudgetByYearMonth(
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