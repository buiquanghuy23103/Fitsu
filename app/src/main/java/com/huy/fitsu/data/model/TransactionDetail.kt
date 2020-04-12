package com.huy.fitsu.data.model

import androidx.room.DatabaseView
import java.util.*

@DatabaseView("SELECT transactions.id, transactions.date, transactions.value, transactions.categoryId," +
        "categories.title AS categoryTitle, categories.color AS categoryColor " +
        "FROM transactions INNER JOIN categories " +
        "ON transactions.categoryId = categories.id")
data class TransactionDetail(
    val id: String,
    val value: Int,
    val date: Date,
    val categoryId: String,
    val categoryTitle: String,
    val categoryColor: Int
)