package com.huy.fitsu.data.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.*
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.model.TransactionDetail

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewTransaction(transaction: Transaction)

    @Query("SELECT * FROM TransactionDetail WHERE id = :id")
    fun getTransactionDetail(id: String): LiveData<TransactionDetail>

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun getTransactionLiveData(id: String): LiveData<Transaction>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun getTransaction(id: String): Transaction

    @Query("SELECT * FROM TransactionDetail ORDER BY date DESC")
    fun getTransactionDetails(): DataSource.Factory<Int, TransactionDetail>

    @Update
    suspend fun updateTransaction(transaction: Transaction)

    @Query("DELETE FROM transactions")
    suspend fun deleteAllTransactions()

}