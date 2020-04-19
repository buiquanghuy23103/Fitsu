package com.huy.fitsu.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.huy.fitsu.data.local.FitsuDatabase
import com.huy.fitsu.data.model.Budget
import javax.inject.Inject

class BudgetRepositoryImpl @Inject constructor(
    db: FitsuDatabase
) : BudgetRepository {

    private val budgetDao = db.budgetDao()

    override fun getAllBudgets(): LiveData<PagedList<Budget>> =
        budgetDao.getAll().toLiveData(pageSize = 5)
}