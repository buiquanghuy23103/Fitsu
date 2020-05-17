package com.huy.fitsu.addEditTransaction

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Event
import com.huy.fitsu.data.model.Transaction
import com.nhaarman.mockitokotlin2.any
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

    @Mock
    private lateinit var repositoryDefault: DefaultAddEditTransactionRepository

    @Mock
    private lateinit var navigateUpObserver: Observer<Event<Unit>>

    @Mock
    private lateinit var categoryObserver: Observer<Category?>

    @Mock
    private lateinit var categoriesObserver: Observer<List<Category>>

    private val testDispatcher = TestCoroutineDispatcher()

    private val testCategory = Category()
    private val testCategories = listOf(testCategory)
    private val testTransaction = Transaction(categoryId = testCategory.id)


    @Before
    fun setUp() = testDispatcher.runBlockingTest {

        viewModel = AddEditTransactionViewModel(repositoryDefault, testDispatcher)

        viewModel.navigateUp.observeForever(navigateUpObserver)
        viewModel.getCategoryByTransactionId(any()).observeForever(categoryObserver)
        viewModel.categoriesLiveData.observeForever(categoriesObserver)
    }


    @Test
    fun deleteTransaction_shouldNavigateUp() {

        viewModel.deleteTransaction(testTransaction)

        verify(navigateUpObserver).onChanged(notNull())
    }

    @Test
    fun updateTransaction_shouldNavigateUp() {
        viewModel.updateTransaction(testTransaction)

        verify(navigateUpObserver).onChanged(notNull())
    }

}