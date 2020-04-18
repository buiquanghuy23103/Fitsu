package com.huy.fitsu.data.repository

import androidx.paging.DataSource
import com.huy.fitsu.data.local.FitsuDatabase
import com.huy.fitsu.data.model.Budget
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    db: FitsuDatabase
): BudgetRepository {

    private val budgetDao = db.budgetDao()
    override fun getAllBudgets(): DataSource.Factory<Int, Budget>
        = budgetDao.getAll()
}