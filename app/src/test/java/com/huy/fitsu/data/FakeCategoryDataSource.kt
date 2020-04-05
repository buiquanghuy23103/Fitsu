package com.huy.fitsu.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.huy.fitsu.data.manager.CategoryDataSource
import com.huy.fitsu.data.model.Category

class FakeCategoryDataSource(
    val categories: MutableList<Category>? = mutableListOf()
): CategoryDataSource {

    override fun getCategory(id: String): LiveData<Category> {
        return MutableLiveData<Category>().apply {
            value = categories?.find { it.id == id }
        }
    }

    override fun getCategories(): LiveData<List<Category>> {
        return MutableLiveData<List<Category>>().apply {
            value = categories
        }
    }

    override suspend fun insertNewCategory(category: Category) {
        categories?.add(category)
    }

    override suspend fun updateCategory(category: Category) {
        if (categories?.contains(category) == true) {
            val index = categories.indexOf(category)
            categories[index] = category
        }
    }
}