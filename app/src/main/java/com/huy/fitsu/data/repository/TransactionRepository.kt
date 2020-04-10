package com.huy.fitsu.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.model.TransactionDetail

interface TransactionRepository {

    suspend fun insertNewTransaction(transaction: Transaction)

    suspend fun deleteAllTransactions()  // for testing

    fun getTransactionDetails(): LiveData<PagedList<TransactionDetail>>

    fun getTransaction(id: String): LiveData<Transaction>

}