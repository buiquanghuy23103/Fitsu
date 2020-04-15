package com.huy.fitsu.data.model

import androidx.room.DatabaseView
import java.time.LocalDate
import java.util.*

@DatabaseView(
    "SELECT transactions.id, transactions.date, transactions.value, " +
            "transactions.categoryId, transactions.createdAt, " +
            "categories.title AS categoryTitle, categories.color AS categoryColor " +
            "FROM transactions INNER JOIN categories " +
            "ON transactions.categoryId = categories.id"
)
data class TransactionDetail(
    val id: String,
    val value: Int,
    val date: Date,
    val createdAt: LocalDate,
    val categoryId: String,
    val categoryTitle: String,
    val categoryColor: Int
)