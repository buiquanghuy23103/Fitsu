package com.huy.fitsu.data.local

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class BudgetSharedPrefManager @Inject constructor(
    private val sharedPref: SharedPreferences
) {
    private val MONTH_BUDGET_KEY = "month_budget"
    private val editor = sharedPref.edit()

    fun saveDefaultBudget(budgetValue: Float) = with(editor) {
        putFloat(MONTH_BUDGET_KEY, budgetValue)
        apply()
    }

    fun getDefaultBudget(): Float =
        sharedPref.getFloat(MONTH_BUDGET_KEY, 0F)

}