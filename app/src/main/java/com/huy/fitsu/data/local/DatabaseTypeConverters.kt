package com.huy.fitsu.data.local

import androidx.room.TypeConverter
import com.huy.fitsu.data.model.BudgetDuration
import java.util.*

class DatabaseTypeConverters {

    @TypeConverter
    fun budgetDurationToString(value: BudgetDuration): String {
        return value.name
    }

    @TypeConverter
    fun stringToBudgetDuration(value: String): BudgetDuration {
        return when(value) {
            BudgetDuration.WEEKLY.name -> BudgetDuration.WEEKLY
            BudgetDuration.MONTHLY.name -> BudgetDuration.MONTHLY
            BudgetDuration.ANNUAL.name -> BudgetDuration.ANNUAL
            else -> BudgetDuration.NONE
        }
    }

    @TypeConverter
    fun fromTimestamp(value: Long?): Date? {
        return value?.let { Date(it) }
    }

    @TypeConverter
    fun dateToTimestamp(date: Date?): Long? {
        return date?.time?.toLong()
    }

}