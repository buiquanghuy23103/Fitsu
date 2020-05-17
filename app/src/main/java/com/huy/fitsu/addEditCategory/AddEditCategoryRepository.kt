package com.huy.fitsu.addEditCategory

import androidx.lifecycle.LiveData
import com.huy.fitsu.data.model.Category

interface AddEditCategoryRepository {

    fun getCategoryLiveDataById(id: String): LiveData<Category>

    suspend fun updateCategory(category: Category)

    suspend fun deleteCategoryById(id: String)


}