package com.huy.fitsu

import com.huy.fitsu.data.model.Budget
import com.huy.fitsu.data.model.Category
import com.huy.fitsu.data.model.Transaction
import java.time.LocalDate
import java.time.YearMonth

val categoryFood = Category(
    title = "Food",
    color = -255
)

val categoryClothes = Category(
    title = "Clothes",
    color = -500
)

val transactionFoodMay = Transaction(
    value = -100.50f,
    createdAt = LocalDate.of(2020, 5, 1),
    categoryId = categoryFood.id
)

val transactionFoodMay2 = Transaction(
    value = -123.45f,
    createdAt = LocalDate.of(2020, 5, 15),
    categoryId = categoryFood.id
)

val transactionApril = Transaction(
    value = -55f,
    createdAt = LocalDate.of(2020, 4, 1),
    categoryId = categoryFood.id
)

val budgetMay = Budget(
    value = 200f,
    yearMonth = YearMonth.of(2020, 5)
)