package com.huy.fitsu.util

import android.widget.Button
import androidx.databinding.BindingAdapter
import java.util.*

@BindingAdapter("showDate")
fun showDateOnEditText(button: Button, date: Date?) {
    val dateString = date?.let { DateConverter.dateToString(date) } ?: ""
    button.text = dateString
}