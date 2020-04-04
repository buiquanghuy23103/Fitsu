package com.huy.fitsu.data.manager

import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Result

interface CategoryDataSource {

    suspend fun getCategory(id: String): Result<Category>

    suspend fun getCategories(): Result<List<Category>>

    suspend fun insertNewCategory(category: Category)

    suspend fun updateCategory(category: Category)

}