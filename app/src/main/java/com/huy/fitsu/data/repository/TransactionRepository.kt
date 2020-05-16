package com.huy.fitsu.data.repository

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.huy.fitsu.data.model.Budget
import com.huy.fitsu.data.model.CategoryExpense
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.model.TransactionDetail
import java.time.YearMonth

interface TransactionRepository {

    suspend fun getTransaction(id: String) : Transaction?

    suspend fun insertNewTransaction(transaction: Transaction)

    @VisibleForTesting
    suspend fun deleteAllTransactions()

    suspend fun deleteTransaction(transaction: Transaction)

    fun getTransactionDetailPagedList(): LiveData<PagedList<TransactionDetail>>

    fun getTransactionDetailLiveData(id: String): LiveData<TransactionDetail>

    fun getTransactionLiveData(id: String): LiveData<Transaction>

    suspend fun updateTransaction(transaction: Transaction)

    fun getCategoryExpenseByYearMonth(yearMonth: YearMonth): LiveData<List<CategoryExpense>>

    fun getAccountBalanceLiveData(): LiveData<Float>

    fun saveAccountBalance(accountBalance: Float)

    fun getBudgetLiveDataByYearMonth(yearMonth: YearMonth): LiveData<Budget>

}