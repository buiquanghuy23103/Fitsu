package com.huy.fitsu.data.local.database

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.huy.fitsu.data.model.Budget
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Transaction
import com.huy.fitsu.data.model.TransactionDetail

@Database(
    entities = [Category::class, Transaction::class, Budget::class],
    views = [TransactionDetail::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(DatabaseTypeConverters::class)
abstract class FitsuDatabase: RoomDatabase() {
    abstract fun categoryDao(): CategoryDao
    abstract fun transactionDao(): TransactionDao
    abstract fun transactionDetailDao(): TransactionDetailDao
    abstract fun budgetDao() : BudgetDao
}