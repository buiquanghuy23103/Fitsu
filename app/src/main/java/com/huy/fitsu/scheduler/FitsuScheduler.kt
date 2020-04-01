package com.huy.fitsu.scheduler

import io.reactivex.Scheduler

interface FitsuScheduler {

    fun io(): Scheduler

}