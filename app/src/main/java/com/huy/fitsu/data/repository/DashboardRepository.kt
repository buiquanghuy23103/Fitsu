package com.huy.fitsu.data.repository

import androidx.lifecycle.LiveData

interface DashboardRepository {

    fun saveAccountBalance(accountBalance: Float)

    fun getAccountBalanceLiveData(): LiveData<Float>

}