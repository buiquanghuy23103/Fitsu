package com.huy.fitsu.addEditCategory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Event
import com.huy.fitsu.data.repository.CategoryRepository
import com.huy.fitsu.di.DispatcherModule
import com.huy.fitsu.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddEditCategoryViewModel @Inject constructor(
    private val repository: CategoryRepository,
    @DispatcherModule.MainDispatcher
    private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private var categoryId: String = ""
    private val navigateBackLiveData = MutableLiveData<Event<Unit>>()
    private val loadingLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<String>()

    fun navigateBackLiveData(): LiveData<Event<Unit>> = navigateBackLiveData
    fun loadingLiveData(): LiveData<Boolean> = loadingLiveData
    fun errorLiveData(): LiveData<String> = errorLiveData

    fun setCategoryId(id: String) {
        categoryId = id
    }

    fun getCategory(): LiveData<Category> {
        return repository.getCategoryLiveData(categoryId)
    }

    fun updateCategory(category: Category) {
        loadingLiveData.value = true
        errorLiveData.value = ""

        wrapEspressoIdlingResource {
            viewModelScope.launch(mainDispatcher) {
                try {
                    repository.updateCategory(category)
                    loadingLiveData.postValue(false)
                    navigateBackLiveData.postValue(Event(Unit))
                } catch (e: Exception) {
                    loadingLiveData.postValue(false)
                    errorLiveData.postValue(e.message)
                }
            }
        }
    }

    fun deleteCategory() {
        wrapEspressoIdlingResource {
            viewModelScope.launch(mainDispatcher) {
                repository.deleteCategory(categoryId)
                navigateBackLiveData.postValue(Event(Unit))
            }
        }
    }

}