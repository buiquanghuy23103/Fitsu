package com.huy.fitsu.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huy.fitsu.data.model.Event
import com.huy.fitsu.data.repository.CategoryRepository
import javax.inject.Inject

class CategoriesViewModel @Inject constructor(
    private val repository: CategoryRepository
): ViewModel() {

    private val editCategoryEventLiveData = MutableLiveData<Event<String>>()

    fun editCategoryEventLiveData(): LiveData<Event<String>> = editCategoryEventLiveData

    init {
//        val food = Category(title = "Food")
//        val houseRent = Category(title = "House rent")
//        repository.addCategory(food)
//            .andThen(repository.addCategory(houseRent))
//            .subscribeOn(Schedulers.io())
//            .subscribe()
    }

    fun getAllCategories() = repository.getAllCategories()

    fun editCategoryWithId(id: String) {
        editCategoryEventLiveData.value = Event(id)
    }

}