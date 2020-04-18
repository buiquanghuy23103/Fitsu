package com.huy.fitsu.data.repository

import androidx.paging.DataSource
import com.huy.fitsu.data.model.Budget

interface BudgetRepository {

    fun getAllBudgets(): DataSource.Factory<Int, Budget>

}