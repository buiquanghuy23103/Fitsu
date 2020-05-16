package com.huy.fitsu.data.local

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.huy.fitsu.data.local.database.FitsuDatabase
import com.huy.fitsu.data.model.Budget
import com.huy.fitsu.di.DispatcherModule
import kotlinx.coroutines.CoroutineDispatcher
import java.time.YearMonth
import javax.inject.Inject

class BudgetLocalDataSource@Inject constructor(
    db: FitsuDatabase,
    private val fitsuSharedPrefManager: FitsuSharedPrefManager,
    @DispatcherModule.IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) {

    private val budgetDao = db.budgetDao()

    fun getBudgetLiveDataByYearMonth(yearMonth: YearMonth) : LiveData<Budget> = liveData(ioDispatcher) {
        var budget = budgetDao.findByYearMonth(yearMonth)
        if (budget == null) {
            val defaultBudgetValue = fitsuSharedPrefManager.getDefaultBudget()
            budget = Budget(
                yearMonth = yearMonth,
                value = defaultBudgetValue
            )
            budgetDao.insert(budget)
        }
        emit(budget!!)
    }

}