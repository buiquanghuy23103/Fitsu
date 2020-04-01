package com.huy.fitsu.data.local

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.huy.fitsu.data.model.Category
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
    fun getAllCategories() {
        categoryDao.insert(sampleCategory).subscribe()

        val categoriesLiveData = categoryDao.getAll()
        val categories = LiveDataTestUtil.getValue(categoriesLiveData)

        assertTrue(categories.contains(sampleCategory))
    }

    @Test
    fun getCategoryById() {
        categoryDao.insert(sampleCategory).subscribe()

        val categoryLiveData = categoryDao.findById(sampleCategory.id)
        val category = LiveDataTestUtil.getValue(categoryLiveData)

        assertEquals(category, sampleCategory)
    }

    @Test
    fun updateCategory() {
        categoryDao.insert(sampleCategory).subscribe()

        val newCategory = sampleCategory.copy(title = "New")
        categoryDao.update(newCategory).subscribe()

        val categoryLiveData = categoryDao.findById(sampleCategory.id)
        val categoryFromDb = LiveDataTestUtil.getValue(categoryLiveData)

        assertEquals(newCategory.title, categoryFromDb.title)
    }
}
