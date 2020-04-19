package com.huy.fitsu.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.huy.fitsu.data.model.Budget
import com.huy.fitsu.data.repository.BudgetRepository
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    budgetRepository: BudgetRepository
) : ViewModel() {

    val budgets: LiveData<PagedList<Budget>> =
        budgetRepository.getAllBudgets().toLiveData(pageSize = 5)

}