package com.example.voicememoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo")
data class Memo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val name: String,
    val folderId: Int,
    val filePath: String,
    val transcription: String = "",
)