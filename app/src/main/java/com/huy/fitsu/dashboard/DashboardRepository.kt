package com.huy.fitsu.dashboard

import androidx.lifecycle.LiveData

interface DashboardRepository {

    fun saveAccountBalance(accountBalance: Float)

    fun getAccountBalanceLiveData(): LiveData<Float>

}