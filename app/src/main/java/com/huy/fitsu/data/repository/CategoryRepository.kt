package com.huy.fitsu.data.repository

import androidx.lifecycle.LiveData
import com.huy.fitsu.data.model.Category
import io.reactivex.Completable

interface CategoryRepository {

    fun getAllCategories(): LiveData<List<Category>>

    fun addCategory(category: Category): Completable

}