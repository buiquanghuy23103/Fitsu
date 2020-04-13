package com.huy.fitsu.di

import android.content.Context
import androidx.room.Room
import com.huy.fitsu.data.local.FitsuDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DatabaseModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideDatabase(appContext: Context): FitsuDatabase {
        return Room.databaseBuilder(
            appContext,
            FitsuDatabase::class.java,
            "Fitsu.db"
        ).build()
    }

}