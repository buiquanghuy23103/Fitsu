package com.huy.fitsu.data.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Transaction
import kotlinx.coroutines.runBlocking
import org.junit.*
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class FitsuDatabaseTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: FitsuDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var transactionDao: TransactionDao
    private val sampleCategory = Category()

    @Before
    fun createDb() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            appContext, FitsuDatabase::class.java
        ).build()
        categoryDao = db.categoryDao()
        transactionDao = db.transactionDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun getAllCategories() = runBlocking {
        categoryDao.insertNewCategory(sampleCategory)

        val categoriesLiveData = categoryDao.getAll()
        val categories = LiveDataTestUtil.getValue(categoriesLiveData)

        assertTrue(categories.contains(sampleCategory))
    }

    @Test
    fun getCategoryById() = runBlocking {
        categoryDao.insertNewCategory(sampleCategory)

        val categoryLiveData = categoryDao.findById(sampleCategory.id)
        val category = LiveDataTestUtil.getValue(categoryLiveData)

        assertEquals(category, sampleCategory)
    }

    @Test
    fun updateCategory() = runBlocking {
        categoryDao.insertNewCategory(sampleCategory)

        val newCategory = sampleCategory.copy(title = "New")
        categoryDao.updateCategory(newCategory)

        val categoryLiveData = categoryDao.findById(sampleCategory.id)
        val categoryFromDb = LiveDataTestUtil.getValue(categoryLiveData)

        assertEquals(newCategory.title, categoryFromDb.title)
    }

    @Test
    fun getTransactionById() = runBlocking {
        val transaction = Transaction(categoryId = "categoryId")

        transactionDao.insertNewTransaction(transaction)

        val transactionLiveData = transactionDao.getTransaction(transaction.id)
        val dbTransaction = LiveDataTestUtil.getValue(transactionLiveData)
        Assert.assertNotNull(dbTransaction)
        assertEquals("Date should match", transaction.date, dbTransaction.date)
        assertEquals("categoryId should match", transaction.categoryId, dbTransaction.categoryId)
    }
}
