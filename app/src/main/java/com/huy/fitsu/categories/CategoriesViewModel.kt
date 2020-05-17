package com.huy.fitsu.categories

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Event
import com.huy.fitsu.di.DispatcherModule
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class CategoriesViewModel @Inject constructor(
    private val repositoryDefault: CategoriesRepository,
    @DispatcherModule.MainDispatcher
    private val mainDispatcher: CoroutineDispatcher
): ViewModel() {

    private val editCategoryEventLiveData = MutableLiveData<Event<String>>()

    fun editCategoryEventLiveData(): LiveData<Event<String>> = editCategoryEventLiveData

    fun getCategoriesLiveData() = repositoryDefault.getCategoriesLiveData()

    fun editCategoryWithId(id: String) {
        editCategoryEventLiveData.value = Event(id)
    }

    fun addCategory() {
        val newCategory = Category()
        viewModelScope.launch(mainDispatcher) {
            repositoryDefault.insertNewCategory(newCategory)
            editCategoryEventLiveData.postValue(Event(newCategory.id))
        }
    }

}