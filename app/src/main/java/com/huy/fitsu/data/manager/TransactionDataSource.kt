package com.huy.fitsu.data.manager

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.model.TransactionDetail

interface TransactionDataSource {

    suspend fun insertNewTransaction(transaction: Transaction)

    fun getTransactionDetails(): DataSource.Factory<Int, TransactionDetail>

    fun getTransaction(id: String): LiveData<Transaction>

}