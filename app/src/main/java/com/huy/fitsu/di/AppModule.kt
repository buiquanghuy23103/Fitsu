package com.huy.fitsu.di

import android.content.Context
import androidx.room.Room
import com.huy.fitsu.data.local.CategoryDao
import com.huy.fitsu.data.local.FitsuDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object AppModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideCategoryDao(db: FitsuDatabase): CategoryDao = db.categoryDao()

    @JvmStatic
    @Singleton
    @Provides
    fun provideDatabase(context: Context): FitsuDatabase {
        return Room.databaseBuilder(
            context.applicationContext,
            FitsuDatabase::class.java,
            "Fitsu.db"
        ).build()
    }

}