package com.huy.fitsu.data.local.database

import androidx.room.TypeConverter
import java.time.LocalDate
import java.time.YearMonth

object DatabaseTypeConverters {

    @JvmStatic
    @TypeConverter
    fun fromLocalDate(localDate: LocalDate?): Long? {
        return localDate?.toEpochDay()
    }

    @JvmStatic
    @TypeConverter
    fun toLocalDate(long: Long?): LocalDate? {
        return long?.let { LocalDate.ofEpochDay(it) }
    }

    @JvmStatic
    @TypeConverter
    fun fromYearMonth(yearMonth: YearMonth?): String? {
        return yearMonth.toString()
    }

    @JvmStatic
    @TypeConverter
    fun toYearMonth(string: String?): YearMonth? {
        return YearMonth.parse(string)
    }

}