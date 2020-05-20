package com.huy.fitsu.transactions

import androidx.lifecycle.LiveData
import com.huy.fitsu.data.model.TransactionDetail

interface TransactionsRepository {
    fun getTransactionsLiveData(): LiveData<List<TransactionDetail>>
}