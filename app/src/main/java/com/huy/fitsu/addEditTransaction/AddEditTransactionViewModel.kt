package com.huy.fitsu.addEditTransaction

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huy.fitsu.data.model.Event
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.di.DispatcherModule
import com.huy.fitsu.util.combineWith
import com.huy.fitsu.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddEditTransactionViewModel @Inject constructor(
    private val repository: AddEditTransactionRepository,
    @DispatcherModule.MainDispatcher
    private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _navigateUp = MutableLiveData<Event<Unit>>()
    val navigateUp : LiveData<Event<Unit>> = _navigateUp

    val categoriesLiveData = repository.getCategoriesLiveData()

    fun getTransactionLiveDataById(id: String) =
        repository.getTransactionLiveDataById(id)

    fun getCategoryByTransactionId(id: String) =
        getTransactionLiveDataById(id).combineWith(categoriesLiveData) {transaction, categories ->
            categories.find { it.id == transaction.categoryId }
        }

    fun deleteTransaction(transaction: Transaction) {
        wrapEspressoIdlingResource {
            viewModelScope.launch(mainDispatcher) {
                repository.deleteTransaction(transaction)
                navigateUp()
            }
        }
    }

    fun updateTransaction(transaction: Transaction) {
        wrapEspressoIdlingResource {
            viewModelScope.launch(mainDispatcher) {
                repository.updateTransaction(transaction)
                navigateUp()
            }
        }
    }

    private fun navigateUp() {
        _navigateUp.postValue(Event(Unit))
    }

}