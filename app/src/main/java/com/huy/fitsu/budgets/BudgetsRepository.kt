package com.huy.fitsu.budgets

import androidx.lifecycle.LiveData
import com.huy.fitsu.data.model.Budget
import com.huy.fitsu.data.model.CategoryExpense
import java.time.YearMonth

interface BudgetsRepository {

    fun getCategoryExpenseOfYearMonth(yearMonth: YearMonth): LiveData<List<CategoryExpense>>

    fun getBudgetLiveDataByYearMonth(yearMonth: YearMonth) : LiveData<Budget>

    suspend fun updateBudget(budget: Budget)

}