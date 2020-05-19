package com.huy.fitsu.budgets

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huy.fitsu.data.model.Event
import com.huy.fitsu.di.DispatcherModule
import com.huy.fitsu.util.combineWith
import com.huy.fitsu.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import java.time.YearMonth
import javax.inject.Inject

class BudgetsViewModel @Inject constructor(
    private val repository: BudgetsRepository,
    @DispatcherModule.MainDispatcher
    private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val currentYearMonth = YearMonth.now()

    private val _editTransactionEvent = MutableLiveData<Event<String>>()
    val editTransactionEvent: LiveData<Event<String>> = _editTransactionEvent

    val categoryExpensesLiveData =
        repository.getCategoryExpenseOfYearMonth(currentYearMonth)

    val budgetLiveData = repository.getBudgetLiveDataByYearMonth(currentYearMonth)

    fun budgetLeftLiveData(): LiveData<Float> =
        categoryExpensesLiveData.combineWith(budgetLiveData) { categoryExpenses, budget ->
            val monthExpense = if (categoryExpenses.isEmpty()) 0f
                else categoryExpenses.map { it.totalExpense }.reduce { a, b -> a + b }
            val budgetValue = budget.value
            budgetValue + monthExpense
        }

    fun updateBudgetValue(value: Float) {
        wrapEspressoIdlingResource {
            viewModelScope.launch(mainDispatcher) {
                repository.updateBudgetValueByYearMonth(value, currentYearMonth)
            }
        }
    }
}