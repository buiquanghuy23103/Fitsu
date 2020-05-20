package com.huy.fitsu.transactions

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.huy.fitsu.data.model.Event
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.di.DispatcherModule
import com.huy.fitsu.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class TransactionsViewModel @Inject constructor(
    private val repository: TransactionsRepository,
    @DispatcherModule.MainDispatcher
    private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    val transactionsLiveData = repository.getTransactionsLiveData()

    private val _editTransaction = MutableLiveData<Event<String>>()
    val editTransaction: LiveData<Event<String>> = _editTransaction

    fun addTransaction() {
        wrapEspressoIdlingResource {
            viewModelScope.launch(mainDispatcher) {
                val newTransaction = Transaction()
                repository.insertNewTransaction(newTransaction)
                _editTransaction.value = Event(newTransaction.id)
            }
        }
    }

}