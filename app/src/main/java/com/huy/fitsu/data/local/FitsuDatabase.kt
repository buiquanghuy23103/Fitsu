package com.huy.fitsu.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.huy.fitsu.data.model.Category

@Database(entities = [Category::class], version = 1, exportSchema = false)
@TypeConverters(DatabaseTypeConverters::class)
abstract class FitsuDatabase: RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
}