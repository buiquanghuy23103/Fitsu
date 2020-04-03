package com.huy.fitsu.scheduler

import dagger.Binds
import dagger.Module

@Module
abstract class SchedulerModule {

    @Binds
    abstract fun scheduler(schedulerImpl: FitsuSchedulerImpl): FitsuScheduler
}