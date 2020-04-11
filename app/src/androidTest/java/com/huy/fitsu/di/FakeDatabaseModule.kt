package com.huy.fitsu.di

import android.content.Context
import androidx.room.Room
import com.huy.fitsu.data.local.FitsuDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object FakeDatabaseModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideDatabase(context: Context): FitsuDatabase {
        return Room.inMemoryDatabaseBuilder(
            context.applicationContext,
            FitsuDatabase::class.java
        ).allowMainThreadQueries().build()
    }

}