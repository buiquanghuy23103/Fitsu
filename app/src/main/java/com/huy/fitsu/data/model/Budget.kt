package com.huy.fitsu.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey @ColumnInfo(name = "id") val id : String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "value") val value : Int = 0,
    @ColumnInfo(name = "expense") var expense : Int = 0,
    @Embedded val semanticWeek: SemanticWeek,
    @ColumnInfo(name = "budget_duration") val budgetDuration: BudgetDuration = BudgetDuration.WEEKLY
)