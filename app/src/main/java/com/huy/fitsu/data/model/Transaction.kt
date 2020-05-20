package com.huy.fitsu.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate
import java.util.*

@Entity(tableName = "transactions")
data class Transaction(
    @PrimaryKey @ColumnInfo(name = "id") val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "value") val value: Float = 0F,
    @ColumnInfo(name = "categoryId") val categoryId: String = "",
    @ColumnInfo(name = "createdAt") val createdAt: LocalDate = LocalDate.now()
)