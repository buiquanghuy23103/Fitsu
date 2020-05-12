package com.huy.fitsu.data.local

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import com.huy.fitsu.data.local.database.FitsuDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object FakeLocalStorageModule {

    private const val BUDGET_SHARED_PREFERENCES = "budget_shared_preferences"

    @Provides @Singleton @JvmStatic
    fun db(appContext: Context) : FitsuDatabase = Room.inMemoryDatabaseBuilder(
        appContext, FitsuDatabase::class.java
    ).build()

    @Provides @Singleton @JvmStatic
    fun sharedPref(appContext: Context): SharedPreferences = appContext.getSharedPreferences(
        BUDGET_SHARED_PREFERENCES,
        Context.MODE_PRIVATE
    )

}