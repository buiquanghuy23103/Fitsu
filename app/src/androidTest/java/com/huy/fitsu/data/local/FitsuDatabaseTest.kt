package com.huy.fitsu.data.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.huy.fitsu.LiveDataTestUtil
import com.huy.fitsu.data.model.Category
import kotlinx.coroutines.runBlocking
import org.junit.After
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import java.io.IOException

@RunWith(AndroidJUnit4::class)
class FitsuDatabaseTest {

    @get:Rule
    val taskExecutorRule = InstantTaskExecutorRule()

    private lateinit var db: FitsuDatabase
    private lateinit var categoryDao: CategoryDao
    private val sampleCategory = Category()

    @Before
    fun createDb() {
        val appContext = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            appContext, FitsuDatabase::class.java
        ).build()
        categoryDao = db.categoryDao()

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
}
