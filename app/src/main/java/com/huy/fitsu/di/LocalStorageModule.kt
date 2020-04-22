package com.huy.fitsu.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.huy.fitsu.data.local.FitsuDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object LocalStorageModule {

    private const val BUDGET_SHARED_PREFERENCES = "budget_shared_preferences"

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

    @JvmStatic
    @Singleton
    @Provides
    fun provideBudgetSharedPreferences(appContext: Context): SharedPreferences {
        return appContext.getSharedPreferences(
            BUDGET_SHARED_PREFERENCES,
            Context.MODE_PRIVATE
        )
    }

}