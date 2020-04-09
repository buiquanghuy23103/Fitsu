package com.huy.fitsu.data.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.model.TransactionDetail

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewTransaction(transaction: Transaction)

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getTransaction(id: String): LiveData<Transaction>

    @Query("SELECT * FROM TransactionDetail ORDER BY date DESC")
    fun getTransactionDetails(): DataSource.Factory<Int, TransactionDetail>

    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()

}