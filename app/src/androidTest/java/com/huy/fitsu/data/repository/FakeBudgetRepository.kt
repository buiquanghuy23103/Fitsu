package com.huy.fitsu.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.PagedList
import com.huy.fitsu.data.model.Budget
import com.huy.fitsu.toTestPagedList
import com.huy.fitsu.util.DateConverter
import java.time.LocalDate
import javax.inject.Inject

class FakeBudgetRepository @Inject constructor(): BudgetRepository {

    private val thisWeek = DateConverter.localDateToSemanticWeek(LocalDate.now())
    private val budgets = mutableListOf(Budget(semanticWeek = thisWeek!!))

    override fun getAllBudgets(): LiveData<PagedList<Budget>> = liveData {
        emit(budgets.toTestPagedList())
    }
}