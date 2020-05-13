package com.huy.fitsu.util

import android.app.Activity
import android.content.SharedPreferences
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import java.text.NumberFormat
import java.util.*
import kotlin.math.round

fun Float.round(decimals: Int): Float {
    var multiplier = 1.0F
    repeat(decimals) { multiplier *= 10 }
    return round(this * multiplier) / multiplier
}

@RequiresApi(Build.VERSION_CODES.LOLLIPOP)
fun View.toTransitionMap() = this to transitionName

fun Fragment.waitForTransition(targetView: View) {
    postponeEnterTransition()
    targetView.doOnPreDraw { startPostponedEnterTransition() }
}

fun Float.toCurrencyString(): String {
    val format = NumberFormat.getCurrencyInstance()
    format.maximumFractionDigits = 2
    format.currency = Currency.getInstance("EUR")
    return format.format(this)
}

fun Fragment.hideKeyboardFromView(view: View) {
    val imm = requireContext().getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(view.windowToken, 0)
}

fun SharedPreferences.intLiveData(key: String, defValue: Int): SharedPreferenceLiveData<Int> {
    return SharedPreferenceIntLiveData(this, key, defValue)
}

fun SharedPreferences.stringLiveData(
    key: String,
    defValue: String
): SharedPreferenceLiveData<String> {
    return SharedPreferenceStringLiveData(this, key, defValue)
}

fun SharedPreferences.booleanLiveData(
    key: String,
    defValue: Boolean
): SharedPreferenceLiveData<Boolean> {
    return SharedPreferenceBooleanLiveData(this, key, defValue)
}

fun SharedPreferences.floatLiveData(key: String, defValue: Float): SharedPreferenceLiveData<Float> {
    return SharedPreferenceFloatLiveData(this, key, defValue)
}

fun SharedPreferences.longLiveData(key: String, defValue: Long): SharedPreferenceLiveData<Long> {
    return SharedPreferenceLongLiveData(this, key, defValue)
}

fun SharedPreferences.stringSetLiveData(
    key: String,
    defValue: Set<String>
): SharedPreferenceLiveData<Set<String>> {
    return SharedPreferenceStringSetLiveData(this, key, defValue)
}