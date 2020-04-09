package com.huy.fitsu.addEditTransaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.repository.TransactionRepository
import javax.inject.Inject

class AddEditTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    private var transactionId = ""

    fun withTransactionId(id: String) {
        this.transactionId = id
    }

    val transaction : LiveData<Transaction>
        get() = transactionRepository.getTransaction(transactionId)

}