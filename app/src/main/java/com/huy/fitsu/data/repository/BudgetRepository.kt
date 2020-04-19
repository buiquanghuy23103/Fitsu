package com.huy.fitsu.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.huy.fitsu.data.model.Budget

interface BudgetRepository {

    fun getAllBudgets(): LiveData<PagedList<Budget>>

}