package com.huy.fitsu.budgets

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.huy.fitsu.budgetMay
import com.huy.fitsu.transactionFoodMay
import com.huy.fitsu.transactionFoodMay2
import com.nhaarman.mockitokotlin2.eq
import com.nhaarman.mockitokotlin2.verify
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class BudgetsViewModelTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var viewModel: BudgetsViewModel

    private lateinit var repository: BudgetsRepository

    @Mock
    private lateinit var budgetLeftObserver: Observer<Float>

    @Before
    fun setUp() {
        repository = FakeBudgetsRepository()
        viewModel = BudgetsViewModel(repository)

        viewModel.budgetLeftLiveData().observeForever(budgetLeftObserver)
    }

    @Test
    fun budgetLeftLiveData() {
        val result = budgetMay.value + transactionFoodMay.value + transactionFoodMay2.value
        verify(budgetLeftObserver).onChanged(eq(result))
    }
}