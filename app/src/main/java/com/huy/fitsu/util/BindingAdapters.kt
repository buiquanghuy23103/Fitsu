package com.huy.fitsu.util

import android.content.res.ColorStateList
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate
import java.util.*

@BindingAdapter("showDate")
fun showDate(textView: TextView, date: Date?) {
    val dateString = date?.let { DateConverter.dateToString(date) } ?: ""
    textView.text = dateString
}

@BindingAdapter("showDateText")
fun showDateText(textView: TextView, localDate: LocalDate?) {
    val str = localDate?.let { DateConverter.localDateToString(it) }
    textView.text = str
}

@BindingAdapter("showColorBadge")
fun showColorBadge(fab: FloatingActionButton, @ColorInt colorInt : Int) {
    fab.backgroundTintList = ColorStateList.valueOf(colorInt)
}