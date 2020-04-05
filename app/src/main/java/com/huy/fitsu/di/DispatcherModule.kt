package com.huy.fitsu.di

import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import kotlinx.coroutines.Dispatchers
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
object DispatcherModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class IoDispatcher

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class MainDispatcher

    @JvmStatic
    @Singleton
    @Provides
    @IoDispatcher
    fun provideIoDispatcher(): CoroutineDispatcher = Dispatchers.IO

    @JvmStatic
    @Singleton
    @Provides
    @MainDispatcher
    fun provideMainDispatcher(): CoroutineDispatcher = Dispatchers.Main

}