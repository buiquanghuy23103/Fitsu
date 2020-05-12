package com.huy.fitsu.data.local

import android.content.SharedPreferences
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class FitsuSharedPrefManager @Inject constructor(
    private val sharedPref: SharedPreferences
) {
    private val monthBudgetKey = "month_budget"
    private val accountBalanceKey = "account_balance"
    private val editor = sharedPref.edit()

    fun saveDefaultBudget(budgetValue: Float) = with(editor) {
        putFloat(monthBudgetKey, budgetValue)
        apply()
    }

    fun getDefaultBudget(): Float =
        sharedPref.getFloat(monthBudgetKey, 0F)

    fun saveAccountBalance(accountBalanceValue: Float) = with(editor) {
        putFloat(accountBalanceKey, accountBalanceValue)
        apply()
    }

    fun getAccountBalanceValue(): Float =
        sharedPref.getFloat(accountBalanceKey, Float.MIN_VALUE)

}