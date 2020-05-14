package com.huy.fitsu.util

import android.content.res.ColorStateList
import android.widget.TextView
import androidx.annotation.ColorInt
import androidx.databinding.BindingAdapter
import com.google.android.material.button.MaterialButton
import com.google.android.material.floatingactionbutton.FloatingActionButton
import java.time.LocalDate

@BindingAdapter("showDateText")
fun showDateText(textView: TextView, localDate: LocalDate?) {
    val str = localDate?.let { DateConverter.localDateToString(it) }
    textView.text = str
}

@BindingAdapter("showColorBadge")
fun showColorBadge(fab: FloatingActionButton, @ColorInt colorInt : Int) {
    fab.backgroundTintList = ColorStateList.valueOf(colorInt)
}

@BindingAdapter("showColorIcon")
fun showColorIcon(button: MaterialButton, @ColorInt colorInt: Int) {
    button.iconTint = ColorStateList.valueOf(colorInt)
}