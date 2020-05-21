package com.huy.fitsu.addEditTransaction

import com.huy.fitsu.data.local.FitsuSharedPrefManager
import com.huy.fitsu.data.local.database.FitsuDatabase
import com.huy.fitsu.data.model.Budget
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.di.DispatcherModule
import com.huy.fitsu.util.round
import com.huy.fitsu.util.toYearMonth
import com.huy.fitsu.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.YearMonth
import javax.inject.Inject

class DefaultAddEditTransactionRepository @Inject constructor(
    db: FitsuDatabase,
    private val fitsuSharedPrefManager: FitsuSharedPrefManager,
    @DispatcherModule.IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) : AddEditTransactionRepository {


    private val transactionDao = db.transactionDao()
    private val categoryDao = db.categoryDao()
    private val budgetDao = db.budgetDao()

    override fun getTransactionLiveDataById(id: String) =
        transactionDao.findByIdLiveData(id)

    override fun getCategoriesLiveData() =
        categoryDao.getAllLiveData()

    override suspend fun deleteTransaction(transaction: Transaction) = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            transactionDao.delete(transaction)
            recalculateBalance(0f, transaction.value)
        }
    }

    override suspend fun updateTransaction(newTransaction: Transaction) = withContext(ioDispatcher) {
        wrapEspressoIdlingResource {
            transactionDao.findById(newTransaction.id)?.let { oldTransaction ->
                recalculateBalance(newTransaction.value, oldTransaction.value)
            }

            checkBudgetOfCurrentTransaction(newTransaction)

            transactionDao.update(newTransaction)
        }
    }

    private fun recalculateBalance(newTransactionValue: Float, oldTransactionValue: Float) {
        val difference = newTransactionValue - oldTransactionValue
        val oldBalance = fitsuSharedPrefManager.getAccountBalanceValue()
        val newBalance = oldBalance + difference
        fitsuSharedPrefManager.saveAccountBalance(newBalance.round(2))
    }

    private suspend fun checkBudgetOfCurrentTransaction(transaction: Transaction) {
        val yearMonth = transaction.createdAt.toYearMonth()
        if (budgetDao.findByYearMonth(yearMonth) == null) {
            addBudgetByYearMonth(yearMonth)
        }
    }

    private suspend fun addBudgetByYearMonth(yearMonth: YearMonth) {
        val newBudget = Budget(
            yearMonth = yearMonth,
            value = fitsuSharedPrefManager.getDefaultBudget()
        )

        budgetDao.insert(newBudget)
    }

}