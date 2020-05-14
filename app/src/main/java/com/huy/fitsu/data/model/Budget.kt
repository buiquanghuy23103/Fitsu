package com.huy.fitsu.data.model

import androidx.room.ColumnInfo
import androidx.room.Embedded
import androidx.room.Entity
import androidx.room.PrimaryKey
import java.util.*

@Entity(tableName = "budgets")
data class Budget(
    @PrimaryKey @ColumnInfo(name = "id") val id : String = UUID.randomUUID().toString(),
    @ColumnInfo(name = "value") val value : Float = 0F,
    @ColumnInfo(name = "expense") var expense : Float = 0F,
    @Embedded val semanticWeek: SemanticWeek
)