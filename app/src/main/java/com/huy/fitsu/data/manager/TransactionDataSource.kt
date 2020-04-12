package com.huy.fitsu.data.manager

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.model.TransactionDetail

interface TransactionDataSource {

    suspend fun getTransaction(id: String): Transaction?

    suspend fun insertNewTransaction(transaction: Transaction)

    suspend fun deleteAllTransactions()

    suspend fun updateTransaction(transaction: Transaction)

    fun getTransactionDetails(): DataSource.Factory<Int, TransactionDetail>

    fun getTransactionDetail(id: String): LiveData<TransactionDetail>

    fun getTransactionLiveData(id: String): LiveData<Transaction>

}