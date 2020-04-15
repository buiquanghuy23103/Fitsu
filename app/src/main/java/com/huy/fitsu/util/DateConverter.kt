package com.huy.fitsu.util

import java.text.SimpleDateFormat
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

object DateConverter {

    private const val date_format = "EEEE, dd MMMM yyyy"
    private val locale = Locale("en-US")
    private val formatter = SimpleDateFormat(date_format, locale)
    private val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
    private val zoneId = ZoneId.systemDefault()

    fun dateToString(date: Date): String {
        return formatter.format(date)
    }

    fun stringToDate(string: String): Date? {
        return formatter.parse(string)
    }

    fun localDateToString(localDate: LocalDate?): String? = localDate?.format(dateFormatter)

    fun stringToLocalDate(string: String?): LocalDate? =
        string?.let { LocalDate.parse(it, dateFormatter) }

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