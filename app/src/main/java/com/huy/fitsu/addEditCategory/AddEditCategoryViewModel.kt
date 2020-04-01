package com.huy.fitsu.addEditCategory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Event
import com.huy.fitsu.data.repository.CategoryRepository
import com.huy.fitsu.scheduler.FitsuScheduler
import io.reactivex.CompletableObserver
import io.reactivex.disposables.Disposable

class AddEditCategoryViewModel(
    private val repository: CategoryRepository,
    private val scheduler: FitsuScheduler
): ViewModel() {

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
        return repository.findCategoryById(categoryId)
    }

    fun updateCategory(category: Category) {
        loadingLiveData.value = true
        errorLiveData.value = ""
        repository.updateCategory(category)
            .subscribeOn(scheduler.io())
            .subscribe(
            object : CompletableObserver {

                override fun onComplete() {
                    loadingLiveData.postValue(false)
                    navigateBackLiveData.postValue(Event(Unit))
                }

                override fun onSubscribe(d: Disposable) {

                }

                override fun onError(e: Throwable) {
                    loadingLiveData.postValue(false)
                    errorLiveData.postValue(e.message)
                }
            }
        )
    }
}