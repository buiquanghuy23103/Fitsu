package com.huy.fitsu.data.local.database

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.huy.fitsu.data.model.Budget
import java.time.YearMonth

@Dao
interface BudgetDao {

    @Query("SELECT * FROM budgets WHERE year_month = :yearMonth")
    suspend fun findByYearMonth(yearMonth: YearMonth): Budget?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: Budget)

    @Query("DELETE FROM budgets")
    suspend fun deleteAll()
}