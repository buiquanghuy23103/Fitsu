package com.huy.fitsu.scheduler

import io.reactivex.schedulers.Schedulers

class FitsuSchedulerImpl: FitsuScheduler {

    override fun io() = Schedulers.io()

}