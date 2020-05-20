package com.huy.fitsu.transactions

import androidx.lifecycle.ViewModel
import javax.inject.Inject

class TransactionsViewModel @Inject constructor(
    repository: TransactionsRepository
) : ViewModel() {

    val transactionsLiveData = repository.getTransactionsLiveData()

}