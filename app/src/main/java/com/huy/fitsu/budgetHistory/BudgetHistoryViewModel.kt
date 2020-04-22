package com.huy.fitsu.budgetHistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.huy.fitsu.data.model.Budget
import com.huy.fitsu.data.repository.BudgetRepository
import javax.inject.Inject

class BudgetHistoryViewModel @Inject constructor(
    budgetRepository: BudgetRepository
) : ViewModel() {

    val budgets: LiveData<PagedList<Budget>> =
        budgetRepository.getAllBudgets()

}