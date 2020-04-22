package com.huy.fitsu.data.local

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.huy.fitsu.data.model.Budget

@Dao
interface BudgetDao {

    @Query("SELECT * FROM budgets WHERE year = :year AND weekNumber = :weekNumber")
    suspend fun getWeekBudgetByWeekNumberAndYear(weekNumber: Int, year: Int): Budget?

    @Query("SELECT * FROM budgets WHERE month = :month AND year = :year")
    suspend fun getBudgetByMonthAndYear(month: Int, year: Int): Budget?

    @Query("SELECT * FROM budgets")
    fun getAll(): DataSource.Factory<Int, Budget>

    @Query("SELECT * FROM budgets WHERE id = :id")
    suspend fun findById(id: String): Budget

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: Budget)

    @Query("UPDATE budgets SET expense = :expense WHERE id = :id")
    suspend fun updateExpense(id: String, expense: Int)

    @Query("UPDATE budgets SET expense = expense + :newExpense WHERE year = :year AND month = :month")
    suspend fun addNewExpenseByYearAndMonth(newExpense: Int, year: Int, month: Int): Int


}