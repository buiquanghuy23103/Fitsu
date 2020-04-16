package com.huy.fitsu.data.repository

import androidx.lifecycle.LiveData
import androidx.paging.PagedList
import androidx.paging.toLiveData
import com.huy.fitsu.data.local.FitsuDatabase
import com.huy.fitsu.data.model.Budget
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.model.TransactionDetail
import com.huy.fitsu.di.DispatcherModule
import com.huy.fitsu.util.DateConverter
import com.huy.fitsu.util.wrapEspressoIdlingResource
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.withContext
import javax.inject.Inject

class TransactionRepositoryImpl @Inject constructor(
    db: FitsuDatabase,
    @DispatcherModule.IoDispatcher
    private val ioDispatcher: CoroutineDispatcher
) : TransactionRepository {

    private val transactionDao = db.transactionDao()
    private val transactionDetailDao = db.transactionDetailDao()
    private val budgetDao = db.budgetDao()

    override suspend fun getTransaction(id: String): Transaction? {
        return wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                transactionDao.getTransaction(id)
            }
        }
    }

    override suspend fun insertNewTransaction(transaction: Transaction) {
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                transactionDao.insert(transaction)
                DateConverter.localDateToSemanticWeek(transaction.createdAt)?.let {
                    val budget = budgetDao.getBySemanticWeek(it.year, it.weekNumber)
                    if (budget == null) {
                        DateConverter.localDateToSemanticWeek(transaction.createdAt)?.let { semanticWeek ->
                            val newBudget = Budget(semanticWeek = semanticWeek)
                            budgetDao.insert(newBudget)
                        }
                    }
                }
            }
        }
    }

    override suspend fun deleteAllTransactions() {
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                transactionDao.deleteAll()
            }
        }
    }

    override suspend fun deleteTransaction(id: String) {
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                transactionDao.deleteByCategoryId(id)
            }
        }
    }

    override suspend fun updateTransaction(transaction: Transaction) {
        wrapEspressoIdlingResource {
            withContext(ioDispatcher) {
                transactionDao.update(transaction)
                DateConverter.localDateToSemanticWeek(transaction.createdAt)?.let {
                    budgetDao.getBySemanticWeek(it.year, it.weekNumber)?.let { budget ->
                        budgetDao.addTransactionValue(budget.id, transaction.value)
                    }
                }
            }
        }
    }

    override fun getTransactionDetailPagedList(): LiveData<PagedList<TransactionDetail>> {
        val factory = transactionDetailDao.getPagedListLiveData()
        return factory.toLiveData(pageSize = 5)
    }

    override fun getTransactionDetailLiveData(id: String): LiveData<TransactionDetail> {
        return transactionDetailDao.findByIdLiveData(id)
    }

    override fun getTransactionLiveData(id: String): LiveData<Transaction> {
        return transactionDao.findByIdLiveData(id)
    }
}