package com.example.voicememoapp.ui

import android.content.Context
import androidx.room.Room
import com.example.voicememoapp.data.AppDatabase
import com.example.voicememoapp.data.FolderDao
import com.example.voicememoapp.data.MemoDao
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton
import kotlin.jvm.java

@Module
@InstallIn(SingletonComponent::class)
object DataBaseModule {
    @Provides
    @Singleton
    fun provideMovieDatabase(@ApplicationContext context: Context): AppDatabase {
        return Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "voiceMemo.db"
        ).fallbackToDestructiveMigration().build()
    }

    @Singleton
    @Provides
    fun provideFolderDao(appDatabase: AppDatabase): FolderDao =
        appDatabase.folderDao()

    @Singleton
    @Provides
    fun provideMemoDao(appDatabase: AppDatabase): MemoDao =
        appDatabase.memoDao()

}