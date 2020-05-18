package com.huy.fitsu.budgets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huy.fitsu.data.model.Event
import com.huy.fitsu.util.combineWith
import java.time.YearMonth
import javax.inject.Inject

class BudgetsViewModel @Inject constructor(
    defaultBudgetsRepository: BudgetsRepository
) : ViewModel() {

    private val currentYearMonth = YearMonth.now()

    private val _editTransactionEvent = MutableLiveData<Event<String>>()
    val editTransactionEvent: LiveData<Event<String>> = _editTransactionEvent

    val categoryExpensesLiveData =
        defaultBudgetsRepository.getCategoryExpenseOfYearMonth(currentYearMonth)

    val budgetLiveData = defaultBudgetsRepository.getBudgetLiveDataByYearMonth(currentYearMonth)

    fun budgetLeftLiveData(): LiveData<Float> =
        categoryExpensesLiveData.combineWith(budgetLiveData) { categoryExpenses, budget ->
            val monthExpense = if (categoryExpenses.isEmpty()) 0f
                else categoryExpenses.map { it.totalExpense }.reduce { a, b -> a + b }
            val budgetValue = budget.value
            budgetValue + monthExpense
        }

}