package com.huy.fitsu.budgets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.huy.fitsu.budgetMay
import com.huy.fitsu.categoryFood
import com.huy.fitsu.data.model.Budget
import com.huy.fitsu.data.model.CategoryExpense
import com.huy.fitsu.transactionFoodMay
import com.huy.fitsu.transactionFoodMay2
import java.time.YearMonth

class FakeBudgetsRepository : BudgetsRepository {

    override fun getCategoryExpenseOfYearMonth(yearMonth: YearMonth): LiveData<List<CategoryExpense>> {
        val categoryExpense = CategoryExpense(
            categoryTitle = categoryFood.title,
            categoryColor = categoryFood.color,
            totalExpense = transactionFoodMay.value + transactionFoodMay2.value
        )

        val categoryExpenses = listOf(categoryExpense)

        return MutableLiveData(categoryExpenses)
    }

    override fun getBudgetLiveDataByYearMonth(yearMonth: YearMonth): LiveData<Budget> =
        MutableLiveData(budgetMay)
}