package com.huy.fitsu.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Event
import com.huy.fitsu.data.repository.CategoryRepository
import kotlinx.coroutines.launch
import javax.inject.Inject

class CategoriesViewModel @Inject constructor(
    private val repository: CategoryRepository
): ViewModel() {

    private val editCategoryEventLiveData = MutableLiveData<Event<String>>()

    fun editCategoryEventLiveData(): LiveData<Event<String>> = editCategoryEventLiveData

    fun createDummyCategories() {
        val food = Category(title = "Food")
        val houseRent = Category(title = "House rent")
        viewModelScope.launch {
            repository.insertNewCategory(food)
            repository.insertNewCategory(houseRent)
        }
    }

    fun getAllCategories() : LiveData<List<Category>>
            = repository.getCategoriesLiveData()

    fun editCategoryWithId(id: String) {
        editCategoryEventLiveData.value = Event(id)
    }

}