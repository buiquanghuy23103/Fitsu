package com.huy.fitsu.util

import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle

object DateConverter {

    private val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.MEDIUM)
    private val zoneId = ZoneId.systemDefault()

    fun localDateToString(localDate: LocalDate?): String? = localDate?.format(dateFormatter)

    fun localDateToEpochSeconds(localDate: LocalDate?): Long? =
        localDate?.atStartOfDay(zoneId)
            ?.toInstant()
            ?.toEpochMilli()

    fun epochSecondsToLocalDate(epochSeconds: Long?): LocalDate? =
        epochSeconds?.let {
            Instant.ofEpochMilli(it).atZone(
                zoneId
            ).toLocalDate()
        }

}