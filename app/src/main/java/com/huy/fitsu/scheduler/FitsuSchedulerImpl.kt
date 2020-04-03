package com.huy.fitsu.scheduler

import io.reactivex.schedulers.Schedulers
import javax.inject.Inject

class FitsuSchedulerImpl @Inject constructor(): FitsuScheduler {

    override fun io() = Schedulers.io()

}