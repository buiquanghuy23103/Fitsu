package com.huy.fitsu.util

import java.text.SimpleDateFormat
import java.util.*

object DateConverter {

    private const val date_format = "EEEE, dd MMMM yyyy"
    private val locale = Locale("en-US")
    private val formatter = SimpleDateFormat(date_format, locale)

    fun dateToString(date: Date) : String {
        return formatter.format(date)
    }

    fun stringToDate(string: String): Date? {
        return formatter.parse(string)
    }

}