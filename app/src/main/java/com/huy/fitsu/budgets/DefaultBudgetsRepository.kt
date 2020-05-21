package com.huy.fitsu.budgets

import androidx.lifecycle.LiveData
import com.huy.fitsu.data.local.FitsuSharedPrefManager
import com.huy.fitsu.data.local.database.DatabaseTypeConverters
import com.huy.fitsu.data.local.database.FitsuDatabase
import com.huy.fitsu.data.model.Budget
import com.huy.fitsu.data.model.CategoryExpense
import com.huy.fitsu.di.DispatcherModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import java.time.YearMonth
import javax.inject.Inject

class DefaultBudgetsRepository @Inject constructor(
    db: FitsuDatabase,
    private val fitsuSharedPrefManager: FitsuSharedPrefManager,
    @DispatcherModule.IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) : BudgetsRepository {

    private val budgetDao = db.budgetDao()
    private val transactionDetailDao = db.transactionDetailDao()

    override fun getCategoryExpenseOfYearMonth(yearMonth: YearMonth): LiveData<List<CategoryExpense>> {
        val startDateOfMonth = yearMonth.atDay(1)
        val start = DatabaseTypeConverters.fromLocalDate(startDateOfMonth)

        val endDateOfMonth = yearMonth.atEndOfMonth()
        val end = DatabaseTypeConverters.fromLocalDate(endDateOfMonth)

        return transactionDetailDao.getCategoryExpenseBetween(start, end)
    }

    override fun getBudgetLiveDataById(id: String): LiveData<Budget> {
        return budgetDao.getLiveDataById(id)
    }

    override suspend fun updateBudget(budget: Budget) =
        withContext(ioDispatcher) {
            budgetDao.update(budget)
        }

    override fun getAllBudgetsLiveData(): LiveData<List<Budget>> {
        return budgetDao.getAllLiveData()
    }
}