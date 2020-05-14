package com.huy.fitsu.data.local.database

import androidx.room.TypeConverter
import java.time.LocalDate

class DatabaseTypeConverters {

    @TypeConverter
    fun fromLocalDate(localDate: LocalDate?) : Long? {
        return localDate?.toEpochDay()
    }

    @TypeConverter
    fun toLocalDate(long: Long?) : LocalDate? {
        return long?.let { LocalDate.ofEpochDay(it) }
    }

}