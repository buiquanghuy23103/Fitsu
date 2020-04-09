package com.huy.fitsu.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.huy.fitsu.data.model.Category

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories WHERE id = :id")
    fun findById(id: String): LiveData<Category>

    @Query("SELECT * FROM categories ORDER BY title")
    fun getAll(): LiveData<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

    @Query("DELETE FROM categories")
    suspend fun deleteAllCategories() // Use for testing

}