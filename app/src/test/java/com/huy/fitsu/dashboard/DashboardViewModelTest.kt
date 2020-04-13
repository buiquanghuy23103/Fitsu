package com.huy.fitsu.dashboard

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.Observer
import androidx.paging.PagedList
import com.huy.fitsu.data.model.Event
import com.huy.fitsu.data.model.TransactionDetail
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
class DashboardViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: DashboardViewModel

    @Mock
    private lateinit var transactionRepository: TransactionRepository

    @Mock
    private lateinit var editTransactionEventObserver: Observer<Event<String>>

    @Mock
    private lateinit var transactionDetailPagedListObserver: Observer<PagedList<TransactionDetail>>

    private val testDispatcher = TestCoroutineDispatcher()

    @Before
    fun setUp() {
        whenever(transactionRepository.getTransactionDetailPagedList())
            .thenReturn(MutableLiveData())
        viewModel = DashboardViewModel(transactionRepository, testDispatcher)

        viewModel.editTransactionEvent.observeForever(editTransactionEventObserver)
        viewModel.transactions.observeForever(transactionDetailPagedListObserver)
    }

    @Test
    fun editTransaction_shouldNavigate_toAddEditTransactionFragment() {
        val transactionId = "id"

        viewModel.editTransaction(transactionId)

        verify(editTransactionEventObserver).onChanged(notNull())

    }

    @Test
    fun addTransaction_shouldCreateNewTransaction() = testDispatcher.runBlockingTest {
        viewModel.addTransaction()

        verify(transactionRepository).insertNewTransaction(any())
    }

    @Test
    fun addTransaction_shouldNavigate_toAddEditTransactionFragment() {
        viewModel.addTransaction()

        verify(editTransactionEventObserver).onChanged(notNull())
    }

}