package com.huy.fitsu.data.local.database

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.YearMonth

class DatabaseTypeConverters {

    @TypeConverter
    fun fromLocalDate(localDate: LocalDate?): Long? {
        return localDate?.toEpochDay()
    }

    @TypeConverter
    fun toLocalDate(long: Long?): LocalDate? {
        return long?.let { LocalDate.ofEpochDay(it) }
    }

    @TypeConverter
    fun fromYearMonth(yearMonth: YearMonth?): String? {
        return yearMonth.toString()
    }

    @TypeConverter
    fun toYearMonth(string: String?): YearMonth? {
        return YearMonth.parse(string)
    }

}