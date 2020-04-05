package com.huy.fitsu.data.manager

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import com.huy.fitsu.data.model.Transaction

interface TransactionDataSource {

    suspend fun insertNewTransaction(transaction: Transaction)

    fun getTransactions(): DataSource.Factory<Int, Transaction>

    fun getTransaction(id: String): LiveData<Transaction>

}