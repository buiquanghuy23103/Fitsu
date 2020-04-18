package com.huy.fitsu.util

import com.huy.fitsu.data.model.SemanticWeek
import java.time.DayOfWeek
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields
import java.util.*

object DateConverter {

    private val dateFormatter = DateTimeFormatter.ofLocalizedDate(FormatStyle.FULL)
    private val zoneId = ZoneId.systemDefault()
    private val weekFields = WeekFields.of(Locale.US)
    private val firstDayOfWeek = weekFields.firstDayOfWeek
    private val lastDayOfWeek = DayOfWeek.of(
        ( (firstDayOfWeek.value + 5) % DayOfWeek.values().size ) + 1
    )


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

    fun localDateToSemanticWeek(localDate: LocalDate?): SemanticWeek? = localDate?.let {
        SemanticWeek(
            year = it.year,
            month = it.monthValue,
            weekNumber = it.get(weekFields.weekOfWeekBasedYear())
        )
    }

    fun firstDayOfWeek(localDate: LocalDate): LocalDate? =
        localDate.with(TemporalAdjusters.previousOrSame(firstDayOfWeek))

    fun lastDayOfWeek(localDate: LocalDate): LocalDate? =
        localDate.with(TemporalAdjusters.nextOrSame(lastDayOfWeek))

}