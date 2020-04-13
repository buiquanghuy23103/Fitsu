package com.huy.fitsu.addEditTransaction

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.huy.fitsu.data.model.Event
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.repository.CategoryRepository
import com.huy.fitsu.data.repository.TransactionRepository
import com.nhaarman.mockitokotlin2.any
import com.nhaarman.mockitokotlin2.notNull
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.whenever
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

    @Mock
    private lateinit var categoryRepository: CategoryRepository

    @Mock
    private lateinit var transactionRepository: TransactionRepository

    @Mock
    private lateinit var navigateUpObserver: Observer<Event<Unit>>

    private val testDispatcher = TestCoroutineDispatcher()

    private val testTransaction = Transaction(categoryId = "")


    @Before
    fun setUp() = testDispatcher.runBlockingTest {

        whenever(transactionRepository.getTransaction(any()))
            .thenReturn(testTransaction)

        viewModel = AddEditTransactionViewModel(transactionRepository, categoryRepository, testDispatcher)

        viewModel.loadTransactionWithId(testTransaction.categoryId)
        viewModel.navigateUp.observeForever(navigateUpObserver)
    }

    @Test
    fun updateTransaction_shouldNavigateUp() = testDispatcher.runBlockingTest {


        viewModel.updateTransactionToDb()

        verify(navigateUpObserver).onChanged(notNull())
    }

    @Test
    fun updateTransaction_shouldDelegateToRepository() = testDispatcher.runBlockingTest {

        viewModel.updateTransactionToDb()

        verify(transactionRepository).updateTransaction(any())
    }

    @Test
    fun deleteTransaction_shouldDelegateToRepository() = testDispatcher.runBlockingTest {
        val id = "id"

        viewModel.deleteTransaction(id)

        verify(transactionRepository).deleteTransaction(id)
    }

    @Test
    fun deleteTransaction_shouldNavigateUp() {
        val id = "id"

        viewModel.deleteTransaction(id)

        verify(navigateUpObserver).onChanged(notNull())
    }
}