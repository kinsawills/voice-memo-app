package com.example.voicememoapp.data

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import androidx.room.OnConflictStrategy

@Dao
interface FolderDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(folder: Folder)

    @Query("SELECT * FROM folder")
    suspend fun getAllFolders(): List<Folder>

    @Query("SELECT * FROM folder WHERE name = :name")
    suspend fun getFolderByName(name: String) : Folder

    @Query("SELECT * FROM folder WHERE id = :id")
    suspend fun getFolderById(id: Int): Folder?
}

@Dao
interface MemoDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(memo: Memo)

    @Query("SELECT * FROM memo")
    suspend fun getAllMemos(): List<Memo>

    @Query("SELECT * FROM memo WHERE folderId = :folderId")
    suspend fun getMemosByFolderId(folderId: Int): List<Memo>

    @Query("SELECT * FROM memo WHERE id = :id")
    suspend fun getMemoById(id: Int): Memo?

    @Query("DELETE FROM memo WHERE id = :id")
    suspend fun deleteMemo(id: Int)
}
