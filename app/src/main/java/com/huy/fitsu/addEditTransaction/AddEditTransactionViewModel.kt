package com.huy.fitsu.addEditTransaction

import androidx.lifecycle.*
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Event
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.repository.CategoryRepository
import com.huy.fitsu.data.repository.TransactionRepository
import com.huy.fitsu.di.DispatcherModule
import com.huy.fitsu.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.launch
import javax.inject.Inject

class AddEditTransactionViewModel @Inject constructor(
    private val transactionRepository: TransactionRepository,
    private val categoryRepository: CategoryRepository,
    @DispatcherModule.MainDispatcher
    private val mainDispatcher: CoroutineDispatcher
) : ViewModel() {

    private val _navigateUp = MutableLiveData<Event<Unit>>()
    val navigateUp : LiveData<Event<Unit>> = _navigateUp

    private val _transaction = MutableLiveData<Transaction>()

    fun getTransactionLiveDataById(id: String) : LiveData<Transaction> =
        transactionRepository.getTransactionLiveData(id)

    val category : LiveData<Category>
        get() = Transformations.switchMap(_transaction) {
            categoryRepository.getCategoryLiveData(it.categoryId)
        }

    val categories = categoryRepository.getCategoriesLiveData()

    fun deleteTransaction(transaction: Transaction) {
        wrapEspressoIdlingResource {
            viewModelScope.launch(mainDispatcher) {
                transactionRepository.deleteTransaction(transaction)
                navigateUp()
            }
        }
    }

    fun updateTransaction(transaction: Transaction) {
        wrapEspressoIdlingResource {
            viewModelScope.launch(mainDispatcher) {
                transactionRepository.updateTransaction(transaction)
                navigateUp()
            }
        }
    }

    private fun navigateUp() {
        _navigateUp.postValue(Event(Unit))
    }

}