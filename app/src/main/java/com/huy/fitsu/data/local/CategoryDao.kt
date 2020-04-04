package com.huy.fitsu.data.local

import androidx.lifecycle.LiveData
import androidx.room.*
import com.huy.fitsu.data.model.Category
import io.reactivex.Completable

@Dao
interface CategoryDao {

    @Query("SELECT * FROM categories WHERE id = :id")
    fun findById(id: String): LiveData<Category>

    @Query("SELECT * FROM categories")
    fun getAll(): LiveData<List<Category>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insert(category: Category): Completable

    @Update
    fun update(category: Category): Completable


    @Query("SELECT * FROM categories WHERE id = :id")
    suspend fun getCategoryById(id: String): Category

    @Query("SELECT * FROM categories")
    suspend fun getAllCategories(): List<Category>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewCategory(category: Category)

    @Update
    suspend fun updateCategory(category: Category)

}