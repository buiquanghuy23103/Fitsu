package com.huy.fitsu.util

import androidx.test.espresso.IdlingResource
import java.util.concurrent.atomic.AtomicInteger

class SimpleCountingIdlingResource(
    private val resourceName: String
) : IdlingResource {

    private val counter = AtomicInteger(0)

    @Volatile
    private var resourceCallback: IdlingResource.ResourceCallback? = null

    override fun getName() = resourceName

    override fun isIdleNow() = counter.get() == 0

    override fun registerIdleTransitionCallback(callback: IdlingResource.ResourceCallback?) {
        this.resourceCallback = callback
    }

    fun increment() {
        counter.getAndIncrement()
    }

    fun decrement() {
        val counterValue = counter.getAndDecrement()
        if (counterValue == 0) {
            resourceCallback?.onTransitionToIdle()
        } else if (counterValue < 0) {
            throw IllegalStateException("Counter has been corrupted")
        }
    }
}