package com.huy.fitsu.data.local.database

import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.huy.fitsu.data.model.Budget

@Dao
interface BudgetDao {

    @Query("SELECT * FROM budgets")
    fun getAll(): DataSource.Factory<Int, Budget>

    @Query("SELECT * FROM budgets WHERE id = :id")
    suspend fun findById(id: String): Budget

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(budget: Budget)

    @Query("DELETE FROM budgets")
    suspend fun deleteAll()
}