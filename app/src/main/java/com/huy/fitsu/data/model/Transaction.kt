package com.huy.fitsu.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.*

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey @ColumnInfo(name = "id") val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "value") val value: Int = 0,
    @ColumnInfo(name = "categoryId") val categoryId: String,
    @ColumnInfo(name = "createdAt") val createdAt: LocalDate = LocalDate.now()
)