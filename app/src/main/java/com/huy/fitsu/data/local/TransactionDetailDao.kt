package com.huy.fitsu.data.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.huy.fitsu.data.model.CategoryReport
import com.huy.fitsu.data.model.TransactionDetail

@Dao
interface TransactionDetailDao {

    @Query("SELECT * FROM TransactionDetail WHERE id = :id")
    fun findByIdLiveData(id: String): LiveData<TransactionDetail>

    @Query("SELECT * FROM TransactionDetail ORDER BY createdAt DESC")
    fun getPagedListLiveData(): DataSource.Factory<Int, TransactionDetail>

    @Query("SELECT SUM(value) as transactionSum, categoryTitle, categoryColor FROM TransactionDetail GROUP BY categoryId")
    fun transactionCountByCategory(): LiveData<List<CategoryReport>>
}