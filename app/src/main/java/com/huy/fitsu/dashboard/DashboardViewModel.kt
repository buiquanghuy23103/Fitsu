package com.huy.fitsu.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import com.huy.fitsu.data.repository.DashboardRepository
import com.huy.fitsu.util.toCurrencyString
import javax.inject.Inject

class DashboardViewModel @Inject constructor(
    private val dashboardRepository: DashboardRepository
) : ViewModel() {

    // Used by data binding
    val accountBalance = dashboardRepository.getAccountBalanceLiveData()

    // Used by data binding
    val accountBalanceString = accountBalance.map { it.toCurrencyString() }

    fun saveAccountBalance(accountBalance: Float) {
        dashboardRepository.saveAccountBalance(accountBalance)
    }

}