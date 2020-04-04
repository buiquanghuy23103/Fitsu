package com.huy.fitsu.data.manager

import com.huy.fitsu.data.local.FitsuDatabase
import dagger.Module
import dagger.Provides
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Qualifier
import javax.inject.Singleton

@Module
object DataSourceModule {

    @Qualifier
    @Retention(AnnotationRetention.RUNTIME)
    annotation class CategoryLocalDataSource

    @JvmStatic
    @Singleton
    @CategoryLocalDataSource
    @Provides
    fun localDataSource(
        database: FitsuDatabase,
        ioDispatcher: CoroutineDispatcher
    ) : CategoryDataSource {
        return CategoryLocalDataSource(
            database.categoryDao(),
            ioDispatcher
        )
    }

}