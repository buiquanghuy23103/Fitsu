package com.huy.fitsu.di

import com.huy.fitsu.data.local.CategoryDao
import com.huy.fitsu.data.local.FitsuDatabase
import dagger.Module
import dagger.Provides
import javax.inject.Singleton

@Module
object DaoModule {

    @JvmStatic
    @Singleton
    @Provides
    fun provideCategoryDao(db: FitsuDatabase): CategoryDao = db.categoryDao()



}