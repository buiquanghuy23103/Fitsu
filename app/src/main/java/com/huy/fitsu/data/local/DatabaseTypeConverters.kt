package com.huy.fitsu.data.local

import androidx.room.TypeConverter
import com.huy.fitsu.data.model.BudgetDuration
import java.time.LocalDate

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
    fun fromLocalDate(localDate: LocalDate?) : Long? {
        return localDate?.toEpochDay()
    }

    @TypeConverter
    fun toLocalDate(long: Long?) : LocalDate? {
        return long?.let { LocalDate.ofEpochDay(it) }
    }

}