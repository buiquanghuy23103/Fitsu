package com.huy.fitsu.addEditTransaction

import androidx.lifecycle.LiveData
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Transaction

interface AddEditTransactionRepository {

    fun getTransactionLiveDataById(id: String) : LiveData<Transaction>

    fun getCategoriesLiveData() : LiveData<List<Category>>

    suspend fun deleteTransaction(transaction: Transaction)

    suspend fun updateTransaction(newTransaction: Transaction)

}