package com.huy.fitsu.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import com.huy.fitsu.data.model.Budget
import com.huy.fitsu.data.repository.BudgetRepository
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val budgetRepository: BudgetRepository
) : ViewModel() {

    val budgets: LiveData<PagedList<Budget>> =
        budgetRepository.getAllBudgets()
    val defaultBudget: Float = budgetRepository.getDefaultBudget()

    fun updateDefaultBudget(newBudgetValue: Float) {
        budgetRepository.updateDefaultBudget(newBudgetValue)
    }

}