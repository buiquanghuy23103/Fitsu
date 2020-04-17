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

    private var transactionId = ""
    var selectedCategoryIndex = -1
        set(value) {
            field = if (value < -1) {
                -1
            } else {
                value
            }
        }

    private val _navigateUp = MutableLiveData<Event<Unit>>()
    val navigateUp : LiveData<Event<Unit>> = _navigateUp

    private val _transaction = MutableLiveData<Transaction>()

    fun loadTransactionWithId(id: String) {
        this.transactionId = id
        wrapEspressoIdlingResource {
            viewModelScope.launch(mainDispatcher) {
                val transaction = transactionRepository.getTransaction(id)
                _transaction.postValue(transaction)
            }
        }
    }

    val transaction : LiveData<Transaction> = _transaction

    private val category : LiveData<Category>
        get() = Transformations.switchMap(_transaction) {
            categoryRepository.getCategoryLiveData(it.categoryId)
        }

    private val categories = categoryRepository.getCategoriesLiveData()

    fun categoriesAndChosenCategory() : LiveData<Pair<List<Category>, Category>> {

        val mediatorLiveData = MediatorLiveData<Pair<List<Category>, Category>>()

        mediatorLiveData.addSource(categories) { categoriesValue ->
            category.value?.let {categoryValue ->
                mediatorLiveData.value = Pair(categoriesValue, categoryValue)
            }
        }

        mediatorLiveData.addSource(category) { categoryValue ->
            categories.value?.let {categoriesValue ->
                mediatorLiveData.value = Pair(categoriesValue, categoryValue)
            }
        }

        return mediatorLiveData
    }

    fun updateTransactionToDb() {
        wrapEspressoIdlingResource {
            viewModelScope.launch(mainDispatcher) {
                _transaction.value?.let {
                    transactionRepository.updateTransaction(it)
                    _navigateUp.value = Event(Unit)
                }

            }
        }
    }


    fun deleteTransaction(transaction: Transaction) {
        wrapEspressoIdlingResource {
            viewModelScope.launch(mainDispatcher) {
                transactionRepository.deleteTransaction(transaction)
                _navigateUp.postValue(Event(Unit))
            }
        }
    }

    fun updateTransaction(transactionFromUi: Transaction) {
        val transactionGoToDb = transactionFromUi.copy(
            id = transactionId
        )
        wrapEspressoIdlingResource {
            viewModelScope.launch(mainDispatcher) {
                transactionRepository.updateTransaction(transactionGoToDb)
                _navigateUp.postValue(Event(Unit))
            }
        }
    }

}