package com.huy.fitsu.util

import android.app.Activity
import android.content.SharedPreferences
import android.os.Build
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.RequiresApi
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import java.text.NumberFormat
import java.util.*

fun Float.round(decimals: Int): Float {
    var multiplier = 1.0F
    repeat(decimals) { multiplier *= 10 }
    return kotlin.math.round(this * multiplier) / multiplier
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

fun <A, B, C> LiveData<A>.combineWith(otherLiveData: LiveData<B>, combineOperation: (A, B) -> C)
    : LiveData<C>
{
    var sourceA_emitted = false
    var sourceB_emitted = false

    val result = MediatorLiveData<C>()

    val calculateResult = {
        val sourceA_value = this.value
        val sourceB_value = otherLiveData.value

        if (sourceA_emitted && sourceB_emitted) {
            result.value = combineOperation.invoke(sourceA_value!!, sourceB_value!!)
        }
    }

    result.addSource(this) {
        it?.let {
            sourceA_emitted = true
            calculateResult.invoke()
        }
    }
    result.addSource(otherLiveData) {
        it?.let {
            sourceB_emitted = true
            calculateResult.invoke()
        }
    }

    return result
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