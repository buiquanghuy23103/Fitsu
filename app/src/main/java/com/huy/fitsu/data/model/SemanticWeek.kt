package com.huy.fitsu.data.model

data class SemanticWeek(
    val weekNumber: Int,
    val month: Int,
    val year: Int
) {
    fun isEqualTo(anotherSemanticWeek: SemanticWeek): Boolean =
        weekNumber == anotherSemanticWeek.weekNumber
                && year == anotherSemanticWeek.year
}