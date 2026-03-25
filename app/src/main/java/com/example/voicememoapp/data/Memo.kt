package com.example.voicememoapp.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "memo")
data class Memo(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val folderId: Int,
    val content: Int,
    val transcription: String = "",
)