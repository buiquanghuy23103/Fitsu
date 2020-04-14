package com.huy.fitsu.data.model

import java.util.*

data class Budget(
    val id : String = UUID.randomUUID().toString(),
    val value : Int = 0,
    val expense : Int = 0,
    val createdAt : Date = Date(),
    val budgetDuration: BudgetDuration = BudgetDuration.WEEKLY
)