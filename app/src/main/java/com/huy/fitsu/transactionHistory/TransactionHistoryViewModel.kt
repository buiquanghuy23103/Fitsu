package com.huy.fitsu.transactionHistory

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import com.huy.fitsu.data.model.Event
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.model.TransactionDetail
import com.huy.fitsu.data.repository.TransactionRepository
import com.huy.fitsu.di.DispatcherModule
import com.huy.fitsu.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class TransactionHistoryViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    @DispatcherModule.MainDispatcher
    private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _editTransactionEvent = MutableLiveData<Event<String>>()
    val editTransactionEvent: LiveData<Event<String>> = _editTransactionEvent

    val transactions: LiveData<PagedList<TransactionDetail>> =
        transactionRepository.getTransactionDetailPagedList()

    fun editTransaction(transactionId: String) {
        _editTransactionEvent.value = Event(transactionId)
    }

    fun addTransaction() {
        wrapEspressoIdlingResource {
            viewModelScope.launch(mainDispatcher) {
                val newTransaction = Transaction(categoryId = "")
                transactionRepository.insertNewTransaction(newTransaction)
                _editTransactionEvent.value = Event(newTransaction.id)
            }
        }
    }

}