package com.huy.fitsu.addEditTransaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.huy.fitsu.categoryClothes
import com.huy.fitsu.categoryFood
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.transactionFoodMay

class FakeAddEditTransactionRepository : AddEditTransactionRepository {

    val transaction = transactionFoodMay
    val categories = listOf(categoryFood, categoryClothes)

    override fun getTransactionLiveDataById(id: String): LiveData<Transaction> {
        return MutableLiveData(transaction)
    }

    override fun getCategoriesLiveData(): LiveData<List<Category>> {
        return MutableLiveData(categories)
    }

    override suspend fun deleteTransaction(transaction: Transaction) {

    }

    override suspend fun updateTransaction(transaction: Transaction) {

    }
}