package com.example.voicememoapp.data

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [Folder::class, Memo::class], version = 1, exportSchema = false)
abstract class AppDatabase: RoomDatabase() {
    abstract fun folderDao(): FolderDao
    abstract fun memoDao(): MemoDao
}