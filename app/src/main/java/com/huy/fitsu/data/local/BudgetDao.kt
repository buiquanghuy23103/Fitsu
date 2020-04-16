package com.huy.fitsu.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.huy.fitsu.data.model.Budget

@Dao
interface BudgetDao {

    @Query("SELECT * FROM budgets WHERE year = :year AND weekNumber = :weekNumber")
    suspend fun getBySemanticWeek(year: Int, weekNumber: Int): Budget?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: Budget)

    @Query("UPDATE budgets SET expense = expense + :transactionValue WHERE id = :id")
    suspend fun addTransactionValue(id: String, transactionValue: Int)


}