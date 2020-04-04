package com.huy.fitsu.data.repository

import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Result

interface CategoryRepositoryUpgrade {

    suspend fun getCategories(): Result<List<Category>>

    suspend fun getCategory(id: String): Result<Category>

    suspend fun insertNewCategory(category: Category)

    suspend fun updateCategory(category: Category)

}