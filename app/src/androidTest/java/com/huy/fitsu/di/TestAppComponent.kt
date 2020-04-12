package com.huy.fitsu.di

import android.content.Context
import com.huy.fitsu.data.repository.FakeRepositoryModule
import com.huy.fitsu.scheduler.SchedulerModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        DatabaseModule::class,
        DaoModule::class,
        SubComponentModule::class,
        ViewModelBuilderModule::class,
        FakeRepositoryModule::class,
        SchedulerModule::class,
        DispatcherModule::class
    ]
)
interface TestAppComponent: AppComponent {

    @Component.Factory
    interface Factory {
        fun create(@BindsInstance context: Context): AppComponent
    }

}