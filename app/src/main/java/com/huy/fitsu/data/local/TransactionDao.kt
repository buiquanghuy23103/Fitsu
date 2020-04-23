package com.huy.fitsu.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.huy.fitsu.data.model.Transaction

@Dao
interface TransactionDao {

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(transaction: Transaction)

    @Query("SELECT * FROM transactions WHERE id = :id")
    fun findByIdLiveData(id: String): LiveData<Transaction>

    @Query("SELECT * FROM transactions WHERE id = :id")
    suspend fun findById(id: String): Transaction?

    @Update
    suspend fun update(transaction: Transaction)

    @Query("DELETE FROM transactions")
    suspend fun deleteAll()

    @Query("DELETE FROM transactions WHERE categoryId = :categoryId")
    suspend fun deleteByCategoryId(categoryId: String)

    @Delete
    suspend fun delete(transaction: Transaction)

}