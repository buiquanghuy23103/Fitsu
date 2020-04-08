package com.huy.fitsu.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Event
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.repository.CategoryRepository
import com.huy.fitsu.data.repository.TransactionRepository
import com.huy.fitsu.util.wrapEspressoIdlingResource
import kotlinx.coroutines.launch
import javax.inject.Inject

class CategoriesViewModel @Inject constructor(
    private val repository: CategoryRepository,
    private val transRepo: TransactionRepository
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
            = repository.getCategories()

    fun editCategoryWithId(id: String) {
        editCategoryEventLiveData.value = Event(id)
    }

    fun addDummyCategories(category: Category) {
        val transaction = Transaction(categoryId = category.id)
        wrapEspressoIdlingResource {
            viewModelScope.launch {
                transRepo.insertNewTransaction(transaction)
            }
        }
    }

}