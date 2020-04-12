package com.huy.fitsu.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.huy.fitsu.data.model.Category
import timber.log.Timber
import javax.inject.Inject

class FakeCategoryRepository @Inject constructor() : CategoryRepository {

    private val categories = mutableListOf<Category>()

    override fun getCategoriesLiveData(): LiveData<List<Category>> = liveData {
        emit(categories.toList())
    }

    override fun getCategoryLiveData(id: String): LiveData<Category> = liveData {
        findById(id)?.let { emit(it) }
    }

    override suspend fun insertNewCategory(category: Category) {
        categories.add(category)
    }

    override suspend fun updateCategory(category: Category) {
        val index = categories.indexOfFirst { it.id == category.id }
        if (index == -1) {
            Timber.e("category not found")
        }
        categories[index] = category
    }

    override suspend fun deleteAllCategories() {
        categories.removeAll { true }
    }

    private fun findById(id: String) = categories.find { it.id == id }
}