package com.huy.fitsu

import com.huy.fitsu.scheduler.FitsuScheduler
import io.reactivex.Scheduler
import io.reactivex.schedulers.Schedulers


class FakeFitsuScheduler: FitsuScheduler {

    override fun io(): Scheduler {
        return Schedulers.trampoline()
    }
}
