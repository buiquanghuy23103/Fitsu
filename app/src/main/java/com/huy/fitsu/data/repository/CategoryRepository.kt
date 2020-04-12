package com.huy.fitsu.data.repository

import androidx.lifecycle.LiveData
import com.huy.fitsu.data.model.Category

interface CategoryRepository {

    fun getCategoriesLiveData(): LiveData<List<Category>>

    fun getCategoryLiveData(id: String): LiveData<Category>

    suspend fun insertNewCategory(category: Category)

    suspend fun updateCategory(category: Category)

    suspend fun deleteAllCategories()

}