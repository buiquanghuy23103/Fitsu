package com.huy.fitsu.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Transactions

@Database(entities = [Category::class, Transactions::class], version = 1, exportSchema = false)
@TypeConverters(DatabaseTypeConverters::class)
abstract class FitsuDatabase: RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
}