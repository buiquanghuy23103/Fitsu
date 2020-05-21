package com.huy.fitsu.budgets

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class BudgetsViewPagerViewModel @Inject constructor(
    repository: BudgetsRepository
) : ViewModel() {

    val budgetsLiveData = repository.getAllBudgetsLiveData()

}