package com.huy.fitsu.dashboard

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.model.TransactionDetail
import com.huy.fitsu.data.repository.TransactionRepository
import com.huy.fitsu.di.DispatcherModule
import com.huy.fitsu.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class TransactionsViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    @DispatcherModule.MainDispatcher
    private val mainDispatcher: CoroutineDispatcher
): ViewModel() {

    val transactions: LiveData<PagedList<TransactionDetail>>
            = transactionRepository.getTransactionDetails().toLiveData(pageSize = 5)

    fun insertDummyTransactions() {
        wrapEspressoIdlingResource {
            viewModelScope.launch(mainDispatcher) {

                val transaction = Transaction(categoryId = "abcde")
                for (i in 0..4) {
                    transactionRepository.insertNewTransaction(transaction)
                }
            }
        }
    }

}