package com.huy.fitsu.data.repository

import androidx.lifecycle.LiveData
import com.huy.fitsu.data.local.FitsuSharedPrefManager
import javax.inject.Inject

class DashboardRepositoryImpl @Inject constructor(
    private val fitsuSharedPrefManager: FitsuSharedPrefManager
): DashboardRepository {

    override fun saveAccountBalance(accountBalance: Float) {
        fitsuSharedPrefManager.saveAccountBalance(accountBalance)
    }

    override fun getAccountBalanceLiveData(): LiveData<Float> {
        return fitsuSharedPrefManager.getAccountBalanceLiveData()
    }
}