package com.huy.fitsu.budgets

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import androidx.lifecycle.viewModelScope
import com.huy.fitsu.data.model.Budget
import com.huy.fitsu.data.model.CategoryExpense
import com.huy.fitsu.di.DispatcherModule
import com.huy.fitsu.util.combineWith
import com.huy.fitsu.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class BudgetsViewModel @Inject constructor(
    private val repository: BudgetsRepository,
    @DispatcherModule.MainDispatcher
    private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var budgetId = ""

    fun setBudgetId(id: String) {
        this.budgetId = id
    }

    val budgetLiveData : LiveData<Budget>
        get() = repository.getBudgetLiveDataById(budgetId)

    val categoryExpensesLiveData : LiveData<List<CategoryExpense>>
        get() = budgetLiveData.switchMap { budget ->
            repository.getCategoryExpenseOfYearMonth(budget.yearMonth)
        }

    fun budgetLeftLiveData(): LiveData<Float> =
        categoryExpensesLiveData.combineWith(budgetLiveData) { categoryExpenses, budget ->
            val monthExpense = if (categoryExpenses.isEmpty()) 0f
                else categoryExpenses.map { it.totalExpense }.reduce { a, b -> a + b }
            val budgetValue = budget.value
            budgetValue + monthExpense
        }

    fun updateBudget(budget: Budget) {
        wrapEspressoIdlingResource {
            viewModelScope.launch(mainDispatcher) {
                repository.updateBudget(budget)
            }
        }
    }
}