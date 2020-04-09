package com.huy.fitsu.data.manager

import androidx.lifecycle.LiveData
import com.huy.fitsu.data.model.Category

interface CategoryDataSource {

    fun getCategory(id: String): LiveData<Category>

    fun getCategories(): LiveData<List<Category>>

    suspend fun insertNewCategory(category: Category)

    suspend fun updateCategory(category: Category)

    suspend fun deleteAllCategories()

}