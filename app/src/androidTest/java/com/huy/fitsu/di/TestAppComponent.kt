package com.huy.fitsu.di

import android.content.Context
import com.huy.fitsu.data.local.FakeLocalStorageModule
import com.huy.fitsu.scheduler.SchedulerModule
import dagger.BindsInstance
import dagger.Component
import javax.inject.Singleton

@Singleton
@Component(
    modules = [
        FakeLocalStorageModule::class,
        SubComponentModule::class,
        RepositoryModule::class,
        ViewModelBuilderModule::class,
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