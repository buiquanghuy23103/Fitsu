package com.huy.fitsu.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.huy.fitsu.data.model.Budget

@Dao
interface BudgetDao {

    @Query("SELECT * FROM budgets WHERE year = :year AND weekNumber = :weekNumber")
    suspend fun getWeekBudget(weekNumber: Int, year: Int): Budget?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: Budget)

    @Query("UPDATE budgets SET expense = :expense WHERE id = :id")
    suspend fun updateExpense(id: String, expense: Int)


}