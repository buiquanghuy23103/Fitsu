package com.huy.fitsu.dashboard

import androidx.lifecycle.LiveData
import com.huy.fitsu.data.local.FitsuSharedPrefManager
import javax.inject.Inject

class DashboardRepository @Inject constructor(
    private val fitsuSharedPrefManager: FitsuSharedPrefManager
) {

    fun saveAccountBalance(accountBalance: Float) {
        fitsuSharedPrefManager.saveAccountBalance(accountBalance)
    }

    fun getAccountBalanceLiveData(): LiveData<Float> {
        return fitsuSharedPrefManager.getAccountBalanceLiveData()
    }

}