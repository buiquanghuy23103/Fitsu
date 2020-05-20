package com.huy.fitsu.transactions

import androidx.lifecycle.LiveData
import com.huy.fitsu.data.local.database.FitsuDatabase
import com.huy.fitsu.data.model.TransactionDetail
import javax.inject.Inject

class DefaultTransactionRepository @Inject constructor(
    db: FitsuDatabase
): TransactionsRepository {

    private val transactionDao = db.transactionDao()

    override fun getTransactionsLiveData(): LiveData<List<TransactionDetail>> {
        return transactionDao.getAllLiveData()
    }
}