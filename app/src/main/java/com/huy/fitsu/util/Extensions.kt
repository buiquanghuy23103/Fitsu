package com.huy.fitsu.util

import android.os.Build
import android.view.View
import androidx.annotation.RequiresApi
import androidx.core.view.doOnPreDraw
import androidx.fragment.app.Fragment
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