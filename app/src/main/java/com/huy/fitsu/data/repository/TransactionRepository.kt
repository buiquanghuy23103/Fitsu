package com.huy.fitsu.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.model.TransactionDetail

interface TransactionRepository {

    suspend fun insertNewTransaction(transaction: Transaction)

    suspend fun deleteAllTransactions()  // for testing

    fun getTransactionDetails(): DataSource.Factory<Int, TransactionDetail>

    fun getTransaction(id: String): LiveData<Transaction>

}