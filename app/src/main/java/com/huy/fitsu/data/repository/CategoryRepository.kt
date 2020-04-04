package com.huy.fitsu.data.repository

import androidx.lifecycle.LiveData
import com.huy.fitsu.data.model.Category

interface CategoryRepository {

    fun getCategories(): LiveData<List<Category>>

    fun getCategory(id: String): LiveData<Category>

    suspend fun insertNewCategory(category: Category)

    suspend fun updateCategory(category: Category)

}