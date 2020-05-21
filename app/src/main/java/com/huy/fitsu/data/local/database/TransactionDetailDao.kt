package com.huy.fitsu.data.local.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Query
import com.huy.fitsu.data.model.CategoryExpense
import com.huy.fitsu.data.model.TransactionDetail

@Dao
interface TransactionDetailDao {

    @Query("SELECT * FROM TransactionDetail ORDER BY createdAt DESC")
    fun getAllLiveData(): LiveData<List<TransactionDetail>>

    @Query("SELECT * FROM TransactionDetail WHERE id = :id")
    fun findByIdLiveData(id: String): LiveData<TransactionDetail>

    @Query("SELECT SUM(value) as totalExpense, categoryTitle, categoryColor " +
            "FROM TransactionDetail " +
            "WHERE TransactionDetail.value < 0 " +
            "AND (TransactionDetail.createdAt BETWEEN :startEpochDay AND :endEpochDay) " +
            "GROUP BY categoryId " +
            "ORDER BY categoryTitle")
    fun getCategoryExpenseBetween(startEpochDay: Long?, endEpochDay: Long?): LiveData<List<CategoryExpense>>
}