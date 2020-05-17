package com.huy.fitsu.budgets

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.huy.fitsu.data.local.FitsuSharedPrefManager
import com.huy.fitsu.data.local.database.DatabaseTypeConverters
import com.huy.fitsu.data.local.database.FitsuDatabase
import com.huy.fitsu.data.model.Budget
import com.huy.fitsu.data.model.CategoryExpense
import com.huy.fitsu.di.DispatcherModule
import kotlinx.coroutines.CoroutineDispatcher
import java.time.YearMonth
import javax.inject.Inject

class BudgetsRepository @Inject constructor(
    db: FitsuDatabase,
    private val fitsuSharedPrefManager: FitsuSharedPrefManager,
    @DispatcherModule.IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) {

    private val budgetDao = db.budgetDao()
    private val transactionDetailDao = db.transactionDetailDao()

    fun getCategoryExpenseOfYearMonth(yearMonth: YearMonth): LiveData<List<CategoryExpense>> {
        val startDateOfMonth = yearMonth.atDay(1)
        val start = DatabaseTypeConverters.fromLocalDate(startDateOfMonth)

        val endDateOfMonth = yearMonth.atEndOfMonth()
        val end = DatabaseTypeConverters.fromLocalDate(endDateOfMonth)

        return transactionDetailDao.getCategoryExpenseBetween(start, end)
    }

    fun getBudgetLiveDataByYearMonth(yearMonth: YearMonth) : LiveData<Budget> = liveData(ioDispatcher) {
        var budget = budgetDao.findByYearMonth(yearMonth)
        if (budget == null) {
            val defaultBudgetValue = fitsuSharedPrefManager.getDefaultBudget()
            budget = Budget(
                yearMonth = yearMonth,
                value = defaultBudgetValue
            )
            budgetDao.insert(budget)
        }
        emit(budget!!)
    }
}