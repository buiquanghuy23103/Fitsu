package com.huy.fitsu.data.local

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton


class BudgetSharedPrefManager @Singleton @Inject constructor(
    private val sharedPref: SharedPreferences
) {
    private val MONTH_BUDGET_KEY = "month_budget"
    private val editor = sharedPref.edit()

    fun saveDefaultBudget(budgetValue: Int) = with(editor) {
        putInt(MONTH_BUDGET_KEY, budgetValue)
        apply()
    }

    fun getDefaultBudget(): Int =
        sharedPref.getInt(MONTH_BUDGET_KEY, 0)

}