package com.huy.fitsu.data.local

import androidx.lifecycle.LiveData
import androidx.paging.DataSource
import androidx.room.Dao
import androidx.room.Query
import com.huy.fitsu.data.model.TransactionDetail

@Dao
interface TransactionDetailDao {

    @Query("SELECT * FROM TransactionDetail WHERE id = :id")
    fun findByIdLiveData(id: String): LiveData<TransactionDetail>

    @Query("SELECT * FROM TransactionDetail ORDER BY date DESC")
    fun getPagedListLiveData(): DataSource.Factory<Int, TransactionDetail>


}