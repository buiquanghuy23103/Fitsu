package com.huy.fitsu.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
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

    fun saveAccountBalance(accountBalance: Float) {
        transactionRepository.saveAccountBalance(accountBalance)
    }

}