package com.huy.fitsu.categories

import androidx.lifecycle.ViewModel
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.repository.CategoryRepository
import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class CategoriesViewModel @Inject constructor(
    private val repository: CategoryRepository
): ViewModel() {

    init {
        val food = Category(title = "Food")
        val houseRent = Category(title = "House rent")
        repository.addCategory(food)
            .andThen(repository.addCategory(houseRent))
            .subscribeOn(Schedulers.io())
            .subscribe()
    }

    fun getAllCategories() = repository.getAllCategories()

}