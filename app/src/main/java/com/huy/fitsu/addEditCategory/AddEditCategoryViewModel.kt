package com.huy.fitsu.addEditCategory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Event
import com.huy.fitsu.di.DispatcherModule
import com.huy.fitsu.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddEditCategoryViewModel @Inject constructor(
    private val repositoryDefault: AddEditCategoryRepository,
    @DispatcherModule.MainDispatcher
    private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val navigateBackLiveData = MutableLiveData<Event<Unit>>()
    private val loadingLiveData = MutableLiveData<Boolean>()
    private val errorLiveData = MutableLiveData<String>()

    fun navigateBackLiveData(): LiveData<Event<Unit>> = navigateBackLiveData
    fun loadingLiveData(): LiveData<Boolean> = loadingLiveData
    fun errorLiveData(): LiveData<String> = errorLiveData


    fun getCategoryLiveDataById(id: String) =
        repositoryDefault.getCategoryLiveDataById(id)

    fun updateCategory(category: Category) {
        loadingLiveData.value = true
        errorLiveData.value = ""

        wrapEspressoIdlingResource {
            viewModelScope.launch(mainDispatcher) {
                try {
                    repositoryDefault.updateCategory(category)
                    loadingLiveData.postValue(false)
                    navigateBackLiveData.postValue(Event(Unit))
                } catch (e: Exception) {
                    loadingLiveData.postValue(false)
                    errorLiveData.postValue(e.message)
                }
            }
        }
    }

    fun deleteCategoryById(id: String) {
        wrapEspressoIdlingResource {
            viewModelScope.launch(mainDispatcher) {
                repositoryDefault.deleteCategoryById(id)
                navigateBackLiveData.postValue(Event(Unit))
            }
        }
    }

}