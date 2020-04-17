package com.huy.fitsu.data.repository

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.model.TransactionDetail

interface TransactionRepository {

    suspend fun getTransaction(id: String) : Transaction?

    suspend fun insertNewTransaction(transaction: Transaction)

    @VisibleForTesting
    suspend fun deleteAllTransactions()

    suspend fun deleteTransaction(id: String)

    fun getTransactionDetailPagedList(): LiveData<PagedList<TransactionDetail>>

    fun getTransactionDetailLiveData(id: String): LiveData<TransactionDetail>

    fun getTransactionLiveData(id: String): LiveData<Transaction>

    suspend fun updateTransaction(transaction: Transaction)

}