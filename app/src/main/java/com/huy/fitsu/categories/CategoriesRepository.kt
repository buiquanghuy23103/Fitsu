package com.huy.fitsu.categories

import androidx.lifecycle.LiveData
import com.huy.fitsu.data.model.Category

interface CategoriesRepository {

    fun getCategoriesLiveData(): LiveData<List<Category>>

    suspend fun insertNewCategory(category: Category)
}