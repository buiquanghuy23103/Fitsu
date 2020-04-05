package com.huy.fitsu.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "transactions")
data class Transactions(
    @PrimaryKey @ColumnInfo(name = "id") val id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "date") val date: Date = Date(),
    @ColumnInfo(name = "categoryId") val categoryId: String
)