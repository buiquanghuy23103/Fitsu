package com.huy.fitsu.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.*
import com.huy.fitsu.data.model.Budget
import java.time.YearMonth

@Dao
interface BudgetDao {

    @Query("SELECT * FROM budgets WHERE id = :id")
    fun getLiveDataById(id: String): LiveData<Budget>

    @Query("SELECT * FROM budgets ORDER BY year_month")
    fun getAllLiveData(): LiveData<List<Budget>>

    @Query("SELECT * FROM budgets WHERE year_month = :yearMonth")
    suspend fun findByYearMonth(yearMonth: YearMonth): Budget?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: Budget)

    @Query("DELETE FROM budgets")
    suspend fun deleteAll()

    @Update
    suspend fun update(budget: Budget)
}