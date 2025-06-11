package com.tkjen.youtube.di

import android.content.Context
import androidx.room.Room
import com.tkjen.youtube.data.local.AppDatabase
import com.tkjen.youtube.data.local.DatabaseHelper
import com.tkjen.youtube.data.local.DatabaseHelperImpl
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class DatabaseModule {

    @Binds
    @Singleton
    abstract fun bindDatabaseHelper(
        databaseHelperImpl: DatabaseHelperImpl
    ): DatabaseHelper

    companion object {
        @Provides
        @Singleton
        fun provideAppDatabase(@ApplicationContext appContext: Context): AppDatabase {
            return Room.databaseBuilder(
                appContext,
                AppDatabase::class.java,
                "youtube-database"
            ).fallbackToDestructiveMigration(true)
                .build()
        }

        @Provides
        @Singleton
        fun provideYoutubeDao(appDatabase: AppDatabase) = appDatabase.youtubeDao()
    }
}
