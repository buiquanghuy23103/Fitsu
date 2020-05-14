package com.huy.fitsu.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "categories")
data class Category(
    @PrimaryKey @ColumnInfo(name = "id") var id: String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "title") var title: String = "",
    @ColumnInfo(name = "color") var color: Int = -65535,
    @ColumnInfo(name = "isIncome") var isIncome: Boolean = false
)