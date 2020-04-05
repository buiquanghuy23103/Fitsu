package com.huy.fitsu.data.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.huy.fitsu.data.model.Transaction

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getTransaction(id: String): LiveData<Transaction>

    @Query("SELECT * FROM transactions ORDER BY date DESC")
    fun getAllTransactionsByDate(): DataSource.Factory<Int, Transaction>

}