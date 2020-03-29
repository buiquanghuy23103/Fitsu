package com.huy.fitsu.data

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "description") var description: String = "",
    @ColumnInfo(name = "weekBudgetIncluded") var weekBudgetIncluded: Boolean = false,
    @ColumnInfo(name = "monthBudgetIncluded") var monthBudgetIncluded: Boolean = false,
    @ColumnInfo(name = "isIncome") var isIncome: Boolean = false
)