package com.huy.fitsu.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.huy.fitsu.data.model.Event
import com.huy.fitsu.data.model.TransactionDetail
import com.huy.fitsu.data.repository.TransactionRepository
import com.huy.fitsu.di.DispatcherModule
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject

class TransactionsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    @DispatcherModule.MainDispatcher
    private val mainDispatcher: CoroutineDispatcher
): ViewModel() {

    private val _editTransactionEvent = MutableLiveData<Event<String>>()
    val editTransactionEvent: LiveData<Event<String>> = _editTransactionEvent

    val transactions: LiveData<PagedList<TransactionDetail>>
            = transactionRepository.getTransactionDetails().toLiveData(pageSize = 5)

    fun editTransaction(transactionId: String) {
        _editTransactionEvent.value = Event(transactionId)
    }

}