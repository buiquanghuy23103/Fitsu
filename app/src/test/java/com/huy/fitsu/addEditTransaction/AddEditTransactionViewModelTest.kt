package com.huy.fitsu.addEditTransaction

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.huy.fitsu.categoryFood
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Event
import com.huy.fitsu.transactionFoodMay
import com.nhaarman.mockitokotlin2.notNull
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.TestCoroutineDispatcher
import kotlinx.coroutines.test.runBlockingTest
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class AddEditTransactionViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: AddEditTransactionViewModel

    private lateinit var repository: FakeAddEditTransactionRepository

    @Mock
    private lateinit var navigateUpObserver: Observer<Event<Unit>>

    @Mock
    private lateinit var categoryObserver: Observer<Category?>

    private val testDispatcher = TestCoroutineDispatcher()


    @Before
    fun setUp() = testDispatcher.runBlockingTest {
        repository = FakeAddEditTransactionRepository()

        viewModel = AddEditTransactionViewModel(repository, testDispatcher)

        viewModel.navigateUp.observeForever(navigateUpObserver)

        val transaction = repository.transaction
        viewModel.getCategoryByTransactionId(transaction.id).observeForever(categoryObserver)

    }

    @Test
    fun getCategoryByTransactionId() {
        verify(categoryObserver).onChanged(categoryFood)
    }

    @Test
    fun deleteTransaction_shouldNavigateUp() {

        viewModel.deleteTransaction(transactionFoodMay)

        verify(navigateUpObserver).onChanged(notNull())
    }

    @Test
    fun updateTransaction_shouldNavigateUp() {
        viewModel.updateTransaction(transactionFoodMay)

        verify(navigateUpObserver).onChanged(notNull())
    }

}