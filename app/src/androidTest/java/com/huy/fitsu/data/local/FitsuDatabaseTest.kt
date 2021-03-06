package com.huy.fitsu.data.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.huy.fitsu.LiveDataTestUtil
import com.huy.fitsu.data.local.database.CategoryDao
import com.huy.fitsu.data.local.database.FitsuDatabase
import com.huy.fitsu.data.local.database.TransactionDao
import com.huy.fitsu.data.local.database.TransactionDetailDao
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Transaction
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException
import java.time.LocalDate

@RunWith(AndroidJUnit4::class)
class FitsuDatabaseTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: FitsuDatabase
    private lateinit var categoryDao: CategoryDao
    private lateinit var transactionDao: TransactionDao
    private lateinit var transactionDetailDao: TransactionDetailDao
    private val sampleCategory = Category(
        color = -255,
        title = "Food"
    )
    private val sampleTransaction = Transaction(
        value = 123f,
        categoryId = sampleCategory.id,
        createdAt = LocalDate.now()
    )

    @Before
    fun createDb() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            appContext, FitsuDatabase::class.java
        ).build()
        categoryDao = db.categoryDao()
        transactionDao = db.transactionDao()
        transactionDetailDao = db.transactionDetailDao()
    }

    @After
    @Throws(IOException::class)
    fun closeDb() {
        db.close()
    }

    @Test
    fun getAllCategories() = runBlocking {
        categoryDao.insert(sampleCategory)

        val categoriesLiveData = categoryDao.getAllLiveData()
        val categories = LiveDataTestUtil.getValue(categoriesLiveData)

        assertTrue(categories.contains(sampleCategory))
    }

    @Test
    fun getCategoryById() = runBlocking {
        categoryDao.insert(sampleCategory)

        val categoryLiveData = categoryDao.findByIdLiveData(sampleCategory.id)
        val category = LiveDataTestUtil.getValue(categoryLiveData)

        assertEquals(category, sampleCategory)
    }

    @Test
    fun updateCategory() = runBlocking {
        categoryDao.insert(sampleCategory)

        val newCategory = sampleCategory.copy(title = "New")
        categoryDao.update(newCategory)

        val categoryLiveData = categoryDao.findByIdLiveData(sampleCategory.id)
        val categoryFromDb = LiveDataTestUtil.getValue(categoryLiveData)

        assertEquals(newCategory.title, categoryFromDb.title)
    }

    @Test
    fun getTransactionById() = runBlocking {
        transactionDao.insert(sampleTransaction)

        val transactionFromDb = transactionDao.findById(sampleTransaction.id)
        assertEquals("Date should match", sampleTransaction.createdAt, transactionFromDb?.createdAt)
        assertEquals("categoryId should match", sampleTransaction.categoryId, transactionFromDb?.categoryId)
    }

    @Test
    fun getTransactionDetail() = runBlocking {
        categoryDao.insert(sampleCategory)
        transactionDao.insert(sampleTransaction)

        val transactionDetailLiveData = transactionDetailDao.findByIdLiveData(sampleTransaction.id)
        val transactionDetail = LiveDataTestUtil.getValue(transactionDetailLiveData)

        assertEquals(sampleTransaction.id, transactionDetail.id)
        assertEquals(sampleTransaction.createdAt, transactionDetail.createdAt)
        assertEquals(sampleTransaction.value, transactionDetail.value)
        assertEquals(sampleTransaction.categoryId, transactionDetail.categoryId)
        assertEquals(sampleCategory.title, transactionDetail.categoryTitle)
        assertEquals(sampleCategory.color, transactionDetail.categoryColor)
        assertEquals(sampleCategory.id, transactionDetail.categoryId)
    }
}
