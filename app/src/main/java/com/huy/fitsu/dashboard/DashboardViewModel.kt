package com.huy.fitsu.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.huy.fitsu.data.model.CategoryReport
import com.huy.fitsu.data.repository.TransactionRepository
import com.huy.fitsu.util.toCurrencyString
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository
) : ViewModel() {

    // Used by data binding
    val accountBalance = transactionRepository.getAccountBalanceLiveData()

    // Used by data binding
    val accountBalanceString = accountBalance.map { it.toCurrencyString() }

    val transactionCountByCategory: LiveData<List<CategoryReport>> =
        transactionRepository.transactionCountByCategory()

    fun saveAccountBalance(accountBalance: Float) {
        transactionRepository.saveAccountBalance(accountBalance)
    }

}