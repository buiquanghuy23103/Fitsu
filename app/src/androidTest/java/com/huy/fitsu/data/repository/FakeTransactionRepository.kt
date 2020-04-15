package com.huy.fitsu.data.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.PagedList
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.model.TransactionDetail
import com.huy.fitsu.toTestPagedList
import timber.log.Timber
import javax.inject.Inject

class FakeTransactionRepository @Inject constructor(
    private val categoryRepository: CategoryRepository
) : TransactionRepository {

    private val transactions = mutableListOf<Transaction>()

    override suspend fun getTransaction(id: String): Transaction? {
        return transactions.find { it.id == id }
    }

    override suspend fun insertNewTransaction(transaction: Transaction) {
        transactions.add(transaction)
    }

    override suspend fun deleteTransaction(id: String) {
        transactions.removeAll { it.id == id }
    }

    override suspend fun deleteAllTransactions() {
        transactions.removeAll { true }
    }

    override fun getTransactionLiveData(id: String): LiveData<Transaction> = liveData {
        getTransaction(id)?.let { emit(it) }
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        val index = transactions.indexOfFirst { it.id == transaction.id }
        if (index == -1) {
            Timber.e("transaction not found")
        }
        transactions[index] = transaction
    }

    override fun getTransactionDetailLiveData(id: String): LiveData<TransactionDetail> = liveData {
        findTransactionDetailById(id)?.let { emit(it) }
    }

    override fun getTransactionDetailPagedList(): LiveData<PagedList<TransactionDetail>> = liveData {
        val pagedList = transactions.map { it.id }
            .map { findTransactionDetailById(it) }
            .requireNoNulls()
            .toMutableList()
            .toTestPagedList()

        emit(pagedList)
    }

    private suspend fun findTransactionDetailById(id: String): TransactionDetail? {
        val transactionDb = getTransaction(id)
        val categoryDb = transactionDb?.let { categoryRepository.getCategory(it.categoryId) }

        return transactionDb?.let { transactionNonNull ->
            categoryDb?.let { categoryNonNull ->
                TransactionDetail(
                    id = transactionNonNull.id,
                    value = transactionNonNull.value,
                    categoryId = transactionNonNull.categoryId,
                    categoryColor = categoryNonNull.color,
                    categoryTitle = categoryNonNull.title,
                    createdAt = transactionNonNull.createdAt
                )
            }
        }
    }


}
