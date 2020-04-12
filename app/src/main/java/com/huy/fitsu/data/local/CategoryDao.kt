package com.huy.fitsu.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.huy.fitsu.data.model.Category

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories WHERE id = :id")
    fun findByIdLiveData(id: String): LiveData<Category>

    @Query("SELECT * FROM categories ORDER BY title")
    fun getAllLiveData(): LiveData<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(category: Category)

    @Update
    suspend fun update(category: Category)

    @Query("DELETE FROM categories")
    suspend fun deleteAll() // Use for testing

}