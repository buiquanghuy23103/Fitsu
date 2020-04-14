package com.huy.fitsu.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey @ColumnInfo(name = "id") val id : String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "value") val value : Int = 0,
    @ColumnInfo(name = "expense") val expense : Int = 0,
    @ColumnInfo(name = "createdAt") val createdAt : Date = Date(),
    @ColumnInfo(name = "budget_duration") val budgetDuration: BudgetDuration = BudgetDuration.WEEKLY
)