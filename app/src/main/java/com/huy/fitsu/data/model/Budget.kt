package com.huy.fitsu.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.YearMonth
import java.util.*

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey @ColumnInfo(name = "id") val id : String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "value") val value : Float = 0F,
    @ColumnInfo(name = "year_month") val yearMonth: YearMonth
)