package com.huy.fitsu.data.repository

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import com.huy.fitsu.data.model.Budget
import com.huy.fitsu.data.model.CategoryExpense
import com.huy.fitsu.data.model.Transaction
import java.time.YearMonth

interface TransactionRepository {

    suspend fun insertNewTransaction(transaction: Transaction)

    @VisibleForTesting
    suspend fun deleteAllTransactions()

    suspend fun deleteTransaction(transaction: Transaction)

    fun getTransactionLiveDataById(id: String): LiveData<Transaction>

    suspend fun updateTransaction(transaction: Transaction)

    fun getCategoryExpenseByYearMonth(yearMonth: YearMonth): LiveData<List<CategoryExpense>>

    fun getBudgetLiveDataByYearMonth(yearMonth: YearMonth): LiveData<Budget>

}